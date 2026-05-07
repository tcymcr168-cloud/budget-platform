package com.budgetplatform.budgetsubmission.api;

import com.budgetplatform.budgetsubmission.service.SubmissionService;
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
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping("/tasks")
    ApiResponse<SubmissionTaskResponse> createTask(@Valid @RequestBody CreateSubmissionTaskRequest request) {
        return ApiResponse.success(submissionService.createTask(request));
    }

    @GetMapping("/tasks")
    ApiResponse<List<SubmissionTaskResponse>> listTasks(@RequestParam UUID budgetTemplateId) {
        return ApiResponse.success(submissionService.listTasks(budgetTemplateId));
    }

    @GetMapping("/tasks/{taskId}")
    ApiResponse<SubmissionTaskResponse> getTask(@PathVariable UUID taskId) {
        return ApiResponse.success(submissionService.getTask(taskId));
    }

    @PostMapping("/tasks/{taskId}/values")
    ApiResponse<FactValueResponse> saveValue(
            @PathVariable UUID taskId,
            @Valid @RequestBody SaveFactValueRequest request
    ) {
        return ApiResponse.success(submissionService.saveValue(taskId, request));
    }

    @GetMapping("/tasks/{taskId}/values")
    ApiResponse<List<FactValueResponse>> listValues(@PathVariable UUID taskId) {
        return ApiResponse.success(submissionService.listValues(taskId));
    }

    @PostMapping("/tasks/{taskId}/submit")
    ApiResponse<SubmissionTaskResponse> submitTask(@PathVariable UUID taskId) {
        return ApiResponse.success(submissionService.submitTask(taskId));
    }

    @PostMapping("/tasks/{taskId}/return")
    ApiResponse<SubmissionTaskResponse> returnTask(
            @PathVariable UUID taskId,
            @Valid @RequestBody ReturnSubmissionTaskRequest request
    ) {
        return ApiResponse.success(submissionService.returnTask(taskId, request));
    }

    @PostMapping("/tasks/{taskId}/approve")
    ApiResponse<SubmissionTaskResponse> approveTask(@PathVariable UUID taskId) {
        return ApiResponse.success(submissionService.approveTask(taskId));
    }

    @PostMapping("/tasks/{taskId}/lock")
    ApiResponse<SubmissionTaskResponse> lockTask(@PathVariable UUID taskId) {
        return ApiResponse.success(submissionService.lockTask(taskId));
    }
}
