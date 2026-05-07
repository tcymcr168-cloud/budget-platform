package com.budgetplatform.metadata.domain;

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
        name = "dimension",
        uniqueConstraints = @UniqueConstraint(name = "uk_dimension_workspace_code", columnNames = {"workspace_id", "code"})
)
public class Dimension {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private BudgetWorkspace workspace;

    @Column(nullable = false, length = 64)
    private String code;

    @Column(nullable = false, length = 160)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "dimension_type", nullable = false, length = 32)
    private DimensionType dimensionType;

    @Column(name = "is_system", nullable = false)
    private boolean system;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private MetadataStatus status;

    @Column(name = "attributes_schema")
    private String attributesSchema;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Dimension() {
    }

    public Dimension(BudgetWorkspace workspace, String code, String name, DimensionType dimensionType, boolean system, String attributesSchema) {
        this.id = UUID.randomUUID();
        this.workspace = workspace;
        this.code = normalizeCode(code);
        this.name = name.trim();
        this.dimensionType = dimensionType;
        this.system = system;
        this.status = MetadataStatus.ACTIVE;
        this.attributesSchema = attributesSchema;
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

    public DimensionType getDimensionType() {
        return dimensionType;
    }

    public boolean isSystem() {
        return system;
    }

    public MetadataStatus getStatus() {
        return status;
    }

    public String getAttributesSchema() {
        return attributesSchema;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void rename(String name) {
        this.name = name.trim();
    }

    public static String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }
}
