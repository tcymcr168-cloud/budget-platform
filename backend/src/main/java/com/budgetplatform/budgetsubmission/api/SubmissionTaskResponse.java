package com.budgetplatform.budgetsubmission.api;

import com.budgetplatform.budgetsubmission.domain.SubmissionStatus;
import com.budgetplatform.budgetsubmission.domain.SubmissionTask;

import java.util.UUID;

public record SubmissionTaskResponse(
        UUID id,
        UUID budgetTemplateId,
        UUID budgetModelId,
        UUID entityMemberId,
        String entityMemberCode,
        UUID timeMemberId,
        String timeMemberCode,
        UUID categoryMemberId,
        String categoryMemberCode,
        UUID versionMemberId,
        String versionMemberCode,
        String ownerUser,
        String reviewerUser,
        SubmissionStatus status,
        String returnReason
) {

    public static SubmissionTaskResponse from(SubmissionTask task) {
        return new SubmissionTaskResponse(
                task.getId(),
                task.getBudgetTemplate().getId(),
                task.getBudgetModel().getId(),
                task.getEntityMember().getId(),
                task.getEntityMember().getCode(),
                task.getTimeMember().getId(),
                task.getTimeMember().getCode(),
                task.getCategoryMember().getId(),
                task.getCategoryMember().getCode(),
                task.getVersionMember().getId(),
                task.getVersionMember().getCode(),
                task.getOwnerUser(),
                task.getReviewerUser(),
                task.getStatus(),
                task.getReturnReason()
        );
    }
}
