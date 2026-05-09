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

    private static final String ADMIN_USER = "admin@example.com";
    private static final String ADMIN_ROLES = "BUDGET_ADMIN";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void queriesFactsAndSummarizesByAccount() throws Exception {
        Fixture fixture = createApprovedFactFixture("BUD008_SUM");

        mockMvc.perform(get("/api/budget-query/facts")
                        .param("budgetModelId", fixture.modelId())
                        .param("status", "APPROVED")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].accountCode").value(fixture.accountCode()))
                .andExpect(jsonPath("$.data[0].amount").value(930.25));

        mockMvc.perform(get("/api/budget-query/summary")
                        .param("budgetModelId", fixture.modelId())
                        .param("groupBy", "ACCOUNT")
                        .param("status", "APPROVED")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].memberCode").value(fixture.accountCode()))
                .andExpect(jsonPath("$.data[0].totalAmount").value(930.25))
                .andExpect(jsonPath("$.data[0].lineCount").value(1));
    }

    @Test
    void queriesFactsPageWithDefaults() throws Exception {
        Fixture fixture = createApprovedFactFixture("PERF002_PAGE");

        mockMvc.perform(get("/api/budget-query/facts/page")
                        .param("budgetModelId", fixture.modelId())
                        .param("status", "APPROVED")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items", hasSize(1)))
                .andExpect(jsonPath("$.data.items[0].accountCode").value(fixture.accountCode()))
                .andExpect(jsonPath("$.data.items[0].amount").value(930.25))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(25))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1));
    }

    @Test
    void rejectsInvalidFactsPageParameters() throws Exception {
        Fixture fixture = createApprovedFactFixture("PERF002_BAD");

        mockMvc.perform(get("/api/budget-query/facts/page")
                        .param("budgetModelId", fixture.modelId())
                        .param("page", "-1")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));

        mockMvc.perform(get("/api/budget-query/facts/page")
                        .param("budgetModelId", fixture.modelId())
                        .param("size", "101")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));

        mockMvc.perform(get("/api/budget-query/facts/page")
                        .param("budgetModelId", fixture.modelId())
                        .param("sort", "accountCode")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));

        mockMvc.perform(get("/api/budget-query/facts/page")
                        .param("budgetModelId", fixture.modelId())
                        .param("direction", "SIDEWAYS")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    void exportsFactsAsCsv() throws Exception {
        Fixture fixture = createApprovedFactFixture("BUD008_CSV");

        mockMvc.perform(get("/api/budget-query/facts.csv")
                        .param("budgetModelId", fixture.modelId())
                        .param("status", "APPROVED")
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/csv"))
                .andExpect(content().string(containsString("account,entity,time,category,version,amount,status,source")))
                .andExpect(content().string(containsString(fixture.accountCode())));
    }

    @Test
    void analyzesBudgetActualVarianceByAccountEntityAndTime() throws Exception {
        VarianceFixture fixture = createVarianceFixture("BUD010_VAR");

        mockMvc.perform(get("/api/budget-query/variance")
                        .param("budgetModelId", fixture.modelId())
                        .param("budgetCategoryMemberId", fixture.budgetCategoryMemberId())
                        .param("actualCategoryMemberId", fixture.actualCategoryMemberId())
                        .param("budgetVersionMemberId", fixture.budgetVersionMemberId())
                        .param("actualVersionMemberId", fixture.actualVersionMemberId())
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].accountCode").value(fixture.accountCode()))
                .andExpect(jsonPath("$.data[0].entityCode").value(fixture.entityCode()))
                .andExpect(jsonPath("$.data[0].timeCode").value(fixture.timeCode()))
                .andExpect(jsonPath("$.data[0].budgetAmount").value(1000.00))
                .andExpect(jsonPath("$.data[0].actualAmount").value(1200.00))
                .andExpect(jsonPath("$.data[0].varianceAmount").value(200.00))
                .andExpect(jsonPath("$.data[0].variancePercent").value(20.0000))
                .andExpect(jsonPath("$.data[0].budgetLineCount").value(1))
                .andExpect(jsonPath("$.data[0].actualLineCount").value(1));
    }

    @Test
    void filtersFactsByReadOnlyEntityScope() throws Exception {
        Fixture allowed = createApprovedFactFixture("SEC003_ALLOWED");
        Fixture blocked = createApprovedFactFixture("SEC003_BLOCKED");
        String userId = createSecurityUser("scope.reader@example.com");
        grantRole(userId, allowed.workspaceId(), "READ_ONLY");
        grantEntityScope(userId, allowed.workspaceId(), allowed.entityMemberId());

        mockMvc.perform(get("/api/budget-query/facts")
                        .param("budgetModelId", allowed.modelId())
                        .header("X-User-Id", "scope.reader@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].entityCode").value(allowed.entityCode()));

        mockMvc.perform(get("/api/budget-query/facts")
                        .param("budgetModelId", blocked.modelId())
                        .header("X-User-Id", "scope.reader@example.com"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.code").value("FORBIDDEN"));
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
        mockMvc.perform(post("/api/budget-models/{budgetModelId}/activate", modelId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk());

        String templateId = createTemplate(modelId, prefix + "_TEMPLATE", prefix + " Template");
        addAxis(templateId, accountBindingId, "ROW", 10);
        addAxis(templateId, timeBindingId, "COLUMN", 20);
        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/activate", templateId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk());

        String taskId = createTask(templateId, entityMemberId, timeMemberId, categoryMemberId, versionMemberId);
        saveValue(taskId, accountMemberId, "930.25");
        mockMvc.perform(post("/api/submissions/tasks/{taskId}/submit", taskId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/submissions/tasks/{taskId}/approve", taskId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk());

        return new Fixture(modelId, workspaceId, accountCode, entityMemberId, prefix + "_OPS");
    }

    private VarianceFixture createVarianceFixture(String prefix) throws Exception {
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
        String timeCode = prefix + "_202604";
        String timeMemberId = createMember(timeDimensionId, timeCode, "2026-04");
        String budgetCategoryMemberId = createMember(categoryDimensionId, prefix + "_BUDGET", "Budget");
        String actualCategoryMemberId = createMember(categoryDimensionId, "ACTUAL", "Actual");
        String budgetVersionMemberId = createMember(versionDimensionId, prefix + "_WORKING", "Working");
        String actualVersionMemberId = createMember(versionDimensionId, prefix + "_FINAL", "Final");

        String modelId = createBudgetModel(workspaceId, prefix + "_MODEL", prefix + " Model");
        String accountBindingId = bindDimension(modelId, accountDimensionId, 10);
        bindDimension(modelId, entityDimensionId, 20);
        String timeBindingId = bindDimension(modelId, timeDimensionId, 30);
        bindDimension(modelId, categoryDimensionId, 40);
        bindDimension(modelId, versionDimensionId, 50);
        mockMvc.perform(post("/api/budget-models/{budgetModelId}/activate", modelId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk());

        String templateId = createTemplate(modelId, prefix + "_TEMPLATE", prefix + " Template");
        addAxis(templateId, accountBindingId, "ROW", 10);
        addAxis(templateId, timeBindingId, "COLUMN", 20);
        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/activate", templateId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk());

        String taskId = createTask(templateId, entityMemberId, timeMemberId, budgetCategoryMemberId, budgetVersionMemberId);
        saveValue(taskId, accountMemberId, "1000.00");
        mockMvc.perform(post("/api/submissions/tasks/{taskId}/submit", taskId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/submissions/tasks/{taskId}/approve", taskId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk());

        String batchId = mockMvc.perform(post("/api/actual-imports/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "budgetModelId": "%s",
                                  "fileName": "actual-variance.csv",
                                  "operatorUser": "importer@example.com",
                                  "csvContent": "account,entity,time,category,version,amount\\n%s,%s,%s,ACTUAL,%s,1200.00"
                                }
                                """.formatted(modelId, accountCode, entityCode, timeCode, prefix + "_FINAL")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceFirst(".*?\"id\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(post("/api/actual-imports/{batchId}/commit", batchId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk());

        return new VarianceFixture(
                modelId,
                accountCode,
                entityCode,
                timeCode,
                budgetCategoryMemberId,
                actualCategoryMemberId,
                budgetVersionMemberId,
                actualVersionMemberId
        );
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

    private String createBudgetModel(String workspaceId, String code, String name) throws Exception {
        return mockMvc.perform(post("/api/budget-models")
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
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private String bindDimension(String modelId, String dimensionId, int displayOrder) throws Exception {
        return mockMvc.perform(post("/api/budget-models/{budgetModelId}/dimensions", modelId)
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
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private String createTemplate(String modelId, String code, String name) throws Exception {
        return mockMvc.perform(post("/api/budget-templates")
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
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
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

    private String createTask(
            String templateId,
            String entityMemberId,
            String timeMemberId,
            String categoryMemberId,
            String versionMemberId
    ) throws Exception {
        return mockMvc.perform(post("/api/submissions/tasks")
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
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "accountMemberId": "%s",
                                  "amount": %s
                                }
                                """.formatted(accountMemberId, amount)))
                .andExpect(status().isOk());
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

    private void grantRole(String userId, String workspaceId, String roleCode) throws Exception {
        mockMvc.perform(post("/api/security/users/{userId}/roles", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", "admin@example.com")
                        .header("X-User-Roles", "BUDGET_ADMIN")
                        .content("""
                                {
                                  "workspaceId": "%s",
                                  "roleCode": "%s"
                                }
                                """.formatted(workspaceId, roleCode)))
                .andExpect(status().isOk());
    }

    private void grantEntityScope(String userId, String workspaceId, String entityMemberId) throws Exception {
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
                                """.formatted(workspaceId, entityMemberId)))
                .andExpect(status().isOk());
    }

    private record Fixture(String modelId, String workspaceId, String accountCode, String entityMemberId, String entityCode) {
    }

    private record VarianceFixture(
            String modelId,
            String accountCode,
            String entityCode,
            String timeCode,
            String budgetCategoryMemberId,
            String actualCategoryMemberId,
            String budgetVersionMemberId,
            String actualVersionMemberId
    ) {
    }
}
