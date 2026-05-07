package com.budgetplatform.metadata.api;

import com.budgetplatform.metadata.domain.BudgetWorkspace;
import com.budgetplatform.metadata.domain.MetadataStatus;

import java.time.Instant;
import java.util.UUID;

public record WorkspaceResponse(
        UUID id,
        String code,
        String name,
        MetadataStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static WorkspaceResponse from(BudgetWorkspace workspace) {
        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getCode(),
                workspace.getName(),
                workspace.getStatus(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
        );
    }
}
