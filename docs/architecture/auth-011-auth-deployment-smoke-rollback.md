# AUTH-011 Auth Deployment Smoke And Rollback

## Stage Goal

AUTH-011 documents deployment smoke checks and rollback steps for the two production-capable authentication routes:

1. `REVERSE_PROXY` trusted principal mode for browser traffic behind an enterprise gateway.
2. `JWT` bearer mode for gateway/API/service clients that can manage tokens outside browser JavaScript.

This stage does not commit secrets, raw tokens, `.env` files, gateway config, IdP config, certificates, private keys, external service calls, frontend token code, migrations, ERP integration, BI charts, consolidation reports, PDF files, or OCR output.

## Preflight Checks

Run before either auth mode is enabled.

| Check | Command or evidence | Expected |
| --- | --- | --- |
| Backend tests | `cd C:\codex\budget-platform\backend; mvn test` | Passing |
| Protected materials | `git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/` | All paths ignored |
| Runtime secrets | Deployment platform secret store | No secrets committed to Git |
| User provisioning | Security admin UI/API | Target users exist in `app_user` and are `ACTIVE` |
| Application grants | Security admin UI/API | Workspace roles and Entity scopes are assigned in application tables |
| Audit route | `/api/audit/events` for admin users | Queryable after auth succeeds |

## Reverse Proxy Mode

### Required Environment

| Variable | Example | Notes |
| --- | --- | --- |
| `BUDGET_PLATFORM_AUTH_MODE` | `REVERSE_PROXY` | Required |
| `BUDGET_PLATFORM_AUTH_REVERSE_PROXY_USER_HEADER` | `X-Forwarded-User` | Must be set only by gateway |
| `BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES` | `false` | Required in production |
| `BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS` | empty or emergency controlled list | Keep empty unless break-glass is needed |

### Gateway Requirements

1. Browser cannot reach backend directly.
2. Gateway authenticates through enterprise SSO.
3. Gateway strips inbound client identity headers before forwarding.
4. Gateway sets exactly one trusted principal header.
5. Gateway does not forward client-supplied `X-User-Roles`.
6. TLS terminates at the trusted edge or later; no public plaintext route.

### Smoke Checks

Successful browser/gateway check:

```powershell
Invoke-WebRequest `
  -Uri https://<gateway-host>/api/security/me `
  -Headers @{"X-Request-Id"="auth-rp-smoke-001"}
```

Expected:

1. HTTP 200.
2. Response `authenticated=true`.
3. Response `authMode=REVERSE_PROXY`.
4. Response `userId` equals the SSO-authenticated principal normalized by the backend.
5. Application roles and Entity scopes come from application grants, not gateway role headers.

Negative direct-backend check:

```powershell
Invoke-WebRequest `
  -Uri http://<private-backend-host>:8080/api/security/me `
  -Headers @{"X-Request-Id"="auth-rp-negative-001"}
```

Expected if direct backend is reachable in a controlled private smoke environment:

1. HTTP 401.
2. Error is `UNAUTHORIZED`.
3. Audit reason is `MISSING_REVERSE_PROXY_PRINCIPAL`.

If this request succeeds from an untrusted network path, rollback immediately and close the direct backend route.

## JWT Mode

### Required Environment

| Variable | Example | Notes |
| --- | --- | --- |
| `BUDGET_PLATFORM_AUTH_MODE` | `JWT` | Required |
| `BUDGET_PLATFORM_AUTH_JWT_ISSUER` | `https://idp.example.com/realms/budget` | Must match token `iss` |
| `BUDGET_PLATFORM_AUTH_JWT_AUDIENCE` | `budget-platform-api` | Must match token `aud` |
| `BUDGET_PLATFORM_AUTH_JWT_JWKS_URI` | `https://idp.example.com/.../jwks.json` | Do not commit environment-specific value if sensitive |
| `BUDGET_PLATFORM_AUTH_JWT_USERNAME_CLAIM` | `sub` or `preferred_username` | Must map to `app_user.username` |
| `BUDGET_PLATFORM_AUTH_JWT_CLOCK_SKEW_SECONDS` | `60` | Keep small |
| `BUDGET_PLATFORM_AUTH_JWT_MAX_TOKEN_LENGTH` | `8192` | Reject oversized bearer strings |
| `BUDGET_PLATFORM_AUTH_JWT_ALLOWED_ALGORITHMS` | `RS256` | Do not allow `none` |
| `BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES` | `false` | Required in production |

Never paste a real token into a committed file. For local shell smoke, keep the token in a transient environment variable only:

```powershell
$env:BUDGET_PLATFORM_SMOKE_JWT='<redacted-runtime-token>'
```

### Smoke Checks

Successful JWT check:

