package com.budgetplatform.security.service;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.security.context.AuthProperties;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.domain.AppUser;
import com.budgetplatform.security.domain.SecurityRoleCode;
import com.budgetplatform.security.repository.AppUserEntityScopeRepository;
import com.budgetplatform.security.repository.AppUserRepository;
import com.budgetplatform.security.repository.AppUserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AuthorizationService {

    private final AppUserRepository userRepository;
    private final AppUserRoleRepository roleRepository;
    private final AppUserEntityScopeRepository scopeRepository;
    private final AuthProperties authProperties;

    public AuthorizationService(
            AppUserRepository userRepository,
            AppUserRoleRepository roleRepository,
            AppUserEntityScopeRepository scopeRepository,
            AuthProperties authProperties
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.scopeRepository = scopeRepository;
        this.authProperties = authProperties;
    }

    @Transactional(readOnly = true)
    public void requireHeaderAdmin(CurrentUserContext context) {
        requireAuthenticated(context);
        boolean headerAdmin = authProperties.isAllowHeaderRoles()
                && context.roles().contains(SecurityRoleCode.BUDGET_ADMIN);
        if (!headerAdmin && !isBootstrapAdmin(context)) {
            throw forbidden("BUDGET_ADMIN bootstrap or trusted request role is required.");
        }
    }

    @Transactional(readOnly = true)
    public void requireAdmin(CurrentUserContext context, UUID workspaceId) {
        requireAuthenticated(context);
        Set<SecurityRoleCode> roles = rolesForWorkspace(context, workspaceId);
        if (!roles.contains(SecurityRoleCode.BUDGET_ADMIN)) {
            throw forbidden("BUDGET_ADMIN role is required.");
        }
    }

    @Transactional(readOnly = true)
    public void requireAnyRole(CurrentUserContext context, UUID workspaceId, SecurityRoleCode... allowedRoles) {
        requireAuthenticated(context);
        Set<SecurityRoleCode> roles = rolesForWorkspace(context, workspaceId);
        boolean allowed = Arrays.stream(allowedRoles).anyMatch(roles::contains);
        if (!allowed) {
            throw forbidden("Required role is missing.");
        }
    }

    @Transactional(readOnly = true)
    public boolean canReadEntity(CurrentUserContext context, UUID workspaceId, UUID entityMemberId) {
        Set<SecurityRoleCode> roles = rolesForWorkspace(context, workspaceId);
        if (roles.contains(SecurityRoleCode.BUDGET_ADMIN)) {
            return true;
        }
        AppUser user = loadUser(context);
        return scopeRepository.existsByUser_IdAndWorkspace_IdAndEntityMember_Id(
                user.getId(),
                workspaceId,
                entityMemberId
        );
    }

    @Transactional(readOnly = true)
    public Set<UUID> readableEntityMemberIds(CurrentUserContext context, UUID workspaceId) {
        Set<SecurityRoleCode> roles = rolesForWorkspace(context, workspaceId);
        if (roles.contains(SecurityRoleCode.BUDGET_ADMIN)) {
            return Set.of();
        }
        AppUser user = loadUser(context);
        return scopeRepository.findByUser_IdAndWorkspace_IdOrderByEntityMember_CodeAsc(user.getId(), workspaceId)
                .stream()
                .map(scope -> scope.getEntityMember().getId())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Transactional(readOnly = true)
    public Set<SecurityRoleCode> rolesForWorkspace(CurrentUserContext context, UUID workspaceId) {
        if (context == null || !context.authenticated()) {
            return Set.of();
        }
        Set<SecurityRoleCode> persistedRoles = userRepository.findByUsername(AppUser.normalizeUsername(context.userId()))
                .map(user -> roleRepository.findByUser_IdAndWorkspace_IdOrderByRoleCodeAsc(user.getId(), workspaceId)
                        .stream()
                        .map(role -> role.getRoleCode()))
                .orElseGet(Stream::empty)
                .collect(Collectors.toSet());
        if (authProperties.isAllowHeaderRoles()) {
            persistedRoles.addAll(context.roles());
        }
        return Set.copyOf(persistedRoles);
    }

    private boolean isBootstrapAdmin(CurrentUserContext context) {
        String userId = AppUser.normalizeUsername(context.userId());
        return authProperties.getBootstrapAdminUsers()
                .stream()
                .filter(value -> value != null && !value.isBlank())
                .map(AppUser::normalizeUsername)
                .anyMatch(userId::equals);
    }

    private void requireAuthenticated(CurrentUserContext context) {
        if (context == null || !context.authenticated()) {
            throw new ApplicationException(
                    ErrorCode.UNAUTHORIZED,
                    HttpStatus.UNAUTHORIZED,
                    "Authenticated user context is required."
            );
        }
    }

    private AppUser loadUser(CurrentUserContext context) {
        requireAuthenticated(context);
        return userRepository.findByUsername(AppUser.normalizeUsername(context.userId()))
                .orElseThrow(() -> forbidden("Security user is not registered: " + context.userId()));
    }

    private ApplicationException forbidden(String message) {
        return new ApplicationException(ErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN, message);
    }
}
