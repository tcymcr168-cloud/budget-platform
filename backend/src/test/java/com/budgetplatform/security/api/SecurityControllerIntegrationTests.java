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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityControllerIntegrationTests {

    private static final String ADMIN_USER = "admin@example.com";
    private static final String ADMIN_ROLES = "BUDGET_ADMIN";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditEventRecordRepository auditRepository;

    @Test
    void createsUserAndGrantsRoleAndEntityScope() throws Exception {
        String workspaceId = createWorkspace("SEC002_WS", "SEC-002 Workspace");
        String entityDimensionId = createDimension(workspaceId, "SEC002_ENTITY", "Entity", "ENTITY");
        String entityMemberId = createMember(entityDimensionId, "SEC002_OPS", "Operations");

        String userId = mockMvc.perform(post("/api/security/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .content("""
                                {
                                  "username": "Budget.Owner@Example.com",
                                  "displayName": "Budget Owner",
                                  "email": "Budget.Owner@Example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("budget.owner@example.com"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");

        String roleId = mockMvc.perform(post("/api/security/users/{userId}/roles", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "roleCode": "BUDGET_OWNER"
                                }
                                """.formatted(workspaceId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.roleCode").value("BUDGET_OWNER"))
                .andExpect(jsonPath("$.data.workspaceId").value(workspaceId))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");

        String scopeId = mockMvc.perform(post("/api/security/users/{userId}/entity-scopes", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "entityMemberId": "%s",
                                  "includeDescendants": true
                                }
                                """.formatted(workspaceId, entityMemberId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.entityMemberCode").value("SEC002_OPS"))
                .andExpect(jsonPath("$.data.includeDescendants").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(get("/api/security/users/{userId}/roles", userId)
                        .param("workspaceId", workspaceId)
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));

        mockMvc.perform(get("/api/security/users/{userId}/entity-scopes", userId)
                        .param("workspaceId", workspaceId)
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));

        assertThat(auditRepository.findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc("app_user", userId))
                .hasSize(1);
        assertThat(auditRepository.findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc("app_user_role", roleId))
                .hasSize(1);
        assertThat(auditRepository.findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc("app_user_entity_scope", scopeId))
                .hasSize(1);
    }

    @Test
    void rejectsDuplicateRoleGrantAndNonEntityScopeMember() throws Exception {
        String workspaceId = createWorkspace("SEC002_REJECT_WS", "SEC-002 Reject Workspace");
        String accountDimensionId = createDimension(workspaceId, "SEC002_ACCOUNT", "Account", "ACCOUNT");
        String accountMemberId = createMember(accountDimensionId, "SEC002_EXP", "Expense");
        String userId = createSecurityUser("sec.reject@example.com");

        mockMvc.perform(post("/api/security/users/{userId}/roles", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "roleCode": "READ_ONLY"
                                }
                                """.formatted(workspaceId)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/security/users/{userId}/roles", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "roleCode": "READ_ONLY"
                                }
                                """.formatted(workspaceId)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("CONFLICT"));

        mockMvc.perform(post("/api/security/users/{userId}/entity-scopes", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "entityMemberId": "%s",
                                  "includeDescendants": false
                                }
                                """.formatted(workspaceId, accountMemberId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    void resolvesCurrentUserFromHeaders() throws Exception {
        mockMvc.perform(get("/api/security/me")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN,READ_ONLY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value("admin@example.com"))
                .andExpect(jsonPath("$.data.authenticated").value(true))
                .andExpect(jsonPath("$.data.roles", hasSize(2)));
    }

    @Test
    void rejectsSecurityManagementWithoutAdminRole() throws Exception {
        mockMvc.perform(get("/api/security/users")
                        .header("X-User-Id", "reader@example.com")
                        .header("X-User-Roles", "READ_ONLY"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.code").value("FORBIDDEN"));
    }

    private String createSecurityUser(String username) throws Exception {
        return mockMvc.perform(post("/api/security/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .content("""
                                {
                                  "username": "%s",
                                  "displayName": "%s"
                                }
                                """.formatted(username, username)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private String createWorkspace(String code, String name) throws Exception {
        return mockMvc.perform(post("/api/metadata/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "code": "%s",
                                  "name": "%s"
                                }
                                """.formatted(code, name)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private String createDimension(String workspaceId, String code, String name, String type) throws Exception {
        return mockMvc.perform(post("/api/metadata/dimensions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "code": "%s",
                                  "name": "%s",
                                  "dimensionType": "%s",
                                  "system": true
                                }
                                """.formatted(workspaceId, code, name, type)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private String createMember(String dimensionId, String code, String name) throws Exception {
        return mockMvc.perform(post("/api/metadata/dimensions/{dimensionId}/members", dimensionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "code": "%s",
                                  "name": "%s"
                                }
                                """.formatted(code, name)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }
}
