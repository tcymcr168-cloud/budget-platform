package com.budgetplatform.security.context;

import com.budgetplatform.security.domain.SecurityRoleCode;

import java.util.Set;

public record CurrentUserContext(
        String userId,
        Set<SecurityRoleCode> roles
) {

    public boolean authenticated() {
        return userId != null && !userId.isBlank();
    }
}
