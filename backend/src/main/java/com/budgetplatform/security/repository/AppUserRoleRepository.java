package com.budgetplatform.security.repository;

import com.budgetplatform.security.domain.AppUserRole;
import com.budgetplatform.security.domain.SecurityRoleCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, UUID> {

    boolean existsByUser_IdAndWorkspace_IdAndRoleCode(UUID userId, UUID workspaceId, SecurityRoleCode roleCode);

    List<AppUserRole> findByUser_IdAndWorkspace_IdOrderByRoleCodeAsc(UUID userId, UUID workspaceId);

    List<AppUserRole> findByUser_IdOrderByWorkspace_CodeAscRoleCodeAsc(UUID userId);
}
