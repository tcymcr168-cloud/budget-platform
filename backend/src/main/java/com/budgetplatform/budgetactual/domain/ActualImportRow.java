package com.budgetplatform.budgetactual.domain;

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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "actual_import_row")
public class ActualImportRow {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "import_batch_id", nullable = false)
    private ActualImportBatch batch;

    @Column(name = "row_number", nullable = false)
    private int rowNumber;

    @Column(name = "account_code", nullable = false, length = 64)
    private String accountCode;

    @Column(name = "entity_code", nullable = false, length = 64)
    private String entityCode;

    @Column(name = "time_code", nullable = false, length = 64)
    private String timeCode;

    @Column(name = "category_code", nullable = false, length = 64)
    private String categoryCode;

    @Column(name = "version_code", nullable = false, length = 64)
    private String versionCode;

    @Column(precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "row_status", nullable = false, length = 32)
    private ActualImportRowStatus rowStatus;

    @Column(name = "error_message")
    private String errorMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_member_id")
    private DimensionMember accountMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_member_id")
    private DimensionMember entityMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_member_id")
    private DimensionMember timeMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_member_id")
    private DimensionMember categoryMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_member_id")
    private DimensionMember versionMember;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ActualImportRow() {
    }

    public ActualImportRow(
            ActualImportBatch batch,
            int rowNumber,
            String accountCode,
            String entityCode,
            String timeCode,
            String categoryCode,
            String versionCode,
            BigDecimal amount,
            ActualImportRowStatus rowStatus,
            String errorMessage,
            DimensionMember accountMember,
            DimensionMember entityMember,
            DimensionMember timeMember,
            DimensionMember categoryMember,
            DimensionMember versionMember
    ) {
        this.id = UUID.randomUUID();
        this.batch = batch;
        this.rowNumber = rowNumber;
        this.accountCode = accountCode;
        this.entityCode = entityCode;
        this.timeCode = timeCode;
        this.categoryCode = categoryCode;
        this.versionCode = versionCode;
        this.amount = amount;
        this.rowStatus = rowStatus;
        this.errorMessage = errorMessage == null || errorMessage.isBlank() ? null : errorMessage.trim();
        this.accountMember = accountMember;
        this.entityMember = entityMember;
        this.timeMember = timeMember;
        this.categoryMember = categoryMember;
        this.versionMember = versionMember;
    }

    @PrePersist
    void beforeCreate() {
        createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public ActualImportBatch getBatch() {
        return batch;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ActualImportRowStatus getRowStatus() {
        return rowStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public DimensionMember getAccountMember() {
        return accountMember;
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
}
