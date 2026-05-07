package com.budgetplatform.budgetsubmission.repository;

import com.budgetplatform.budgetsubmission.domain.SubmissionTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubmissionTaskRepository extends JpaRepository<SubmissionTask, UUID> {

    boolean existsByBudgetTemplate_IdAndEntityMember_IdAndTimeMember_IdAndCategoryMember_IdAndVersionMember_Id(
            UUID budgetTemplateId,
            UUID entityMemberId,
            UUID timeMemberId,
            UUID categoryMemberId,
            UUID versionMemberId
    );

    List<SubmissionTask> findByBudgetTemplate_IdOrderByUpdatedAtDesc(UUID budgetTemplateId);
}
