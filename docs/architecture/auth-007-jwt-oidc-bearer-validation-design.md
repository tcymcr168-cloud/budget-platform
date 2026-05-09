# AUTH-007 JWT/OIDC Bearer Validation Design

## Stage Goal

AUTH-007 designs the future JWT/OIDC bearer validation route for `budget-platform.auth.mode=JWT`. It prepares the implementation boundary without adding dependencies, runtime code, secrets, external IdP calls, migrations, login UI, ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Current Baseline

| Area | Current state |
| --- | --- |
| Auth modes | `DEV_HEADER`, `REVERSE_PROXY`, and `JWT` exist |
| JWT runtime behavior | Fails closed with `UNAUTHORIZED` and `AUTH_FAILURE` |
| Production-ready route | `REVERSE_PROXY` trusted principal mode |
| Roles | Application-owned `app_user_role` grants |
| Entity scopes | Application-owned `app_user_entity_scope` grants |
| User lifecycle | Inactive registered users are rejected in authorization |
| Audit | Failed authentication events record stable categories without secrets |

## Design Decision

JWT/OIDC bearer validation should be implemented as a backend authentication adapter behind the existing `CurrentUserContextResolver` contract. It should resolve only the trusted principal identity and auth mode. Budget authorization must continue to come from application tables.

Recommended dependency direction for the implementation stage:

| Candidate | Decision |
| --- | --- |
| Spring Security OAuth2 Resource Server | Preferred for JWT signature, issuer, audience, expiry, and JWKS integration under the Spring Boot BOM |
| Hand-rolled token parser | Not recommended; easy to miss validation and rotation edge cases |
| IdP-specific SDK | Avoid unless a deployment target requires provider-specific behavior |

AUTH-007 does not add the dependency yet. The future implementation stage should add the minimum dependency set only after tests are defined.

## Configuration Contract

Future JWT mode should fail closed unless all required settings are valid.

| Property | Environment variable | Required | Purpose |
| --- | --- | --- | --- |
| `budget-platform.auth.jwt.issuer` | `BUDGET_PLATFORM_AUTH_JWT_ISSUER` | yes | Expected token issuer |
| `budget-platform.auth.jwt.audience` | `BUDGET_PLATFORM_AUTH_JWT_AUDIENCE` | yes | Expected API audience |
| `budget-platform.auth.jwt.jwks-uri` | `BUDGET_PLATFORM_AUTH_JWT_JWKS_URI` | yes unless static public key is explicitly designed | JWKS endpoint for public signing keys |
| `budget-platform.auth.jwt.username-claim` | `BUDGET_PLATFORM_AUTH_JWT_USERNAME_CLAIM` | no, default `sub` | Claim used to map to `app_user.username` |
| `budget-platform.auth.jwt.clock-skew-seconds` | `BUDGET_PLATFORM_AUTH_JWT_CLOCK_SKEW_SECONDS` | no, default `60` | Small clock skew tolerance |
| `budget-platform.auth.jwt.max-token-length` | `BUDGET_PLATFORM_AUTH_JWT_MAX_TOKEN_LENGTH` | no, default `8192` | Reject oversized bearer strings before validation |
| `budget-platform.auth.jwt.allowed-algorithms` | `BUDGET_PLATFORM_AUTH_JWT_ALLOWED_ALGORITHMS` | no, default implementation-safe list | Reject `none` and unexpected algorithms |

Do not commit issuer secrets, private keys, client secrets, raw tokens, `.env` files, or environment-specific credentials.

## Request Handling

