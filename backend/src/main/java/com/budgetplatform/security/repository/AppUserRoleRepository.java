package com.budgetplatform.security.repository;

import com.budgetplatform.security.domain.AppUserRole;
import com.budgetplatform.security.domain.SecurityGrantStatus;
import com.budgetplatform.security.domain.SecurityRoleCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, UUID> {

    boolean existsByUser_IdAndWorkspace_IdAndRoleCodeAndStatus(
            UUID userId,
            UUID workspaceId,
            SecurityRoleCode roleCode,
            SecurityGrantStatus status
    );

    Optional<AppUserRole> findByUser_IdAndWorkspace_IdAndRoleCode(
            UUID userId,
            UUID workspaceId,
            SecurityRoleCode roleCode
    );

    List<AppUserRole> findByUser_IdAndWorkspace_IdAndStatusOrderByRoleCodeAsc(
            UUID userId,
            UUID workspaceId,
            SecurityGrantStatus status
    );

    List<AppUserRole> findByUser_IdAndStatusOrderByWorkspace_CodeAscRoleCodeAsc(
            UUID userId,
            SecurityGrantStatus status
    );

    Optional<AppUserRole> findFirstByWorkspace_IdAndRoleCodeAndStatus(
            UUID workspaceId,
            SecurityRoleCode roleCode,
            SecurityGrantStatus status
    );
}
