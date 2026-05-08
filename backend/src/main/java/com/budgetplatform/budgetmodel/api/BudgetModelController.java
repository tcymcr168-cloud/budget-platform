package com.budgetplatform.budgetmodel.api;

import com.budgetplatform.budgetmodel.service.BudgetModelService;
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
@RequestMapping("/api/budget-models")
public class BudgetModelController {

    private final BudgetModelService budgetModelService;
    private final CurrentUserContextResolver contextResolver;

    public BudgetModelController(BudgetModelService budgetModelService, CurrentUserContextResolver contextResolver) {
        this.budgetModelService = budgetModelService;
        this.contextResolver = contextResolver;
    }

    @PostMapping
    ApiResponse<BudgetModelResponse> createBudgetModel(
            @Valid @RequestBody CreateBudgetModelRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetModelService.createBudgetModel(context, request));
    }

    @GetMapping
    ApiResponse<List<BudgetModelResponse>> listBudgetModels(
            @RequestParam UUID workspaceId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetModelService.listBudgetModels(context, workspaceId));
    }

    @GetMapping("/{budgetModelId}")
    ApiResponse<BudgetModelResponse> getBudgetModel(
            @PathVariable UUID budgetModelId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetModelService.getBudgetModel(context, budgetModelId));
    }

    @PostMapping("/{budgetModelId}/dimensions")
    ApiResponse<BudgetModelDimensionResponse> bindDimension(
            @PathVariable UUID budgetModelId,
            @Valid @RequestBody BindBudgetModelDimensionRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetModelService.bindDimension(context, budgetModelId, request));
    }

    @GetMapping("/{budgetModelId}/dimensions")
    ApiResponse<List<BudgetModelDimensionResponse>> listBoundDimensions(
            @PathVariable UUID budgetModelId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetModelService.listBoundDimensions(context, budgetModelId));
    }

    @PostMapping("/{budgetModelId}/activate")
    ApiResponse<BudgetModelResponse> activateBudgetModel(
            @PathVariable UUID budgetModelId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetModelService.activateBudgetModel(context, budgetModelId));
    }

    @PostMapping("/{budgetModelId}/deactivate")
    ApiResponse<BudgetModelResponse> deactivateBudgetModel(
            @PathVariable UUID budgetModelId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetModelService.deactivateBudgetModel(context, budgetModelId));
    }
}
