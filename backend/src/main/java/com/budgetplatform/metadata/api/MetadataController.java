package com.budgetplatform.metadata.api;

import com.budgetplatform.common.api.ApiResponse;
import com.budgetplatform.metadata.domain.DimensionType;
import com.budgetplatform.metadata.service.MetadataService;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.context.CurrentUserContextResolver;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/api/metadata")
public class MetadataController {

    private final MetadataService metadataService;
    private final CurrentUserContextResolver contextResolver;

    public MetadataController(MetadataService metadataService, CurrentUserContextResolver contextResolver) {
        this.metadataService = metadataService;
        this.contextResolver = contextResolver;
    }

    @PostMapping("/workspaces")
    ApiResponse<WorkspaceResponse> createWorkspace(
            @Valid @RequestBody CreateWorkspaceRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(metadataService.createWorkspace(context, request));
    }

    @GetMapping("/workspaces")
    ApiResponse<List<WorkspaceResponse>> listWorkspaces(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(metadataService.listWorkspaces(context));
    }

    @PostMapping("/dimensions")
    ApiResponse<DimensionResponse> createDimension(
            @Valid @RequestBody CreateDimensionRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(metadataService.createDimension(context, request));
    }

    @GetMapping("/dimensions")
    ApiResponse<List<DimensionResponse>> listDimensions(
            @RequestParam UUID workspaceId,
            @RequestParam(required = false) DimensionType type,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(metadataService.listDimensions(context, workspaceId, type));
    }

    @GetMapping("/dimensions/{dimensionId}")
    ApiResponse<DimensionResponse> getDimension(
            @PathVariable UUID dimensionId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(metadataService.getDimension(context, dimensionId));
    }

    @PostMapping("/dimensions/{dimensionId}/members")
    ApiResponse<DimensionMemberResponse> createMember(
            @PathVariable UUID dimensionId,
            @Valid @RequestBody CreateDimensionMemberRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(metadataService.createMember(context, dimensionId, request));
    }

    @GetMapping("/dimensions/{dimensionId}/members")
    ApiResponse<List<DimensionMemberResponse>> listMembers(
            @PathVariable UUID dimensionId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(metadataService.listMembers(context, dimensionId));
    }

    @PatchMapping("/members/{memberId}")
    ApiResponse<DimensionMemberResponse> updateMember(
            @PathVariable UUID memberId,
            @Valid @RequestBody UpdateDimensionMemberRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(metadataService.updateMember(context, memberId, request));
    }
}
