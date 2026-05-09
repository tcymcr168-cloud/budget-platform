package com.budgetplatform.security.context;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.security.domain.SecurityRoleCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrentUserContextResolverTests {

    @AfterEach
    void clearRequestContext() {
        RequestContextHolder.resetRequestAttributes();
    }

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

    @Test
    void resolvesReverseProxyPrincipalFromConfiguredHeader() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.REVERSE_PROXY);
        properties.setReverseProxyUserHeader("X-Trusted-User");
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(properties);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Trusted-User", " proxy.user@example.com ");
        request.addHeader("X-User-Roles", "BUDGET_ADMIN");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        CurrentUserContext context = resolver.resolve("client-supplied@example.com", "BUDGET_ADMIN");

        assertThat(context.userId()).isEqualTo("proxy.user@example.com");
        assertThat(context.roles()).isEmpty();
    }

    @Test
    void rejectsReverseProxyModeWhenPrincipalHeaderIsMissing() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.REVERSE_PROXY);
        properties.setReverseProxyUserHeader("X-Trusted-User");
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(properties);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        assertThatThrownBy(() -> resolver.resolve("client-supplied@example.com", null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("Trusted reverse proxy principal header is missing");
    }

    @Test
    void rejectsReverseProxyModeWhenPrincipalHeaderIsBlank() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.REVERSE_PROXY);
        properties.setReverseProxyUserHeader("X-Trusted-User");
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(properties);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Trusted-User", " ");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("Trusted reverse proxy principal header is missing");
    }

    @Test
    void rejectsReverseProxyModeWhenHeaderNameIsBlank() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.REVERSE_PROXY);
        properties.setReverseProxyUserHeader(" ");
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(properties);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("Reverse proxy user header is not configured");
    }
}
