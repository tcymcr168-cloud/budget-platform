package com.budgetplatform.security.api;

import com.budgetplatform.security.domain.AppUser;
import com.budgetplatform.security.domain.SecurityUserStatus;

import java.util.UUID;

public record SecurityUserResponse(
        UUID id,
        String username,
        String displayName,
        String email,
        SecurityUserStatus status
) {

    public static SecurityUserResponse from(AppUser user) {
        return new SecurityUserResponse(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getStatus()
        );
    }
}
