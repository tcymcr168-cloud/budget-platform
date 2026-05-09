package com.budgetplatform.security.context;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.security.domain.SecurityRoleCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrentUserContextResolverTests {

    @Test
    void resolvesDevHeaderUserAndRolesWhenAllowed() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.DEV_HEADER);
        properties.setAllowHeaderRoles(true);
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(properties);

        CurrentUserContext context = resolver.resolve(" admin@example.com ", "BUDGET_ADMIN,READ_ONLY");

        assertThat(context.userId()).isEqualTo("admin@example.com");
        assertThat(context.roles()).containsExactlyInAnyOrder(
                SecurityRoleCode.BUDGET_ADMIN,
                SecurityRoleCode.READ_ONLY
        );
    }

    @Test
    void ignoresDevHeaderRolesWhenDisabled() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.DEV_HEADER);
        properties.setAllowHeaderRoles(false);
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(properties);

        CurrentUserContext context = resolver.resolve("admin@example.com", "BUDGET_ADMIN");

        assertThat(context.userId()).isEqualTo("admin@example.com");
        assertThat(context.roles()).isEmpty();
    }

    @Test
    void rejectsJwtModeUntilAdapterIsImplemented() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(properties);

        assertThatThrownBy(() -> resolver.resolve("admin@example.com", "BUDGET_ADMIN"))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("JWT authentication adapter is not implemented");
    }
}
