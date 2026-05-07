package com.budgetplatform.budgetquery.api;

import com.budgetplatform.budgetsubmission.domain.FactSourceType;
import com.budgetplatform.budgetsubmission.domain.FactValue;
import com.budgetplatform.budgetsubmission.domain.FactValueStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record FactQueryResponse(
        UUID id,
        UUID budgetModelId,
        UUID budgetTemplateId,
        UUID submissionTaskId,
        String accountCode,
        String accountName,
        String entityCode,
        String timeCode,
        String categoryCode,
        String versionCode,
        BigDecimal amount,
        FactValueStatus valueStatus,
        FactSourceType sourceType
) {

    public static FactQueryResponse from(FactValue value) {
        return new FactQueryResponse(
                value.getId(),
                value.getBudgetModel().getId(),
                value.getBudgetTemplate().getId(),
                value.getSubmissionTask().getId(),
                value.getAccountMember().getCode(),
                value.getAccountMember().getName(),
                value.getEntityMember().getCode(),
                value.getTimeMember().getCode(),
                value.getCategoryMember().getCode(),
                value.getVersionMember().getCode(),
                value.getAmount(),
                value.getValueStatus(),
                value.getSourceType()
        );
    }
}
