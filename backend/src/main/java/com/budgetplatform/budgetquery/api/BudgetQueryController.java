package com.budgetplatform.budgetquery.api;

import com.budgetplatform.budgetquery.service.BudgetQueryService;
import com.budgetplatform.budgetsubmission.domain.FactValueStatus;
import com.budgetplatform.common.api.ApiResponse;
import com.budgetplatform.common.api.PageResponse;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.context.CurrentUserContextResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budget-query")
public class BudgetQueryController {

    private final BudgetQueryService budgetQueryService;
    private final CurrentUserContextResolver contextResolver;

    public BudgetQueryController(BudgetQueryService budgetQueryService, CurrentUserContextResolver contextResolver) {
        this.budgetQueryService = budgetQueryService;
        this.contextResolver = contextResolver;
    }

    @GetMapping("/facts")
    ApiResponse<List<FactQueryResponse>> queryFacts(
            @RequestParam UUID budgetModelId,
            @RequestParam(required = false) UUID entityMemberId,
            @RequestParam(required = false) UUID timeMemberId,
            @RequestParam(required = false) UUID categoryMemberId,
            @RequestParam(required = false) UUID versionMemberId,
            @RequestParam(required = false) FactValueStatus status,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetQueryService.queryFacts(
                context,
                budgetModelId,
                entityMemberId,
                timeMemberId,
                categoryMemberId,
                versionMemberId,
                status
        ));
    }

    @GetMapping("/facts/page")
    ApiResponse<PageResponse<FactQueryResponse>> queryFactsPage(
            @RequestParam UUID budgetModelId,
            @RequestParam(required = false) UUID entityMemberId,
            @RequestParam(required = false) UUID timeMemberId,
            @RequestParam(required = false) UUID categoryMemberId,
            @RequestParam(required = false) UUID versionMemberId,
            @RequestParam(required = false) FactValueStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "updatedAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetQueryService.queryFactsPage(
                context,
                budgetModelId,
                entityMemberId,
                timeMemberId,
                categoryMemberId,
                versionMemberId,
                status,
                page,
                size,
                sort,
                direction
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
            @RequestParam(required = false) FactValueStatus status,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetQueryService.summarizeFacts(
                context,
                budgetModelId,
                groupBy,
                entityMemberId,
                timeMemberId,
                categoryMemberId,
                versionMemberId,
                status
        ));
    }

    @GetMapping("/summary/page")
    ApiResponse<PageResponse<FactSummaryResponse>> summarizeFactsPage(
            @RequestParam UUID budgetModelId,
            @RequestParam QueryGroupBy groupBy,
            @RequestParam(required = false) UUID entityMemberId,
            @RequestParam(required = false) UUID timeMemberId,
            @RequestParam(required = false) UUID categoryMemberId,
            @RequestParam(required = false) UUID versionMemberId,
            @RequestParam(required = false) FactValueStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "memberCode") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetQueryService.summarizeFactsPage(
                context,
                budgetModelId,
                groupBy,
                entityMemberId,
                timeMemberId,
                categoryMemberId,
                versionMemberId,
                status,
                page,
                size,
                sort,
                direction
        ));
    }

    @GetMapping(value = "/facts.csv", produces = "text/csv")
    ResponseEntity<String> exportFactsCsv(
            @RequestParam UUID budgetModelId,
            @RequestParam(required = false) UUID entityMemberId,
            @RequestParam(required = false) UUID timeMemberId,
            @RequestParam(required = false) UUID categoryMemberId,
            @RequestParam(required = false) UUID versionMemberId,
            @RequestParam(required = false) FactValueStatus status,
            @RequestParam(defaultValue = "1000") int limit,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        BudgetQueryService.CsvExportResult result = budgetQueryService.exportFactsCsv(
                context,
                budgetModelId,
                entityMemberId,
                timeMemberId,
                categoryMemberId,
                versionMemberId,
                status,
                limit
        );
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("text/csv"))
                .header("X-Budget-Platform-Export-Truncated", Boolean.toString(result.truncated()))
                .header("X-Budget-Platform-Export-Total-Rows", Integer.toString(result.totalRows()))
                .header("X-Budget-Platform-Export-Returned-Rows", Integer.toString(result.returnedRows()))
                .body(result.content());
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
            @RequestParam(required = false) FactValueStatus status,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetQueryService.analyzeBudgetActualVariance(
                context,
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

    @GetMapping("/variance/page")
    ApiResponse<PageResponse<BudgetActualVarianceResponse>> analyzeBudgetActualVariancePage(
            @RequestParam UUID budgetModelId,
            @RequestParam UUID budgetCategoryMemberId,
            @RequestParam UUID actualCategoryMemberId,
            @RequestParam(required = false) UUID budgetVersionMemberId,
            @RequestParam(required = false) UUID actualVersionMemberId,
            @RequestParam(required = false) UUID entityMemberId,
            @RequestParam(required = false) UUID timeMemberId,
            @RequestParam(required = false) FactValueStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "accountCode") String sort,
            @RequestParam(defaultValue = "ASC") String direction,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(budgetQueryService.analyzeBudgetActualVariancePage(
                context,
                budgetModelId,
                budgetCategoryMemberId,
                actualCategoryMemberId,
                budgetVersionMemberId,
                actualVersionMemberId,
                entityMemberId,
                timeMemberId,
                status,
                page,
                size,
                sort,
                direction
        ));
    }
}
