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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
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

    public SubmissionService(
            BudgetTemplateRepository templateRepository,
            BudgetModelDimensionBindingRepository bindingRepository,
            DimensionMemberRepository memberRepository,
            SubmissionTaskRepository taskRepository,
            FactValueRepository factValueRepository,
            AuditService auditService
    ) {
        this.templateRepository = templateRepository;
        this.bindingRepository = bindingRepository;
        this.memberRepository = memberRepository;
        this.taskRepository = taskRepository;
        this.factValueRepository = factValueRepository;
        this.auditService = auditService;
    }

    @Transactional
    public SubmissionTaskResponse createTask(CreateSubmissionTaskRequest request) {
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
    public List<SubmissionTaskResponse> listTasks(UUID budgetTemplateId) {
        ensureTemplateExists(budgetTemplateId);
        return taskRepository.findByBudgetTemplate_IdOrderByUpdatedAtDesc(budgetTemplateId)
                .stream()
                .map(SubmissionTaskResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubmissionTaskResponse getTask(UUID taskId) {
        return SubmissionTaskResponse.from(loadTask(taskId));
    }

    @Transactional
    public FactValueResponse saveValue(UUID taskId, SaveFactValueRequest request) {
        SubmissionTask task = loadTask(taskId);
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
    public List<FactValueResponse> listValues(UUID taskId) {
        loadTask(taskId);
        return factValueRepository.findBySubmissionTask_IdOrderByAccountMember_CodeAsc(taskId)
                .stream()
                .map(FactValueResponse::from)
                .toList();
    }

    @Transactional
    public SubmissionTaskResponse submitTask(UUID taskId) {
        SubmissionTask task = loadTask(taskId);
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
    public SubmissionTaskResponse returnTask(UUID taskId, ReturnSubmissionTaskRequest request) {
        SubmissionTask task = loadTask(taskId);
        requireStatus(task, SubmissionStatus.SUBMITTED);
        task.returnForRevision(request.reason());
        factValueRepository.updateStatuses(taskId, FactValueStatus.DRAFT);
        record(task, AuditAction.STATUS_CHANGE, "Task returned");
        return SubmissionTaskResponse.from(task);
    }

    @Transactional
    public SubmissionTaskResponse approveTask(UUID taskId) {
        SubmissionTask task = loadTask(taskId);
        requireStatus(task, SubmissionStatus.SUBMITTED);
        task.approve();
        factValueRepository.updateStatuses(taskId, FactValueStatus.APPROVED);
        record(task, AuditAction.STATUS_CHANGE, "Task approved");
        return SubmissionTaskResponse.from(task);
    }

    @Transactional
    public SubmissionTaskResponse lockTask(UUID taskId) {
        SubmissionTask task = loadTask(taskId);
        requireStatus(task, SubmissionStatus.APPROVED);
        task.lock();
        factValueRepository.updateStatuses(taskId, FactValueStatus.LOCKED);
        record(task, AuditAction.STATUS_CHANGE, "Task locked");
        return SubmissionTaskResponse.from(task);
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

    private void ensureTemplateExists(UUID budgetTemplateId) {
        if (!templateRepository.existsById(budgetTemplateId)) {
            throw notFound("Budget template was not found: " + budgetTemplateId);
        }
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
}
