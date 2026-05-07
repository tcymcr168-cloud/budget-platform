package com.budgetplatform.budgettemplate.repository;

import com.budgetplatform.budgettemplate.domain.BudgetTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetTemplateRepository extends JpaRepository<BudgetTemplate, UUID> {

    boolean existsByBudgetModel_IdAndCode(UUID budgetModelId, String code);

    List<BudgetTemplate> findByBudgetModel_IdOrderByCode(UUID budgetModelId);
}
