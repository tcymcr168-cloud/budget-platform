package com.budgetplatform.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MvpApiSmokeIntegrationTests {

    private static final String ADMIN_USER = "admin@example.com";
    private static final String ADMIN_ROLES = "BUDGET_ADMIN";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void runsCoreBudgetToActualApiSmoke() throws Exception {
        mockMvc.perform(get("/api/security/me")
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.authenticated").value(true))
                .andExpect(jsonPath("$.data.authMode").value("DEV_HEADER"));

        Fixture fixture = createPlanningFixture("E2E001");
        String taskId = createTask(fixture);
        saveBudgetValue(taskId, fixture.accountMemberId(), "1000.00");

        mockMvc.perform(post("/api/submissions/tasks/{taskId}/submit", taskId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"));

        mockMvc.perform(post("/api/submissions/tasks/{taskId}/approve", taskId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        String batchId = validateActualImport(fixture, "850.00");
        mockMvc.perform(post("/api/actual-imports/{batchId}/commit", batchId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMMITTED"));

        mockMvc.perform(get("/api/budget-query/variance")
                        .param("budgetModelId", fixture.modelId())
                        .param("budgetCategoryMemberId", fixture.budgetCategoryMemberId())
                        .param("actualCategoryMemberId", fixture.actualCategoryMemberId())
                        .param("budgetVersionMemberId", fixture.budgetVersionMemberId())
                        .param("actualVersionMemberId", fixture.actualVersionMemberId())
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].accountCode").value(fixture.accountCode()))
                .andExpect(jsonPath("$.data[0].entityCode").value(fixture.entityCode()))
                .andExpect(jsonPath("$.data[0].timeCode").value(fixture.timeCode()))
                .andExpect(jsonPath("$.data[0].budgetAmount").value(1000.00))
                .andExpect(jsonPath("$.data[0].actualAmount").value(850.00))
                .andExpect(jsonPath("$.data[0].varianceAmount").value(-150.00))
                .andExpect(jsonPath("$.data[0].variancePercent").value(-15.0000));
    }

    private Fixture createPlanningFixture(String prefix) throws Exception {
        String workspaceId = createWorkspace(prefix + "_WS", prefix + " Workspace");
        String accountDimensionId = createDimension(workspaceId, prefix + "_ACCOUNT", "Account", "ACCOUNT");
        String entityDimensionId = createDimension(workspaceId, prefix + "_ENTITY", "Entity", "ENTITY");
        String timeDimensionId = createDimension(workspaceId, prefix + "_TIME", "Time", "TIME");
        String categoryDimensionId = createDimension(workspaceId, prefix + "_CATEGORY", "Category", "CATEGORY");
        String versionDimensionId = createDimension(workspaceId, prefix + "_VERSION", "Version", "VERSION");

        String accountCode = prefix + "_TRAVEL";
        String accountMemberId = createMember(accountDimensionId, accountCode, "Travel Expense");
        String entityCode = prefix + "_OPS";
        String entityMemberId = createMember(entityDimensionId, entityCode, "Operations");
        String timeCode = prefix + "_202605";
        String timeMemberId = createMember(timeDimensionId, timeCode, "2026-05");
        String budgetCategoryMemberId = createMember(categoryDimensionId, prefix + "_BUDGET", "Budget");
        String actualCategoryMemberId = createMember(categoryDimensionId, "ACTUAL", "Actual");
        String budgetVersionMemberId = createMember(versionDimensionId, prefix + "_WORKING", "Working");
        String actualVersionCode = prefix + "_FINAL";
        String actualVersionMemberId = createMember(versionDimensionId, actualVersionCode, "Final");

        String modelId = createBudgetModel(workspaceId, prefix + "_MODEL", prefix + " Model");
        String accountBindingId = bindDimension(modelId, accountDimensionId, 10);
        bindDimension(modelId, entityDimensionId, 20);
        String timeBindingId = bindDimension(modelId, timeDimensionId, 30);
        bindDimension(modelId, categoryDimensionId, 40);
        bindDimension(modelId, versionDimensionId, 50);

        mockMvc.perform(post("/api/budget-models/{budgetModelId}/activate", modelId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));

        String templateId = createTemplate(modelId, prefix + "_TEMPLATE", prefix + " Template");
        addAxis(templateId, accountBindingId, "ROW", 10);
        addAxis(templateId, timeBindingId, "COLUMN", 20);

        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/activate", templateId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));

        return new Fixture(
                modelId,
                templateId,
                accountCode,
                accountMemberId,
                entityCode,
                entityMemberId,
                timeCode,
                timeMemberId,
                budgetCategoryMemberId,
                actualCategoryMemberId,
                budgetVersionMemberId,
                actualVersionMemberId,
                actualVersionCode
        );
    }

    private String createWorkspace(String code, String name) throws Exception {
        return idFrom(mockMvc.perform(post("/api/metadata/workspaces")
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
                .andReturn());
    }

    private String createDimension(String workspaceId, String code, String name, String type) throws Exception {
        return idFrom(mockMvc.perform(post("/api/metadata/dimensions")
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
                .andReturn());
    }

    private String createMember(String dimensionId, String code, String name) throws Exception {
        return idFrom(mockMvc.perform(post("/api/metadata/dimensions/{dimensionId}/members", dimensionId)
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
                .andReturn());
    }

    private String createBudgetModel(String workspaceId, String code, String name) throws Exception {
        return idFrom(mockMvc.perform(post("/api/budget-models")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "code": "%s",
                                  "name": "%s"
                                }
                                """.formatted(workspaceId, code, name)))
                .andExpect(status().isOk())
                .andReturn());
    }

    private String bindDimension(String modelId, String dimensionId, int displayOrder) throws Exception {
        return idFrom(mockMvc.perform(post("/api/budget-models/{budgetModelId}/dimensions", modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "dimensionId": "%s",
                                  "requiredForEntry": true,
                                  "displayOrder": %d
                                }
                                """.formatted(dimensionId, displayOrder)))
                .andExpect(status().isOk())
                .andReturn());
    }

    private String createTemplate(String modelId, String code, String name) throws Exception {
        return idFrom(mockMvc.perform(post("/api/budget-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "budgetModelId": "%s",
                                  "code": "%s",
                                  "name": "%s"
                                }
                                """.formatted(modelId, code, name)))
                .andExpect(status().isOk())
                .andReturn());
    }

    private void addAxis(String templateId, String modelDimensionId, String axisType, int displayOrder) throws Exception {
        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/axes", templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
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

    private String createTask(Fixture fixture) throws Exception {
        return idFrom(mockMvc.perform(post("/api/submissions/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
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
                                """.formatted(
                                fixture.templateId(),
                                fixture.entityMemberId(),
                                fixture.timeMemberId(),
                                fixture.budgetCategoryMemberId(),
                                fixture.budgetVersionMemberId()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("NOT_STARTED"))
                .andReturn());
    }

    private void saveBudgetValue(String taskId, String accountMemberId, String amount) throws Exception {
        mockMvc.perform(post("/api/submissions/tasks/{taskId}/values", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "accountMemberId": "%s",
                                  "amount": %s,
                                  "note": "E2E smoke input"
                                }
                                """.formatted(accountMemberId, amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valueStatus").value("DRAFT"));
    }

    private String validateActualImport(Fixture fixture, String amount) throws Exception {
        return idFrom(mockMvc.perform(post("/api/actual-imports/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "budgetModelId": "%s",
                                  "fileName": "e2e-actual.csv",
                                  "operatorUser": "importer@example.com",
                                  "csvContent": "account,entity,time,category,version,amount\\n%s,%s,%s,ACTUAL,%s,%s"
                                }
                                """.formatted(
                                fixture.modelId(),
                                fixture.accountCode(),
                                fixture.entityCode(),
                                fixture.timeCode(),
                                fixture.actualVersionCode(),
                                amount
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("VALIDATED"))
                .andExpect(jsonPath("$.data.validRows").value(1))
                .andExpect(jsonPath("$.data.errorRows").value(0))
                .andReturn());
    }

    private String idFrom(MvcResult result) throws Exception {
        return result.getResponse()
                .getContentAsString()
                .replaceFirst(".*?\"id\":\"([^\"]+)\".*", "$1");
    }

    private record Fixture(
            String modelId,
            String templateId,
            String accountCode,
            String accountMemberId,
            String entityCode,
            String entityMemberId,
            String timeCode,
            String timeMemberId,
            String budgetCategoryMemberId,
            String actualCategoryMemberId,
            String budgetVersionMemberId,
            String actualVersionMemberId,
            String actualVersionCode
    ) {
    }
}
