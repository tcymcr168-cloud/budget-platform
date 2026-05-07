package com.budgetplatform.metadata.repository;

import com.budgetplatform.metadata.domain.BudgetWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BudgetWorkspaceRepository extends JpaRepository<BudgetWorkspace, UUID> {

    boolean existsByCode(String code);

    Optional<BudgetWorkspace> findByCode(String code);
}
