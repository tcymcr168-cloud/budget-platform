package com.budgetplatform.common.api;

public record ApiError(
        String code,
        String message,
        String traceId
) {
}
