package com.budgetplatform.common.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class JpaAuditService implements AuditService {

    private final AuditEventRecordRepository repository;
    private final ObjectMapper objectMapper;

    JpaAuditService(AuditEventRecordRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void record(AuditEvent event) {
        repository.save(new AuditEventRecord(event, detailsJson(event)));
    }

    private String detailsJson(AuditEvent event) {
        try {
            return objectMapper.writeValueAsString(event.details());
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Audit event details could not be serialized.", exception);
        }
    }
}
