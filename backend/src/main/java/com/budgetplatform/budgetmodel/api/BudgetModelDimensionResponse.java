package com.budgetplatform.budgetmodel.api;

import com.budgetplatform.budgetmodel.domain.BudgetModelDimensionBinding;
import com.budgetplatform.metadata.domain.DimensionType;

import java.util.UUID;

public record BudgetModelDimensionResponse(
        UUID id,
        UUID budgetModelId,
        UUID dimensionId,
        String dimensionCode,
        String dimensionName,
        DimensionType dimensionRole,
        boolean requiredForEntry,
        int displayOrder
) {

    public static BudgetModelDimensionResponse from(BudgetModelDimensionBinding binding) {
        return new BudgetModelDimensionResponse(
                binding.getId(),
                binding.getBudgetModel().getId(),
                binding.getDimension().getId(),
                binding.getDimension().getCode(),
                binding.getDimension().getName(),
                binding.getDimensionRole(),
                binding.isRequiredForEntry(),
                binding.getDisplayOrder()
        );
    }
}
