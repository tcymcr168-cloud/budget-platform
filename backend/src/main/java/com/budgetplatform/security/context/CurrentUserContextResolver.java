package com.budgetplatform.security.context;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.common.audit.AuditAction;
import com.budgetplatform.common.audit.AuditEvent;
import com.budgetplatform.common.audit.AuditService;
import com.budgetplatform.security.domain.AppUser;
import com.budgetplatform.security.domain.SecurityRoleCode;
import com.budgetplatform.security.domain.SecurityUserStatus;
import com.budgetplatform.security.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtAudienceValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@EnableConfigurationProperties(AuthProperties.class)
public class CurrentUserContextResolver {

    private final AuthProperties properties;
    private final AuditService auditService;
    private final JwtDecoder testJwtDecoder;
    private final AppUserRepository userRepository;
    private volatile JwtDecoder configuredJwtDecoder;

    @Autowired
    public CurrentUserContextResolver(
            AuthProperties properties,
            AuditService auditService,
            AppUserRepository userRepository
    ) {
        this(properties, auditService, null, userRepository);
    }

    CurrentUserContextResolver(
            AuthProperties properties,
            AuditService auditService,
            JwtDecoder testJwtDecoder,
            AppUserRepository userRepository
    ) {
        this.properties = properties;
        this.auditService = auditService;
        this.testJwtDecoder = testJwtDecoder;
        this.userRepository = userRepository;
    }

    public CurrentUserContextResolver(AuthProperties properties, AuditService auditService) {
        this(properties, auditService, null, null);
    }

    public CurrentUserContextResolver(AuthProperties properties) {
        this(properties, null);
    }

    public CurrentUserContext resolve(String userIdHeader, String rolesHeader) {
        return switch (properties.getMode()) {
            case DEV_HEADER -> resolveDevHeader(userIdHeader, rolesHeader);
            case JWT -> resolveJwt();
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
        HttpServletRequest request = currentRequest("Reverse proxy authentication requires an HTTP request.");
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

    private CurrentUserContext resolveJwt() {
        HttpServletRequest request = currentRequest("JWT authentication requires an HTTP request.");
        String authorization = normalize(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (authorization == null) {
            throw unauthorized("Bearer token is missing.", "MISSING_BEARER_TOKEN", HttpHeaders.AUTHORIZATION);
        }
        if (!authorization.regionMatches(true, 0, "Bearer ", 0, "Bearer ".length())) {
            throw unauthorized(
                    "Authorization header must use the Bearer scheme.",
                    "UNSUPPORTED_AUTHORIZATION_SCHEME",
                    HttpHeaders.AUTHORIZATION
            );
        }
        String token = normalize(authorization.substring("Bearer ".length()));
        if (token == null) {
            throw unauthorized("Bearer token is missing.", "MISSING_BEARER_TOKEN", HttpHeaders.AUTHORIZATION);
        }
        if (token.length() > properties.getJwt().getMaxTokenLength()) {
            throw unauthorized("Bearer token is too large.", "TOKEN_TOO_LARGE", HttpHeaders.AUTHORIZATION);
        }

        Jwt jwt = decodeJwt(token);
        String claimName = normalize(properties.getJwt().getUsernameClaim());
        if (claimName == null) {
            throw unauthorized("JWT username claim is not configured.", "JWT_CONFIGURATION_INVALID", null);
        }
        String username = normalize(jwt.getClaimAsString(claimName));
        if (username == null) {
            throw unauthorized("JWT username claim is missing.", "MISSING_USERNAME_CLAIM", null);
        }
        String normalizedUsername = AppUser.normalizeUsername(username);
        requireRegisteredActiveJwtUser(normalizedUsername);
        return new CurrentUserContext(normalizedUsername, Set.of(), AuthMode.JWT);
    }

    private HttpServletRequest currentRequest() {
        return currentRequest("Authentication requires an HTTP request.");
    }

    private HttpServletRequest currentRequest(String missingRequestMessage) {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        throw unauthorized(
                missingRequestMessage,
                "MISSING_HTTP_REQUEST",
                null
        );
    }

    private Jwt decodeJwt(String token) {
        try {
            return jwtDecoder().decode(token);
        } catch (JwtValidationException exception) {
            throw unauthorized("Bearer token validation failed.", jwtFailureReason(exception), HttpHeaders.AUTHORIZATION);
        } catch (BadJwtException exception) {
            throw unauthorized("Bearer token is malformed or rejected.", jwtFailureReason(exception), HttpHeaders.AUTHORIZATION);
        } catch (JwtException exception) {
            throw unauthorized("Bearer token could not be validated.", jwtFailureReason(exception), HttpHeaders.AUTHORIZATION);
        }
    }

    private JwtDecoder jwtDecoder() {
        if (testJwtDecoder != null) {
            return testJwtDecoder;
        }
        JwtDecoder localDecoder = configuredJwtDecoder;
        if (localDecoder != null) {
            return localDecoder;
        }
        synchronized (this) {
            if (configuredJwtDecoder == null) {
                configuredJwtDecoder = buildConfiguredJwtDecoder();
            }
            return configuredJwtDecoder;
        }
    }

    private JwtDecoder buildConfiguredJwtDecoder() {
        AuthProperties.Jwt jwtProperties = properties.getJwt();
        String issuer = requiredJwtConfig(jwtProperties.getIssuer(), "issuer");
        String audience = requiredJwtConfig(jwtProperties.getAudience(), "audience");
        String jwksUri = requiredJwtConfig(jwtProperties.getJwksUri(), "jwks-uri");
        List<SignatureAlgorithm> algorithms = allowedAlgorithms(jwtProperties.getAllowedAlgorithms());

        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwksUri)
                .jwsAlgorithms(values -> {
                    values.clear();
                    values.addAll(algorithms);
                })
                .build();
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.ofSeconds(jwtProperties.getClockSkewSeconds())),
                new JwtIssuerValidator(issuer),
                new JwtAudienceValidator(audience)
        );
        decoder.setJwtValidator(validator);
        return decoder;
    }

