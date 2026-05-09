package com.budgetplatform.security.api;

import com.budgetplatform.common.audit.AuditEventRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "budget-platform.auth.mode=REVERSE_PROXY",
        "budget-platform.auth.reverse-proxy-user-header=X-Trusted-User"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityControllerReverseProxyIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditEventRecordRepository auditRepository;

    @Test
    void resolvesCurrentUserFromTrustedReverseProxyHeader() throws Exception {
        mockMvc.perform(get("/api/security/me")
                        .header("X-Trusted-User", " proxy.admin@example.com ")
                        .header("X-User-Id", "attacker@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value("proxy.admin@example.com"))
                .andExpect(jsonPath("$.data.authMode").value("REVERSE_PROXY"))
                .andExpect(jsonPath("$.data.authenticated").value(true))
                .andExpect(jsonPath("$.data.roles", hasSize(0)))
                .andExpect(jsonPath("$.data.applicationRoles", hasSize(0)))
                .andExpect(jsonPath("$.data.entityScopes", hasSize(0)));
    }

    @Test
    void rejectsCurrentUserWhenTrustedReverseProxyHeaderIsMissing() throws Exception {
        mockMvc.perform(get("/api/security/me")
                        .header("X-User-Id", "attacker@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .header("X-Request-Id", "auth-missing-principal-test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));

        assertThat(auditRepository.findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc("authentication", "failure"))
                .anySatisfy(event -> {
                    assertThat(event.getAction().name()).isEqualTo("AUTH_FAILURE");
                    assertThat(event.getActorId()).isNull();
                    assertThat(event.getDetailsJson()).contains("MISSING_REVERSE_PROXY_PRINCIPAL");
                    assertThat(event.getDetailsJson()).contains("X-Trusted-User");
                    assertThat(event.getDetailsJson()).contains("auth-missing-principal-test");
                });
    }

    @Test
    void recordsAuditActorFromTrustedReverseProxyHeader() throws Exception {
        String userId = mockMvc.perform(post("/api/security/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Trusted-User", "admin@example.com")
                        .header("X-User-Id", "attacker@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .content("""
                                {
                                  "username": "Reverse.Proxy.Created@Example.com",
                                  "displayName": "Reverse Proxy Created"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("reverse.proxy.created@example.com"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");

        assertThat(auditRepository.findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc("app_user", userId))
                .singleElement()
                .satisfies(event -> assertThat(event.getActorId()).isEqualTo("admin@example.com"));
    }
}
