package com.budgetplatform.budgetmodel.repository;

import com.budgetplatform.budgetmodel.domain.BudgetModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetModelRepository extends JpaRepository<BudgetModel, UUID> {

    boolean existsByWorkspaceIdAndCode(UUID workspaceId, String code);

    List<BudgetModel> findByWorkspaceIdOrderByCode(UUID workspaceId);
}
