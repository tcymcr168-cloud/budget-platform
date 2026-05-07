package com.budgetplatform.metadata.repository;

import com.budgetplatform.metadata.domain.Dimension;
import com.budgetplatform.metadata.domain.DimensionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DimensionRepository extends JpaRepository<Dimension, UUID> {

    boolean existsByWorkspaceIdAndCode(UUID workspaceId, String code);

    List<Dimension> findByWorkspaceIdOrderByCode(UUID workspaceId);

    List<Dimension> findByWorkspaceIdAndDimensionTypeOrderByCode(UUID workspaceId, DimensionType dimensionType);

    Optional<Dimension> findByWorkspaceIdAndCode(UUID workspaceId, String code);
}
