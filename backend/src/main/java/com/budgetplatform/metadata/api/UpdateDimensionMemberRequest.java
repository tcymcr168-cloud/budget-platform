package com.budgetplatform.metadata.api;

import com.budgetplatform.metadata.domain.MetadataStatus;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateDimensionMemberRequest(
        @Size(max = 160) String name,
        UUID parentId,
        MetadataStatus status
) {
}
