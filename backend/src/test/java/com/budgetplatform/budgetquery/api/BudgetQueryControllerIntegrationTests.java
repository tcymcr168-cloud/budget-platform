package com.budgetplatform.budgetquery.api;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BudgetQueryControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queriesFactsAndSummarizesByAccount() throws Exception {
        Fixture fixture = createApprovedFactFixture("BUD008_SUM");

        mockMvc.perform(get("/api/budget-query/facts")
                        .param("budgetModelId", fixture.modelId())
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].accountCode").value(fixture.accountCode()))
                .andExpect(jsonPath("$.data[0].amount").value(930.25));

        mockMvc.perform(get("/api/budget-query/summary")
                        .param("budgetModelId", fixture.modelId())
                        .param("groupBy", "ACCOUNT")
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].memberCode").value(fixture.accountCode()))
                .andExpect(jsonPath("$.data[0].totalAmount").value(930.25))
                .andExpect(jsonPath("$.data[0].lineCount").value(1));
    }

    @Test
    void exportsFactsAsCsv() throws Exception {
        Fixture fixture = createApprovedFactFixture("BUD008_CSV");

        mockMvc.perform(get("/api/budget-query/facts.csv")
                        .param("budgetModelId", fixture.modelId())
                        .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/csv"))
                .andExpect(content().string(containsString("account,entity,time,category,version,amount,status,source")))
                .andExpect(content().string(containsString(fixture.accountCode())));
    }

    private Fixture createApprovedFactFixture(String prefix) throws Exception {
        String workspaceId = createWorkspace(prefix + "_WS", prefix + " Workspace");
        String accountDimensionId = createDimension(workspaceId, prefix + "_ACCOUNT", "Account", "ACCOUNT");
        String entityDimensionId = createDimension(workspaceId, prefix + "_ENTITY", "Entity", "ENTITY");
        String timeDimensionId = createDimension(workspaceId, prefix + "_TIME", "Time", "TIME");
        String categoryDimensionId = createDimension(workspaceId, prefix + "_CATEGORY", "Category", "CATEGORY");
        String versionDimensionId = createDimension(workspaceId, prefix + "_VERSION", "Version", "VERSION");

        String accountCode = prefix + "_TRAVEL";
        String accountMemberId = createMember(accountDimensionId, accountCode, "Travel Expense");
        String entityMemberId = createMember(entityDimensionId, prefix + "_OPS", "Operations");
        String timeMemberId = createMember(timeDimensionId, prefix + "_202602", "2026-02");
        String categoryMemberId = createMember(categoryDimensionId, prefix + "_BUDGET", "Budget");
        String versionMemberId = createMember(versionDimensionId, prefix + "_WORKING", "Working");

        String modelId = createBudgetModel(workspaceId, prefix + "_MODEL", prefix + " Model");
        String accountBindingId = bindDimension(modelId, accountDimensionId, 10);
        bindDimension(modelId, entityDimensionId, 20);
        String timeBindingId = bindDimension(modelId, timeDimensionId, 30);
        bindDimension(modelId, categoryDimensionId, 40);
        bindDimension(modelId, versionDimensionId, 50);
        mockMvc.perform(post("/api/budget-models/{budgetModelId}/activate", modelId))
                .andExpect(status().isOk());

        String templateId = createTemplate(modelId, prefix + "_TEMPLATE", prefix + " Template");
        addAxis(templateId, accountBindingId, "ROW", 10);
        addAxis(templateId, timeBindingId, "COLUMN", 20);
        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/activate", templateId))
                .andExpect(status().isOk());

        String taskId = createTask(templateId, entityMemberId, timeMemberId, categoryMemberId, versionMemberId);
        saveValue(taskId, accountMemberId, "930.25");
        mockMvc.perform(post("/api/submissions/tasks/{taskId}/submit", taskId))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/submissions/tasks/{taskId}/approve", taskId))
                .andExpect(status().isOk());

        return new Fixture(modelId, accountCode);
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

    private String createMember(String dimensionId, String code, String name) throws Exception {
        return mockMvc.perform(post("/api/metadata/dimensions/{dimensionId}/members", dimensionId)
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

    private String bindDimension(String modelId, String dimensionId, int displayOrder) throws Exception {
        return mockMvc.perform(post("/api/budget-models/{budgetModelId}/dimensions", modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dimensionId": "%s",
                                  "requiredForEntry": true,
                                  "displayOrder": %d
                                }
                                """.formatted(dimensionId, displayOrder)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private String createTemplate(String modelId, String code, String name) throws Exception {
        return mockMvc.perform(post("/api/budget-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "budgetModelId": "%s",
                                  "code": "%s",
                                  "name": "%s"
                                }
                                """.formatted(modelId, code, name)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private void addAxis(String templateId, String modelDimensionId, String axisType, int displayOrder) throws Exception {
        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/axes", templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "modelDimensionId": "%s",
                                  "axisType": "%s",
                                  "memberSelector": "ALL_LEAF",
                                  "displayOrder": %d
                                }
                                """.formatted(modelDimensionId, axisType, displayOrder)))
                .andExpect(status().isOk());
    }

    private String createTask(
            String templateId,
            String entityMemberId,
            String timeMemberId,
            String categoryMemberId,
            String versionMemberId
    ) throws Exception {
        return mockMvc.perform(post("/api/submissions/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "budgetTemplateId": "%s",
                                  "entityMemberId": "%s",
                                  "timeMemberId": "%s",
                                  "categoryMemberId": "%s",
                                  "versionMemberId": "%s",
                                  "ownerUser": "owner@example.com",
                                  "reviewerUser": "reviewer@example.com"
                                }
                                """.formatted(templateId, entityMemberId, timeMemberId, categoryMemberId, versionMemberId)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private void saveValue(String taskId, String accountMemberId, String amount) throws Exception {
        mockMvc.perform(post("/api/submissions/tasks/{taskId}/values", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountMemberId": "%s",
                                  "amount": %s
                                }
                                """.formatted(accountMemberId, amount)))
                .andExpect(status().isOk());
    }

    private record Fixture(String modelId, String accountCode) {
    }
}
