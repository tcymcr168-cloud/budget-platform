package com.budgetplatform.security.api;

import com.budgetplatform.security.context.AuthMode;
import com.budgetplatform.security.domain.SecurityRoleCode;

import java.util.List;
import java.util.Set;

public record CurrentUserResponse(
        String userId,
        Set<SecurityRoleCode> roles,
        boolean authenticated,
        AuthMode authMode,
        List<UserRoleResponse> applicationRoles,
        List<EntityScopeResponse> entityScopes
) {
}
