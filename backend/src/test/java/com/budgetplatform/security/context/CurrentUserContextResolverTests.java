package com.budgetplatform.security.context;

import com.budgetplatform.common.api.ApplicationException;
import com.budgetplatform.common.audit.AuditEvent;
import com.budgetplatform.common.audit.AuditService;
import com.budgetplatform.security.domain.SecurityRoleCode;
import com.budgetplatform.security.domain.AppUser;
import com.budgetplatform.security.repository.AppUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
        assertThat(context.authMode()).isEqualTo(AuthMode.DEV_HEADER);
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
        assertThat(context.authMode()).isEqualTo(AuthMode.DEV_HEADER);
        assertThat(context.roles()).isEmpty();
    }

    @Test
    void resolvesJwtBearerForRegisteredActiveUser() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        AppUserRepository userRepository = mock(AppUserRepository.class);
        when(userRepository.findByUsername("admin@example.com"))
                .thenReturn(Optional.of(new AppUser("admin@example.com", "Admin", "admin@example.com")));
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(
                properties,
                null,
                token -> jwtWithSubject(" ADMIN@EXAMPLE.COM "),
                userRepository
        );
        requestWithAuthorization("Bearer valid-token");

        CurrentUserContext context = resolver.resolve("client-supplied@example.com", "BUDGET_ADMIN");

        assertThat(context.userId()).isEqualTo("admin@example.com");
        assertThat(context.authMode()).isEqualTo(AuthMode.JWT);
        assertThat(context.roles()).isEmpty();
    }

    @Test
    void rejectsJwtModeWhenBearerHeaderIsMissing() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        RecordingAuditService auditService = new RecordingAuditService();
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(
                properties,
                auditService,
                token -> jwtWithSubject("admin@example.com"),
                null
        );
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("Bearer token is missing");
        assertThat(auditService.lastReason()).isEqualTo("MISSING_BEARER_TOKEN");
    }

    @Test
    void rejectsJwtModeWhenAuthorizationSchemeIsUnsupported() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        RecordingAuditService auditService = new RecordingAuditService();
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(
                properties,
                auditService,
                token -> jwtWithSubject("admin@example.com"),
                null
        );
        requestWithAuthorization("Basic abc");

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("Bearer scheme");
        assertThat(auditService.lastReason()).isEqualTo("UNSUPPORTED_AUTHORIZATION_SCHEME");
    }

    @Test
    void rejectsOversizedJwtBeforeDecoding() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        properties.getJwt().setMaxTokenLength(4);
        RecordingAuditService auditService = new RecordingAuditService();
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(
                properties,
                auditService,
                token -> {
                    throw new AssertionError("Decoder should not be called for oversized tokens.");
                },
                null
        );
        requestWithAuthorization("Bearer too-large");

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("too large");
        assertThat(auditService.lastReason()).isEqualTo("TOKEN_TOO_LARGE");
    }

    @Test
    void rejectsJwtWhenUsernameClaimIsMissing() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        RecordingAuditService auditService = new RecordingAuditService();
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(
                properties,
                auditService,
                token -> Jwt.withTokenValue("valid-token")
                        .header("alg", "RS256")
                        .issuedAt(Instant.now())
                        .expiresAt(Instant.now().plusSeconds(60))
                        .build(),
                null
        );
        requestWithAuthorization("Bearer valid-token");

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("username claim is missing");
        assertThat(auditService.lastReason()).isEqualTo("MISSING_USERNAME_CLAIM");
    }

    @Test
    void recordsStableReasonForExpiredJwtValidationFailure() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        RecordingAuditService auditService = new RecordingAuditService();
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(
                properties,
                auditService,
                token -> {
                    throw new JwtValidationException(
                            "Jwt expired",
                            List.of(new OAuth2Error("invalid_token", "Jwt expired at 2026-05-09T00:00:00Z", null))
                    );
                },
                null
        );
        requestWithAuthorization("Bearer expired-token");

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("validation failed");
        assertThat(auditService.lastReason()).isEqualTo("TOKEN_EXPIRED");
        assertThat(auditService.lastDetails().values()).doesNotContain("expired-token");
    }

    @Test
    void recordsStableReasonForMalformedJwt() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        RecordingAuditService auditService = new RecordingAuditService();
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(
                properties,
                auditService,
                token -> {
                    throw new BadJwtException("Malformed JWT serialization");
                },
                null
        );
        requestWithAuthorization("Bearer malformed-token");

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("malformed");
        assertThat(auditService.lastReason()).isEqualTo("MALFORMED_TOKEN");
        assertThat(auditService.lastDetails().values()).doesNotContain("malformed-token");
    }

    @Test
    void rejectsJwtForUnknownApplicationUser() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        RecordingAuditService auditService = new RecordingAuditService();
        AppUserRepository userRepository = mock(AppUserRepository.class);
        when(userRepository.findByUsername("missing@example.com")).thenReturn(Optional.empty());
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(
                properties,
                auditService,
                token -> jwtWithSubject("missing@example.com"),
                userRepository
        );
        requestWithAuthorization("Bearer valid-token");

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOfSatisfying(ApplicationException.class, exception -> {
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
                    assertThat(exception).hasMessageContaining("not registered");
                });
        assertThat(auditService.lastReason()).isEqualTo("UNKNOWN_APPLICATION_USER");
    }

    @Test
    void rejectsJwtForInactiveApplicationUser() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        RecordingAuditService auditService = new RecordingAuditService();
        AppUser inactiveUser = new AppUser("inactive@example.com", "Inactive", "inactive@example.com");
        inactiveUser.disable();
        AppUserRepository userRepository = mock(AppUserRepository.class);
        when(userRepository.findByUsername("inactive@example.com")).thenReturn(Optional.of(inactiveUser));
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(
                properties,
                auditService,
                token -> jwtWithSubject("inactive@example.com"),
                userRepository
        );
        requestWithAuthorization("Bearer valid-token");

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOfSatisfying(ApplicationException.class, exception -> {
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
                    assertThat(exception).hasMessageContaining("inactive");
                });
        assertThat(auditService.lastReason()).isEqualTo("INACTIVE_APPLICATION_USER");
    }

    @Test
    void rejectsJwtModeWhenRequiredConfigurationIsMissing() {
        AuthProperties properties = new AuthProperties();
        properties.setMode(AuthMode.JWT);
        RecordingAuditService auditService = new RecordingAuditService();
        AppUserRepository userRepository = mock(AppUserRepository.class);
        CurrentUserContextResolver resolver = new CurrentUserContextResolver(
                properties,
                auditService,
                null,
                userRepository
        );
        requestWithAuthorization("Bearer valid-token");

        assertThatThrownBy(() -> resolver.resolve(null, null))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("missing required configuration");
        assertThat(auditService.lastReason()).isEqualTo("JWT_CONFIGURATION_MISSING");
        verifyNoInteractions(userRepository);
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
        assertThat(context.authMode()).isEqualTo(AuthMode.REVERSE_PROXY);
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

    private void requestWithAuthorization(String authorization) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, authorization);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    private Jwt jwtWithSubject(String subject) {
        return Jwt.withTokenValue("valid-token")
                .header("alg", "RS256")
                .subject(subject)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60))
                .build();
    }

    private static class RecordingAuditService implements AuditService {

        private final List<AuditEvent> events = new ArrayList<>();

        @Override
        public void record(AuditEvent event) {
            events.add(event);
        }

        String lastReason() {
            return (String) lastDetails().get("reason");
        }

        java.util.Map<String, Object> lastDetails() {
            assertThat(events).isNotEmpty();
            return events.get(events.size() - 1).details();
        }
    }
}
