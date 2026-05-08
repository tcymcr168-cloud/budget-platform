package com.budgetplatform.security.api;

import com.budgetplatform.security.domain.SecurityRoleCode;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GrantUserRoleRequest(
        @NotNull UUID workspaceId,
        @NotNull SecurityRoleCode roleCode
) {
}
