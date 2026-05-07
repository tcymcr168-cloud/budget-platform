package com.budgetplatform.budgetsubmission.repository;

import com.budgetplatform.budgetsubmission.domain.FactValue;
import com.budgetplatform.budgetsubmission.domain.FactValueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FactValueRepository extends JpaRepository<FactValue, UUID> {

    Optional<FactValue> findBySubmissionTask_IdAndAccountMember_Id(UUID submissionTaskId, UUID accountMemberId);

    List<FactValue> findBySubmissionTask_IdOrderByAccountMember_CodeAsc(UUID submissionTaskId);

    List<FactValue> findBySubmissionTask_Id(UUID submissionTaskId);

    List<FactValue> findByBudgetModel_IdOrderByUpdatedAtDesc(UUID budgetModelId);

    long countBySubmissionTask_Id(UUID submissionTaskId);

    default void updateStatuses(UUID submissionTaskId, FactValueStatus status) {
        findBySubmissionTask_Id(submissionTaskId).forEach(value -> value.setValueStatus(status));
    }
}
