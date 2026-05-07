package com.budgetplatform.budgetsubmission.domain;

import com.budgetplatform.budgetmodel.domain.BudgetModel;
import com.budgetplatform.budgettemplate.domain.BudgetTemplate;
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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "submission_task",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_submission_scope",
                columnNames = {
                        "budget_template_id",
                        "entity_member_id",
                        "time_member_id",
                        "category_member_id",
                        "version_member_id"
                }
        )
)
public class SubmissionTask {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_template_id", nullable = false)
    private BudgetTemplate budgetTemplate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_model_id", nullable = false)
    private BudgetModel budgetModel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "entity_member_id", nullable = false)
    private DimensionMember entityMember;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "time_member_id", nullable = false)
    private DimensionMember timeMember;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_member_id", nullable = false)
    private DimensionMember categoryMember;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "version_member_id", nullable = false)
    private DimensionMember versionMember;

    @Column(name = "owner_user", nullable = false, length = 120)
    private String ownerUser;

    @Column(name = "reviewer_user", nullable = false, length = 120)
    private String reviewerUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private SubmissionStatus status;

    @Column(name = "return_reason")
    private String returnReason;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected SubmissionTask() {
    }

    public SubmissionTask(
            BudgetTemplate budgetTemplate,
            DimensionMember entityMember,
            DimensionMember timeMember,
            DimensionMember categoryMember,
            DimensionMember versionMember,
            String ownerUser,
            String reviewerUser
    ) {
        this.id = UUID.randomUUID();
        this.budgetTemplate = budgetTemplate;
        this.budgetModel = budgetTemplate.getBudgetModel();
        this.entityMember = entityMember;
        this.timeMember = timeMember;
        this.categoryMember = categoryMember;
        this.versionMember = versionMember;
        this.ownerUser = ownerUser.trim();
        this.reviewerUser = reviewerUser.trim();
        this.status = SubmissionStatus.NOT_STARTED;
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

    public BudgetTemplate getBudgetTemplate() {
        return budgetTemplate;
    }

    public BudgetModel getBudgetModel() {
        return budgetModel;
    }

    public DimensionMember getEntityMember() {
        return entityMember;
    }

    public DimensionMember getTimeMember() {
        return timeMember;
    }

    public DimensionMember getCategoryMember() {
        return categoryMember;
    }

    public DimensionMember getVersionMember() {
        return versionMember;
    }

    public String getOwnerUser() {
        return ownerUser;
    }

    public String getReviewerUser() {
        return reviewerUser;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void markDraft() {
        status = SubmissionStatus.DRAFT;
        returnReason = null;
    }

    public void submit() {
        status = SubmissionStatus.SUBMITTED;
        returnReason = null;
    }

    public void returnForRevision(String reason) {
        status = SubmissionStatus.RETURNED;
        returnReason = reason == null || reason.isBlank() ? null : reason.trim();
    }

    public void approve() {
        status = SubmissionStatus.APPROVED;
        returnReason = null;
    }

    public void lock() {
        status = SubmissionStatus.LOCKED;
    }
}
