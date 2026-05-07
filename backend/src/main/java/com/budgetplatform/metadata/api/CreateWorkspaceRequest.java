package com.budgetplatform.metadata.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateWorkspaceRequest(
        @NotBlank @Size(max = 64) String code,
        @NotBlank @Size(max = 160) String name
) {
}
