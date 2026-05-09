package com.budgetplatform.security.api;

import com.budgetplatform.security.domain.AppUserEntityScope;
import com.budgetplatform.security.domain.SecurityGrantStatus;

import java.time.Instant;
import java.util.UUID;

public record EntityScopeResponse(
        UUID id,
        UUID userId,
        UUID workspaceId,
        String workspaceCode,
        UUID entityMemberId,
        String entityMemberCode,
        String entityMemberName,
        boolean includeDescendants,
        SecurityGrantStatus status,
        Instant revokedAt
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
                scope.isIncludeDescendants(),
                scope.getStatus(),
                scope.getRevokedAt()
        );
    }
}
