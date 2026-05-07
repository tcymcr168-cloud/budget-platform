package com.budgetplatform.metadata.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsDimensionAndMemberHierarchy() throws Exception {
        String workspaceId = createWorkspace("BUD003_WS", "BUD-003 Workspace");
        String dimensionId = createDimension(workspaceId, "ENTITY", "Entity", "ENTITY");
        String rootMemberId = createMember(dimensionId, "HQ", "Headquarters", null);

        mockMvc.perform(post("/api/metadata/dimensions/{dimensionId}/members", dimensionId)
                        .contentType(MediaType.APPLICATION_JSON)
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    void rejectsDuplicateDimensionCodeInWorkspace() throws Exception {
        String workspaceId = createWorkspace("DUP_WS", "Duplicate Workspace");
        createDimension(workspaceId, "ACCOUNT", "Account", "ACCOUNT");

        mockMvc.perform(post("/api/metadata/dimensions")
                        .contentType(MediaType.APPLICATION_JSON)
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

    private String createWorkspace(String code, String name) throws Exception {
        return mockMvc.perform(post("/api/metadata/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
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
