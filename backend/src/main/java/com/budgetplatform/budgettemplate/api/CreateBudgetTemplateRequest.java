package com.budgetplatform.budgettemplate.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateBudgetTemplateRequest(
        @NotNull UUID budgetModelId,
        @NotBlank @Size(max = 64) String code,
        @NotBlank @Size(max = 160) String name,
        @Size(max = 1000) String description
) {
}
