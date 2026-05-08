package com.budgetplatform.security.api;

import com.budgetplatform.common.api.ApiResponse;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.context.CurrentUserContextResolver;
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

    public SecurityController(SecurityService securityService, CurrentUserContextResolver contextResolver) {
        this.securityService = securityService;
        this.contextResolver = contextResolver;
    }

    @PostMapping("/users")
    ApiResponse<SecurityUserResponse> createUser(@Valid @RequestBody CreateSecurityUserRequest request) {
        return ApiResponse.success(securityService.createUser(request));
    }

    @GetMapping("/users")
    ApiResponse<List<SecurityUserResponse>> listUsers() {
        return ApiResponse.success(securityService.listUsers());
    }

    @PostMapping("/users/{userId}/roles")
    ApiResponse<UserRoleResponse> grantRole(
            @PathVariable UUID userId,
            @Valid @RequestBody GrantUserRoleRequest request
    ) {
        return ApiResponse.success(securityService.grantRole(userId, request));
    }

    @GetMapping("/users/{userId}/roles")
    ApiResponse<List<UserRoleResponse>> listRoles(
            @PathVariable UUID userId,
            @RequestParam(required = false) UUID workspaceId
    ) {
        return ApiResponse.success(securityService.listRoles(userId, workspaceId));
    }

    @PostMapping("/users/{userId}/entity-scopes")
    ApiResponse<EntityScopeResponse> grantEntityScope(
            @PathVariable UUID userId,
            @Valid @RequestBody GrantEntityScopeRequest request
    ) {
        return ApiResponse.success(securityService.grantEntityScope(userId, request));
    }

    @GetMapping("/users/{userId}/entity-scopes")
    ApiResponse<List<EntityScopeResponse>> listEntityScopes(
            @PathVariable UUID userId,
            @RequestParam(required = false) UUID workspaceId
    ) {
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
