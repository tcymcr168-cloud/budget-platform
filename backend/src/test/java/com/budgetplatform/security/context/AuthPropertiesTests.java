package com.budgetplatform.security.context;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AuthPropertiesTests {

    @Test
    void jwtPropertiesHaveSafeDefaults() {
        AuthProperties properties = new AuthProperties();

        assertThat(properties.getJwt().getUsernameClaim()).isEqualTo("sub");
        assertThat(properties.getJwt().getClockSkewSeconds()).isEqualTo(60);
        assertThat(properties.getJwt().getMaxTokenLength()).isEqualTo(8192);
        assertThat(properties.getJwt().getAllowedAlgorithms()).containsExactly("RS256");
    }

    @Test
    void bindsJwtProperties() {
        MapConfigurationPropertySource source = new MapConfigurationPropertySource(Map.of(
                "budget-platform.auth.mode", "JWT",
                "budget-platform.auth.jwt.issuer", "https://issuer.example.test",
                "budget-platform.auth.jwt.audience", "budget-platform-api",
                "budget-platform.auth.jwt.jwks-uri", "https://issuer.example.test/.well-known/jwks.json",
                "budget-platform.auth.jwt.username-claim", "preferred_username",
                "budget-platform.auth.jwt.clock-skew-seconds", "120",
                "budget-platform.auth.jwt.max-token-length", "4096",
                "budget-platform.auth.jwt.allowed-algorithms", "RS256,ES256"
        ));

        AuthProperties properties = new Binder(source)
                .bind("budget-platform.auth", AuthProperties.class)
                .orElseThrow(() -> new AssertionError("Auth properties should bind."));

        assertThat(properties.getMode()).isEqualTo(AuthMode.JWT);
        assertThat(properties.getJwt().getIssuer()).isEqualTo("https://issuer.example.test");
        assertThat(properties.getJwt().getAudience()).isEqualTo("budget-platform-api");
        assertThat(properties.getJwt().getJwksUri())
                .isEqualTo("https://issuer.example.test/.well-known/jwks.json");
        assertThat(properties.getJwt().getUsernameClaim()).isEqualTo("preferred_username");
        assertThat(properties.getJwt().getClockSkewSeconds()).isEqualTo(120);
        assertThat(properties.getJwt().getMaxTokenLength()).isEqualTo(4096);
        assertThat(properties.getJwt().getAllowedAlgorithms()).isEqualTo(List.of("RS256", "ES256"));
    }
}
