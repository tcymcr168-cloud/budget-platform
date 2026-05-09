# AUTH-006 Deployment And Secret Operations Runbook

## Stage Goal

AUTH-006 documents production authentication deployment and operations controls for `DEV_HEADER`, `REVERSE_PROXY`, and future `JWT/OIDC` modes. This is a documentation stage only.

This stage does not implement JWT/OIDC validation, add dependencies, create login/logout flows, modify migrations, add secrets, access external services, configure a real gateway, or change frontend/backend runtime code.

## Authentication Mode Matrix

| Mode | Allowed environment | Trust source | Status |
| --- | --- | --- | --- |
| `DEV_HEADER` | Local development and controlled tests only | Developer-supplied headers | Existing local mode |
| `REVERSE_PROXY` | Production candidate behind trusted gateway | Gateway-set principal header | Implemented in AUTH-002A |
| `JWT` | Future production/API route | Backend-validated bearer token | Fails closed until AUTH-002B |

## Required Environment Variables

| Variable | Mode | Purpose | Secret |
| --- | --- | --- | --- |
| `BUDGET_PLATFORM_AUTH_MODE` | all | Selects `DEV_HEADER`, `REVERSE_PROXY`, or future `JWT` | No |
| `BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES` | dev/test only | Allows legacy request role headers when explicitly enabled | No |
| `BUDGET_PLATFORM_AUTH_REVERSE_PROXY_USER_HEADER` | reverse proxy | Header name for the trusted principal | No |
| `BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS` | bootstrap/local recovery | Comma-separated emergency admin usernames | No, but restrict operationally |
| future `BUDGET_PLATFORM_AUTH_JWT_ISSUER` | JWT | Expected token issuer | No |
| future `BUDGET_PLATFORM_AUTH_JWT_AUDIENCE` | JWT | Expected token audience | No |
| future `BUDGET_PLATFORM_AUTH_JWT_JWKS_URI` | JWT | JWKS endpoint or metadata location | Operationally sensitive |
| future `BUDGET_PLATFORM_AUTH_JWT_CLOCK_SKEW_SECONDS` | JWT | Allowed token clock skew | No |

Do not commit `.env`, key files, raw tokens, client secrets, private keys, or environment-specific credentials.

## Reverse Proxy Deployment Checklist

Before enabling `REVERSE_PROXY`:

1. Backend must not be reachable directly from browsers or public clients.
2. Gateway must authenticate the user before forwarding the request.
3. Gateway must strip all inbound identity headers supplied by the client.
4. Gateway must set exactly one trusted principal header, for example `X-Forwarded-User`.
5. Gateway must not forward client-supplied `X-User-Roles`.
6. Backend must run with `BUDGET_PLATFORM_AUTH_MODE=REVERSE_PROXY`.
7. Backend must use `BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES=false`.
8. `/api/security/me` must return `authMode=REVERSE_PROXY`.
9. Missing trusted principal header must produce `UNAUTHORIZED` and an `AUTH_FAILURE` audit event.

Recommended smoke check:

```powershell
Invoke-WebRequest -Uri https://<gateway-host>/api/security/me -Headers @{"X-Request-Id"="auth-smoke-001"}
```

Expected successful response should show the gateway-authenticated user. A direct backend request without the trusted gateway header should fail.

## Future JWT/OIDC Checklist

Before implementing or enabling JWT/OIDC:

1. Select a validation library and document the dependency rationale.
2. Validate signature, issuer, audience, expiry, not-before, and algorithm.
3. Reject `none` and unexpected algorithms.
4. Map `sub` or configured username claim to `app_user.username`.
5. Keep `app_user_role` and Entity scope as the only budget authorization sources.
6. Do not store bearer tokens in the frontend.
7. Do not persist refresh tokens in this application unless a separate stage designs it.
8. Configure JWKS cache and rotation behavior.
9. Fail closed on missing issuer, audience, or key configuration.
10. Add tests with test keys or mocked verifier.

## CORS, TLS, And Cookie Rules

| Area | Rule |
| --- | --- |
| TLS | Production traffic must terminate over HTTPS |
| Backend exposure | Backend should be private behind gateway for reverse proxy mode |
| CORS origins | Allow only known frontend origins per environment |
| Credentials | Enable only when cookie/session auth is explicitly designed |
| Cookies | If introduced later, use `HttpOnly`, `Secure`, and `SameSite=Lax` or stricter |
| CSRF | Required for future cookie-authenticated write requests |

## Logging And Audit Redaction

Never log or audit:

1. Raw bearer tokens.
2. Refresh tokens.
3. Cookies.
4. Client secrets.
5. Private keys.
6. Full IdP payloads.
7. Passwords.

Allowed authentication audit detail examples:

1. Stable failure category.
2. `authMode`.
3. Request method and path.
4. Request id.
5. Configured header name.
6. Subject hash or normalized username only when safely parseable in future JWT stages.

## Rollback Plan

| Failure | Rollback |
| --- | --- |
| Gateway principal header missing | Restore gateway header mapping and keep backend in `REVERSE_PROXY` only after smoke passes |
| Backend exposed directly | Remove direct route or firewall access before re-enabling reverse proxy trust |
| Production users locked out | Temporarily add controlled bootstrap admin users, then remove after role repair |
| JWT issuer/key misconfiguration in future stage | Return to `REVERSE_PROXY` or `DEV_HEADER` only in controlled non-production environments |
| Audit volume spike | Filter `AUTH_FAILURE` by reason and request path; do not disable audit without incident record |

## Daily Production Inspection

Minimum checks:

1. `/api/security/me` returns expected `authMode`.
2. `X-User-Roles` is not accepted as production authorization.
3. `AUTH_FAILURE` events are present for failed authentication attempts.
4. No raw tokens or cookies appear in application logs.
5. `app_user_role` and Entity scope remain the authorization source.
6. PDF/OCR/source materials are absent from commits and build artifacts.

## Close Recommendation

Close AUTH-006 when this runbook is committed with README and project step updates, repository checks pass, and no runtime code, migration, secret, external service, PDF/OCR, ERP, BI, or consolidation scope is introduced.
