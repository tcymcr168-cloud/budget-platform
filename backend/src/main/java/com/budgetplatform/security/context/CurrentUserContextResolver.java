package com.budgetplatform.security.context;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.security.domain.SecurityRoleCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableConfigurationProperties(AuthProperties.class)
public class CurrentUserContextResolver {

    private final AuthProperties properties;

    public CurrentUserContextResolver(AuthProperties properties) {
        this.properties = properties;
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
        return new CurrentUserContext(userId, roles);
    }

    private CurrentUserContext resolveReverseProxy() {
        String headerName = normalize(properties.getReverseProxyUserHeader());
        if (headerName == null) {
            throw unauthorized("Reverse proxy user header is not configured.");
        }
        HttpServletRequest request = currentRequest();
        String userId = normalize(request.getHeader(headerName));
        if (userId == null) {
            throw unauthorized("Trusted reverse proxy principal header is missing.");
        }
        return new CurrentUserContext(userId, Set.of());
    }

    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        throw unauthorized("Reverse proxy authentication requires an HTTP request.");
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private ApplicationException unsupportedMode(String message) {
        return unauthorized(message);
    }

    private ApplicationException unauthorized(String message) {
        return new ApplicationException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, message);
    }
}
