package com.budgetplatform.budgetsubmission.service;

import com.budgetplatform.budgetmodel.domain.BudgetModelDimensionBinding;
import com.budgetplatform.budgetmodel.repository.BudgetModelDimensionBindingRepository;
import com.budgetplatform.budgetsubmission.api.CreateSubmissionTaskRequest;
import com.budgetplatform.budgetsubmission.api.FactValueResponse;
import com.budgetplatform.budgetsubmission.api.ReturnSubmissionTaskRequest;
import com.budgetplatform.budgetsubmission.api.SaveFactValueRequest;
import com.budgetplatform.budgetsubmission.api.SubmissionTaskResponse;
import com.budgetplatform.budgetsubmission.domain.FactValue;
import com.budgetplatform.budgetsubmission.domain.FactValueStatus;
import com.budgetplatform.budgetsubmission.domain.SubmissionStatus;
import com.budgetplatform.budgetsubmission.domain.SubmissionTask;
import com.budgetplatform.budgetsubmission.repository.FactValueRepository;
import com.budgetplatform.budgetsubmission.repository.SubmissionTaskRepository;
import com.budgetplatform.budgettemplate.domain.BudgetTemplate;
import com.budgetplatform.budgettemplate.domain.BudgetTemplateStatus;
import com.budgetplatform.budgettemplate.repository.BudgetTemplateRepository;
import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.common.audit.AuditAction;
import com.budgetplatform.common.audit.AuditEvent;
import com.budgetplatform.common.audit.AuditService;
import com.budgetplatform.metadata.domain.DimensionMember;
import com.budgetplatform.metadata.domain.DimensionType;
import com.budgetplatform.metadata.repository.DimensionMemberRepository;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.domain.AppUser;
import com.budgetplatform.security.domain.SecurityRoleCode;
import com.budgetplatform.security.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    private final BudgetTemplateRepository templateRepository;
    private final BudgetModelDimensionBindingRepository bindingRepository;
    private final DimensionMemberRepository memberRepository;
    private final SubmissionTaskRepository taskRepository;
    private final FactValueRepository factValueRepository;
    private final AuditService auditService;
    private final AuthorizationService authorizationService;

    public SubmissionService(
            BudgetTemplateRepository templateRepository,
            BudgetModelDimensionBindingRepository bindingRepository,
            DimensionMemberRepository memberRepository,
            SubmissionTaskRepository taskRepository,
            FactValueRepository factValueRepository,
            AuditService auditService,
            AuthorizationService authorizationService
    ) {
        this.templateRepository = templateRepository;
        this.bindingRepository = bindingRepository;
        this.memberRepository = memberRepository;
        this.taskRepository = taskRepository;
        this.factValueRepository = factValueRepository;
        this.auditService = auditService;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public SubmissionTaskResponse createTask(CurrentUserContext context, CreateSubmissionTaskRequest request) {
        BudgetTemplate template = templateRepository.findById(request.budgetTemplateId())
                .orElseThrow(() -> notFound("Budget template was not found: " + request.budgetTemplateId()));
        if (template.getStatus() != BudgetTemplateStatus.ACTIVE) {
            throw badRequest("Submission task can only be created for an active budget template.");
        }

        if (taskRepository.existsByBudgetTemplate_IdAndEntityMember_IdAndTimeMember_IdAndCategoryMember_IdAndVersionMember_Id(
                template.getId(),
                request.entityMemberId(),
                request.timeMemberId(),
                request.categoryMemberId(),
                request.versionMemberId()
        )) {
            throw conflict("Submission task already exists for the template scope.");
        }

        Map<DimensionType, DimensionMember> scope = loadAndValidateScopeMembers(template, request);
        requireTaskCreate(context, template, scope.get(DimensionType.ENTITY), request.ownerUser());
        SubmissionTask task = taskRepository.save(new SubmissionTask(
                template,
                scope.get(DimensionType.ENTITY),
                scope.get(DimensionType.TIME),
                scope.get(DimensionType.CATEGORY),
                scope.get(DimensionType.VERSION),
                request.ownerUser(),
                request.reviewerUser()
        ));
        record(task, AuditAction.CREATE, "Task created");
        return SubmissionTaskResponse.from(task);
    }

    @Transactional(readOnly = true)
    public List<SubmissionTaskResponse> listTasks(CurrentUserContext context, UUID budgetTemplateId) {
        BudgetTemplate template = loadTemplate(budgetTemplateId);
        requireTaskList(context, template.getBudgetModel().getWorkspace().getId());
        return taskRepository.findByBudgetTemplate_IdOrderByUpdatedAtDesc(budgetTemplateId)
                .stream()
                .map(SubmissionTaskResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubmissionTaskResponse getTask(CurrentUserContext context, UUID taskId) {
        SubmissionTask task = loadTask(taskId);
        requireTaskRead(context, task);
        return SubmissionTaskResponse.from(task);
    }

    @Transactional
    public FactValueResponse saveValue(CurrentUserContext context, UUID taskId, SaveFactValueRequest request) {
        SubmissionTask task = loadTask(taskId);
        requireOwnerAction(context, task);
        ensureEditable(task);

        DimensionMember accountMember = memberRepository.findById(request.accountMemberId())
                .orElseThrow(() -> notFound("Account member was not found: " + request.accountMemberId()));
        validateMemberForModel(task.getBudgetModel().getId(), accountMember, DimensionType.ACCOUNT);

        FactValue factValue = factValueRepository.findBySubmissionTask_IdAndAccountMember_Id(taskId, accountMember.getId())
                .map(existing -> {
                    existing.updateAmount(request.amount(), request.note());
                    return existing;
                })
                .orElseGet(() -> factValueRepository.save(new FactValue(task, accountMember, request.amount(), request.note())));

        task.markDraft();
        record(task, AuditAction.UPDATE, "Draft value saved");
        return FactValueResponse.from(factValue);
    }

    @Transactional(readOnly = true)
    public List<FactValueResponse> listValues(CurrentUserContext context, UUID taskId) {
        SubmissionTask task = loadTask(taskId);
        requireTaskRead(context, task);
        return factValueRepository.findBySubmissionTask_IdOrderByAccountMember_CodeAsc(taskId)
                .stream()
                .map(FactValueResponse::from)
                .toList();
    }

    @Transactional
    public SubmissionTaskResponse submitTask(CurrentUserContext context, UUID taskId) {
        SubmissionTask task = loadTask(taskId);
        requireOwnerAction(context, task);
        requireStatus(task, SubmissionStatus.DRAFT, SubmissionStatus.RETURNED);
        if (factValueRepository.countBySubmissionTask_Id(taskId) == 0) {
            throw badRequest("Submission task must contain at least one value before submit.");
        }
        task.submit();
        factValueRepository.updateStatuses(taskId, FactValueStatus.SUBMITTED);
        record(task, AuditAction.STATUS_CHANGE, "Task submitted");
        return SubmissionTaskResponse.from(task);
    }

    @Transactional
    public SubmissionTaskResponse returnTask(
            CurrentUserContext context,
            UUID taskId,
            ReturnSubmissionTaskRequest request
    ) {
        SubmissionTask task = loadTask(taskId);
        requireReviewerAction(context, task);
        requireStatus(task, SubmissionStatus.SUBMITTED);
        task.returnForRevision(request.reason());
        factValueRepository.updateStatuses(taskId, FactValueStatus.DRAFT);
        record(task, AuditAction.STATUS_CHANGE, "Task returned");
        return SubmissionTaskResponse.from(task);
    }

    @Transactional
    public SubmissionTaskResponse approveTask(CurrentUserContext context, UUID taskId) {
        SubmissionTask task = loadTask(taskId);
        requireReviewerAction(context, task);
        requireStatus(task, SubmissionStatus.SUBMITTED);
        task.approve();
        factValueRepository.updateStatuses(taskId, FactValueStatus.APPROVED);
        record(task, AuditAction.STATUS_CHANGE, "Task approved");
        return SubmissionTaskResponse.from(task);
    }

    @Transactional
    public SubmissionTaskResponse lockTask(CurrentUserContext context, UUID taskId) {
        SubmissionTask task = loadTask(taskId);
        requireReviewerAction(context, task);
        requireStatus(task, SubmissionStatus.APPROVED);
        task.lock();
        factValueRepository.updateStatuses(taskId, FactValueStatus.LOCKED);
        record(task, AuditAction.STATUS_CHANGE, "Task locked");
        return SubmissionTaskResponse.from(task);
    }

    private void requireTaskCreate(
            CurrentUserContext context,
            BudgetTemplate template,
            DimensionMember entityMember,
            String ownerUser
    ) {
        requireAuthenticated(context);
        UUID workspaceId = template.getBudgetModel().getWorkspace().getId();
        if (isAdmin(context, workspaceId) || sameUser(context, ownerUser)) {
            return;
        }
        if (hasRole(context, workspaceId, SecurityRoleCode.BUDGET_OWNER)
                && authorizationService.canReadEntity(context, workspaceId, entityMember.getId())) {
            return;
        }
        throw forbidden("Submission task creation requires BUDGET_ADMIN, the assigned owner, or BUDGET_OWNER entity scope.");
    }

    private void requireTaskList(CurrentUserContext context, UUID workspaceId) {
        authorizationService.requireAnyRole(
                context,
                workspaceId,
                SecurityRoleCode.BUDGET_ADMIN,
                SecurityRoleCode.BUDGET_OWNER,
                SecurityRoleCode.BUDGET_REVIEWER,
                SecurityRoleCode.READ_ONLY
        );
    }

    private void requireTaskRead(CurrentUserContext context, SubmissionTask task) {
        requireAuthenticated(context);
        UUID workspaceId = task.getBudgetModel().getWorkspace().getId();
        if (isAdmin(context, workspaceId) || sameUser(context, task.getOwnerUser()) || sameUser(context, task.getReviewerUser())) {
            return;
        }
        authorizationService.requireAnyRole(
                context,
                workspaceId,
                SecurityRoleCode.BUDGET_OWNER,
                SecurityRoleCode.BUDGET_REVIEWER,
                SecurityRoleCode.READ_ONLY
        );
        if (!authorizationService.canReadEntity(context, workspaceId, task.getEntityMember().getId())) {
            throw forbidden("Entity scope does not allow reading this submission task.");
        }
    }

    private void requireOwnerAction(CurrentUserContext context, SubmissionTask task) {
        requireAuthenticated(context);
        UUID workspaceId = task.getBudgetModel().getWorkspace().getId();
        if (isAdmin(context, workspaceId) || sameUser(context, task.getOwnerUser())) {
            return;
        }
        if (hasRole(context, workspaceId, SecurityRoleCode.BUDGET_OWNER)
                && authorizationService.canReadEntity(context, workspaceId, task.getEntityMember().getId())) {
            return;
        }
        throw forbidden("Submission owner action requires BUDGET_ADMIN, assigned owner, or BUDGET_OWNER entity scope.");
    }

    private void requireReviewerAction(CurrentUserContext context, SubmissionTask task) {
        requireAuthenticated(context);
        UUID workspaceId = task.getBudgetModel().getWorkspace().getId();
        if (isAdmin(context, workspaceId) || sameUser(context, task.getReviewerUser())) {
            return;
        }
        if (hasRole(context, workspaceId, SecurityRoleCode.BUDGET_REVIEWER)
                && authorizationService.canReadEntity(context, workspaceId, task.getEntityMember().getId())) {
            return;
        }
        throw forbidden("Submission reviewer action requires BUDGET_ADMIN, assigned reviewer, or BUDGET_REVIEWER entity scope.");
    }

    private boolean isAdmin(CurrentUserContext context, UUID workspaceId) {
        return hasRole(context, workspaceId, SecurityRoleCode.BUDGET_ADMIN);
    }

    private boolean hasRole(CurrentUserContext context, UUID workspaceId, SecurityRoleCode roleCode) {
        Set<SecurityRoleCode> roles = authorizationService.rolesForWorkspace(context, workspaceId);
        return roles.contains(roleCode);
    }

    private boolean sameUser(CurrentUserContext context, String expectedUser) {
        return expectedUser != null
                && context != null
                && context.authenticated()
                && AppUser.normalizeUsername(context.userId()).equals(AppUser.normalizeUsername(expectedUser));
    }

    private void requireAuthenticated(CurrentUserContext context) {
        if (context == null || !context.authenticated()) {
            throw new ApplicationException(
                    ErrorCode.UNAUTHORIZED,
                    HttpStatus.UNAUTHORIZED,
                    "Authenticated user context is required."
            );
        }
    }

    private Map<DimensionType, DimensionMember> loadAndValidateScopeMembers(
            BudgetTemplate template,
            CreateSubmissionTaskRequest request
    ) {
        Map<DimensionType, DimensionMember> scope = new EnumMap<>(DimensionType.class);
        scope.put(DimensionType.ENTITY, loadMember(request.entityMemberId()));
        scope.put(DimensionType.TIME, loadMember(request.timeMemberId()));
        scope.put(DimensionType.CATEGORY, loadMember(request.categoryMemberId()));
        scope.put(DimensionType.VERSION, loadMember(request.versionMemberId()));
        scope.forEach((type, member) -> validateMemberForModel(template.getBudgetModel().getId(), member, type));
        return scope;
    }

    private DimensionMember loadMember(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> notFound("Dimension member was not found: " + memberId));
    }

    private void validateMemberForModel(UUID budgetModelId, DimensionMember member, DimensionType expectedType) {
        if (member.getDimension().getDimensionType() != expectedType) {
            throw badRequest("Member " + member.getCode() + " must belong to " + expectedType + " dimension.");
        }

        Map<UUID, DimensionType> modelDimensionTypes = bindingRepository
                .findByBudgetModel_IdOrderByDisplayOrderAscDimension_CodeAsc(budgetModelId)
                .stream()
                .collect(Collectors.toMap(
                        binding -> binding.getDimension().getId(),
                        BudgetModelDimensionBinding::getDimensionRole
                ));

        if (!modelDimensionTypes.containsKey(member.getDimension().getId())) {
            throw badRequest("Member dimension is not bound to this budget model: " + member.getDimension().getCode());
        }
    }

    private void ensureEditable(SubmissionTask task) {
        if (task.getStatus() != SubmissionStatus.NOT_STARTED
                && task.getStatus() != SubmissionStatus.DRAFT
                && task.getStatus() != SubmissionStatus.RETURNED) {
            throw badRequest("Submission task is not editable in status: " + task.getStatus());
        }
    }

    private void requireStatus(SubmissionTask task, SubmissionStatus... allowedStatuses) {
        for (SubmissionStatus allowedStatus : allowedStatuses) {
            if (task.getStatus() == allowedStatus) {
                return;
            }
        }
        throw badRequest("Submission task cannot transition from status: " + task.getStatus());
    }

    private SubmissionTask loadTask(UUID taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> notFound("Submission task was not found: " + taskId));
    }

    private BudgetTemplate loadTemplate(UUID budgetTemplateId) {
        return templateRepository.findById(budgetTemplateId)
                .orElseThrow(() -> notFound("Budget template was not found: " + budgetTemplateId));
    }

    private void record(SubmissionTask task, AuditAction action, String message) {
        auditService.record(new AuditEvent(
                "system",
                "submission_task",
                task.getId().toString(),
                action,
                null,
                Map.of("status", task.getStatus().name(), "message", message)
        ));
    }

    private ApplicationException notFound(String message) {
        return new ApplicationException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, message);
    }

    private ApplicationException conflict(String message) {
        return new ApplicationException(ErrorCode.CONFLICT, HttpStatus.CONFLICT, message);
    }

    private ApplicationException badRequest(String message) {
        return new ApplicationException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, message);
    }

    private ApplicationException forbidden(String message) {
        return new ApplicationException(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN, message);
    }
}
