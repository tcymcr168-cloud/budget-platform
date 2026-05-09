package com.budgetplatform.security.domain;

import com.budgetplatform.metadata.domain.BudgetWorkspace;
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
        name = "app_user_role",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_app_user_role_user_workspace_role",
                columnNames = {"user_id", "workspace_id", "role_code"}
        )
)
public class AppUserRole {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false)
    private BudgetWorkspace workspace;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_code", nullable = false, length = 48)
    private SecurityRoleCode roleCode;

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

    protected AppUserRole() {
    }

    public AppUserRole(AppUser user, BudgetWorkspace workspace, SecurityRoleCode roleCode) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.workspace = workspace;
        this.roleCode = roleCode;
        this.status = SecurityGrantStatus.ACTIVE;
    }

    @PrePersist
    void beforeCreate() {
        createdAt = Instant.now();
        if (status == null) {
            status = SecurityGrantStatus.ACTIVE;
        }
    }

    public void reactivate() {
        this.status = SecurityGrantStatus.ACTIVE;
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

    public SecurityRoleCode getRoleCode() {
        return roleCode;
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
