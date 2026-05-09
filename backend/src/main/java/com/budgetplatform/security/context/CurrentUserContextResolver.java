package com.budgetplatform.security.context;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.security.domain.SecurityRoleCode;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

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
            case REVERSE_PROXY -> throw unsupportedMode("Reverse proxy authentication adapter is not implemented in SEC-006.");
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

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private ApplicationException unsupportedMode(String message) {
        return new ApplicationException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, message);
    }
}
