package com.budgetplatform.metadata.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateDimensionMemberRequest(
        @NotBlank @Size(max = 64) String code,
        @NotBlank @Size(max = 160) String name,
        UUID parentId,
        Integer sortOrder,
        String attributes
) {
}
