package com.budgetplatform.common.audit;

import org.springframework.stereotype.Service;

@Service
class NoopAuditService implements AuditService {

    @Override
    public void record(AuditEvent event) {
        // Persistence is intentionally deferred until the audit storage stage.
    }
}
