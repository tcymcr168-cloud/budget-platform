package com.budgetplatform.common.api;

public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> failure(ErrorCode code, String message, String traceId) {
        return new ApiResponse<>(false, null, new ApiError(code.name(), message, traceId));
    }
}
