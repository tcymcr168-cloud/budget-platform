package com.budgetplatform.budgetsubmission.api;

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
class SubmissionControllerIntegrationTests {

    private static final String ADMIN_USER = "admin@example.com";
    private static final String ADMIN_ROLES = "BUDGET_ADMIN";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void savesSubmitsReturnsApprovesAndLocksSubmissionTask() throws Exception {
        Fixture fixture = createFixture("BUD007_FULL");
        String taskId = createTask(fixture);

        saveValue(taskId, fixture.accountMemberId(), "1200.50");

        mockMvc.perform(post("/api/submissions/tasks/{taskId}/submit", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"));

        mockMvc.perform(post("/api/submissions/tasks/{taskId}/return", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "reason": "Please revise marketing amount."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("RETURNED"));

        mockMvc.perform(get("/api/submissions/tasks/{taskId}/values", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].valueStatus").value("DRAFT"));

        saveValue(taskId, fixture.accountMemberId(), "1250.00");

        mockMvc.perform(post("/api/submissions/tasks/{taskId}/submit", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("SUBMITTED"));

        mockMvc.perform(post("/api/submissions/tasks/{taskId}/approve", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        mockMvc.perform(post("/api/submissions/tasks/{taskId}/lock", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("LOCKED"));

        mockMvc.perform(get("/api/submissions/tasks/{taskId}/values", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].valueStatus").value("LOCKED"))
                .andExpect(jsonPath("$.data[0].amount").value(1250.00));
    }

    @Test
    void rejectsSavingWhenTaskIsSubmitted() throws Exception {
        Fixture fixture = createFixture("BUD007_SUBMITTED");
        String taskId = createTask(fixture);
        saveValue(taskId, fixture.accountMemberId(), "500.00");

        mockMvc.perform(post("/api/submissions/tasks/{taskId}/submit", taskId))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/submissions/tasks/{taskId}/values", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountMemberId": "%s",
                                  "amount": 700.00
                                }
                                """.formatted(fixture.accountMemberId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    void rejectsTaskForInactiveTemplate() throws Exception {
        Fixture fixture = createFixture("BUD007_INACTIVE");

        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/deactivate", fixture.templateId()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/submissions/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson(fixture)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    void rejectsMemberOutsideExpectedDimension() throws Exception {
        Fixture fixture = createFixture("BUD007_BAD_MEMBER");

        mockMvc.perform(post("/api/submissions/tasks")
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
                                """.formatted(
                                fixture.templateId(),
                                fixture.timeMemberId(),
                                fixture.timeMemberId(),
                                fixture.categoryMemberId(),
                                fixture.versionMemberId()
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    private Fixture createFixture(String prefix) throws Exception {
        String workspaceId = createWorkspace(prefix + "_WS", prefix + " Workspace");
        String accountDimensionId = createDimension(workspaceId, prefix + "_ACCOUNT", "Account", "ACCOUNT");
        String entityDimensionId = createDimension(workspaceId, prefix + "_ENTITY", "Entity", "ENTITY");
        String timeDimensionId = createDimension(workspaceId, prefix + "_TIME", "Time", "TIME");
        String categoryDimensionId = createDimension(workspaceId, prefix + "_CATEGORY", "Category", "CATEGORY");
        String versionDimensionId = createDimension(workspaceId, prefix + "_VERSION", "Version", "VERSION");

        String accountMemberId = createMember(accountDimensionId, prefix + "_MKT", "Marketing Expense");
        String entityMemberId = createMember(entityDimensionId, prefix + "_FIN", "Finance");
        String timeMemberId = createMember(timeDimensionId, prefix + "_202601", "2026-01");
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

        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/activate", templateId))
                .andExpect(status().isOk());

        return new Fixture(templateId, accountMemberId, entityMemberId, timeMemberId, categoryMemberId, versionMemberId);
    }

    private String createTask(Fixture fixture) throws Exception {
        return mockMvc.perform(post("/api/submissions/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson(fixture)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("NOT_STARTED"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"id\":\"([^\"]+)\".*", "$1");
    }

    private String taskJson(Fixture fixture) {
        return """
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
                fixture.categoryMemberId(),
                fixture.versionMemberId()
        );
    }

    private void saveValue(String taskId, String accountMemberId, String amount) throws Exception {
        mockMvc.perform(post("/api/submissions/tasks/{taskId}/values", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "accountMemberId": "%s",
                                  "amount": %s,
                                  "note": "Draft input"
                                }
                                """.formatted(accountMemberId, amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.valueStatus").value("DRAFT"));
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

    private record Fixture(
            String templateId,
            String accountMemberId,
            String entityMemberId,
            String timeMemberId,
            String categoryMemberId,
            String versionMemberId
    ) {
    }
}