```powershell
Invoke-WebRequest `
  -Uri https://<api-host>/api/security/me `
  -Headers @{
    "Authorization"="Bearer $env:BUDGET_PLATFORM_SMOKE_JWT"
    "X-Request-Id"="auth-jwt-smoke-001"
  }
```

Expected:

1. HTTP 200.
2. Response `authenticated=true`.
3. Response `authMode=JWT`.
4. Response `userId` equals the configured username claim after backend normalization.
5. Response roles and Entity scopes reflect application grants only.

Negative missing bearer check:

```powershell
Invoke-WebRequest `
  -Uri https://<api-host>/api/security/me `
  -Headers @{"X-Request-Id"="auth-jwt-negative-missing-001"}
```

Expected:

1. HTTP 401.
2. Audit reason `MISSING_BEARER_TOKEN`.

Negative unsupported scheme check:

```powershell
Invoke-WebRequest `
  -Uri https://<api-host>/api/security/me `
  -Headers @{
    "Authorization"="Basic not-a-bearer-token"
    "X-Request-Id"="auth-jwt-negative-scheme-001"
  }
```

Expected:

1. HTTP 401.
2. Audit reason `UNSUPPORTED_AUTHORIZATION_SCHEME`.

### Audit Checks

After smoke tests, query audit as an authorized admin:

```powershell
Invoke-WebRequest `
  -Uri "https://<api-host>/api/audit/events?action=AUTH_FAILURE&page=0&size=20" `
  -Headers @{"X-Request-Id"="auth-audit-smoke-001"}
```

Expected:

1. Failure records include stable `details.reason`.
2. `details.authMode` matches `REVERSE_PROXY` or `JWT`.
3. Request id appears when provided.
4. Raw bearer tokens, JWT header/payload, cookies, private keys, client secrets, and full IdP claim dumps do not appear.

## Failure Reason Triage

| Reason | Likely cause | First action |
| --- | --- | --- |
| `MISSING_REVERSE_PROXY_PRINCIPAL` | Gateway did not set trusted principal header | Check gateway header mapping and stripping rules |
| `REVERSE_PROXY_HEADER_NOT_CONFIGURED` | Missing or blank backend header config | Set `BUDGET_PLATFORM_AUTH_REVERSE_PROXY_USER_HEADER` |
| `MISSING_BEARER_TOKEN` | Missing `Authorization: Bearer` header | Check caller token attachment |
| `UNSUPPORTED_AUTHORIZATION_SCHEME` | Caller sent non-bearer auth | Fix caller auth scheme |
| `TOKEN_TOO_LARGE` | Token exceeds configured max length | Inspect token source and configured size guard |
| `JWT_CONFIGURATION_MISSING` | Missing issuer, audience, or JWKS URI | Fix deployment environment variables |
| `JWT_CONFIGURATION_INVALID` | Bad algorithm or username claim config | Fix allow-list and claim settings |
| `TOKEN_EXPIRED` | Expired access token | Refresh through IdP/client flow |
| `TOKEN_NOT_YET_VALID` | Clock skew or future `nbf` | Check system clocks and token lifetime |
| `INVALID_ISSUER` | Token issuer does not match config | Correct issuer or reject wrong tenant |
| `INVALID_AUDIENCE` | Token audience does not match API | Correct API audience/client config |
| `INVALID_SIGNATURE` | Wrong JWKS, rotated key, or tampered token | Check JWKS and key rotation |
| `MALFORMED_TOKEN` | Bad JWT serialization | Reject caller token |
| `UNKNOWN_APPLICATION_USER` | Valid token but user not provisioned | Create/activate app user and grants |
| `INACTIVE_APPLICATION_USER` | Valid token maps to inactive app user | Re-enable user only after security review |

## Rollback

| Failure | Rollback |
| --- | --- |
| Gateway route exposes backend directly | Remove direct route or firewall access before re-enabling |
| Reverse proxy header missing | Roll back gateway config to last known good header mapping |
| JWT issuer/audience/JWKS drift | Switch `BUDGET_PLATFORM_AUTH_MODE` back to `REVERSE_PROXY` if browser gateway route is available |
| JWT key rotation outage | Keep JWT disabled until JWKS and signing key state are verified |
| Production users locked out | Use controlled bootstrap admin only as break-glass, then remove immediately after grant repair |
| Raw token appears in logs/audit | Disable affected auth mode, preserve incident evidence, redact logs per policy, and patch before re-enable |

Rollback must not switch production to `DEV_HEADER`. `DEV_HEADER` remains local development and controlled test only.

## Close Recommendation

Close AUTH-011 when this smoke and rollback runbook, README update, stage record, repository checks, and boundary scan are complete, with no secrets, raw tokens, runtime code changes, migrations, external service calls, PDF/OCR source, ERP, BI, or consolidation scope introduced.
