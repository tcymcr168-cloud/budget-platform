package com.budgetplatform.common.audit;

public interface AuditService {

    void record(AuditEvent event);
}
