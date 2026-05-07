package com.budgetplatform.budgetmodel.domain;

import com.budgetplatform.metadata.domain.BudgetWorkspace;
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
        name = "budget_model",
        uniqueConstraints = @UniqueConstraint(name = "uk_budget_model_workspace_code", columnNames = {"workspace_id", "code"})
)
public class BudgetModel {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private BudgetWorkspace workspace;

    @Column(nullable = false, length = 64)
    private String code;

    @Column(nullable = false, length = 160)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private BudgetModelStatus status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected BudgetModel() {
    }

    public BudgetModel(BudgetWorkspace workspace, String code, String name, String description) {
        this.id = UUID.randomUUID();
        this.workspace = workspace;
        this.code = Dimension.normalizeCode(code);
        this.name = name.trim();
        this.description = description == null || description.isBlank() ? null : description.trim();
        this.status = BudgetModelStatus.DRAFT;
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

    public BudgetWorkspace getWorkspace() {
        return workspace;
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

    public BudgetModelStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void activate() {
        status = BudgetModelStatus.ACTIVE;
    }

    public void deactivate() {
        status = BudgetModelStatus.INACTIVE;
    }
}
