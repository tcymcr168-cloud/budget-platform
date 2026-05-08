package com.budgetplatform.security.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSecurityUserRequest(
        @NotBlank @Size(max = 120) String username,
        @NotBlank @Size(max = 160) String displayName,
        @Email @Size(max = 240) String email
) {
}
