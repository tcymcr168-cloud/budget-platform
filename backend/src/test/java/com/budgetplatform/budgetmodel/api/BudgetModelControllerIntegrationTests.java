package com.budgetplatform.budgetmodel.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BudgetModelControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsBudgetModelBindsDimensionsAndActivates() throws Exception {
        String workspaceId = createWorkspace("BUD005_WS", "BUD-005 Workspace");
        String accountId = createDimension(workspaceId, "BUD005_ACCOUNT", "Account", "ACCOUNT");
        String entityId = createDimension(workspaceId, "BUD005_ENTITY", "Entity", "ENTITY");
        String timeId = createDimension(workspaceId, "BUD005_TIME", "Time", "TIME");
        String categoryId = createDimension(workspaceId, "BUD005_CATEGORY", "Category", "CATEGORY");
        String versionId = createDimension(workspaceId, "BUD005_VERSION", "Version", "VERSION");
        String modelId = createBudgetModel(workspaceId, "OPEX", "OPEX Budget Model");

        bindDimension(modelId, accountId, 10);
        bindDimension(modelId, entityId, 20);
        bindDimension(modelId, timeId, 30);
        bindDimension(modelId, categoryId, 40);
        bindDimension(modelId, versionId, 50);

        mockMvc.perform(get("/api/budget-models/{budgetModelId}/dimensions", modelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(5)));

        mockMvc.perform(post("/api/budget-models/{budgetModelId}/activate", modelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    void rejectsDuplicateBudgetModelCodeInWorkspace() throws Exception {
        String workspaceId = createWorkspace("BUD005_DUP_WS", "Duplicate Model Workspace");
        createBudgetModel(workspaceId, "CAPEX", "CAPEX Model");

        mockMvc.perform(post("/api/budget-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "code": "capex",
                                  "name": "Duplicate CAPEX Model"
                                }
                                """.formatted(workspaceId)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error.code").value("CONFLICT"));
    }

    @Test
    void rejectsCrossWorkspaceDimensionBinding() throws Exception {
        String workspaceId = createWorkspace("BUD005_MODEL_WS", "Model Workspace");
        String otherWorkspaceId = createWorkspace("BUD005_DIM_WS", "Dimension Workspace");
        String otherDimensionId = createDimension(otherWorkspaceId, "BUD005_OTHER_ACCOUNT", "Other Account", "ACCOUNT");
        String modelId = createBudgetModel(workspaceId, "SALES", "Sales Budget Model");

        mockMvc.perform(post("/api/budget-models/{budgetModelId}/dimensions", modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dimensionId": "%s",
                                  "requiredForEntry": true,
                                  "displayOrder": 10
                                }
                                """.formatted(otherDimensionId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    void rejectsActivationWhenRequiredDimensionsAreMissing() throws Exception {
        String workspaceId = createWorkspace("BUD005_MISSING_WS", "Missing Dimension Workspace");
        String accountId = createDimension(workspaceId, "BUD005_MIN_ACCOUNT", "Account", "ACCOUNT");
        String modelId = createBudgetModel(workspaceId, "MINIMAL", "Minimal Model");
        bindDimension(modelId, accountId, 10);

        mockMvc.perform(post("/api/budget-models/{budgetModelId}/activate", modelId))
                .andExpect(status().isBadRequest())
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

    private String createBudgetModel(String workspaceId, String code, String name) throws Exception {
        return mockMvc.perform(post("/api/budget-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "code": "%s",
                                  "name": "%s",
                                  "description": "MVP budget model"
                                }
                                """.formatted(workspaceId, code, name)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private void bindDimension(String modelId, String dimensionId, int displayOrder) throws Exception {
        mockMvc.perform(post("/api/budget-models/{budgetModelId}/dimensions", modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dimensionId": "%s",
                                  "requiredForEntry": true,
                                  "displayOrder": %d
                                }
                                """.formatted(dimensionId, displayOrder)))
                .andExpect(status().isOk());
    }
}
