package com.budgetplatform.security.api;

import com.budgetplatform.security.domain.AppUserEntityScope;

import java.util.UUID;

public record EntityScopeResponse(
        UUID id,
        UUID userId,
        UUID workspaceId,
        String workspaceCode,
        UUID entityMemberId,
        String entityMemberCode,
        String entityMemberName,
        boolean includeDescendants
) {

    public static EntityScopeResponse from(AppUserEntityScope scope) {
        return new EntityScopeResponse(
                scope.getId(),
                scope.getUser().getId(),
                scope.getWorkspace().getId(),
                scope.getWorkspace().getCode(),
                scope.getEntityMember().getId(),
                scope.getEntityMember().getCode(),
                scope.getEntityMember().getName(),
                scope.isIncludeDescendants()
        );
    }
}
