package com.budgetplatform.budgetmodel.api;

import com.budgetplatform.budgetmodel.domain.BudgetModel;
import com.budgetplatform.budgetmodel.domain.BudgetModelStatus;

import java.util.UUID;

public record BudgetModelResponse(
        UUID id,
        UUID workspaceId,
        String code,
        String name,
        String description,
        BudgetModelStatus status
) {

    public static BudgetModelResponse from(BudgetModel budgetModel) {
        return new BudgetModelResponse(
                budgetModel.getId(),
                budgetModel.getWorkspace().getId(),
                budgetModel.getCode(),
                budgetModel.getName(),
                budgetModel.getDescription(),
                budgetModel.getStatus()
        );
    }
}
