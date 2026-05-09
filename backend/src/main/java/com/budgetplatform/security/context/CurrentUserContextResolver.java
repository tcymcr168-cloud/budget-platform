package com.budgetplatform.security.context;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.common.audit.AuditAction;
import com.budgetplatform.common.audit.AuditEvent;
import com.budgetplatform.common.audit.AuditService;
import com.budgetplatform.security.domain.SecurityRoleCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableConfigurationProperties(AuthProperties.class)
public class CurrentUserContextResolver {

    private final AuthProperties properties;
    private final AuditService auditService;

    @Autowired
    public CurrentUserContextResolver(AuthProperties properties, AuditService auditService) {
        this.properties = properties;
        this.auditService = auditService;
    }

    public CurrentUserContextResolver(AuthProperties properties) {
        this(properties, null);
    }

    public CurrentUserContext resolve(String userIdHeader, String rolesHeader) {
        return switch (properties.getMode()) {
            case DEV_HEADER -> resolveDevHeader(userIdHeader, rolesHeader);
            case JWT -> throw unsupportedMode("JWT authentication adapter is not implemented in SEC-006.");
            case REVERSE_PROXY -> resolveReverseProxy();
        };
    }

    public AuthMode mode() {
        return properties.getMode();
    }

    private CurrentUserContext resolveDevHeader(String userIdHeader, String rolesHeader) {
        String userId = normalize(userIdHeader);
        Set<SecurityRoleCode> roles = !properties.isAllowHeaderRoles() || rolesHeader == null || rolesHeader.isBlank()
                ? Set.of()
                : Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .map(SecurityRoleCode::valueOf)
                .collect(Collectors.toUnmodifiableSet());
        return new CurrentUserContext(userId, roles, AuthMode.DEV_HEADER);
    }

    private CurrentUserContext resolveReverseProxy() {
        String headerName = normalize(properties.getReverseProxyUserHeader());
        if (headerName == null) {
            throw unauthorized(
                    "Reverse proxy user header is not configured.",
                    "REVERSE_PROXY_HEADER_NOT_CONFIGURED",
                    null
            );
        }
        HttpServletRequest request = currentRequest();
        String userId = normalize(request.getHeader(headerName));
        if (userId == null) {
            throw unauthorized(
                    "Trusted reverse proxy principal header is missing.",
                    "MISSING_REVERSE_PROXY_PRINCIPAL",
                    headerName
            );
        }
        return new CurrentUserContext(userId, Set.of(), AuthMode.REVERSE_PROXY);
    }

    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        throw unauthorized(
                "Reverse proxy authentication requires an HTTP request.",
                "MISSING_HTTP_REQUEST",
                properties.getReverseProxyUserHeader()
        );
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private ApplicationException unsupportedMode(String message) {
        return unauthorized(message, properties.getMode().name() + "_NOT_IMPLEMENTED", null);
    }

    private ApplicationException unauthorized(String message) {
        return unauthorized(message, "UNAUTHORIZED", null);
    }

    private ApplicationException unauthorized(String message, String reason, String headerName) {
        recordAuthenticationFailure(reason, message, headerName);
        return new ApplicationException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, message);
    }

    private void recordAuthenticationFailure(String reason, String message, String headerName) {
        if (auditService == null) {
            return;
        }
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("reason", reason);
        details.put("message", message);
        details.put("authMode", properties.getMode().name());
        if (headerName != null && !headerName.isBlank()) {
            details.put("headerName", headerName);
        }
        requestDetails().forEach(details::put);
        auditService.record(new AuditEvent(
                null,
                "authentication",
                "failure",
                AuditAction.AUTH_FAILURE,
                null,
                details
        ));
    }

    private Map<String, Object> requestDetails() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            HttpServletRequest request = attributes.getRequest();
            Map<String, Object> details = new LinkedHashMap<>();
            details.put("method", request.getMethod());
            details.put("path", request.getRequestURI());
            String requestId = normalize(request.getHeader("X-Request-Id"));
            if (requestId != null) {
                details.put("requestId", requestId);
            }
            return details;
        }
        return Map.of();
    }
}
