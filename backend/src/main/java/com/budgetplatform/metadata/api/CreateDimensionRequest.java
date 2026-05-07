package com.budgetplatform.metadata.api;

import com.budgetplatform.metadata.domain.DimensionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateDimensionRequest(
        @NotNull UUID workspaceId,
        @NotBlank @Size(max = 64) String code,
        @NotBlank @Size(max = 160) String name,
        @NotNull DimensionType dimensionType,
        boolean system,
        String attributesSchema
) {
}
