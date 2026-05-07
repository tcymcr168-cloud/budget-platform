package com.budgetplatform.budgettemplate.api;

import com.budgetplatform.budgettemplate.domain.TemplateAxisType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateTemplateAxisRequest(
        @NotNull UUID modelDimensionId,
        @NotNull TemplateAxisType axisType,
        @Size(max = 64) String memberSelector,
        @PositiveOrZero Integer displayOrder
) {
}
