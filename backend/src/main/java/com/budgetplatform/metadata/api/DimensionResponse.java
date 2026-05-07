package com.budgetplatform.metadata.api;

import com.budgetplatform.metadata.domain.Dimension;
import com.budgetplatform.metadata.domain.DimensionType;
import com.budgetplatform.metadata.domain.MetadataStatus;

import java.time.Instant;
import java.util.UUID;

public record DimensionResponse(
        UUID id,
        UUID workspaceId,
        String code,
        String name,
        DimensionType dimensionType,
        boolean system,
        MetadataStatus status,
        String attributesSchema,
        Instant createdAt,
        Instant updatedAt
) {
    public static DimensionResponse from(Dimension dimension) {
        return new DimensionResponse(
                dimension.getId(),
                dimension.getWorkspace().getId(),
                dimension.getCode(),
                dimension.getName(),
                dimension.getDimensionType(),
                dimension.isSystem(),
                dimension.getStatus(),
                dimension.getAttributesSchema(),
                dimension.getCreatedAt(),
                dimension.getUpdatedAt()
        );
    }
}
