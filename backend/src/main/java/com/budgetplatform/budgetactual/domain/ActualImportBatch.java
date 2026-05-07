package com.budgetplatform.budgetactual.domain;

import com.budgetplatform.budgetmodel.domain.BudgetModel;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "actual_import_batch")
public class ActualImportBatch {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "budget_model_id", nullable = false)
    private BudgetModel budgetModel;

    @Column(name = "file_name", nullable = false, length = 240)
    private String fileName;

    @Column(name = "operator_user", nullable = false, length = 120)
    private String operatorUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ActualImportStatus status;

    @Column(name = "total_rows", nullable = false)
    private int totalRows;

    @Column(name = "valid_rows", nullable = false)
    private int validRows;

    @Column(name = "error_rows", nullable = false)
    private int errorRows;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(name = "error_report")
    private String errorReport;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected ActualImportBatch() {
    }

    public ActualImportBatch(BudgetModel budgetModel, String fileName, String operatorUser) {
        this.id = UUID.randomUUID();
        this.budgetModel = budgetModel;
        this.fileName = normalize(fileName, "actual-import.csv");
        this.operatorUser = normalize(operatorUser, "import.operator");
        this.status = ActualImportStatus.VALIDATED;
        this.totalAmount = BigDecimal.ZERO;
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

    public String getFileName() {
        return fileName;
    }

    public String getOperatorUser() {
        return operatorUser;
    }

    public ActualImportStatus getStatus() {
        return status;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getValidRows() {
        return validRows;
    }

    public int getErrorRows() {
        return errorRows;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getErrorReport() {
        return errorReport;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void updateValidationSummary(int totalRows, int validRows, int errorRows, BigDecimal totalAmount, String errorReport) {
        this.totalRows = totalRows;
        this.validRows = validRows;
        this.errorRows = errorRows;
        this.totalAmount = totalAmount;
        this.errorReport = normalizeBlank(errorReport);
        this.status = ActualImportStatus.VALIDATED;
    }

    public void markFailed(String errorReport) {
        this.errorReport = normalizeBlank(errorReport);
        this.status = ActualImportStatus.FAILED;
    }

    public void commit() {
        this.status = ActualImportStatus.COMMITTED;
    }

    private String normalize(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private String normalizeBlank(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
