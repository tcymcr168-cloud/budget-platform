package com.budgetplatform.budgetmodel.repository;

import com.budgetplatform.budgetmodel.domain.BudgetModelDimensionBinding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetModelDimensionBindingRepository extends JpaRepository<BudgetModelDimensionBinding, UUID> {

    boolean existsByBudgetModel_IdAndDimension_Id(UUID budgetModelId, UUID dimensionId);

    List<BudgetModelDimensionBinding> findByBudgetModel_IdOrderByDisplayOrderAscDimension_CodeAsc(UUID budgetModelId);
}
