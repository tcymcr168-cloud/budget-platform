package com.budgetplatform.budgetactual.service;

import com.budgetplatform.budgetactual.api.ActualImportBatchResponse;
import com.budgetplatform.budgetactual.api.ActualImportRowResponse;
import com.budgetplatform.budgetactual.api.ValidateActualImportRequest;
import com.budgetplatform.budgetactual.domain.ActualImportBatch;
import com.budgetplatform.budgetactual.domain.ActualImportRow;
import com.budgetplatform.budgetactual.domain.ActualImportRowStatus;
import com.budgetplatform.budgetactual.domain.ActualImportStatus;
import com.budgetplatform.budgetactual.repository.ActualImportBatchRepository;
import com.budgetplatform.budgetactual.repository.ActualImportRowRepository;
import com.budgetplatform.budgetmodel.domain.BudgetModel;
import com.budgetplatform.budgetmodel.domain.BudgetModelDimensionBinding;
import com.budgetplatform.budgetmodel.domain.BudgetModelStatus;
import com.budgetplatform.budgetmodel.repository.BudgetModelDimensionBindingRepository;
import com.budgetplatform.budgetmodel.repository.BudgetModelRepository;
import com.budgetplatform.budgetsubmission.domain.FactValue;
import com.budgetplatform.budgetsubmission.repository.FactValueRepository;
import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.common.audit.AuditAction;
import com.budgetplatform.common.audit.AuditEvent;
import com.budgetplatform.common.audit.AuditService;
import com.budgetplatform.metadata.domain.Dimension;
import com.budgetplatform.metadata.domain.DimensionMember;
import com.budgetplatform.metadata.domain.DimensionType;
import com.budgetplatform.metadata.repository.DimensionMemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ActualImportService {

    private static final List<String> REQUIRED_HEADERS = List.of("account", "entity", "time", "category", "version", "amount");
    private static final List<DimensionType> REQUIRED_DIMENSION_TYPES = List.of(
            DimensionType.ACCOUNT,
            DimensionType.ENTITY,
            DimensionType.TIME,
            DimensionType.CATEGORY,
            DimensionType.VERSION
    );

    private final BudgetModelRepository budgetModelRepository;
    private final BudgetModelDimensionBindingRepository bindingRepository;
    private final DimensionMemberRepository memberRepository;
    private final ActualImportBatchRepository batchRepository;
    private final ActualImportRowRepository rowRepository;
    private final FactValueRepository factValueRepository;
    private final AuditService auditService;

    public ActualImportService(
            BudgetModelRepository budgetModelRepository,
            BudgetModelDimensionBindingRepository bindingRepository,
            DimensionMemberRepository memberRepository,
            ActualImportBatchRepository batchRepository,
            ActualImportRowRepository rowRepository,
            FactValueRepository factValueRepository,
            AuditService auditService
    ) {
        this.budgetModelRepository = budgetModelRepository;
        this.bindingRepository = bindingRepository;
        this.memberRepository = memberRepository;
        this.batchRepository = batchRepository;
        this.rowRepository = rowRepository;
        this.factValueRepository = factValueRepository;
        this.auditService = auditService;
    }

    @Transactional
    public ActualImportBatchResponse validateCsv(ValidateActualImportRequest request) {
        BudgetModel model = loadActiveModel(request.budgetModelId());
        ActualImportBatch batch = batchRepository.save(new ActualImportBatch(model, request.fileName(), request.operatorUser()));
        Map<DimensionType, UUID> dimensionIds = loadRequiredDimensionIds(model.getId());

        List<String> lines = request.csvContent()
                .lines()
                .filter(line -> !line.isBlank())
                .toList();
        if (lines.isEmpty()) {
            batch.markFailed("CSV content is empty.");
            record(batch, AuditAction.CREATE, "CSV validation failed");
            return toResponse(batch);
        }

        Map<String, Integer> headerIndexes = parseHeaders(lines.get(0));
        List<String> missingHeaders = REQUIRED_HEADERS.stream()
                .filter(header -> !headerIndexes.containsKey(header))
                .toList();
        if (!missingHeaders.isEmpty()) {
            batch.markFailed("Missing required CSV headers: " + String.join(", ", missingHeaders));
            record(batch, AuditAction.CREATE, "CSV validation failed");
            return toResponse(batch);
        }

        List<ActualImportRow> rows = new ArrayList<>();
        Set<String> coordinates = new HashSet<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        int validRows = 0;
        int errorRows = 0;
        List<String> errors = new ArrayList<>();

        for (int index = 1; index < lines.size(); index++) {
            int rowNumber = index + 1;
            List<String> values = parseCsvLine(lines.get(index));
            RowDraft draft = rowDraft(values, headerIndexes);
            ValidationResult result = validateRow(draft, rowNumber, dimensionIds, coordinates);
            if (result.errorMessage() == null) {
                validRows++;
                totalAmount = totalAmount.add(result.amount());
            } else {
                errorRows++;
                errors.add("Row " + rowNumber + ": " + result.errorMessage());
            }
            rows.add(result.toRow(batch, rowNumber, draft));
        }

        batch.updateValidationSummary(rows.size(), validRows, errorRows, totalAmount, String.join("\n", errors));
        rowRepository.saveAll(rows);
        record(batch, AuditAction.CREATE, "CSV validation completed");
        return toResponse(batch);
    }

    @Transactional
    public ActualImportBatchResponse commit(UUID batchId) {
        ActualImportBatch batch = loadBatch(batchId);
        if (batch.getStatus() == ActualImportStatus.COMMITTED) {
            throw conflict("Actual import batch has already been committed.");
        }
        if (batch.getStatus() != ActualImportStatus.VALIDATED) {
            throw badRequest("Actual import batch must be VALIDATED before commit.");
        }
        if (batch.getErrorRows() > 0) {
            throw badRequest("Actual import batch contains validation errors and cannot be committed.");
        }
        if (batch.getValidRows() == 0) {
            throw badRequest("Actual import batch has no valid rows to commit.");
        }

        List<FactValue> facts = rowRepository.findByBatch_IdAndRowStatusOrderByRowNumberAsc(batchId, ActualImportRowStatus.VALID)
                .stream()
                .map(row -> new FactValue(
                        batch.getBudgetModel(),
                        batch,
                        row.getAccountMember(),
                        row.getEntityMember(),
                        row.getTimeMember(),
                        row.getCategoryMember(),
                        row.getVersionMember(),
                        row.getAmount(),
                        "Actual import " + batch.getFileName() + " row " + row.getRowNumber()
                ))
                .toList();
        factValueRepository.saveAll(facts);
        batch.commit();
        record(batch, AuditAction.STATUS_CHANGE, "CSV batch committed");
        return toResponse(batch);
    }

    @Transactional(readOnly = true)
    public List<ActualImportBatchResponse> listBatches(UUID budgetModelId) {
        ensureModelExists(budgetModelId);
        return batchRepository.findByBudgetModel_IdOrderByUpdatedAtDesc(budgetModelId)
                .stream()
                .map(batch -> ActualImportBatchResponse.from(batch, List.of()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ActualImportRowResponse> listRows(UUID batchId) {
        loadBatch(batchId);
        return rowRepository.findByBatch_IdOrderByRowNumberAsc(batchId)
                .stream()
                .map(ActualImportRowResponse::from)
                .toList();
    }

    private BudgetModel loadActiveModel(UUID budgetModelId) {
        BudgetModel model = budgetModelRepository.findById(budgetModelId)
                .orElseThrow(() -> notFound("Budget model was not found: " + budgetModelId));
        if (model.getStatus() != BudgetModelStatus.ACTIVE) {
            throw badRequest("Actual import requires an active budget model.");
        }
        return model;
    }

    private void ensureModelExists(UUID budgetModelId) {
        if (!budgetModelRepository.existsById(budgetModelId)) {
            throw notFound("Budget model was not found: " + budgetModelId);
        }
    }

    private ActualImportBatch loadBatch(UUID batchId) {
        return batchRepository.findById(batchId)
                .orElseThrow(() -> notFound("Actual import batch was not found: " + batchId));
    }

    private Map<DimensionType, UUID> loadRequiredDimensionIds(UUID budgetModelId) {
        Map<DimensionType, UUID> dimensionIds = new EnumMap<>(DimensionType.class);
        for (BudgetModelDimensionBinding binding : bindingRepository.findByBudgetModel_IdOrderByDisplayOrderAscDimension_CodeAsc(budgetModelId)) {
            if (REQUIRED_DIMENSION_TYPES.contains(binding.getDimensionRole())) {
                dimensionIds.putIfAbsent(binding.getDimensionRole(), binding.getDimension().getId());
            }
        }
        for (DimensionType requiredType : REQUIRED_DIMENSION_TYPES) {
            if (!dimensionIds.containsKey(requiredType)) {
                throw badRequest("Budget model is missing required dimension: " + requiredType);
            }
        }
        return dimensionIds;
    }

    private Map<String, Integer> parseHeaders(String headerLine) {
        Map<String, Integer> indexes = new HashMap<>();
        List<String> headers = parseCsvLine(headerLine);
        for (int index = 0; index < headers.size(); index++) {
            indexes.put(headers.get(index).trim().toLowerCase(), index);
        }
        return indexes;
    }

    private RowDraft rowDraft(List<String> values, Map<String, Integer> headerIndexes) {
        return new RowDraft(
                value(values, headerIndexes, "account"),
                value(values, headerIndexes, "entity"),
                value(values, headerIndexes, "time"),
                value(values, headerIndexes, "category"),
                value(values, headerIndexes, "version"),
                value(values, headerIndexes, "amount")
        );
    }

    private String value(List<String> values, Map<String, Integer> headerIndexes, String header) {
        int index = headerIndexes.get(header);
        if (index >= values.size()) {
            return "";
        }
        return Dimension.normalizeCode(values.get(index));
    }

    private ValidationResult validateRow(
            RowDraft draft,
            int rowNumber,
            Map<DimensionType, UUID> dimensionIds,
            Set<String> coordinates
    ) {
        List<String> errors = new ArrayList<>();
        Map<DimensionType, DimensionMember> members = new EnumMap<>(DimensionType.class);
        members.put(DimensionType.ACCOUNT, loadMember(dimensionIds.get(DimensionType.ACCOUNT), draft.accountCode(), "account", errors));
        members.put(DimensionType.ENTITY, loadMember(dimensionIds.get(DimensionType.ENTITY), draft.entityCode(), "entity", errors));
        members.put(DimensionType.TIME, loadMember(dimensionIds.get(DimensionType.TIME), draft.timeCode(), "time", errors));
        members.put(DimensionType.CATEGORY, loadMember(dimensionIds.get(DimensionType.CATEGORY), draft.categoryCode(), "category", errors));
        members.put(DimensionType.VERSION, loadMember(dimensionIds.get(DimensionType.VERSION), draft.versionCode(), "version", errors));

        BigDecimal amount = parseAmount(draft.amountText(), errors);
        if (!draft.categoryCode().equals("ACTUAL")) {
            errors.add("category must be ACTUAL for actual imports");
        }

        String coordinate = String.join("|", draft.accountCode(), draft.entityCode(), draft.timeCode(), draft.categoryCode(), draft.versionCode());
        if (!coordinates.add(coordinate)) {
            errors.add("duplicate coordinate in the same batch");
        }

        if (!errors.isEmpty()) {
            return ValidationResult.error(String.join("; ", errors), amount, members);
        }
        return ValidationResult.valid(amount, members);
    }

    private DimensionMember loadMember(UUID dimensionId, String code, String label, List<String> errors) {
        if (code.isBlank()) {
            errors.add(label + " code is blank");
            return null;
        }
        return memberRepository.findByDimensionIdAndCode(dimensionId, code)
                .orElseGet(() -> {
                    errors.add(label + " member was not found: " + code);
                    return null;
                });
    }

    private BigDecimal parseAmount(String amountText, List<String> errors) {
        if (amountText.isBlank()) {
            errors.add("amount is blank");
            return null;
        }
        try {
            return new BigDecimal(amountText.replace(",", ""));
        } catch (NumberFormatException exception) {
            errors.add("amount is not numeric: " + amountText);
            return null;
        }
    }

    private List<String> parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (character == '"') {
                if (quoted && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    current.append('"');
                    index++;
                } else {
                    quoted = !quoted;
                }
            } else if (character == ',' && !quoted) {
                values.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(character);
            }
        }
        values.add(current.toString().trim());
        return values;
    }

    private ActualImportBatchResponse toResponse(ActualImportBatch batch) {
        return ActualImportBatchResponse.from(batch, listRows(batch.getId()));
    }

    private void record(ActualImportBatch batch, AuditAction action, String message) {
        auditService.record(new AuditEvent(
                batch.getOperatorUser(),
                "actual_import_batch",
                batch.getId().toString(),
                action,
                null,
                Map.of("status", batch.getStatus().name(), "message", message)
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

    private record RowDraft(
            String accountCode,
            String entityCode,
            String timeCode,
            String categoryCode,
            String versionCode,
            String amountText
    ) {
    }

    private record ValidationResult(
            String errorMessage,
            BigDecimal amount,
            Map<DimensionType, DimensionMember> members
    ) {

        static ValidationResult valid(BigDecimal amount, Map<DimensionType, DimensionMember> members) {
            return new ValidationResult(null, amount, members);
        }

        static ValidationResult error(String errorMessage, BigDecimal amount, Map<DimensionType, DimensionMember> members) {
            return new ValidationResult(errorMessage, amount, members);
        }

        ActualImportRow toRow(ActualImportBatch batch, int rowNumber, RowDraft draft) {
            return new ActualImportRow(
                    batch,
                    rowNumber,
                    draft.accountCode(),
                    draft.entityCode(),
                    draft.timeCode(),
                    draft.categoryCode(),
                    draft.versionCode(),
                    amount,
                    errorMessage == null ? ActualImportRowStatus.VALID : ActualImportRowStatus.ERROR,
                    errorMessage,
                    members.get(DimensionType.ACCOUNT),
                    members.get(DimensionType.ENTITY),
                    members.get(DimensionType.TIME),
                    members.get(DimensionType.CATEGORY),
                    members.get(DimensionType.VERSION)
            );
        }
    }
}
