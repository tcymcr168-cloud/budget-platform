package com.budgetplatform.budgettemplate.domain;

import com.budgetplatform.budgetmodel.domain.BudgetModelDimensionBinding;
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
        name = "budget_template_axis",
        uniqueConstraints = @UniqueConstraint(name = "uk_budget_template_axis_dimension", columnNames = {"budget_template_id", "model_dimension_id"})
)
public class BudgetTemplateAxis {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_template_id", nullable = false)
    private BudgetTemplate budgetTemplate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "model_dimension_id", nullable = false)
    private BudgetModelDimensionBinding modelDimension;

    @Enumerated(EnumType.STRING)
    @Column(name = "axis_type", nullable = false, length = 32)
    private TemplateAxisType axisType;

    @Column(name = "member_selector", nullable = false, length = 64)
    private String memberSelector;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected BudgetTemplateAxis() {
    }

    public BudgetTemplateAxis(
            BudgetTemplate budgetTemplate,
            BudgetModelDimensionBinding modelDimension,
            TemplateAxisType axisType,
            String memberSelector,
            int displayOrder
    ) {
        this.id = UUID.randomUUID();
        this.budgetTemplate = budgetTemplate;
        this.modelDimension = modelDimension;
        this.axisType = axisType;
        this.memberSelector = memberSelector == null || memberSelector.isBlank() ? "ALL_LEAF" : memberSelector.trim().toUpperCase();
        this.displayOrder = displayOrder;
    }

    @PrePersist
    void beforeCreate() {
        createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public BudgetTemplate getBudgetTemplate() {
        return budgetTemplate;
    }

    public BudgetModelDimensionBinding getModelDimension() {
        return modelDimension;
    }

    public TemplateAxisType getAxisType() {
        return axisType;
    }

    public String getMemberSelector() {
        return memberSelector;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }
}
