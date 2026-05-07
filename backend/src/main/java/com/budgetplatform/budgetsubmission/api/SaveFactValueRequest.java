package com.budgetplatform.budgetsubmission.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record SaveFactValueRequest(
        @NotNull UUID accountMemberId,
        @NotNull BigDecimal amount,
        @Size(max = 1000) String note
) {
}
