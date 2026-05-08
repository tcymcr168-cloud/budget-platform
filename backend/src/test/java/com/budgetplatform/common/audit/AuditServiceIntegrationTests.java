package com.budgetplatform.common.audit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AuditServiceIntegrationTests {

    @Autowired
    private AuditService auditService;

    @Autowired
    private AuditEventRecordRepository auditRepository;

    @Test
    void persistsAuditEventDetailsAsJson() {
        AuditEvent event = new AuditEvent(
                "auditor@example.com",
                "audit_test_subject",
                "subject-001",
                AuditAction.ACCESS_CHANGE,
                Instant.parse("2026-05-08T00:00:00Z"),
                Map.of("roleCode", "BUDGET_ADMIN", "includeDescendants", true)
        );

        auditService.record(event);

        var records = auditRepository.findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc(
                "audit_test_subject",
                "subject-001"
        );
        assertThat(records).hasSize(1);
        assertThat(records.get(0).getActorId()).isEqualTo("auditor@example.com");
        assertThat(records.get(0).getAction()).isEqualTo(AuditAction.ACCESS_CHANGE);
        assertThat(records.get(0).getDetailsJson()).contains("\"roleCode\":\"BUDGET_ADMIN\"");
        assertThat(records.get(0).getDetailsJson()).contains("\"includeDescendants\":true");
    }
}
