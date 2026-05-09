package com.budgetplatform.security.domain;

import com.budgetplatform.metadata.domain.BudgetWorkspace;
import com.budgetplatform.metadata.domain.DimensionMember;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SecurityGrantStatus status;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "revoked_by", length = 120)
    private String revokedBy;

    @Column(name = "revoked_reason", length = 240)
    private String revokedReason;

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
        this.status = SecurityGrantStatus.ACTIVE;
    }

    @PrePersist
    void beforeCreate() {
        createdAt = Instant.now();
        if (status == null) {
            status = SecurityGrantStatus.ACTIVE;
        }
    }

    public void reactivate(boolean includeDescendants) {
        this.status = SecurityGrantStatus.ACTIVE;
        this.includeDescendants = includeDescendants;
        this.revokedAt = null;
        this.revokedBy = null;
        this.revokedReason = null;
    }

    public void revoke(String actorId, String reason) {
        this.status = SecurityGrantStatus.REVOKED;
        this.revokedAt = Instant.now();
        this.revokedBy = actorId;
        this.revokedReason = reason;
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

    public SecurityGrantStatus getStatus() {
        return status;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public String getRevokedBy() {
        return revokedBy;
    }

    public String getRevokedReason() {
        return revokedReason;
    }
}