| Case | Result |
| --- | --- |
| Missing `Authorization` header | `UNAUTHORIZED`, audit reason `MISSING_BEARER_TOKEN` |
| Non-bearer header | `UNAUTHORIZED`, audit reason `UNSUPPORTED_AUTHORIZATION_SCHEME` |
| Oversized bearer string | `UNAUTHORIZED`, audit reason `TOKEN_TOO_LARGE` |
| Malformed JWT | `UNAUTHORIZED`, audit reason `MALFORMED_TOKEN` |
| Invalid signature | `UNAUTHORIZED`, audit reason `INVALID_SIGNATURE` |
| Invalid issuer | `UNAUTHORIZED`, audit reason `INVALID_ISSUER` |
| Invalid audience | `UNAUTHORIZED`, audit reason `INVALID_AUDIENCE` |
| Expired token | `UNAUTHORIZED`, audit reason `TOKEN_EXPIRED` |
| Token not yet valid | `UNAUTHORIZED`, audit reason `TOKEN_NOT_YET_VALID` |
| Missing username claim | `UNAUTHORIZED`, audit reason `MISSING_USERNAME_CLAIM` |
| Unknown application user | `FORBIDDEN` or `UNAUTHORIZED` to be finalized in implementation; recommended `FORBIDDEN` after successful authentication |
| Inactive application user | Existing authorization layer returns `FORBIDDEN` |

## Principal Mapping

The JWT adapter should map one token claim to a normalized application username:

1. Default claim: `sub`.
2. Optional configured claim: for example `preferred_username` or `email`.
3. Normalize using `AppUser.normalizeUsername`.
4. Do not create application users automatically from token claims in the first implementation.
5. Do not use IdP groups, roles, or scopes as budget authorization.

## Audit And Redaction

Authentication failure audit should continue to use:

| Field | Rule |
| --- | --- |
| `subjectType` | `authentication` |
| `subjectId` | `failure` |
| `action` | `AUTH_FAILURE` |
| `actorId` | `null` until principal is trusted |
| `details.reason` | Stable failure category |
| `details.authMode` | `JWT` |
| `details.method` / `details.path` / `details.requestId` | Reuse current request details |

Never audit or log:

1. Raw bearer token.
2. Raw JWT header/payload.
3. Refresh token.
4. Cookie.
5. Client secret.
6. Private key.
7. PII-heavy IdP claims.

If a subject must be recorded for diagnostics, use normalized username only after the token is structurally parseable and never include the full payload.

## Implementation Split

| Stage | Scope |
| --- | --- |
| `AUTH-008` | Add JWT/OIDC backend dependencies and configuration properties |
| `AUTH-009` | Implement JWT adapter with tests for validation success and failure categories |
| `AUTH-010` | Frontend bearer boundary design if direct browser token flow is required |
| `AUTH-011` | Deployment smoke tests and rollback playbook for JWT mode |

The project can continue to use `REVERSE_PROXY` as the production-safe path before these stages are implemented.

## Test Matrix For Future Implementation

| Test | Expected result |
| --- | --- |
| Valid token with registered active user | `CurrentUserContext` with `authMode=JWT` |
| Missing header | `UNAUTHORIZED` and `AUTH_FAILURE` |
| `Basic` or unsupported scheme | `UNAUTHORIZED` and `AUTH_FAILURE` |
| Oversized token | `UNAUTHORIZED` before parsing |
| Malformed token | `UNAUTHORIZED` and no raw token in audit |
| Expired token | `UNAUTHORIZED` |
| Invalid issuer | `UNAUTHORIZED` |
| Invalid audience | `UNAUTHORIZED` |
| Unknown user | Stable failure or forbidden response; no auto-provision |
| Inactive user | `FORBIDDEN` by existing authorization service |
| Token with IdP group claims | Groups ignored for budget authorization |

## Rollback Plan

| Failure | Rollback |
| --- | --- |
| IdP/JWKS outage | Switch production route back to `REVERSE_PROXY` if gateway route is available |
| Audience/issuer drift | Keep JWT mode disabled until configuration is corrected |
| Unexpected token validation failures | Use audit `AUTH_FAILURE` reason categories to isolate cause |
| Token data leakage risk | Disable JWT mode and inspect logs/audit before re-enabling |

## Non-Goals

1. Do not add login/logout UI.
2. Do not store access tokens or refresh tokens.
3. Do not use browser `localStorage` or `sessionStorage` for tokens.
4. Do not create local password accounts.
5. Do not use IdP group claims as budget authorization.
6. Do not add ERP integration, BI dashboards, or consolidation reports.

## Close Recommendation

Close AUTH-007 when this design, README, stage record, and repository checks are complete, with no runtime code, dependency, migration, secret, external service call, PDF/OCR source, ERP, BI, or consolidation scope introduced.
