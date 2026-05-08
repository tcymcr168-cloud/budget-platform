package com.budgetplatform.common.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AuditEventRecordRepository extends JpaRepository<AuditEventRecord, UUID> {

    List<AuditEventRecord> findBySubjectTypeAndSubjectIdOrderByOccurredAtAsc(String subjectType, String subjectId);

    List<AuditEventRecord> findByActorIdOrderByOccurredAtAsc(String actorId);

    @Query("""
            select event
            from AuditEventRecord event
            where (:actorId is null or event.actorId = :actorId)
              and (:subjectType is null or event.subjectType = :subjectType)
              and (:subjectId is null or event.subjectId = :subjectId)
              and (:action is null or event.action = :action)
            """)
    Page<AuditEventRecord> search(
            String actorId,
            String subjectType,
            String subjectId,
            AuditAction action,
            Pageable pageable
    );
}
