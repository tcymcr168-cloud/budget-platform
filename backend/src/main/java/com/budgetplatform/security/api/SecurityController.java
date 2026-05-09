package com.budgetplatform.security.api;

import com.budgetplatform.common.api.ApiResponse;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.context.CurrentUserContextResolver;
import com.budgetplatform.security.service.AuthorizationService;
import com.budgetplatform.security.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final SecurityService securityService;
    private final CurrentUserContextResolver contextResolver;
    private final AuthorizationService authorizationService;

    public SecurityController(
            SecurityService securityService,
            CurrentUserContextResolver contextResolver,
            AuthorizationService authorizationService
    ) {
        this.securityService = securityService;
        this.contextResolver = contextResolver;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/users")
    ApiResponse<SecurityUserResponse> createUser(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @Valid @RequestBody CreateSecurityUserRequest request
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        authorizationService.requireHeaderAdmin(context);
        return ApiResponse.success(securityService.createUser(context, request));
    }

    @GetMapping("/users")
    ApiResponse<List<SecurityUserResponse>> listUsers(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        authorizationService.requireHeaderAdmin(contextResolver.resolve(userId, roles));
        return ApiResponse.success(securityService.listUsers());
    }

    @PostMapping("/users/{userId}/disable")
    ApiResponse<SecurityUserResponse> disableUser(
            @PathVariable UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @Valid @RequestBody SecurityUserStatusChangeRequest request
    ) {
        CurrentUserContext context = contextResolver.resolve(actorId, roles);
        authorizationService.requireHeaderAdmin(context);
        return ApiResponse.success(securityService.disableUser(context, userId, request));
    }

    @PostMapping("/users/{userId}/enable")
    ApiResponse<SecurityUserResponse> enableUser(
            @PathVariable UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @Valid @RequestBody SecurityUserStatusChangeRequest request
    ) {
        CurrentUserContext context = contextResolver.resolve(actorId, roles);
        authorizationService.requireHeaderAdmin(context);
        return ApiResponse.success(securityService.enableUser(context, userId, request));
    }

    @PostMapping("/users/{userId}/roles")
    ApiResponse<UserRoleResponse> grantRole(
            @PathVariable UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @Valid @RequestBody GrantUserRoleRequest request
    ) {
        CurrentUserContext context = contextResolver.resolve(actorId, roles);
        authorizationService.requireAdmin(context, request.workspaceId());
        return ApiResponse.success(securityService.grantRole(context, userId, request));
    }

    @GetMapping("/users/{userId}/roles")
    ApiResponse<List<UserRoleResponse>> listRoles(
            @PathVariable UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestParam(required = false) UUID workspaceId
    ) {
        CurrentUserContext context = contextResolver.resolve(actorId, roles);
        if (workspaceId == null) {
            authorizationService.requireHeaderAdmin(context);
        } else {
            authorizationService.requireAdmin(context, workspaceId);
        }
        return ApiResponse.success(securityService.listRoles(userId, workspaceId));
    }

    @PostMapping("/users/{userId}/entity-scopes")
    ApiResponse<EntityScopeResponse> grantEntityScope(
            @PathVariable UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @Valid @RequestBody GrantEntityScopeRequest request
    ) {
        CurrentUserContext context = contextResolver.resolve(actorId, roles);
        authorizationService.requireAdmin(context, request.workspaceId());
        return ApiResponse.success(securityService.grantEntityScope(context, userId, request));
    }

    @GetMapping("/users/{userId}/entity-scopes")
    ApiResponse<List<EntityScopeResponse>> listEntityScopes(
            @PathVariable UUID userId,
            @RequestHeader(value = "X-User-Id", required = false) String actorId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles,
            @RequestParam(required = false) UUID workspaceId
    ) {
        CurrentUserContext context = contextResolver.resolve(actorId, roles);
        if (workspaceId == null) {
            authorizationService.requireHeaderAdmin(context);
        } else {
            authorizationService.requireAdmin(context, workspaceId);
        }
        return ApiResponse.success(securityService.listEntityScopes(userId, workspaceId));
    }

    @GetMapping("/me")
    ApiResponse<CurrentUserResponse> currentUser(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Roles", required = false) String roles
    ) {
        CurrentUserContext context = contextResolver.resolve(userId, roles);
        return ApiResponse.success(securityService.currentUser(context));
    }
}
