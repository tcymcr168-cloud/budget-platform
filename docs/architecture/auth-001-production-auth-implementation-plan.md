# AUTH-001 Production Authentication Implementation Plan

## Stage Goal

AUTH-001 decomposes the production authentication implementation route for the budget platform. It turns the earlier security boundary documents into an executable plan while keeping this stage documentation-only.

This stage does not change backend code, frontend code, migrations, dependencies, secrets, external identity services, ERP integration, BI charts, consolidation reports, PDF files, or OCR outputs.

## Current Baseline

| Area | Current state | Implementation gap |
| --- | --- | --- |
| Authentication mode | `DEV_HEADER`, `JWT`, `REVERSE_PROXY` enum exists | `JWT` and `REVERSE_PROXY` fail closed |
| Local identity | `X-User-Id` in `DEV_HEADER` mode | Suitable only for local/dev validation |
| Role trust | `X-User-Roles` disabled by default | Production must keep roles app-owned |
| Application roles | `app_user_role` is the role source | Needs trusted principal before production use |
| Entity scope | Persisted app-owned scope table | Depends on trusted user identity |
| Frontend | Production build does not auto-inject dev headers | Needs current-user display and login/session boundary |
| Audit | Persistent business audit exists | Failed authentication audit is not implemented |

## Decision Summary

AUTH-001 recommends implementing the production route in this order:

1. Implement `REVERSE_PROXY` trusted principal mode first when the target deployment has an enterprise gateway that can authenticate users and strip or overwrite inbound identity headers.
2. Keep `JWT/OIDC` bearer validation as the second route when the application must validate tokens itself or when no trusted gateway exists.
3. Keep budget authorization app-owned in both routes: Workspace role and Entity scope come from platform tables, not from token/group claims.
4. Avoid local passwords unless a later explicit local-account stage designs password storage, lockout, reset, rotation, and audit.

## Route A: Reverse Proxy Trusted Principal

This route trusts a gateway-authenticated principal after the gateway has enforced authentication and sanitized identity headers.

| Item | Rule |
| --- | --- |
| Backend mode | `budget-platform.auth.mode=REVERSE_PROXY` |
| Principal source | Configured header such as `X-Forwarded-User` |
| Role source | `app_user_role` only |
| Entity scope source | Application Entity scope tables |
| Required gateway behavior | Strip inbound identity headers from clients, then set trusted headers |
| App secret handling | No token signing secret required in the application |
| Main risk | Direct backend exposure or misconfigured header overwrite can allow spoofing |

Recommended use:

1. Enterprise deployment already has SSO at gateway, ingress, reverse proxy, or API gateway.
2. Backend is reachable only from the trusted gateway network.
3. Gateway ownership and header overwrite behavior are operationally controlled.

## Route B: JWT/OIDC Bearer Validation

This route makes the backend validate bearer tokens directly.

| Item | Rule |
| --- | --- |
| Backend mode | `budget-platform.auth.mode=JWT` |
| Principal source | Bearer token `sub` or configured username claim |
| Role source | `app_user_role` only |
| Entity scope source | Application Entity scope tables |
| Required validation | Signature, issuer, audience, expiry, not-before, algorithm allow-list |
| Required configuration | Issuer, audience, JWKS or public key, clock skew, claim mapping |
| Main risk | More dependency, key rotation, token parsing, and IdP configuration complexity |

Recommended use:

1. Backend must validate identity without relying on a gateway.
2. API clients need direct bearer authentication.
3. The team can operate issuer, audience, JWKS, rotation, and test key configuration safely.

## Decision Matrix

| Dimension | Route A: Reverse proxy | Route B: JWT/OIDC |
| --- | --- | --- |
| MVP implementation speed | Faster | Slower |
| New Java dependencies | Not required for MVP | Likely required |
| App-managed secrets | Minimal | JWKS/public key or issuer metadata |
| Frontend login work | Gateway-driven | IdP/token client boundary needed |
| Operational dependency | Gateway correctness | IdP/JWKS/token correctness |
| Direct API support | Needs gateway path | Natural fit |
| Spoofing risk | High if backend exposed directly | Lower after token validation |
| Enterprise fit | Strong when SSO gateway exists | Strong when API resource server is required |

