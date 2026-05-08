package com.budgetplatform.budgetsubmission.api;

import com.budgetplatform.budgetsubmission.service.SubmissionService;
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
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final CurrentUserContextResolver contextResolver;

    public SubmissionController(SubmissionService submissionService, CurrentUserContextResolver contextResolver) {
        this.submissionService = submissionService;
        this.contextResolver = contextResolver;
    }

    @PostMapping("/tasks")
    ApiResponse<SubmissionTaskResponse> createTask(
            @Valid @RequestBody CreateSubmissionTaskRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(submissionService.createTask(context, request));
    }

    @GetMapping("/tasks")
    ApiResponse<List<SubmissionTaskResponse>> listTasks(
            @RequestParam UUID budgetTemplateId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(submissionService.listTasks(context, budgetTemplateId));
    }

    @GetMapping("/tasks/{taskId}")
    ApiResponse<SubmissionTaskResponse> getTask(
            @PathVariable UUID taskId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(submissionService.getTask(context, taskId));
    }

    @PostMapping("/tasks/{taskId}/values")
    ApiResponse<FactValueResponse> saveValue(
            @PathVariable UUID taskId,
            @Valid @RequestBody SaveFactValueRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(submissionService.saveValue(context, taskId, request));
    }

    @GetMapping("/tasks/{taskId}/values")
    ApiResponse<List<FactValueResponse>> listValues(
            @PathVariable UUID taskId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(submissionService.listValues(context, taskId));
    }

    @PostMapping("/tasks/{taskId}/submit")
    ApiResponse<SubmissionTaskResponse> submitTask(
            @PathVariable UUID taskId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(submissionService.submitTask(context, taskId));
    }

    @PostMapping("/tasks/{taskId}/return")
    ApiResponse<SubmissionTaskResponse> returnTask(
            @PathVariable UUID taskId,
            @Valid @RequestBody ReturnSubmissionTaskRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(submissionService.returnTask(context, taskId, request));
    }

    @PostMapping("/tasks/{taskId}/approve")
    ApiResponse<SubmissionTaskResponse> approveTask(
            @PathVariable UUID taskId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(submissionService.approveTask(context, taskId));
    }

    @PostMapping("/tasks/{taskId}/lock")
    ApiResponse<SubmissionTaskResponse> lockTask(
            @PathVariable UUID taskId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(submissionService.lockTask(context, taskId));
    }
}
