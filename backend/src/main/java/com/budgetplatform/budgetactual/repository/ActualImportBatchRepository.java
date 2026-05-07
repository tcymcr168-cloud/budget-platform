package com.budgetplatform.budgetactual.repository;

import com.budgetplatform.budgetactual.domain.ActualImportBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActualImportBatchRepository extends JpaRepository<ActualImportBatch, UUID> {

    List<ActualImportBatch> findByBudgetModel_IdOrderByUpdatedAtDesc(UUID budgetModelId);
}
