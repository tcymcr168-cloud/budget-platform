package com.budgetplatform.common.audit;

import com.budgetplatform.common.api.ApiResponse;
import com.budgetplatform.common.api.PageResponse;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.context.CurrentUserContextResolver;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit/events")
public class AuditController {

    private final AuditQueryService auditQueryService;
    private final CurrentUserContextResolver contextResolver;

    public AuditController(
            AuditQueryService auditQueryService,
            CurrentUserContextResolver contextResolver
    ) {
        this.auditQueryService = auditQueryService;
        this.contextResolver = contextResolver;
    }

    @GetMapping
    ApiResponse<PageResponse<AuditEventResponse>> search(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestParam(required = false) String actorId,
            @RequestParam(required = false) String subjectType,
            @RequestParam(required = false) String subjectId,
            @RequestParam(required = false) String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(auditQueryService.search(
                context,
                actorId,
                subjectType,
                subjectId,
                action,
                page,
                size
        ));
    }
}
