package com.budgetplatform.security.domain;

import com.budgetplatform.metadata.domain.BudgetWorkspace;
import com.budgetplatform.metadata.domain.DimensionMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
        name = "app_user_entity_scope",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_app_user_entity_scope_user_workspace_member",
                columnNames = {"user_id", "workspace_id", "entity_member_id"}
        )
)
public class AppUserEntityScope {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private BudgetWorkspace workspace;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_member_id", nullable = false)
    private DimensionMember entityMember;

    @Column(name = "include_descendants", nullable = false)
    private boolean includeDescendants;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected AppUserEntityScope() {
    }

    public AppUserEntityScope(
            AppUser user,
            BudgetWorkspace workspace,
            DimensionMember entityMember,
            boolean includeDescendants
    ) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.workspace = workspace;
        this.entityMember = entityMember;
        this.includeDescendants = includeDescendants;
    }

    @PrePersist
    void beforeCreate() {
        createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public AppUser getUser() {
        return user;
    }

    public BudgetWorkspace getWorkspace() {
        return workspace;
    }

    public DimensionMember getEntityMember() {
        return entityMember;
    }

    public boolean isIncludeDescendants() {
        return includeDescendants;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
