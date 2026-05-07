package com.budgetplatform.budgetsubmission.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateSubmissionTaskRequest(
        @NotNull UUID budgetTemplateId,
        @NotNull UUID entityMemberId,
        @NotNull UUID timeMemberId,
        @NotNull UUID categoryMemberId,
        @NotNull UUID versionMemberId,
        @NotBlank @Size(max = 120) String ownerUser,
        @NotBlank @Size(max = 120) String reviewerUser
) {
}
