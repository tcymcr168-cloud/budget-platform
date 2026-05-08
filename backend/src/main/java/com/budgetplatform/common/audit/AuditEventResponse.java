package com.budgetplatform.common.audit;

import java.time.Instant;
import java.util.UUID;

public record AuditEventResponse(
        UUID id,
        String actorId,
        String subjectType,
        String subjectId,
        AuditAction action,
        Instant occurredAt,
        String detailsJson
) {

    public static AuditEventResponse from(AuditEventRecord record) {
        return new AuditEventResponse(
                record.getId(),
                record.getActorId(),
                record.getSubjectType(),
                record.getSubjectId(),
                record.getAction(),
                record.getOccurredAt(),
                record.getDetailsJson()
        );
    }
}
