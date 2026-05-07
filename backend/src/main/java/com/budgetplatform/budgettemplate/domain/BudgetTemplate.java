package com.budgetplatform.budgettemplate.domain;

import com.budgetplatform.budgetmodel.domain.BudgetModel;
import com.budgetplatform.metadata.domain.Dimension;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "budget_template",
        uniqueConstraints = @UniqueConstraint(name = "uk_budget_template_model_code", columnNames = {"budget_model_id", "code"})
)
public class BudgetTemplate {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_model_id", nullable = false)
    private BudgetModel budgetModel;

    @Column(nullable = false, length = 64)
    private String code;

    @Column(nullable = false, length = 160)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BudgetTemplateStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected BudgetTemplate() {
    }

    public BudgetTemplate(BudgetModel budgetModel, String code, String name, String description) {
        this.id = UUID.randomUUID();
        this.budgetModel = budgetModel;
        this.code = Dimension.normalizeCode(code);
        this.name = name.trim();
        this.description = description == null || description.isBlank() ? null : description.trim();
        this.status = BudgetTemplateStatus.DRAFT;
    }

    @PrePersist
    void beforeCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void beforeUpdate() {
        updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public BudgetModel getBudgetModel() {
        return budgetModel;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BudgetTemplateStatus getStatus() {
        return status;
    }

    public void activate() {
        status = BudgetTemplateStatus.ACTIVE;
    }

    public void deactivate() {
        status = BudgetTemplateStatus.INACTIVE;
    }
}
