package com.budgetplatform.budgetactual.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ActualImportControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void validatesAndCommitsActualCsvIntoFactValues() throws Exception {
        Fixture fixture = createModelFixture("BUD009_OK");

        String batchId = mockMvc.perform(post("/api/actual-imports/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "budgetModelId": "%s",
                                  "fileName": "actual-expense.csv",
                                  "operatorUser": "importer@example.com",
                                  "csvContent": "account,entity,time,category,version,amount\\n%s,%s,%s,%s,%s,1200.50"
                                }
                                """.formatted(
                                fixture.modelId(),
                                fixture.accountCode(),
                                fixture.entityCode(),
                                fixture.timeCode(),
                                fixture.categoryCode(),
                                fixture.versionCode()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("VALIDATED"))
                .andExpect(jsonPath("$.data.totalRows").value(1))
                .andExpect(jsonPath("$.data.validRows").value(1))
                .andExpect(jsonPath("$.data.errorRows").value(0))
                .andExpect(jsonPath("$.data.rows", hasSize(1)))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceFirst(".*?\"id\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(post("/api/actual-imports/{batchId}/commit", batchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMMITTED"));

        mockMvc.perform(get("/api/budget-query/facts")
                        .param("budgetModelId", fixture.modelId())
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].sourceType").value("ACTUAL_IMPORT"))
                .andExpect(jsonPath("$.data[0].importBatchId").value(batchId))
                .andExpect(jsonPath("$.data[0].amount").value(1200.50));
    }

    @Test
    void reportsRowErrorsAndBlocksCommit() throws Exception {
        Fixture fixture = createModelFixture("BUD009_ERR");

        String batchId = mockMvc.perform(post("/api/actual-imports/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "budgetModelId": "%s",
                                  "fileName": "actual-error.csv",
                                  "operatorUser": "importer@example.com",
                                  "csvContent": "account,entity,time,category,version,amount\\nUNKNOWN,%s,%s,%s,%s,88.00"
                                }
                                """.formatted(
                                fixture.modelId(),
                                fixture.entityCode(),
                                fixture.timeCode(),
                                fixture.categoryCode(),
                                fixture.versionCode()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("VALIDATED"))
                .andExpect(jsonPath("$.data.validRows").value(0))
                .andExpect(jsonPath("$.data.errorRows").value(1))
                .andExpect(jsonPath("$.data.errorReport").value(containsString("account member was not found")))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceFirst(".*?\"id\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(post("/api/actual-imports/{batchId}/commit", batchId))
                .andExpect(status().isBadRequest());
    }

    private Fixture createModelFixture(String prefix) throws Exception {
        String workspaceId = createWorkspace(prefix + "_WS", prefix + " Workspace");
        String accountDimensionId = createDimension(workspaceId, prefix + "_ACCOUNT", "Account", "ACCOUNT");
        String entityDimensionId = createDimension(workspaceId, prefix + "_ENTITY", "Entity", "ENTITY");
        String timeDimensionId = createDimension(workspaceId, prefix + "_TIME", "Time", "TIME");
        String categoryDimensionId = createDimension(workspaceId, prefix + "_CATEGORY", "Category", "CATEGORY");
        String versionDimensionId = createDimension(workspaceId, prefix + "_VERSION", "Version", "VERSION");

        String accountCode = prefix + "_TRAVEL";
        createMember(accountDimensionId, accountCode, "Travel Expense");
        String entityCode = prefix + "_OPS";
        createMember(entityDimensionId, entityCode, "Operations");
        String timeCode = prefix + "_202603";
        createMember(timeDimensionId, timeCode, "2026-03");
        String categoryCode = "ACTUAL";
        createMember(categoryDimensionId, categoryCode, "Actual");
        String versionCode = prefix + "_FINAL";
        createMember(versionDimensionId, versionCode, "Final");

        String modelId = createBudgetModel(workspaceId, prefix + "_MODEL", prefix + " Model");
        bindDimension(modelId, accountDimensionId, 10);
        bindDimension(modelId, entityDimensionId, 20);
        bindDimension(modelId, timeDimensionId, 30);
        bindDimension(modelId, categoryDimensionId, 40);
        bindDimension(modelId, versionDimensionId, 50);
        mockMvc.perform(post("/api/budget-models/{budgetModelId}/activate", modelId))
                .andExpect(status().isOk());

        return new Fixture(modelId, accountCode, entityCode, timeCode, categoryCode, versionCode);
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

    private void createMember(String dimensionId, String code, String name) throws Exception {
        mockMvc.perform(post("/api/metadata/dimensions/{dimensionId}/members", dimensionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "code": "%s",
                                  "name": "%s"
                                }
                                """.formatted(code, name)))
                .andExpect(status().isOk());
    }

    private String createBudgetModel(String workspaceId, String code, String name) throws Exception {
        return mockMvc.perform(post("/api/budget-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "code": "%s",
                                  "name": "%s"
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

    private record Fixture(
            String modelId,
            String accountCode,
            String entityCode,
            String timeCode,
            String categoryCode,
            String versionCode
    ) {
    }
}
