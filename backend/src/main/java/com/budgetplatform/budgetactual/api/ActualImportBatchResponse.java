package com.budgetplatform.budgetactual.api;

import com.budgetplatform.budgetactual.domain.ActualImportBatch;
import com.budgetplatform.budgetactual.domain.ActualImportStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ActualImportBatchResponse(
        UUID id,
        UUID budgetModelId,
        String fileName,
        String operatorUser,
        ActualImportStatus status,
        int totalRows,
        int validRows,
        int errorRows,
        BigDecimal totalAmount,
        String errorReport,
        Instant createdAt,
        Instant updatedAt,
        List<ActualImportRowResponse> rows
) {

    public static ActualImportBatchResponse from(ActualImportBatch batch, List<ActualImportRowResponse> rows) {
        return new ActualImportBatchResponse(
                batch.getId(),
                batch.getBudgetModel().getId(),
                batch.getFileName(),
                batch.getOperatorUser(),
                batch.getStatus(),
                batch.getTotalRows(),
                batch.getValidRows(),
                batch.getErrorRows(),
                batch.getTotalAmount(),
                batch.getErrorReport(),
                batch.getCreatedAt(),
                batch.getUpdatedAt(),
                rows
        );
    }
}
