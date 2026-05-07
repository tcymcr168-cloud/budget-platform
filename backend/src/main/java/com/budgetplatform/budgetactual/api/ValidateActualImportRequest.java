package com.budgetplatform.budgetactual.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ValidateActualImportRequest(
        @NotNull UUID budgetModelId,
        @NotBlank String fileName,
        @NotBlank String operatorUser,
        @NotBlank String csvContent
) {
}
