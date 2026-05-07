package com.budgetplatform.budgettemplate.service;

import com.budgetplatform.budgetmodel.domain.BudgetModel;
import com.budgetplatform.budgetmodel.domain.BudgetModelDimensionBinding;
import com.budgetplatform.budgetmodel.domain.BudgetModelStatus;
import com.budgetplatform.budgetmodel.repository.BudgetModelDimensionBindingRepository;
import com.budgetplatform.budgetmodel.repository.BudgetModelRepository;
import com.budgetplatform.budgettemplate.api.BudgetTemplateResponse;
import com.budgetplatform.budgettemplate.api.CreateBudgetTemplateRequest;
import com.budgetplatform.budgettemplate.api.CreateTemplateAxisRequest;
import com.budgetplatform.budgettemplate.api.TemplateAxisResponse;
import com.budgetplatform.budgettemplate.domain.BudgetTemplate;
import com.budgetplatform.budgettemplate.domain.BudgetTemplateAxis;
import com.budgetplatform.budgettemplate.domain.TemplateAxisType;
import com.budgetplatform.budgettemplate.repository.BudgetTemplateAxisRepository;
import com.budgetplatform.budgettemplate.repository.BudgetTemplateRepository;
import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.metadata.domain.Dimension;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BudgetTemplateService {

    private final BudgetModelRepository budgetModelRepository;
    private final BudgetModelDimensionBindingRepository bindingRepository;
    private final BudgetTemplateRepository templateRepository;
    private final BudgetTemplateAxisRepository axisRepository;

    public BudgetTemplateService(
            BudgetModelRepository budgetModelRepository,
            BudgetModelDimensionBindingRepository bindingRepository,
            BudgetTemplateRepository templateRepository,
            BudgetTemplateAxisRepository axisRepository
    ) {
        this.budgetModelRepository = budgetModelRepository;
        this.bindingRepository = bindingRepository;
        this.templateRepository = templateRepository;
        this.axisRepository = axisRepository;
    }

    @Transactional
    public BudgetTemplateResponse createTemplate(CreateBudgetTemplateRequest request) {
        BudgetModel budgetModel = budgetModelRepository.findById(request.budgetModelId())
                .orElseThrow(() -> notFound("Budget model was not found: " + request.budgetModelId()));

        if (budgetModel.getStatus() != BudgetModelStatus.ACTIVE) {
            throw badRequest("Template can only be created for an active budget model.");
        }

        String code = Dimension.normalizeCode(request.code());
        if (templateRepository.existsByBudgetModel_IdAndCode(budgetModel.getId(), code)) {
            throw conflict("Budget template code already exists in model: " + code);
        }

        BudgetTemplate template = templateRepository.save(new BudgetTemplate(
                budgetModel,
                code,
                request.name(),
                request.description()
        ));
        return BudgetTemplateResponse.from(template);
    }

    @Transactional(readOnly = true)
    public List<BudgetTemplateResponse> listTemplates(UUID budgetModelId) {
        ensureBudgetModelExists(budgetModelId);
        return templateRepository.findByBudgetModel_IdOrderByCode(budgetModelId)
                .stream()
                .map(BudgetTemplateResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BudgetTemplateResponse getTemplate(UUID budgetTemplateId) {
        return BudgetTemplateResponse.from(loadTemplate(budgetTemplateId));
    }

    @Transactional
    public TemplateAxisResponse addAxis(UUID budgetTemplateId, CreateTemplateAxisRequest request) {
        BudgetTemplate template = loadTemplate(budgetTemplateId);
        BudgetModelDimensionBinding modelDimension = bindingRepository.findById(request.modelDimensionId())
                .orElseThrow(() -> notFound("Model dimension binding was not found: " + request.modelDimensionId()));

        if (!modelDimension.getBudgetModel().getId().equals(template.getBudgetModel().getId())) {
            throw badRequest("Axis dimension must belong to the same budget model as the template.");
        }

        if (axisRepository.existsByBudgetTemplate_IdAndModelDimension_Id(budgetTemplateId, modelDimension.getId())) {
            throw conflict("Dimension is already used by this template: " + modelDimension.getDimension().getCode());
        }

        int displayOrder = request.displayOrder() == null ? nextDisplayOrder(budgetTemplateId) : request.displayOrder();
        BudgetTemplateAxis axis = axisRepository.save(new BudgetTemplateAxis(
                template,
                modelDimension,
                request.axisType(),
                request.memberSelector(),
                displayOrder
        ));
        return TemplateAxisResponse.from(axis);
    }

    @Transactional(readOnly = true)
    public List<TemplateAxisResponse> listAxes(UUID budgetTemplateId) {
        loadTemplate(budgetTemplateId);
        return axisRepository.findByBudgetTemplate_IdOrderByAxisTypeAscDisplayOrderAsc(budgetTemplateId)
                .stream()
                .map(TemplateAxisResponse::from)
                .toList();
    }

    @Transactional
    public BudgetTemplateResponse activateTemplate(UUID budgetTemplateId) {
        BudgetTemplate template = loadTemplate(budgetTemplateId);
        if (!axisRepository.existsByBudgetTemplate_IdAndAxisType(budgetTemplateId, TemplateAxisType.ROW)) {
            throw badRequest("Template must define at least one row axis before activation.");
        }
        if (!axisRepository.existsByBudgetTemplate_IdAndAxisType(budgetTemplateId, TemplateAxisType.COLUMN)) {
            throw badRequest("Template must define at least one column axis before activation.");
        }
        template.activate();
        return BudgetTemplateResponse.from(template);
    }

    @Transactional
    public BudgetTemplateResponse deactivateTemplate(UUID budgetTemplateId) {
        BudgetTemplate template = loadTemplate(budgetTemplateId);
        template.deactivate();
        return BudgetTemplateResponse.from(template);
    }

    private int nextDisplayOrder(UUID budgetTemplateId) {
        return axisRepository.findByBudgetTemplate_IdOrderByAxisTypeAscDisplayOrderAsc(budgetTemplateId).size() * 10;
    }

    private BudgetTemplate loadTemplate(UUID budgetTemplateId) {
        return templateRepository.findById(budgetTemplateId)
                .orElseThrow(() -> notFound("Budget template was not found: " + budgetTemplateId));
    }

    private void ensureBudgetModelExists(UUID budgetModelId) {
        if (!budgetModelRepository.existsById(budgetModelId)) {
            throw notFound("Budget model was not found: " + budgetModelId);
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
