package com.budgetplatform.metadata.api;

import com.budgetplatform.common.api.ApiResponse;
import com.budgetplatform.metadata.domain.DimensionType;
import com.budgetplatform.metadata.service.MetadataService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/metadata")
public class MetadataController {

    private final MetadataService metadataService;

    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @PostMapping("/workspaces")
    ApiResponse<WorkspaceResponse> createWorkspace(@Valid @RequestBody CreateWorkspaceRequest request) {
        return ApiResponse.success(metadataService.createWorkspace(request));
    }

    @GetMapping("/workspaces")
    ApiResponse<List<WorkspaceResponse>> listWorkspaces() {
        return ApiResponse.success(metadataService.listWorkspaces());
    }

    @PostMapping("/dimensions")
    ApiResponse<DimensionResponse> createDimension(@Valid @RequestBody CreateDimensionRequest request) {
        return ApiResponse.success(metadataService.createDimension(request));
    }

    @GetMapping("/dimensions")
    ApiResponse<List<DimensionResponse>> listDimensions(
            @RequestParam UUID workspaceId,
            @RequestParam(required = false) DimensionType type
    ) {
        return ApiResponse.success(metadataService.listDimensions(workspaceId, type));
    }

    @GetMapping("/dimensions/{dimensionId}")
    ApiResponse<DimensionResponse> getDimension(@PathVariable UUID dimensionId) {
        return ApiResponse.success(metadataService.getDimension(dimensionId));
    }

    @PostMapping("/dimensions/{dimensionId}/members")
    ApiResponse<DimensionMemberResponse> createMember(
            @PathVariable UUID dimensionId,
            @Valid @RequestBody CreateDimensionMemberRequest request
    ) {
        return ApiResponse.success(metadataService.createMember(dimensionId, request));
    }

    @GetMapping("/dimensions/{dimensionId}/members")
    ApiResponse<List<DimensionMemberResponse>> listMembers(@PathVariable UUID dimensionId) {
        return ApiResponse.success(metadataService.listMembers(dimensionId));
    }

    @PatchMapping("/members/{memberId}")
    ApiResponse<DimensionMemberResponse> updateMember(
            @PathVariable UUID memberId,
            @Valid @RequestBody UpdateDimensionMemberRequest request
    ) {
        return ApiResponse.success(metadataService.updateMember(memberId, request));
    }
}
