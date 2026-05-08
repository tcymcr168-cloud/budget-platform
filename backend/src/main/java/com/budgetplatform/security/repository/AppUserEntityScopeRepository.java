package com.budgetplatform.security.repository;

import com.budgetplatform.security.domain.AppUserEntityScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppUserEntityScopeRepository extends JpaRepository<AppUserEntityScope, UUID> {

    boolean existsByUser_IdAndWorkspace_IdAndEntityMember_Id(UUID userId, UUID workspaceId, UUID entityMemberId);

    List<AppUserEntityScope> findByUser_IdAndWorkspace_IdOrderByEntityMember_CodeAsc(UUID userId, UUID workspaceId);

    List<AppUserEntityScope> findByUser_IdOrderByWorkspace_CodeAscEntityMember_CodeAsc(UUID userId);
}
