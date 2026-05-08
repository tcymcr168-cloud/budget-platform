package com.budgetplatform.budgetquery.api;

import com.budgetplatform.budgetquery.service.BudgetQueryService;
import com.budgetplatform.budgetsubmission.domain.FactValueStatus;
import com.budgetplatform.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budget-query")
public class BudgetQueryController {

    private final BudgetQueryService budgetQueryService;

    public BudgetQueryController(BudgetQueryService budgetQueryService) {
        this.budgetQueryService = budgetQueryService;
    }

    @GetMapping("/facts")
    ApiResponse<List<FactQueryResponse>> queryFacts(
            @RequestParam UUID budgetModelId,
            @RequestParam(required = false) UUID entityMemberId,
            @RequestParam(required = false) UUID timeMemberId,
            @RequestParam(required = false) UUID categoryMemberId,
            @RequestParam(required = false) UUID versionMemberId,
            @RequestParam(required = false) FactValueStatus status
    ) {
        return ApiResponse.success(budgetQueryService.queryFacts(
                budgetModelId,
                entityMemberId,
                timeMemberId,
                categoryMemberId,
                versionMemberId,
                status
        ));
    }

    @GetMapping("/summary")
    ApiResponse<List<FactSummaryResponse>> summarizeFacts(
            @RequestParam UUID budgetModelId,
            @RequestParam QueryGroupBy groupBy,
            @RequestParam(required = false) UUID entityMemberId,
            @RequestParam(required = false) UUID timeMemberId,
            @RequestParam(required = false) UUID categoryMemberId,
            @RequestParam(required = false) UUID versionMemberId,
            @RequestParam(required = false) FactValueStatus status
    ) {
        return ApiResponse.success(budgetQueryService.summarizeFacts(
                budgetModelId,
                groupBy,
                entityMemberId,
                timeMemberId,
                categoryMemberId,
                versionMemberId,
                status
        ));
    }

    @GetMapping(value = "/facts.csv", produces = "text/csv")
    String exportFactsCsv(
            @RequestParam UUID budgetModelId,
            @RequestParam(required = false) UUID entityMemberId,
            @RequestParam(required = false) UUID timeMemberId,
            @RequestParam(required = false) UUID categoryMemberId,
            @RequestParam(required = false) UUID versionMemberId,
            @RequestParam(required = false) FactValueStatus status
    ) {
        return budgetQueryService.exportFactsCsv(
                budgetModelId,
                entityMemberId,
                timeMemberId,
                categoryMemberId,
                versionMemberId,
                status
        );
    }

    @GetMapping("/variance")
    ApiResponse<List<BudgetActualVarianceResponse>> analyzeBudgetActualVariance(
            @RequestParam UUID budgetModelId,
            @RequestParam UUID budgetCategoryMemberId,
            @RequestParam UUID actualCategoryMemberId,
            @RequestParam(required = false) UUID budgetVersionMemberId,
            @RequestParam(required = false) UUID actualVersionMemberId,
            @RequestParam(required = false) UUID entityMemberId,
            @RequestParam(required = false) UUID timeMemberId,
            @RequestParam(required = false) FactValueStatus status
    ) {
        return ApiResponse.success(budgetQueryService.analyzeBudgetActualVariance(
                budgetModelId,
                budgetCategoryMemberId,
                actualCategoryMemberId,
                budgetVersionMemberId,
                actualVersionMemberId,
                entityMemberId,
                timeMemberId,
                status
        ));
    }
}
