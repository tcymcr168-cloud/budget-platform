package com.budgetplatform.security.api;

import com.budgetplatform.security.domain.AppUserRole;
import com.budgetplatform.security.domain.SecurityRoleCode;

import java.util.UUID;

public record UserRoleResponse(
        UUID id,
        UUID userId,
        UUID workspaceId,
        String workspaceCode,
        SecurityRoleCode roleCode
) {

    public static UserRoleResponse from(AppUserRole role) {
        return new UserRoleResponse(
                role.getId(),
                role.getUser().getId(),
                role.getWorkspace().getId(),
                role.getWorkspace().getCode(),
                role.getRoleCode()
        );
    }
}
