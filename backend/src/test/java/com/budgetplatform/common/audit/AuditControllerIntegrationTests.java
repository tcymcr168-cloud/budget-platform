package com.budgetplatform.common.audit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuditControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditService auditService;

    @Test
    void searchesAuditEventsWithAdminHeaderAndFilters() throws Exception {
        auditService.record(new AuditEvent(
                "audit.reader@example.com",
                "audit_query_subject",
                "subject-101",
                AuditAction.ACCESS_CHANGE,
                Instant.parse("2026-05-08T01:00:00Z"),
                Map.of("roleCode", "READ_ONLY")
        ));

        mockMvc.perform(get("/api/audit/events")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .param("actorId", "audit.reader@example.com")
                        .param("subjectType", "audit_query_subject")
                        .param("action", "ACCESS_CHANGE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.data.items[0].actorId").value("audit.reader@example.com"))
                .andExpect(jsonPath("$.data.items[0].subjectType").value("audit_query_subject"))
                .andExpect(jsonPath("$.data.items[0].action").value("ACCESS_CHANGE"))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(10));
    }

    @Test
    void rejectsAuditSearchWithoutAdminHeaderRole() throws Exception {
        mockMvc.perform(get("/api/audit/events")
                        .header("X-User-Id", "reader@example.com")
                        .header("X-User-Roles", "READ_ONLY"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.code").value("FORBIDDEN"));
    }

    @Test
    void rejectsInvalidAuditSearchParameters() throws Exception {
        mockMvc.perform(get("/api/audit/events")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .param("action", "UNKNOWN"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));

        mockMvc.perform(get("/api/audit/events")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .param("size", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }
}
