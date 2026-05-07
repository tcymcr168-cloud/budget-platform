package com.budgetplatform.metadata.api;

import com.budgetplatform.metadata.domain.DimensionMember;
import com.budgetplatform.metadata.domain.MetadataStatus;

import java.time.Instant;
import java.util.UUID;

public record DimensionMemberResponse(
        UUID id,
        UUID dimensionId,
        String code,
        String name,
        UUID parentId,
        Integer sortOrder,
        boolean leaf,
        MetadataStatus status,
        String attributes,
        Instant createdAt,
        Instant updatedAt
) {
    public static DimensionMemberResponse from(DimensionMember member) {
        return new DimensionMemberResponse(
                member.getId(),
                member.getDimension().getId(),
                member.getCode(),
                member.getName(),
                member.getParent() == null ? null : member.getParent().getId(),
                member.getSortOrder(),
                member.isLeaf(),
                member.getStatus(),
                member.getAttributes(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
