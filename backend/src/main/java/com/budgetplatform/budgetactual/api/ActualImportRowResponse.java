package com.budgetplatform.budgetactual.api;

import com.budgetplatform.budgetactual.domain.ActualImportRow;
import com.budgetplatform.budgetactual.domain.ActualImportRowStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record ActualImportRowResponse(
        UUID id,
        int rowNumber,
        String accountCode,
        String entityCode,
        String timeCode,
        String categoryCode,
        String versionCode,
        BigDecimal amount,
        ActualImportRowStatus rowStatus,
        String errorMessage
) {

    public static ActualImportRowResponse from(ActualImportRow row) {
        return new ActualImportRowResponse(
                row.getId(),
                row.getRowNumber(),
                row.getAccountCode(),
                row.getEntityCode(),
                row.getTimeCode(),
                row.getCategoryCode(),
                row.getVersionCode(),
                row.getAmount(),
                row.getRowStatus(),
                row.getErrorMessage()
        );
    }
}
