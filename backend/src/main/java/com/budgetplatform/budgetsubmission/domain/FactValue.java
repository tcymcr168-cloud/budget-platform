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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "fact_value",
        uniqueConstraints = @UniqueConstraint(name = "uk_fact_value_submission_account", columnNames = {"submission_task_id", "account_member_id"})
)
public class FactValue {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_model_id", nullable = false)
    private BudgetModel budgetModel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_template_id", nullable = false)
    private BudgetTemplate budgetTemplate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_task_id", nullable = false)
    private SubmissionTask submissionTask;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_member_id", nullable = false)
    private DimensionMember accountMember;

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

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_status", nullable = false, length = 32)
    private FactValueStatus valueStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 32)
    private FactSourceType sourceType;

    @Column
    private String note;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected FactValue() {
    }

    public FactValue(SubmissionTask task, DimensionMember accountMember, BigDecimal amount, String note) {
        this.id = UUID.randomUUID();
        this.budgetModel = task.getBudgetModel();
        this.budgetTemplate = task.getBudgetTemplate();
        this.submissionTask = task;
        this.accountMember = accountMember;
        this.entityMember = task.getEntityMember();
        this.timeMember = task.getTimeMember();
        this.categoryMember = task.getCategoryMember();
        this.versionMember = task.getVersionMember();
        this.amount = amount;
        this.valueStatus = FactValueStatus.DRAFT;
        this.sourceType = FactSourceType.BUDGET_SUBMISSION;
        this.note = normalizeNote(note);
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

    public SubmissionTask getSubmissionTask() {
        return submissionTask;
    }

    public DimensionMember getAccountMember() {
        return accountMember;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public FactValueStatus getValueStatus() {
        return valueStatus;
    }

    public FactSourceType getSourceType() {
        return sourceType;
    }

    public String getNote() {
        return note;
    }

    public void updateAmount(BigDecimal amount, String note) {
        this.amount = amount;
        this.note = normalizeNote(note);
        this.valueStatus = FactValueStatus.DRAFT;
    }

    public void setValueStatus(FactValueStatus valueStatus) {
        this.valueStatus = valueStatus;
    }

    private String normalizeNote(String note) {
        return note == null || note.isBlank() ? null : note.trim();
    }
}
