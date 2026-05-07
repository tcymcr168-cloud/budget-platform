package com.budgetplatform.budgetquery.service;

import com.budgetplatform.budgetquery.api.FactQueryResponse;
import com.budgetplatform.budgetquery.api.FactSummaryResponse;
import com.budgetplatform.budgetquery.api.QueryGroupBy;
import com.budgetplatform.budgetsubmission.domain.FactValue;
import com.budgetplatform.budgetsubmission.domain.FactValueStatus;
import com.budgetplatform.budgetsubmission.repository.FactValueRepository;
import com.budgetplatform.metadata.domain.DimensionMember;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BudgetQueryService {

    private final FactValueRepository factValueRepository;

    public BudgetQueryService(FactValueRepository factValueRepository) {
        this.factValueRepository = factValueRepository;
    }

    @Transactional(readOnly = true)
    public List<FactQueryResponse> queryFacts(
            UUID budgetModelId,
            UUID entityMemberId,
            UUID timeMemberId,
            UUID categoryMemberId,
            UUID versionMemberId,
            FactValueStatus status
    ) {
        return filterFacts(budgetModelId, entityMemberId, timeMemberId, categoryMemberId, versionMemberId, status)
                .stream()
                .map(FactQueryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FactSummaryResponse> summarizeFacts(
            UUID budgetModelId,
            QueryGroupBy groupBy,
            UUID entityMemberId,
            UUID timeMemberId,
            UUID categoryMemberId,
            UUID versionMemberId,
            FactValueStatus status
    ) {
        Map<UUID, SummaryAccumulator> summaries = new LinkedHashMap<>();
        for (FactValue value : filterFacts(budgetModelId, entityMemberId, timeMemberId, categoryMemberId, versionMemberId, status)) {
            DimensionMember member = groupMember(value, groupBy);
            summaries.computeIfAbsent(member.getId(), ignored -> new SummaryAccumulator(member))
                    .add(value.getAmount());
        }

        return summaries.values()
                .stream()
                .map(accumulator -> accumulator.toResponse(groupBy))
                .sorted(Comparator.comparing(FactSummaryResponse::memberCode))
                .toList();
    }

    @Transactional(readOnly = true)
    public String exportFactsCsv(
            UUID budgetModelId,
            UUID entityMemberId,
            UUID timeMemberId,
            UUID categoryMemberId,
            UUID versionMemberId,
            FactValueStatus status
    ) {
        StringBuilder builder = new StringBuilder("account,entity,time,category,version,amount,status,source\n");
        queryFacts(budgetModelId, entityMemberId, timeMemberId, categoryMemberId, versionMemberId, status)
                .forEach(row -> builder.append(csv(row.accountCode())).append(',')
                        .append(csv(row.entityCode())).append(',')
                        .append(csv(row.timeCode())).append(',')
                        .append(csv(row.categoryCode())).append(',')
                        .append(csv(row.versionCode())).append(',')
                        .append(row.amount()).append(',')
                        .append(row.valueStatus()).append(',')
                        .append(row.sourceType()).append('\n'));
        return builder.toString();
    }

    private List<FactValue> filterFacts(
            UUID budgetModelId,
            UUID entityMemberId,
            UUID timeMemberId,
            UUID categoryMemberId,
            UUID versionMemberId,
            FactValueStatus status
    ) {
        return factValueRepository.findByBudgetModel_IdOrderByUpdatedAtDesc(budgetModelId)
                .stream()
                .filter(value -> entityMemberId == null || value.getEntityMember().getId().equals(entityMemberId))
                .filter(value -> timeMemberId == null || value.getTimeMember().getId().equals(timeMemberId))
                .filter(value -> categoryMemberId == null || value.getCategoryMember().getId().equals(categoryMemberId))
                .filter(value -> versionMemberId == null || value.getVersionMember().getId().equals(versionMemberId))
                .filter(value -> status == null || value.getValueStatus() == status)
                .toList();
    }

    private DimensionMember groupMember(FactValue value, QueryGroupBy groupBy) {
        return switch (groupBy) {
            case ACCOUNT -> value.getAccountMember();
            case ENTITY -> value.getEntityMember();
            case TIME -> value.getTimeMember();
            case CATEGORY -> value.getCategoryMember();
            case VERSION -> value.getVersionMember();
        };
    }

    private String csv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static class SummaryAccumulator {

        private final DimensionMember member;
        private BigDecimal total = BigDecimal.ZERO;
        private long lineCount;

        SummaryAccumulator(DimensionMember member) {
            this.member = member;
        }

        void add(BigDecimal amount) {
            total = total.add(amount);
            lineCount++;
        }

        FactSummaryResponse toResponse(QueryGroupBy groupBy) {
            return new FactSummaryResponse(
                    groupBy,
                    member.getId(),
                    member.getCode(),
                    member.getName(),
                    total,
                    lineCount
            );
        }
    }
}
