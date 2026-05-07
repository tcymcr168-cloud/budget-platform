package com.budgetplatform.budgetsubmission.api;

import com.budgetplatform.budgetsubmission.domain.FactSourceType;
import com.budgetplatform.budgetsubmission.domain.FactValue;
import com.budgetplatform.budgetsubmission.domain.FactValueStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record FactValueResponse(
        UUID id,
        UUID submissionTaskId,
        UUID accountMemberId,
        String accountMemberCode,
        String accountMemberName,
        BigDecimal amount,
        FactValueStatus valueStatus,
        FactSourceType sourceType,
        String note
) {

    public static FactValueResponse from(FactValue value) {
        return new FactValueResponse(
                value.getId(),
                value.getSubmissionTask() == null ? null : value.getSubmissionTask().getId(),
                value.getAccountMember().getId(),
                value.getAccountMember().getCode(),
                value.getAccountMember().getName(),
                value.getAmount(),
                value.getValueStatus(),
                value.getSourceType(),
                value.getNote()
        );
    }
}
