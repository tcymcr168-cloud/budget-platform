# AUTH-008 JWT Configuration Baseline

## Stage Goal

AUTH-008 adds the minimum backend dependency and configuration baseline needed for a later JWT/OIDC bearer adapter. It does not implement token validation, does not call an external IdP or JWKS endpoint, does not store tokens, does not add secrets, and does not change frontend behavior.

## Implemented Scope

| Area | Result |
| --- | --- |
| Dependency | Added `spring-security-oauth2-jose` under the Spring Boot managed dependency set |
| Configuration class | Added nested `budget-platform.auth.jwt` properties to `AuthProperties` |
| Runtime config | Added environment-backed JWT settings in `application.yml` |
| Tests | Added binding/default tests for JWT auth properties |
| JWT mode behavior | Still fails closed; full bearer validation remains future work |

The dependency is intentionally limited to JOSE/JWT support. This stage does not add the full OAuth2 resource-server starter, so Spring Security resource-server filters are not automatically activated before the project has an explicit adapter and tests.

## Configuration Contract

| Property | Environment variable | Default | Purpose |
| --- | --- | --- | --- |
| `budget-platform.auth.jwt.issuer` | `BUDGET_PLATFORM_AUTH_JWT_ISSUER` | empty | Expected issuer for future validation |
| `budget-platform.auth.jwt.audience` | `BUDGET_PLATFORM_AUTH_JWT_AUDIENCE` | empty | Expected API audience |
| `budget-platform.auth.jwt.jwks-uri` | `BUDGET_PLATFORM_AUTH_JWT_JWKS_URI` | empty | JWKS endpoint for future public key lookup |
| `budget-platform.auth.jwt.username-claim` | `BUDGET_PLATFORM_AUTH_JWT_USERNAME_CLAIM` | `sub` | Claim mapped to application username |
| `budget-platform.auth.jwt.clock-skew-seconds` | `BUDGET_PLATFORM_AUTH_JWT_CLOCK_SKEW_SECONDS` | `60` | Future clock-skew tolerance |
| `budget-platform.auth.jwt.max-token-length` | `BUDGET_PLATFORM_AUTH_JWT_MAX_TOKEN_LENGTH` | `8192` | Future oversized token guard |
| `budget-platform.auth.jwt.allowed-algorithms` | `BUDGET_PLATFORM_AUTH_JWT_ALLOWED_ALGORITHMS` | `RS256` | Future algorithm allow-list |

Do not commit issuer secrets, private keys, client secrets, raw tokens, `.env` files, or environment-specific credentials.

## Runtime Behavior

`budget-platform.auth.mode=JWT` remains a fail-closed mode in this stage. The existing `CurrentUserContextResolver` path still rejects JWT mode because the JWT adapter has not been implemented yet. This prevents a partially configured deployment from accepting bearer tokens without explicit validation.

## Validation

| Command | Result |
| --- | --- |
| `mvn test` | Passed after one compile fix; 59 tests passed, 0 failures, 0 errors, 0 skipped |

Initial test compilation failed because `BindResult.orElseThrow()` requires a supplier in the Spring Boot version used by the project. The test was fixed to pass `() -> new AssertionError("Auth properties should bind.")`, then the full Maven test suite passed.

## Non-Goals

1. Do not validate JWT signature, issuer, audience, expiry, or claims yet.
2. Do not call JWKS or OIDC discovery endpoints.
3. Do not add browser token handling.
4. Do not store access tokens, refresh tokens, or JWT payloads.
5. Do not use IdP groups or scopes as budget authorization.
6. Do not add ERP integration, BI charts, consolidation reports, or migration changes.

## Next Stage

The next recommended stage is `AUTH-009`: implement the backend JWT adapter behind `CurrentUserContextResolver` with tests for success, missing bearer, unsupported scheme, oversized token, malformed token, invalid issuer, invalid audience, expiry, missing username claim, unknown user, and inactive user behavior.

## Close Recommendation

Close AUTH-008 when the dependency, configuration properties, binding tests, README update, stage record, Maven tests, repository checks, and PDF/OCR protections are complete, with no token validation, frontend token storage, secrets, external service calls, migrations, PDF/OCR source, ERP, BI, or consolidation scope introduced.
