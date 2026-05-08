package com.budgetplatform.security.api;

import com.budgetplatform.security.domain.SecurityRoleCode;

import java.util.Set;

public record CurrentUserResponse(
        String userId,
        Set<SecurityRoleCode> roles,
        boolean authenticated
) {
}
