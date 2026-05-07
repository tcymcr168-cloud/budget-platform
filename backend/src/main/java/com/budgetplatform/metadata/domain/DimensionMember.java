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
        name = "dimension_member",
        uniqueConstraints = @UniqueConstraint(name = "uk_dimension_member_dimension_code", columnNames = {"dimension_id", "code"})
)
public class DimensionMember {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dimension_id", nullable = false)
    private Dimension dimension;

    @Column(nullable = false, length = 64)
    private String code;

    @Column(nullable = false, length = 160)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private DimensionMember parent;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_leaf", nullable = false)
    private boolean leaf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private MetadataStatus status;

    @Column(name = "attributes")
    private String attributes;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected DimensionMember() {
    }

    public DimensionMember(Dimension dimension, String code, String name, DimensionMember parent, Integer sortOrder, String attributes) {
        this.id = UUID.randomUUID();
        this.dimension = dimension;
        this.code = Dimension.normalizeCode(code);
        this.name = name.trim();
        this.parent = parent;
        this.sortOrder = sortOrder;
        this.leaf = true;
        this.status = MetadataStatus.ACTIVE;
        this.attributes = attributes;
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

    public Dimension getDimension() {
        return dimension;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public DimensionMember getParent() {
        return parent;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public MetadataStatus getStatus() {
        return status;
    }

    public String getAttributes() {
        return attributes;
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

    public void moveTo(DimensionMember parent) {
        this.parent = parent;
    }

    public void markLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public void setStatus(MetadataStatus status) {
        this.status = status;
    }
}
