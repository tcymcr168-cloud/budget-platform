package com.budgetplatform.budgettemplate.api;

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
class BudgetTemplateControllerIntegrationTests {

    private static final String ADMIN_USER = "admin@example.com";
    private static final String ADMIN_ROLES = "BUDGET_ADMIN";
    private static final String READ_ONLY_USER = "reader@example.com";
    private static final String READ_ONLY_ROLES = "READ_ONLY";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsTemplateAddsAxesAndActivates() throws Exception {
        Fixture fixture = createActiveModelFixture("BUD006_FULL");
        String templateId = createTemplate(fixture.modelId(), "OPEX_INPUT", "OPEX Input Template");

        addAxis(templateId, fixture.accountBindingId(), "ROW", 10);
        addAxis(templateId, fixture.timeBindingId(), "COLUMN", 20);

        mockMvc.perform(get("/api/budget-templates/{budgetTemplateId}/axes", templateId))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/budget-templates/{budgetTemplateId}/axes", templateId)
                        .header("X-User-Id", READ_ONLY_USER)
                        .header("X-User-Roles", READ_ONLY_ROLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(2)));

        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/activate", templateId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    @Test
    void rejectsTemplateForDraftBudgetModel() throws Exception {
        String workspaceId = createWorkspace("BUD006_DRAFT_WS", "Draft Template Workspace");
        String modelId = createBudgetModel(workspaceId, "DRAFT_MODEL", "Draft Model");

        mockMvc.perform(post("/api/budget-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "budgetModelId": "%s",
                                  "code": "DRAFT_TEMPLATE",
                                  "name": "Draft Template"
                                }
                                """.formatted(modelId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    void rejectsAxisFromAnotherBudgetModel() throws Exception {
        Fixture first = createActiveModelFixture("BUD006_FIRST");
        Fixture second = createActiveModelFixture("BUD006_SECOND");
        String templateId = createTemplate(first.modelId(), "FIRST_TEMPLATE", "First Template");

        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/axes", templateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES)
                        .content("""
                                {
                                  "modelDimensionId": "%s",
                                  "axisType": "ROW",
                                  "memberSelector": "ALL_LEAF",
                                  "displayOrder": 10
                                }
                                """.formatted(second.accountBindingId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    void rejectsActivationWithoutColumnAxis() throws Exception {
        Fixture fixture = createActiveModelFixture("BUD006_MISSING");
        String templateId = createTemplate(fixture.modelId(), "MISSING_AXIS", "Missing Axis Template");
        addAxis(templateId, fixture.accountBindingId(), "ROW", 10);

        mockMvc.perform(post("/api/budget-templates/{budgetTemplateId}/activate", templateId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("BAD_REQUEST"));
    }

    @Test
    void rejectsTemplateWriteWithoutTemplateRole() throws Exception {
        Fixture fixture = createActiveModelFixture("SEC003D_REJECT");

        mockMvc.perform(post("/api/budget-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", READ_ONLY_USER)
                        .header("X-User-Roles", READ_ONLY_ROLES)
                        .content("""
                                {
                                  "budgetModelId": "%s",
                                  "code": "READ_ONLY_TEMPLATE",
                                  "name": "Read Only Template"
                                }
                                """.formatted(fixture.modelId())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error.code").value("FORBIDDEN"));
    }

    private Fixture createActiveModelFixture(String prefix) throws Exception {
        String workspaceId = createWorkspace(prefix + "_WS", prefix + " Workspace");
        String accountId = createDimension(workspaceId, prefix + "_ACCOUNT", "Account", "ACCOUNT");
        String entityId = createDimension(workspaceId, prefix + "_ENTITY", "Entity", "ENTITY");
        String timeId = createDimension(workspaceId, prefix + "_TIME", "Time", "TIME");
        String categoryId = createDimension(workspaceId, prefix + "_CATEGORY", "Category", "CATEGORY");
        String versionId = createDimension(workspaceId, prefix + "_VERSION", "Version", "VERSION");
        String modelId = createBudgetModel(workspaceId, prefix + "_MODEL", prefix + " Model");

        String accountBindingId = bindDimension(modelId, accountId, 10);
        bindDimension(modelId, entityId, 20);
        String timeBindingId = bindDimension(modelId, timeId, 30);
        bindDimension(modelId, categoryId, 40);
        bindDimension(modelId, versionId, 50);

        mockMvc.perform(post("/api/budget-models/{budgetModelId}/activate", modelId)
                        .header("X-User-Id", ADMIN_USER)
                        .header("X-User-Roles", ADMIN_ROLES))
                .andExpect(status().isOk());

        return new Fixture(modelId, accountBindingId, timeBindingId);
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

    private record Fixture(String modelId, String accountBindingId, String timeBindingId) {
    }
}
