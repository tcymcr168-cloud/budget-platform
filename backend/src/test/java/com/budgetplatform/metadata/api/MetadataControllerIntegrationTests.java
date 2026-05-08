package com.budgetplatform.metadata.api;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MetadataControllerIntegrationTests {

    private static final String ADMIN_USER = "admin@example.com";
    private static final String ADMIN_ROLES = "BUDGET_ADMIN";
    private static final String READ_ONLY_USER = "reader@example.com";
    private static final String READ_ONLY_ROLES = "READ_ONLY";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditEventRecordRepository auditRepository;

    @Test
    void createsDimensionAndMemberHierarchy() throws Exception {
        String workspaceId = createWorkspace("BUD003_WS", "BUD-003 Workspace");
        String dimensionId = createDimension(workspaceId, "ENTITY", "Entity", "ENTITY");
        String rootMemberId = createMember(dimensionId, "HQ", "Headquarters", null);

        mockMvc.perform(post("/api/metadata/dimensions/{dimensionId}/members", dimensionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "code": "FIN",
                                  "name": "Finance",
                                  "parentId": "%s",
                                  "sortOrder": 10
                                }
                                """.formatted(rootMemberId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("FIN"))
                .andExpect(jsonPath("$.data.parentId").value(rootMemberId));

        mockMvc.perform(get("/api/metadata/dimensions/{dimensionId}/members", dimensionId))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/metadata/dimensions/{dimensionId}/members", dimensionId)
                        .header("X-User-Id", READ_ONLY_USER)
                        .header("X-User-Roles", READ_ONLY_ROLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));

        assertThat(auditRepository.findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc("budget_workspace", workspaceId))
                .hasSize(1);
        assertThat(auditRepository.findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc("dimension", dimensionId))
                .hasSize(1);
        assertThat(auditRepository.findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc("dimension_member", rootMemberId))
                .hasSize(1);
    }

    @Test
    void rejectsDuplicateDimensionCodeInWorkspace() throws Exception {
        String workspaceId = createWorkspace("DUP_WS", "Duplicate Workspace");
        createDimension(workspaceId, "ACCOUNT", "Account", "ACCOUNT");

        mockMvc.perform(post("/api/metadata/dimensions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "code": "account",
                                  "name": "Duplicate Account",
                                  "dimensionType": "ACCOUNT",
                                  "system": true
                                }
                                """.formatted(workspaceId)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("CONFLICT"));
    }

    @Test
    void rejectsCrossDimensionParent() throws Exception {
        String workspaceId = createWorkspace("CROSS_WS", "Cross Dimension Workspace");
        String accountDimensionId = createDimension(workspaceId, "ACCOUNT", "Account", "ACCOUNT");
        String entityDimensionId = createDimension(workspaceId, "ENTITY", "Entity", "ENTITY");
        String accountMemberId = createMember(accountDimensionId, "EXP", "Expense", null);

        mockMvc.perform(post("/api/metadata/dimensions/{dimensionId}/members", entityDimensionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "code": "FIN",
                                  "name": "Finance",
                                  "parentId": "%s"
                                }
                                """.formatted(accountMemberId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    void rejectsMetadataWriteWithoutMetadataRole() throws Exception {
        String workspaceId = createWorkspace("SEC003B_WS", "SEC-003B Workspace");

        mockMvc.perform(post("/api/metadata/dimensions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", READ_ONLY_USER)
                        .header("X-User-Roles", READ_ONLY_ROLES)
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "code": "ENTITY",
                                  "name": "Entity",
                                  "dimensionType": "ENTITY",
                                  "system": true
                                }
                                """.formatted(workspaceId)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.code").value("FORBIDDEN"));
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

    private String createMember(String dimensionId, String code, String name, String parentId) throws Exception {
        String parentField = parentId == null ? "" : """
                                  ,"parentId": "%s"
                """.formatted(parentId);
        return mockMvc.perform(post("/api/metadata/dimensions/{dimensionId}/members", dimensionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "code": "%s",
                                  "name": "%s"%s
                                }
                                """.formatted(code, name, parentField)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }
}
