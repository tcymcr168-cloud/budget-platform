package com.budgetplatform.budgetsubmission.api;

import jakarta.validation.constraints.Size;

public record ReturnSubmissionTaskRequest(
        @Size(max = 1000) String reason
) {
}
