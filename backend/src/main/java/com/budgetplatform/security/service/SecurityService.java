package com.budgetplatform.security.service;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.api.ErrorCode;
import com.budgetplatform.common.audit.AuditAction;
import com.budgetplatform.common.audit.AuditEvent;
import com.budgetplatform.common.audit.AuditService;
import com.budgetplatform.metadata.domain.BudgetWorkspace;
import com.budgetplatform.metadata.domain.DimensionMember;
import com.budgetplatform.metadata.domain.DimensionType;
import com.budgetplatform.metadata.repository.BudgetWorkspaceRepository;
import com.budgetplatform.metadata.repository.DimensionMemberRepository;
import com.budgetplatform.security.api.CreateSecurityUserRequest;
import com.budgetplatform.security.api.CurrentUserResponse;
import com.budgetplatform.security.api.EntityScopeResponse;
import com.budgetplatform.security.api.GrantEntityScopeRequest;
import com.budgetplatform.security.api.GrantUserRoleRequest;
import com.budgetplatform.security.api.SecurityUserResponse;
import com.budgetplatform.security.api.UserRoleResponse;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.domain.AppUser;
import com.budgetplatform.security.domain.AppUserEntityScope;
import com.budgetplatform.security.domain.AppUserRole;
import com.budgetplatform.security.repository.AppUserEntityScopeRepository;
import com.budgetplatform.security.repository.AppUserRepository;
import com.budgetplatform.security.repository.AppUserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SecurityService {

    private final AppUserRepository userRepository;
    private final AppUserRoleRepository roleRepository;
    private final AppUserEntityScopeRepository scopeRepository;
    private final BudgetWorkspaceRepository workspaceRepository;
    private final DimensionMemberRepository memberRepository;
    private final AuditService auditService;

    public SecurityService(
            AppUserRepository userRepository,
            AppUserRoleRepository roleRepository,
            AppUserEntityScopeRepository scopeRepository,
            BudgetWorkspaceRepository workspaceRepository,
            DimensionMemberRepository memberRepository,
            AuditService auditService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.scopeRepository = scopeRepository;
        this.workspaceRepository = workspaceRepository;
        this.memberRepository = memberRepository;
        this.auditService = auditService;
    }

    @Transactional
    public SecurityUserResponse createUser(CurrentUserContext context, CreateSecurityUserRequest request) {
        String username = AppUser.normalizeUsername(request.username());
        if (userRepository.existsByUsername(username)) {
            throw conflict("Security user already exists: " + username);
        }
        AppUser user = userRepository.save(new AppUser(username, request.displayName(), request.email()));
        record(context, "app_user", user.getId().toString(), AuditAction.CREATE, Map.of("username", user.getUsername()));
        return SecurityUserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public List<SecurityUserResponse> listUsers() {
        return userRepository.findAllByOrderByUsernameAsc()
                .stream()
                .map(SecurityUserResponse::from)
                .toList();
    }

    @Transactional
    public UserRoleResponse grantRole(CurrentUserContext context, UUID userId, GrantUserRoleRequest request) {
        AppUser user = loadUser(userId);
        BudgetWorkspace workspace = loadWorkspace(request.workspaceId());
        if (roleRepository.existsByUser_IdAndWorkspace_IdAndRoleCode(userId, workspace.getId(), request.roleCode())) {
            throw conflict("Role already granted to user in workspace.");
        }
        AppUserRole role = roleRepository.save(new AppUserRole(user, workspace, request.roleCode()));
        record(context, "app_user_role", role.getId().toString(), AuditAction.ACCESS_CHANGE, Map.of(
                "username", user.getUsername(),
                "workspaceId", workspace.getId().toString(),
                "roleCode", role.getRoleCode().name()
        ));
        return UserRoleResponse.from(role);
    }

    @Transactional(readOnly = true)
    public List<UserRoleResponse> listRoles(UUID userId, UUID workspaceId) {
        loadUser(userId);
        if (workspaceId != null) {
            loadWorkspace(workspaceId);
            return roleRepository.findByUser_IdAndWorkspace_IdOrderByRoleCodeAsc(userId, workspaceId)
                    .stream()
                    .map(UserRoleResponse::from)
                    .toList();
        }
        return roleRepository.findByUser_IdOrderByWorkspace_CodeAscRoleCodeAsc(userId)
                .stream()
                .map(UserRoleResponse::from)
                .toList();
    }

    @Transactional
    public EntityScopeResponse grantEntityScope(CurrentUserContext context, UUID userId, GrantEntityScopeRequest request) {
        AppUser user = loadUser(userId);
        BudgetWorkspace workspace = loadWorkspace(request.workspaceId());
        DimensionMember entityMember = loadEntityMemberInWorkspace(request.entityMemberId(), workspace.getId());
        if (scopeRepository.existsByUser_IdAndWorkspace_IdAndEntityMember_Id(userId, workspace.getId(), entityMember.getId())) {
            throw conflict("Entity scope already granted to user in workspace.");
        }
        AppUserEntityScope scope = scopeRepository.save(new AppUserEntityScope(
                user,
                workspace,
                entityMember,
                request.includeDescendants()
        ));
        record(context, "app_user_entity_scope", scope.getId().toString(), AuditAction.ACCESS_CHANGE, Map.of(
                "username", user.getUsername(),
                "workspaceId", workspace.getId().toString(),
                "entityMemberId", entityMember.getId().toString(),
                "includeDescendants", scope.isIncludeDescendants()
        ));
        return EntityScopeResponse.from(scope);
    }

    @Transactional(readOnly = true)
    public List<EntityScopeResponse> listEntityScopes(UUID userId, UUID workspaceId) {
        loadUser(userId);
        if (workspaceId != null) {
            loadWorkspace(workspaceId);
            return scopeRepository.findByUser_IdAndWorkspace_IdOrderByEntityMember_CodeAsc(userId, workspaceId)
                    .stream()
                    .map(EntityScopeResponse::from)
                    .toList();
        }
        return scopeRepository.findByUser_IdOrderByWorkspace_CodeAscEntityMember_CodeAsc(userId)
                .stream()
                .map(EntityScopeResponse::from)
                .toList();
    }

    public CurrentUserResponse currentUser(CurrentUserContext context) {
        return new CurrentUserResponse(context.userId(), context.roles(), context.authenticated());
    }

    private void record(
            CurrentUserContext context,
            String subjectType,
            String subjectId,
            AuditAction action,
            Map<String, Object> details
    ) {
        auditService.record(new AuditEvent(
                context == null ? null : context.userId(),
                subjectType,
                subjectId,
                action,
                null,
                details
        ));
    }

    private AppUser loadUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> notFound("Security user was not found: " + userId));
    }

    private BudgetWorkspace loadWorkspace(UUID workspaceId) {
        return workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> notFound("Workspace was not found: " + workspaceId));
    }

    private DimensionMember loadEntityMemberInWorkspace(UUID memberId, UUID workspaceId) {
        DimensionMember member = memberRepository.findById(memberId)
                .orElseThrow(() -> notFound("Entity member was not found: " + memberId));
        if (member.getDimension().getDimensionType() != DimensionType.ENTITY) {
            throw badRequest("Entity scope member must belong to an ENTITY dimension.");
        }
        if (!member.getDimension().getWorkspace().getId().equals(workspaceId)) {
            throw badRequest("Entity scope member must belong to the same workspace.");
        }
        return member;
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
}
