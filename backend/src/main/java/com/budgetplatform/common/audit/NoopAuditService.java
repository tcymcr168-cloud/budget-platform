package com.budgetplatform.common.audit;

class NoopAuditService implements AuditService {

    @Override
    public void record(AuditEvent event) {
        // Kept as a lightweight fallback for tests or future profiles that explicitly avoid persistence.
    }
}
