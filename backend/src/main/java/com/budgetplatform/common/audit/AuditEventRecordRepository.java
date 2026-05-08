package com.budgetplatform.common.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditEventRecordRepository extends JpaRepository<AuditEventRecord, UUID> {

    List<AuditEventRecord> findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc(String subjectType, String subjectId);

    List<AuditEventRecord> findByActorIdOrderByOccurredAtAsc(String actorId);
}
