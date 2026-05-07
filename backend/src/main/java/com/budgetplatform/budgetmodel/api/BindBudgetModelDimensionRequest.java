package com.budgetplatform.budgetmodel.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record BindBudgetModelDimensionRequest(
        @NotNull UUID dimensionId,
        Boolean requiredForEntry,
        @PositiveOrZero Integer displayOrder
) {
}