## Minimum Stage Split

### AUTH-002A: Reverse Proxy Backend Adapter

Goal: implement `REVERSE_PROXY` mode without changing frontend login.

Scope:

1. Read trusted principal from configured reverse proxy header.
2. Require non-empty normalized username.
3. Ignore caller-supplied role headers.
4. Reuse existing application role and Entity scope resolution.
5. Add tests for accepted principal, missing header, blank header, and ignored roles.
6. Document deployment assumption that backend must not be directly reachable by browsers.

Non-goals:

1. Do not add JWT dependencies.
2. Do not add login UI.
3. Do not add database migration.
4. Do not implement gateway network CIDR enforcement unless explicitly scoped in a later hardening stage.

### AUTH-002B: JWT/OIDC Backend Adapter

Goal: implement `JWT` mode after route A or when route A is not viable.

Scope:

1. Select token validation library and document why it is used.
2. Validate signature, issuer, audience, expiry, not-before, and algorithm.
3. Map configured subject or username claim to `app_user.username`.
4. Reject unknown, inactive, expired, or malformed tokens with stable `UNAUTHORIZED` responses.
5. Add unit and integration tests with test keys or mocked token verifier.

Non-goals:

1. Do not store refresh tokens.
2. Do not persist raw token payloads.
3. Do not use IdP group claims as budget authorization.
4. Do not introduce local password accounts.

### AUTH-003: Current User API And Actor Trust Hardening

Goal: expose the trusted current-user contract for frontend and audit diagnostics.

Scope:

1. Add `/api/security/me`.
2. Return username, display name, auth source, Workspace roles, and accessible Entity scope summary.
3. Ensure audit actor id uses the trusted resolved principal.
4. Add tests for unauthenticated and authenticated current-user responses.

### AUTH-004: Frontend Production Current User Boundary

Goal: replace production identity assumptions with backend current-user display.

Scope:

1. Call `/api/security/me` through the shared frontend API boundary.
2. Display current user and auth source.
3. Keep dev identity selector hidden unless `VITE_ENABLE_DEV_SECURITY_CONTEXT=true`.
4. Show stable unauthenticated and forbidden states.

### AUTH-005: Failed Authentication Audit

Goal: record security events without storing secrets or raw tokens.

Scope:

1. Record missing principal, malformed token, invalid token, unknown user, disabled user, and expired session categories.
2. Store timestamp, request id, username or subject hash when available, and failure category.
3. Avoid storing bearer tokens, raw IdP payloads, or PII-heavy claims.

### AUTH-006: Deployment And Secret Operations Runbook

Goal: document production operation before rollout.

Scope:

1. Required environment variables.
2. Gateway header overwrite requirements for reverse proxy mode.
3. JWT issuer, audience, JWKS, clock skew, and key rotation rules for JWT mode.
4. CORS, TLS termination, log redaction, and rollback checks.

## Acceptance Criteria For AUTH-001

AUTH-001 can close when:

1. Reverse proxy and JWT/OIDC routes are compared.
2. Recommended first implementation route is documented.
3. Follow-up stages are small and independently testable.
4. Non-goals and forbidden scopes are explicit.
5. `README.md` and `PROJECT_STEP_RECORD.md` are updated.
6. Repository checks show no PDF, OCR, build output, secret, code, migration, ERP, BI, or consolidation change in this stage.

## Risks And Controls

| Risk | Control |
| --- | --- |
| Header spoofing in reverse proxy mode | Backend must be private behind the gateway; gateway strips inbound identity headers |
| JWT configuration drift | Use issuer/audience allow-list and fail closed on missing configuration |
| Token data leaking into logs | Never log raw tokens or full IdP payloads |
| IdP groups becoming budget authorization | Keep Workspace roles and Entity scope in application tables |
| Dev header leaking into production | Keep production frontend from injecting dev headers and document `DEV_HEADER` as local only |

## Close Recommendation

Close AUTH-001 after documentation and repository validation pass. The next implementation stage should be `AUTH-002A` unless deployment constraints require direct JWT/OIDC validation first.
