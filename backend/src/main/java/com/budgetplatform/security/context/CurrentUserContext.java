package com.budgetplatform.security.context;

import com.budgetplatform.security.domain.SecurityRoleCode;

import java.util.Set;

public record CurrentUserContext(
        String userId,
        Set<SecurityRoleCode> roles,
        AuthMode authMode
) {

    public CurrentUserContext(String userId, Set<SecurityRoleCode> roles) {
        this(userId, roles, AuthMode.DEV_HEADER);
    }

    public boolean authenticated() {
        return userId != null && !userId.isBlank();
    }
}
