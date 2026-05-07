package com.budgetplatform.budgettemplate.api;

import com.budgetplatform.budgettemplate.service.BudgetTemplateService;
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
@RequestMapping("/api/budget-templates")
public class BudgetTemplateController {

    private final BudgetTemplateService budgetTemplateService;

    public BudgetTemplateController(BudgetTemplateService budgetTemplateService) {
        this.budgetTemplateService = budgetTemplateService;
    }

    @PostMapping
    ApiResponse<BudgetTemplateResponse> createTemplate(@Valid @RequestBody CreateBudgetTemplateRequest request) {
        return ApiResponse.success(budgetTemplateService.createTemplate(request));
    }

    @GetMapping
    ApiResponse<List<BudgetTemplateResponse>> listTemplates(@RequestParam UUID budgetModelId) {
        return ApiResponse.success(budgetTemplateService.listTemplates(budgetModelId));
    }

    @GetMapping("/{budgetTemplateId}")
    ApiResponse<BudgetTemplateResponse> getTemplate(@PathVariable UUID budgetTemplateId) {
        return ApiResponse.success(budgetTemplateService.getTemplate(budgetTemplateId));
    }

    @PostMapping("/{budgetTemplateId}/axes")
    ApiResponse<TemplateAxisResponse> addAxis(
            @PathVariable UUID budgetTemplateId,
            @Valid @RequestBody CreateTemplateAxisRequest request
    ) {
        return ApiResponse.success(budgetTemplateService.addAxis(budgetTemplateId, request));
    }

    @GetMapping("/{budgetTemplateId}/axes")
    ApiResponse<List<TemplateAxisResponse>> listAxes(@PathVariable UUID budgetTemplateId) {
        return ApiResponse.success(budgetTemplateService.listAxes(budgetTemplateId));
    }

    @PostMapping("/{budgetTemplateId}/activate")
    ApiResponse<BudgetTemplateResponse> activateTemplate(@PathVariable UUID budgetTemplateId) {
        return ApiResponse.success(budgetTemplateService.activateTemplate(budgetTemplateId));
    }

    @PostMapping("/{budgetTemplateId}/deactivate")
    ApiResponse<BudgetTemplateResponse> deactivateTemplate(@PathVariable UUID budgetTemplateId) {
        return ApiResponse.success(budgetTemplateService.deactivateTemplate(budgetTemplateId));
    }
}
