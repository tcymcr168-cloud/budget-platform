package com.budgetplatform.budgetmodel.api;

import com.budgetplatform.budgetmodel.service.BudgetModelService;
import com.budgetplatform.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budget-models")
public class BudgetModelController {

    private final BudgetModelService budgetModelService;

    public BudgetModelController(BudgetModelService budgetModelService) {
        this.budgetModelService = budgetModelService;
    }

    @PostMapping
    ApiResponse<BudgetModelResponse> createBudgetModel(@Valid @RequestBody CreateBudgetModelRequest request) {
        return ApiResponse.success(budgetModelService.createBudgetModel(request));
    }

    @GetMapping
    ApiResponse<List<BudgetModelResponse>> listBudgetModels(@RequestParam UUID workspaceId) {
        return ApiResponse.success(budgetModelService.listBudgetModels(workspaceId));
    }

    @GetMapping("/{budgetModelId}")
    ApiResponse<BudgetModelResponse> getBudgetModel(@PathVariable UUID budgetModelId) {
        return ApiResponse.success(budgetModelService.getBudgetModel(budgetModelId));
    }

    @PostMapping("/{budgetModelId}/dimensions")
    ApiResponse<BudgetModelDimensionResponse> bindDimension(
            @PathVariable UUID budgetModelId,
            @Valid @RequestBody BindBudgetModelDimensionRequest request
    ) {
        return ApiResponse.success(budgetModelService.bindDimension(budgetModelId, request));
    }

    @GetMapping("/{budgetModelId}/dimensions")
    ApiResponse<List<BudgetModelDimensionResponse>> listBoundDimensions(@PathVariable UUID budgetModelId) {
        return ApiResponse.success(budgetModelService.listBoundDimensions(budgetModelId));
    }

    @PostMapping("/{budgetModelId}/activate")
    ApiResponse<BudgetModelResponse> activateBudgetModel(@PathVariable UUID budgetModelId) {
        return ApiResponse.success(budgetModelService.activateBudgetModel(budgetModelId));
    }

    @PostMapping("/{budgetModelId}/deactivate")
    ApiResponse<BudgetModelResponse> deactivateBudgetModel(@PathVariable UUID budgetModelId) {
        return ApiResponse.success(budgetModelService.deactivateBudgetModel(budgetModelId));
    }
}
