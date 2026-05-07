package com.budgetplatform.budgettemplate.api;

import com.budgetplatform.budgettemplate.domain.BudgetTemplate;
import com.budgetplatform.budgettemplate.domain.BudgetTemplateStatus;

import java.util.UUID;

public record BudgetTemplateResponse(
        UUID id,
        UUID budgetModelId,
        String code,
        String name,
        String description,
        BudgetTemplateStatus status
) {

    public static BudgetTemplateResponse from(BudgetTemplate budgetTemplate) {
        return new BudgetTemplateResponse(
                budgetTemplate.getId(),
                budgetTemplate.getBudgetModel().getId(),
                budgetTemplate.getCode(),
                budgetTemplate.getName(),
                budgetTemplate.getDescription(),
                budgetTemplate.getStatus()
        );
    }
}
