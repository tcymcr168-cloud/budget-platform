package com.budgetplatform.security.service;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.security.context.AuthProperties;
import com.budgetplatform.security.context.CurrentUserContext;
import com.budgetplatform.security.domain.SecurityRoleCode;
import com.budgetplatform.security.repository.AppUserEntityScopeRepository;
import com.budgetplatform.security.repository.AppUserRepository;
import com.budgetplatform.security.repository.AppUserRoleRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorizationServiceTests {

    @Test
    void rolesForWorkspaceIgnoresRequestRolesWhenHeaderRolesAreDisabled() {
        AppUserRepository userRepository = mock(AppUserRepository.class);
        when(userRepository.findByUsername("admin@example.com")).thenReturn(java.util.Optional.empty());
        AuthorizationService service = service(userRepository, false, List.of());
        CurrentUserContext context = new CurrentUserContext(
                "admin@example.com",
                Set.of(SecurityRoleCode.BUDGET_ADMIN)
        );

        Set<SecurityRoleCode> roles = service.rolesForWorkspace(context, UUID.randomUUID());

        assertThat(roles).isEmpty();
    }

    @Test
    void requireHeaderAdminAllowsBootstrapAdminWhenHeaderRolesAreDisabled() {
        AuthorizationService service = service(mock(AppUserRepository.class), false, List.of("admin@example.com"));
        CurrentUserContext context = new CurrentUserContext("ADMIN@example.com", Set.of());

        assertThatCode(() -> service.requireHeaderAdmin(context)).doesNotThrowAnyException();
    }

    @Test
    void requireHeaderAdminRejectsRequestRoleWhenHeaderRolesAreDisabled() {
        AuthorizationService service = service(mock(AppUserRepository.class), false, List.of());
        CurrentUserContext context = new CurrentUserContext(
                "admin@example.com",
                Set.of(SecurityRoleCode.BUDGET_ADMIN)
        );

        assertThatThrownBy(() -> service.requireHeaderAdmin(context))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("BUDGET_ADMIN bootstrap or trusted request role is required");
    }

    @Test
    void requireHeaderAdminAllowsRequestRoleOnlyWhenExplicitlyEnabled() {
        AuthorizationService service = service(mock(AppUserRepository.class), true, List.of());
        CurrentUserContext context = new CurrentUserContext(
                "admin@example.com",
                Set.of(SecurityRoleCode.BUDGET_ADMIN)
        );

        assertThatCode(() -> service.requireHeaderAdmin(context)).doesNotThrowAnyException();
    }

    private AuthorizationService service(
            AppUserRepository userRepository,
            boolean allowHeaderRoles,
            List<String> bootstrapAdminUsers
    ) {
        AuthProperties properties = new AuthProperties();
        properties.setAllowHeaderRoles(allowHeaderRoles);
        properties.setBootstrapAdminUsers(bootstrapAdminUsers);
        return new AuthorizationService(
                userRepository,
                mock(AppUserRoleRepository.class),
                mock(AppUserEntityScopeRepository.class),
                properties
        );
    }
}
