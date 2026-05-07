package com.budgetplatform.budgetactual.repository;

import com.budgetplatform.budgetactual.domain.ActualImportRow;
import com.budgetplatform.budgetactual.domain.ActualImportRowStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActualImportRowRepository extends JpaRepository<ActualImportRow, UUID> {

    List<ActualImportRow> findByBatch_IdOrderByRowNumberAsc(UUID batchId);

    List<ActualImportRow> findByBatch_IdAndRowStatusOrderByRowNumberAsc(UUID batchId, ActualImportRowStatus rowStatus);
}
