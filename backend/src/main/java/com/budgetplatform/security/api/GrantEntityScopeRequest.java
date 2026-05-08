package com.budgetplatform.security.api;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GrantEntityScopeRequest(
        @NotNull UUID workspaceId,
        @NotNull UUID entityMemberId,
        boolean includeDescendants
) {
}
