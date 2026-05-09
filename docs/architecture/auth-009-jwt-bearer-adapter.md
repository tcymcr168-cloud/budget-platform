# AUTH-009 JWT Bearer Adapter

## Stage Goal

AUTH-009 implements the backend JWT bearer authentication adapter behind the existing `CurrentUserContextResolver` contract. The adapter validates a bearer token, maps one configured claim to the application username, and leaves all budget authorization decisions in the application-owned role and Entity scope tables.

This stage does not add frontend token storage, login UI, refresh-token handling, external IdP discovery, migrations, ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Implemented Behavior

| Case | Result |
| --- | --- |
| `budget-platform.auth.mode=JWT` with valid bearer token and registered active user | Returns `CurrentUserContext` with normalized username, empty roles, and `authMode=JWT` |
| Missing `Authorization` header | `UNAUTHORIZED`, audit reason `MISSING_BEARER_TOKEN` |
| Non-bearer scheme | `UNAUTHORIZED`, audit reason `UNSUPPORTED_AUTHORIZATION_SCHEME` |
| Blank bearer token | `UNAUTHORIZED`, audit reason `MISSING_BEARER_TOKEN` |
| Token longer than `max-token-length` | `UNAUTHORIZED`, audit reason `TOKEN_TOO_LARGE`; decoder is not called |
| Missing JWT configuration | `UNAUTHORIZED`, audit reason `JWT_CONFIGURATION_MISSING` |
| Invalid JWT configuration | `UNAUTHORIZED`, audit reason `JWT_CONFIGURATION_INVALID` |
| JWT validation failure | `UNAUTHORIZED`, stable audit reason inferred as expiry, issuer, audience, signature, malformed, or generic invalid token |
| Missing configured username claim | `UNAUTHORIZED`, audit reason `MISSING_USERNAME_CLAIM` |
| Unknown application user | `FORBIDDEN`, audit reason `UNKNOWN_APPLICATION_USER`; no auto-provision |
| Inactive application user | `FORBIDDEN`, audit reason `INACTIVE_APPLICATION_USER` |

## Validation Boundary

The production decoder is created lazily from:

1. `budget-platform.auth.jwt.issuer`
2. `budget-platform.auth.jwt.audience`
3. `budget-platform.auth.jwt.jwks-uri`
4. `budget-platform.auth.jwt.clock-skew-seconds`
5. `budget-platform.auth.jwt.allowed-algorithms`

The decoder uses `NimbusJwtDecoder.withJwkSetUri(...)` with an explicit allowed JWS algorithm set and validators for timestamp, issuer, and audience. JWKS is not contacted until a token must be decoded.

## Principal Mapping

The adapter maps only the configured username claim, defaulting to `sub`.

Rules:

1. Trim and normalize the claim through `AppUser.normalizeUsername`.
2. Do not use IdP group, role, or scope claims for budget authorization.
3. Require a registered active `app_user` when the application repository is available.
4. Return no request roles from JWT; roles remain loaded by `AuthorizationService` from `app_user_role`.

## Audit And Redaction

JWT authentication failures continue to use:

| Field | Rule |
| --- | --- |
| `subjectType` | `authentication` |
| `subjectId` | `failure` |
| `action` | `AUTH_FAILURE` |
| `actorId` | `null` |
| `details.reason` | Stable failure category |
| `details.authMode` | `JWT` |
| `details.headerName` | `Authorization` when useful |
| request details | method, path, request id when available |

Raw bearer tokens, JWT headers, JWT payloads, refresh tokens, cookies, private keys, client secrets, and IdP claim dumps are not written to audit details.

## Validation

| Command | Result |
| --- | --- |
| `mvn test` | Passed; 68 tests passed, 0 failures, 0 errors, 0 skipped |

New unit coverage includes valid JWT resolution, missing bearer, unsupported scheme, oversized token, missing username claim, expired JWT validation failure, malformed JWT, unknown registered user, inactive registered user, and missing JWT configuration.

## Operational Notes

1. Production JWT mode should be enabled only when issuer, audience, JWKS URI, username claim, algorithm allow-list, and user provisioning are ready.
2. Unknown users are not auto-created; user lifecycle stays in the security management module.
3. Inactive users are blocked at the authentication boundary for JWT, before role or Entity scope evaluation.
4. `REVERSE_PROXY` remains the lower-integration production route if IdP/JWKS operation is not ready.

## Non-Goals

1. No frontend token acquisition or token storage.
2. No refresh token flow.
3. No use of IdP groups/scopes as budget authorization.
4. No IdP-specific SDK.
5. No migrations.
6. No ERP integration, BI charts, or consolidation reports.

## Next Stage

The next recommended stage is `AUTH-010`: decide whether direct browser bearer flow is required. If required, design the frontend boundary without using `localStorage`/`sessionStorage` for tokens. If not required, document that JWT is intended for gateway/API clients while browser production traffic continues through the reverse proxy trusted principal route.

## Close Recommendation

Close AUTH-009 when JWT bearer adapter behavior, tests, architecture document, README update, stage record, Maven tests, repository checks, and PDF/OCR protections are complete, with no frontend token storage, secrets, migration, external service setup, PDF/OCR source, ERP, BI, or consolidation scope introduced.
