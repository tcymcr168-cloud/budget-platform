package com.budgetplatform.budgettemplate.repository;

import com.budgetplatform.budgettemplate.domain.BudgetTemplateAxis;
import com.budgetplatform.budgettemplate.domain.TemplateAxisType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BudgetTemplateAxisRepository extends JpaRepository<BudgetTemplateAxis, UUID> {

    boolean existsByBudgetTemplate_IdAndModelDimension_Id(UUID budgetTemplateId, UUID modelDimensionId);

    boolean existsByBudgetTemplate_IdAndAxisType(UUID budgetTemplateId, TemplateAxisType axisType);

    List<BudgetTemplateAxis> findByBudgetTemplate_IdOrderByAxisTypeAscDisplayOrderAsc(UUID budgetTemplateId);
}
