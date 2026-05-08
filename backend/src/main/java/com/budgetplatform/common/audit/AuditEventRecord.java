package com.budgetplatform.common.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_event")
public class AuditEventRecord {

    @Id
    private UUID id;

    @Column(name = "actor_id", length = 160)
    private String actorId;

    @Column(name = "subject_type", nullable = false, length = 80)
    private String subjectType;

    @Column(name = "subject_id", length = 120)
    private String subjectId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 48)
    private AuditAction action;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "details_json", nullable = false, columnDefinition = "TEXT")
    private String detailsJson;

    protected AuditEventRecord() {
    }

    public AuditEventRecord(AuditEvent event, String detailsJson) {
        this.id = UUID.randomUUID();
        this.actorId = event.actorId();
        this.subjectType = event.subjectType();
        this.subjectId = event.subjectId();
        this.action = event.action();
        this.occurredAt = event.occurredAt();
        this.detailsJson = detailsJson;
    }

    public UUID getId() {
        return id;
    }

    public String getActorId() {
        return actorId;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public AuditAction getAction() {
        return action;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getDetailsJson() {
        return detailsJson;
    }
}
