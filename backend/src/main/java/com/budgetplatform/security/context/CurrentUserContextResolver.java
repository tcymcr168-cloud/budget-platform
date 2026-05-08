package com.budgetplatform.security.context;

import com.budgetplatform.security.domain.SecurityRoleCode;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CurrentUserContextResolver {

    public CurrentUserContext resolve(String userIdHeader, String rolesHeader) {
        String userId = userIdHeader == null || userIdHeader.isBlank() ? null : userIdHeader.trim();
        Set<SecurityRoleCode> roles = rolesHeader == null || rolesHeader.isBlank()
                ? Set.of()
                : Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .map(SecurityRoleCode::valueOf)
                .collect(Collectors.toUnmodifiableSet());
        return new CurrentUserContext(userId, roles);
    }
}
