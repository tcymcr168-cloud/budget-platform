package com.budgetplatform.budgetmodel.service;

import com.budgetplatform.budgetmodel.api.BindBudgetModelDimensionRequest;
import com.budgetplatform.budgetmodel.api.BudgetModelDimensionResponse;
import com.budgetplatform.budgetmodel.api.BudgetModelResponse;
import com.budgetplatform.budgetmodel.api.CreateBudgetModelRequest;
import com.budgetplatform.budgetmodel.domain.BudgetModel;
import com.budgetplatform.budgetmodel.domain.BudgetModelDimensionBinding;
import com.budgetplatform.budgetmodel.repository.BudgetModelDimensionBindingRepository;
import com.budgetplatform.budgetmodel.repository.BudgetModelRepository;
import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.metadata.domain.BudgetWorkspace;
import com.budgetplatform.metadata.domain.Dimension;
import com.budgetplatform.metadata.domain.DimensionType;
import com.budgetplatform.metadata.repository.BudgetWorkspaceRepository;
import com.budgetplatform.metadata.repository.DimensionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BudgetModelService {

    private static final Set<DimensionType> REQUIRED_DIMENSION_TYPES = EnumSet.of(
            DimensionType.ACCOUNT,
            DimensionType.ENTITY,
            DimensionType.TIME,
            DimensionType.CATEGORY,
            DimensionType.VERSION
    );

    private final BudgetWorkspaceRepository workspaceRepository;
    private final DimensionRepository dimensionRepository;
    private final BudgetModelRepository budgetModelRepository;
    private final BudgetModelDimensionBindingRepository bindingRepository;

    public BudgetModelService(
            BudgetWorkspaceRepository workspaceRepository,
            DimensionRepository dimensionRepository,
            BudgetModelRepository budgetModelRepository,
            BudgetModelDimensionBindingRepository bindingRepository
    ) {
        this.workspaceRepository = workspaceRepository;
        this.dimensionRepository = dimensionRepository;
        this.budgetModelRepository = budgetModelRepository;
        this.bindingRepository = bindingRepository;
    }

    @Transactional
    public BudgetModelResponse createBudgetModel(CreateBudgetModelRequest request) {
        BudgetWorkspace workspace = workspaceRepository.findById(request.workspaceId())
                .orElseThrow(() -> notFound("Workspace was not found: " + request.workspaceId()));
        String code = Dimension.normalizeCode(request.code());

        if (budgetModelRepository.existsByWorkspaceIdAndCode(workspace.getId(), code)) {
            throw conflict("Budget model code already exists in workspace: " + code);
        }

        BudgetModel budgetModel = budgetModelRepository.save(new BudgetModel(
                workspace,
                code,
                request.name(),
                request.description()
        ));
        return BudgetModelResponse.from(budgetModel);
    }

    @Transactional(readOnly = true)
    public List<BudgetModelResponse> listBudgetModels(UUID workspaceId) {
        ensureWorkspaceExists(workspaceId);
        return budgetModelRepository.findByWorkspaceIdOrderByCode(workspaceId)
                .stream()
                .map(BudgetModelResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BudgetModelResponse getBudgetModel(UUID budgetModelId) {
        return BudgetModelResponse.from(loadBudgetModel(budgetModelId));
    }

    @Transactional
    public BudgetModelDimensionResponse bindDimension(UUID budgetModelId, BindBudgetModelDimensionRequest request) {
        BudgetModel budgetModel = loadBudgetModel(budgetModelId);
        Dimension dimension = dimensionRepository.findById(request.dimensionId())
                .orElseThrow(() -> notFound("Dimension was not found: " + request.dimensionId()));

        if (!dimension.getWorkspace().getId().equals(budgetModel.getWorkspace().getId())) {
            throw badRequest("Dimension must belong to the same workspace as the budget model.");
        }

        if (bindingRepository.existsByBudgetModel_IdAndDimension_Id(budgetModelId, dimension.getId())) {
            throw conflict("Dimension is already bound to this budget model: " + dimension.getCode());
        }

        boolean requiredForEntry = request.requiredForEntry() == null || request.requiredForEntry();
        int displayOrder = request.displayOrder() == null ? nextDisplayOrder(budgetModelId) : request.displayOrder();

        BudgetModelDimensionBinding binding = bindingRepository.save(new BudgetModelDimensionBinding(
                budgetModel,
                dimension,
                requiredForEntry,
                displayOrder
        ));
        return BudgetModelDimensionResponse.from(binding);
    }

    @Transactional(readOnly = true)
    public List<BudgetModelDimensionResponse> listBoundDimensions(UUID budgetModelId) {
        loadBudgetModel(budgetModelId);
        return loadBindings(budgetModelId)
                .stream()
                .map(BudgetModelDimensionResponse::from)
                .toList();
    }

    @Transactional
    public BudgetModelResponse activateBudgetModel(UUID budgetModelId) {
        BudgetModel budgetModel = loadBudgetModel(budgetModelId);
        Set<DimensionType> boundTypes = loadBindings(budgetModelId).stream()
                .map(BudgetModelDimensionBinding::getDimensionRole)
                .collect(Collectors.toSet());
        Set<DimensionType> missingTypes = EnumSet.copyOf(REQUIRED_DIMENSION_TYPES);
        missingTypes.removeAll(boundTypes);

        if (!missingTypes.isEmpty()) {
            throw badRequest("Budget model is missing required dimensions: " + missingTypes);
        }

        budgetModel.activate();
        return BudgetModelResponse.from(budgetModel);
    }

    @Transactional
    public BudgetModelResponse deactivateBudgetModel(UUID budgetModelId) {
        BudgetModel budgetModel = loadBudgetModel(budgetModelId);
        budgetModel.deactivate();
        return BudgetModelResponse.from(budgetModel);
    }

    private int nextDisplayOrder(UUID budgetModelId) {
        return loadBindings(budgetModelId).size() * 10;
    }

    private List<BudgetModelDimensionBinding> loadBindings(UUID budgetModelId) {
        return bindingRepository.findByBudgetModel_IdOrderByDisplayOrderAscDimension_CodeAsc(budgetModelId);
    }

    private BudgetModel loadBudgetModel(UUID budgetModelId) {
        return budgetModelRepository.findById(budgetModelId)
                .orElseThrow(() -> notFound("Budget model was not found: " + budgetModelId));
    }

    private void ensureWorkspaceExists(UUID workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            throw notFound("Workspace was not found: " + workspaceId);
        }
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
