package com.budgetplatform.common.audit;

import java.time.Instant;
import java.util.Map;

public record AuditEvent(
        String actorId,
        String subjectType,
        String subjectId,
        AuditAction action,
        Instant occurredAt,
        Map<String, Object> details
) {
    public AuditEvent {
        if (occurredAt == null) {
            occurredAt = Instant.now();
        }
        if (details == null) {
            details = Map.of();
        }
    }
}
