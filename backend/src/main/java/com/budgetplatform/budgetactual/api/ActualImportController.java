package com.budgetplatform.budgetactual.api;

import com.budgetplatform.budgetactual.service.ActualImportService;
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
@RequestMapping("/api/actual-imports")
public class ActualImportController {

    private final ActualImportService actualImportService;

    public ActualImportController(ActualImportService actualImportService) {
        this.actualImportService = actualImportService;
    }

    @PostMapping("/validate")
    ApiResponse<ActualImportBatchResponse> validateCsv(@Valid @RequestBody ValidateActualImportRequest request) {
        return ApiResponse.success(actualImportService.validateCsv(request));
    }

    @PostMapping("/{batchId}/commit")
    ApiResponse<ActualImportBatchResponse> commit(@PathVariable UUID batchId) {
        return ApiResponse.success(actualImportService.commit(batchId));
    }

    @GetMapping("/batches")
    ApiResponse<List<ActualImportBatchResponse>> listBatches(@RequestParam UUID budgetModelId) {
        return ApiResponse.success(actualImportService.listBatches(budgetModelId));
    }

    @GetMapping("/batches/{batchId}/rows")
    ApiResponse<List<ActualImportRowResponse>> listRows(@PathVariable UUID batchId) {
        return ApiResponse.success(actualImportService.listRows(batchId));
    }
}
