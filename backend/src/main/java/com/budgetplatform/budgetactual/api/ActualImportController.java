package com.budgetplatform.budgetactual.api;

import com.budgetplatform.budgetactual.service.ActualImportService;
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
@RequestMapping("/api/actual-imports")
public class ActualImportController {

    private final ActualImportService actualImportService;
    private final CurrentUserContextResolver contextResolver;

    public ActualImportController(ActualImportService actualImportService, CurrentUserContextResolver contextResolver) {
        this.actualImportService = actualImportService;
        this.contextResolver = contextResolver;
    }

    @PostMapping("/validate")
    ApiResponse<ActualImportBatchResponse> validateCsv(
            @Valid @RequestBody ValidateActualImportRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(actualImportService.validateCsv(context, request));
    }

    @PostMapping("/{batchId}/commit")
    ApiResponse<ActualImportBatchResponse> commit(
            @PathVariable UUID batchId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(actualImportService.commit(context, batchId));
    }

    @GetMapping("/batches")
    ApiResponse<List<ActualImportBatchResponse>> listBatches(
            @RequestParam UUID budgetModelId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(actualImportService.listBatches(context, budgetModelId));
    }

    @GetMapping("/batches/{batchId}/rows")
    ApiResponse<List<ActualImportRowResponse>> listRows(
            @PathVariable UUID batchId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(actualImportService.listRows(context, batchId));
    }
}
