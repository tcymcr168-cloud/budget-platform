package com.budgetplatform.metadata.service;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.common.audit.AuditAction;
import com.budgetplatform.common.audit.AuditEvent;
import com.budgetplatform.common.audit.AuditService;
import com.budgetplatform.metadata.api.CreateDimensionMemberRequest;
import com.budgetplatform.metadata.api.CreateDimensionRequest;
import com.budgetplatform.metadata.api.CreateWorkspaceRequest;
import com.budgetplatform.metadata.api.DimensionMemberResponse;
import com.budgetplatform.metadata.api.DimensionResponse;
import com.budgetplatform.metadata.api.UpdateDimensionMemberRequest;
import com.budgetplatform.metadata.api.WorkspaceResponse;
import com.budgetplatform.metadata.domain.BudgetWorkspace;
import com.budgetplatform.metadata.domain.Dimension;
import com.budgetplatform.metadata.domain.DimensionMember;
import com.budgetplatform.metadata.domain.DimensionType;
import com.budgetplatform.metadata.repository.BudgetWorkspaceRepository;
import com.budgetplatform.metadata.repository.DimensionMemberRepository;
import com.budgetplatform.metadata.repository.DimensionRepository;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.domain.SecurityRoleCode;
import com.budgetplatform.security.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class MetadataService {

    private final BudgetWorkspaceRepository workspaceRepository;
    private final DimensionRepository dimensionRepository;
    private final DimensionMemberRepository memberRepository;
    private final AuthorizationService authorizationService;
    private final AuditService auditService;

    public MetadataService(
            BudgetWorkspaceRepository workspaceRepository,
            DimensionRepository dimensionRepository,
            DimensionMemberRepository memberRepository,
            AuthorizationService authorizationService,
            AuditService auditService
    ) {
        this.workspaceRepository = workspaceRepository;
        this.dimensionRepository = dimensionRepository;
        this.memberRepository = memberRepository;
        this.authorizationService = authorizationService;
        this.auditService = auditService;
    }

    @Transactional
    public WorkspaceResponse createWorkspace(CurrentUserContext context, CreateWorkspaceRequest request) {
        authorizationService.requireHeaderAdmin(context);
        String code = Dimension.normalizeCode(request.code());
        if (workspaceRepository.existsByCode(code)) {
            throw conflict("Workspace code already exists: " + code);
        }

        BudgetWorkspace workspace = workspaceRepository.save(new BudgetWorkspace(code, request.name()));
        record(context, "budget_workspace", workspace.getId(), AuditAction.CREATE, Map.of(
                "code", workspace.getCode(),
                "name", workspace.getName()
        ));
        return WorkspaceResponse.from(workspace);
    }

    @Transactional(readOnly = true)
    public List<WorkspaceResponse> listWorkspaces(CurrentUserContext context) {
        authorizationService.requireHeaderAdmin(context);
        return workspaceRepository.findAll()
                .stream()
                .map(WorkspaceResponse::from)
                .toList();
    }

    @Transactional
    public DimensionResponse createDimension(CurrentUserContext context, CreateDimensionRequest request) {
        BudgetWorkspace workspace = workspaceRepository.findById(request.workspaceId())
                .orElseThrow(() -> notFound("Workspace was not found: " + request.workspaceId()));
        requireMetadataWrite(context, workspace.getId());
        String code = Dimension.normalizeCode(request.code());

        if (dimensionRepository.existsByWorkspaceIdAndCode(workspace.getId(), code)) {
            throw conflict("Dimension code already exists in workspace: " + code);
        }

        Dimension dimension = dimensionRepository.save(new Dimension(
                workspace,
                code,
                request.name(),
                request.dimensionType(),
                request.system(),
                request.attributesSchema()
        ));
        record(context, "dimension", dimension.getId(), AuditAction.CREATE, Map.of(
                "workspaceId", workspace.getId().toString(),
                "code", dimension.getCode(),
                "dimensionType", dimension.getDimensionType().name()
        ));
        return DimensionResponse.from(dimension);
    }

    @Transactional(readOnly = true)
    public List<DimensionResponse> listDimensions(CurrentUserContext context, UUID workspaceId, DimensionType type) {
        ensureWorkspaceExists(workspaceId);
        requireMetadataRead(context, workspaceId);
        List<Dimension> dimensions = type == null
                ? dimensionRepository.findByWorkspaceIdOrderByCode(workspaceId)
                : dimensionRepository.findByWorkspaceIdAndDimensionTypeOrderByCode(workspaceId, type);
        return dimensions.stream().map(DimensionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public DimensionResponse getDimension(CurrentUserContext context, UUID dimensionId) {
        Dimension dimension = loadDimension(dimensionId);
        requireMetadataRead(context, dimension.getWorkspace().getId());
        return DimensionResponse.from(dimension);
    }

    @Transactional
    public DimensionMemberResponse createMember(
            CurrentUserContext context,
            UUID dimensionId,
            CreateDimensionMemberRequest request
    ) {
        Dimension dimension = loadDimension(dimensionId);
        requireMetadataWrite(context, dimension.getWorkspace().getId());
        String code = Dimension.normalizeCode(request.code());

        if (memberRepository.existsByDimensionIdAndCode(dimensionId, code)) {
            throw conflict("Member code already exists in dimension: " + code);
        }

        DimensionMember parent = request.parentId() == null ? null : loadMember(request.parentId());
        validateParentBelongsToDimension(dimensionId, parent);

        DimensionMember member = memberRepository.save(new DimensionMember(
                dimension,
                code,
                request.name(),
                parent,
                request.sortOrder(),
                request.attributes()
        ));

        if (parent != null) {
            parent.markLeaf(false);
        }

        record(context, "dimension_member", member.getId(), AuditAction.CREATE, Map.of(
                "dimensionId", dimension.getId().toString(),
                "code", member.getCode(),
                "parentId", parent == null ? "" : parent.getId().toString()
        ));
        return DimensionMemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public List<DimensionMemberResponse> listMembers(CurrentUserContext context, UUID dimensionId) {
        Dimension dimension = loadDimension(dimensionId);
        requireMetadataRead(context, dimension.getWorkspace().getId());
        return memberRepository.findByDimensionIdOrderBySortOrderAscCodeAsc(dimensionId)
                .stream()
                .map(DimensionMemberResponse::from)
                .toList();
    }

    @Transactional
    public DimensionMemberResponse updateMember(
            CurrentUserContext context,
            UUID memberId,
            UpdateDimensionMemberRequest request
    ) {
        DimensionMember member = loadMember(memberId);
        requireMetadataWrite(context, member.getDimension().getWorkspace().getId());

        if (request.name() != null && !request.name().isBlank()) {
            member.rename(request.name());
        }

        if (request.status() != null) {
            member.setStatus(request.status());
        }

        if (request.parentId() != null) {
            DimensionMember previousParent = member.getParent();
            DimensionMember parent = loadMember(request.parentId());
            validateParentBelongsToDimension(member.getDimension().getId(), parent);
            validateNoCycle(member, parent);
            member.moveTo(parent);
            parent.markLeaf(false);
            refreshLeafState(previousParent);
        }

        boolean hasChildren = memberRepository.existsByDimensionIdAndParentId(member.getDimension().getId(), member.getId());
        member.markLeaf(!hasChildren);

        record(context, "dimension_member", member.getId(), AuditAction.UPDATE, Map.of(
                "dimensionId", member.getDimension().getId().toString(),
                "code", member.getCode(),
                "status", member.getStatus().name(),
                "parentId", member.getParent() == null ? "" : member.getParent().getId().toString()
        ));
        return DimensionMemberResponse.from(member);
    }

    private void record(
            CurrentUserContext context,
            String subjectType,
            UUID subjectId,
            AuditAction action,
            Map<String, Object> details
    ) {
        auditService.record(new AuditEvent(
                context == null ? null : context.userId(),
                subjectType,
                subjectId.toString(),
                action,
                null,
                details
        ));
    }

    private void requireMetadataWrite(CurrentUserContext context, UUID workspaceId) {
        authorizationService.requireAnyRole(
                context,
                workspaceId,
                SecurityRoleCode.BUDGET_ADMIN,
                SecurityRoleCode.METADATA_MANAGER
        );
    }

    private void requireMetadataRead(CurrentUserContext context, UUID workspaceId) {
        authorizationService.requireAnyRole(
                context,
                workspaceId,
                SecurityRoleCode.BUDGET_ADMIN,
                SecurityRoleCode.METADATA_MANAGER,
                SecurityRoleCode.TEMPLATE_DESIGNER,
                SecurityRoleCode.BUDGET_OWNER,
                SecurityRoleCode.BUDGET_REVIEWER,
                SecurityRoleCode.IMPORT_OPERATOR,
                SecurityRoleCode.READ_ONLY
        );
    }

    private void refreshLeafState(DimensionMember member) {
        if (member == null) {
            return;
        }
        boolean hasChildren = memberRepository.existsByDimensionIdAndParentId(member.getDimension().getId(), member.getId());
        member.markLeaf(!hasChildren);
    }

    private Dimension loadDimension(UUID dimensionId) {
        return dimensionRepository.findById(dimensionId)
                .orElseThrow(() -> notFound("Dimension was not found: " + dimensionId));
    }

    private DimensionMember loadMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> notFound("Dimension member was not found: " + memberId));
    }

    private void ensureWorkspaceExists(UUID workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            throw notFound("Workspace was not found: " + workspaceId);
        }
    }

    private void validateParentBelongsToDimension(UUID dimensionId, DimensionMember parent) {
        if (parent != null && !parent.getDimension().getId().equals(dimensionId)) {
            throw new ApplicationException(
                    ErrorCode.BAD_REQUEST,
                    HttpStatus.BAD_REQUEST,
                    "Parent member must belong to the same dimension."
            );
        }
    }

    private void validateNoCycle(DimensionMember member, DimensionMember candidateParent) {
        DimensionMember current = candidateParent;
        while (current != null) {
            if (current.getId().equals(member.getId())) {
                throw new ApplicationException(
                        ErrorCode.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST,
                        "Member hierarchy cannot contain cycles."
                );
            }
            current = current.getParent();
        }
    }

    private ApplicationException notFound(String message) {
        return new ApplicationException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, message);
    }

    private ApplicationException conflict(String message) {
        return new ApplicationException(ErrorCode.CONFLICT, HttpStatus.CONFLICT, message);
    }
}