    private String requiredJwtConfig(String value, String propertyName) {
        String normalized = normalize(value);
        if (normalized == null) {
            throw unauthorized(
                    "JWT authentication is missing required configuration: " + propertyName + ".",
                    "JWT_CONFIGURATION_MISSING",
                    null
            );
        }
        return normalized;
    }

    private List<SignatureAlgorithm> allowedAlgorithms(List<String> values) {
        List<SignatureAlgorithm> algorithms = new ArrayList<>();
        if (values != null) {
            values.stream()
                    .map(this::normalize)
                    .filter(value -> value != null)
                    .forEach(value -> algorithms.add(allowedAlgorithm(value)));
        }
        if (algorithms.isEmpty()) {
            throw unauthorized(
                    "JWT authentication requires at least one allowed algorithm.",
                    "JWT_CONFIGURATION_INVALID",
                    null
            );
        }
        return List.copyOf(algorithms);
    }

    private SignatureAlgorithm allowedAlgorithm(String value) {
        SignatureAlgorithm algorithm;
        try {
            algorithm = SignatureAlgorithm.from(value);
        } catch (IllegalArgumentException exception) {
            throw unauthorized(
                    "JWT authentication includes an unsupported allowed algorithm.",
                    "JWT_CONFIGURATION_INVALID",
                    null
            );
        }
        if (algorithm == null) {
            throw unauthorized(
                    "JWT authentication includes an unsupported allowed algorithm.",
                    "JWT_CONFIGURATION_INVALID",
                    null
            );
        }
        return algorithm;
    }

    private void requireRegisteredActiveJwtUser(String normalizedUsername) {
        if (userRepository == null) {
            return;
        }
        AppUser user = userRepository.findByUsername(normalizedUsername)
                .orElseThrow(() -> {
                    recordAuthenticationFailure(
                            "UNKNOWN_APPLICATION_USER",
                            "JWT principal is not registered.",
                            null
                    );
                    return new ApplicationException(
                            ErrorCode.FORBIDDEN,
                            HttpStatus.FORBIDDEN,
                            "Security user is not registered: " + normalizedUsername
                    );
                });
        if (user.getStatus() == SecurityUserStatus.INACTIVE) {
            recordAuthenticationFailure(
                    "INACTIVE_APPLICATION_USER",
                    "JWT principal is inactive.",
                    null
            );
            throw new ApplicationException(
                    ErrorCode.FORBIDDEN,
                    HttpStatus.FORBIDDEN,
                    "Security user is inactive: " + normalizedUsername
            );
        }
    }

    private String jwtFailureReason(JwtValidationException exception) {
        String combined = exception.getErrors()
                .stream()
                .map(OAuth2Error::getDescription)
                .collect(Collectors.joining(" "));
        return jwtFailureReason(combined + " " + exception.getMessage());
    }

    private String jwtFailureReason(JwtException exception) {
        return jwtFailureReason(exception.getMessage());
    }

    private String jwtFailureReason(String message) {
        String value = message == null ? "" : message.toLowerCase();
        if (value.contains("expired")) {
            return "TOKEN_EXPIRED";
        }
        if (value.contains("not before") || value.contains("not yet valid")) {
            return "TOKEN_NOT_YET_VALID";
        }
        if (value.contains("issuer") || value.contains("iss claim")) {
            return "INVALID_ISSUER";
        }
        if (value.contains("audience") || value.contains("aud claim")) {
            return "INVALID_AUDIENCE";
        }
        if (value.contains("signature")) {
            return "INVALID_SIGNATURE";
        }
        if (value.contains("malformed") || value.contains("serialization")) {
            return "MALFORMED_TOKEN";
        }
        return "INVALID_TOKEN";
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
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
