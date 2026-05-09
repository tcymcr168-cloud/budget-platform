package com.budgetplatform.security.api;

import jakarta.validation.constraints.Size;

public record SecurityGrantRevokeRequest(
        @Size(max = 240)
        String reason
) {
}
