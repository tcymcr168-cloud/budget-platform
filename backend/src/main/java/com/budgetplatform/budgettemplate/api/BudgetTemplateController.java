package com.budgetplatform.budgettemplate.api;

import com.budgetplatform.budgettemplate.service.BudgetTemplateService;
import com.budgetplatform.common.api.ApiResponse;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.context.CurrentUserContextResolver;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budget-templates")
public class BudgetTemplateController {

    private final BudgetTemplateService budgetTemplateService;
    private final CurrentUserContextResolver contextResolver;

    public BudgetTemplateController(
            BudgetTemplateService budgetTemplateService,
            CurrentUserContextResolver contextResolver
    ) {
        this.budgetTemplateService = budgetTemplateService;
        this.contextResolver = contextResolver;
    }

    @PostMapping
    ApiResponse<BudgetTemplateResponse> createTemplate(
            @Valid @RequestBody CreateBudgetTemplateRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetTemplateService.createTemplate(context, request));
    }

    @GetMapping
    ApiResponse<List<BudgetTemplateResponse>> listTemplates(
            @RequestParam UUID budgetModelId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetTemplateService.listTemplates(context, budgetModelId));
    }

    @GetMapping("/{budgetTemplateId}")
    ApiResponse<BudgetTemplateResponse> getTemplate(
            @PathVariable UUID budgetTemplateId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetTemplateService.getTemplate(context, budgetTemplateId));
    }

    @PostMapping("/{budgetTemplateId}/axes")
    ApiResponse<TemplateAxisResponse> addAxis(
            @PathVariable UUID budgetTemplateId,
            @Valid @RequestBody CreateTemplateAxisRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetTemplateService.addAxis(context, budgetTemplateId, request));
    }

    @GetMapping("/{budgetTemplateId}/axes")
    ApiResponse<List<TemplateAxisResponse>> listAxes(
            @PathVariable UUID budgetTemplateId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetTemplateService.listAxes(context, budgetTemplateId));
    }

    @PostMapping("/{budgetTemplateId}/activate")
    ApiResponse<BudgetTemplateResponse> activateTemplate(
            @PathVariable UUID budgetTemplateId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetTemplateService.activateTemplate(context, budgetTemplateId));
    }

    @PostMapping("/{budgetTemplateId}/deactivate")
    ApiResponse<BudgetTemplateResponse> deactivateTemplate(
            @PathVariable UUID budgetTemplateId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetTemplateService.deactivateTemplate(context, budgetTemplateId));
    }
}
