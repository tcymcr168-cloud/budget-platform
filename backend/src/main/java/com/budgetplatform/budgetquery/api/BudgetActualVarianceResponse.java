package com.budgetplatform.budgetquery.api;

import java.math.BigDecimal;
import java.util.UUID;

public record BudgetActualVarianceResponse(
        UUID accountMemberId,
        String accountCode,
        String accountName,
        UUID entityMemberId,
        String entityCode,
        String entityName,
        UUID timeMemberId,
        String timeCode,
        String timeName,
        BigDecimal budgetAmount,
        BigDecimal actualAmount,
        BigDecimal varianceAmount,
        BigDecimal variancePercent,
        long budgetLineCount,
        long actualLineCount
) {
}
