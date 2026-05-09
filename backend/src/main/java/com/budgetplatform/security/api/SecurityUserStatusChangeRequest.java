package com.budgetplatform.security.api;

import jakarta.validation.constraints.Size;

public record SecurityUserStatusChangeRequest(
        @Size(max = 240)
        String reason
) {
}
