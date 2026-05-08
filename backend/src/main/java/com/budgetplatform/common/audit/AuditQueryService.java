package com.budgetplatform.common.audit;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.common.api.PageResponse;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.service.AuthorizationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditQueryService {

    private static final int MAX_PAGE_SIZE = 100;

    private final AuditEventRecordRepository repository;
    private final AuthorizationService authorizationService;

    public AuditQueryService(
            AuditEventRecordRepository repository,
            AuthorizationService authorizationService
    ) {
        this.repository = repository;
        this.authorizationService = authorizationService;
    }

    @Transactional(readOnly = true)
    public PageResponse<AuditEventResponse> search(
            CurrentUserContext context,
            String actorId,
            String subjectType,
            String subjectId,
            String action,
            int page,
            int size
    ) {
        authorizationService.requireHeaderAdmin(context);
        validatePage(page, size);
        AuditAction auditAction = parseAction(action);
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt"));
        return PageResponse.from(
                repository.search(blankToNull(actorId), blankToNull(subjectType), blankToNull(subjectId), auditAction, pageable),
                AuditEventResponse::from
        );
    }

    private void validatePage(int page, int size) {
        if (page < 0) {
            throw badRequest("Page must be greater than or equal to 0.");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw badRequest("Page size must be between 1 and " + MAX_PAGE_SIZE + ".");
        }
    }

    private AuditAction parseAction(String action) {
        String normalized = blankToNull(action);
        if (normalized == null) {
            return null;
        }
        try {
            return AuditAction.valueOf(normalized);
        } catch (IllegalArgumentException exception) {
            throw badRequest("Unsupported audit action: " + action);
        }
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private ApplicationException badRequest(String message) {
        return new ApplicationException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, message);
    }
}
