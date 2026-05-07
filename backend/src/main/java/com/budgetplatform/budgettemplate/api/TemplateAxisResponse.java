package com.budgetplatform.budgettemplate.api;

import com.budgetplatform.budgettemplate.domain.BudgetTemplateAxis;
import com.budgetplatform.budgettemplate.domain.TemplateAxisType;
import com.budgetplatform.metadata.domain.DimensionType;

import java.util.UUID;

public record TemplateAxisResponse(
        UUID id,
        UUID budgetTemplateId,
        UUID modelDimensionId,
        UUID dimensionId,
        String dimensionCode,
        String dimensionName,
        DimensionType dimensionRole,
        TemplateAxisType axisType,
        String memberSelector,
        int displayOrder
) {

    public static TemplateAxisResponse from(BudgetTemplateAxis axis) {
        return new TemplateAxisResponse(
                axis.getId(),
                axis.getBudgetTemplate().getId(),
                axis.getModelDimension().getId(),
                axis.getModelDimension().getDimension().getId(),
                axis.getModelDimension().getDimension().getCode(),
                axis.getModelDimension().getDimension().getName(),
                axis.getModelDimension().getDimensionRole(),
                axis.getAxisType(),
                axis.getMemberSelector(),
                axis.getDisplayOrder()
        );
    }
}
