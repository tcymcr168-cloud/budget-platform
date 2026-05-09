package com.budgetplatform.security.repository;

import com.budgetplatform.security.domain.AppUserEntityScope;
import com.budgetplatform.security.domain.SecurityGrantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppUserEntityScopeRepository extends JpaRepository<AppUserEntityScope, UUID> {

    boolean existsByUser_IdAndWorkspace_IdAndEntityMember_IdAndStatus(
            UUID userId,
            UUID workspaceId,
            UUID entityMemberId,
            SecurityGrantStatus status
    );

    Optional<AppUserEntityScope> findByUser_IdAndWorkspace_IdAndEntityMember_Id(
            UUID userId,
            UUID workspaceId,
            UUID entityMemberId
    );

    List<AppUserEntityScope> findByUser_IdAndWorkspace_IdAndStatusOrderByEntityMember_CodeAsc(
            UUID userId,
            UUID workspaceId,
            SecurityGrantStatus status
    );

    List<AppUserEntityScope> findByUser_IdAndStatusOrderByWorkspace_CodeAscEntityMember_CodeAsc(
            UUID userId,
            SecurityGrantStatus status
    );
}
