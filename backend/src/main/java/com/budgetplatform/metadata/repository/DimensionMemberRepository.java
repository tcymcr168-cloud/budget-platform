package com.budgetplatform.metadata.repository;

import com.budgetplatform.metadata.domain.DimensionMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DimensionMemberRepository extends JpaRepository<DimensionMember, UUID> {

    boolean existsByDimensionIdAndCode(UUID dimensionId, String code);

    boolean existsByDimensionIdAndParentId(UUID dimensionId, UUID parentId);

    List<DimensionMember> findByDimensionIdOrderBySortOrderAscCodeAsc(UUID dimensionId);

    Optional<DimensionMember> findByDimensionIdAndCode(UUID dimensionId, String code);
}
