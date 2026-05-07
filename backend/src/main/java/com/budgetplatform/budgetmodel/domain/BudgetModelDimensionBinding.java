package com.budgetplatform.budgetmodel.domain;

import com.budgetplatform.metadata.domain.Dimension;
import com.budgetplatform.metadata.domain.DimensionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "budget_model_dimension",
        uniqueConstraints = @UniqueConstraint(name = "uk_budget_model_dimension", columnNames = {"budget_model_id", "dimension_id"})
)
public class BudgetModelDimensionBinding {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_model_id", nullable = false)
    private BudgetModel budgetModel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dimension_id", nullable = false)
    private Dimension dimension;

    @Enumerated(EnumType.STRING)
    @Column(name = "dimension_role", nullable = false, length = 32)
    private DimensionType dimensionRole;

    @Column(name = "is_required_for_entry", nullable = false)
    private boolean requiredForEntry;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected BudgetModelDimensionBinding() {
    }

    public BudgetModelDimensionBinding(
            BudgetModel budgetModel,
            Dimension dimension,
            boolean requiredForEntry,
            int displayOrder
    ) {
        this.id = UUID.randomUUID();
        this.budgetModel = budgetModel;
        this.dimension = dimension;
        this.dimensionRole = dimension.getDimensionType();
        this.requiredForEntry = requiredForEntry;
        this.displayOrder = displayOrder;
    }

    @PrePersist
    void beforeCreate() {
        createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public BudgetModel getBudgetModel() {
        return budgetModel;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public DimensionType getDimensionRole() {
        return dimensionRole;
    }

    public boolean isRequiredForEntry() {
        return requiredForEntry;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
