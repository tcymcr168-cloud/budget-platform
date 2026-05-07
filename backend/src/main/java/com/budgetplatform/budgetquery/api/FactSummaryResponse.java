package com.budgetplatform.budgetquery.api;

import java.math.BigDecimal;
import java.util.UUID;

public record FactSummaryResponse(
        QueryGroupBy groupBy,
        UUID memberId,
        String memberCode,
        String memberName,
        BigDecimal totalAmount,
        long lineCount
) {
}
