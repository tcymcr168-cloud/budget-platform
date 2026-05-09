# AUTH-002A Reverse Proxy Authentication Adapter

## Stage Goal

AUTH-002A implements the backend `REVERSE_PROXY` authentication mode planned in AUTH-001. The mode resolves a trusted principal from a configured reverse proxy header and keeps budget authorization in application tables.

This stage does not implement JWT/OIDC validation, frontend login, gateway software configuration, network CIDR checks, database migrations, secrets, ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Implemented Scope

| Area | Decision |
| --- | --- |
| Auth mode | `REVERSE_PROXY` now resolves a principal instead of failing closed |
| Principal source | `budget-platform.auth.reverse-proxy-user-header` |
| Default header | `X-Forwarded-User` |
| Role handling | `X-User-Roles` is ignored in `REVERSE_PROXY` mode |
| Missing principal | Fails with `UNAUTHORIZED` |
| Blank principal | Fails with `UNAUTHORIZED` |
| Blank configured header name | Fails with `UNAUTHORIZED` |
| Controller changes | None; resolver reads the current servlet request in reverse proxy mode |

## Runtime Contract

For reverse proxy mode:

```yaml
budget-platform:
  auth:
    mode: REVERSE_PROXY
    reverse-proxy-user-header: X-Forwarded-User
    allow-header-roles: false
```

The reverse proxy or enterprise gateway must:

1. Authenticate the user before forwarding traffic.
2. Strip any inbound identity headers supplied by the browser or API client.
3. Set the configured trusted principal header itself.
4. Prevent browsers and external clients from reaching the backend directly.

## Security Behavior

1. The resolver reads only the configured trusted principal header in `REVERSE_PROXY` mode.
2. Client-supplied `X-User-Id` is ignored in `REVERSE_PROXY` mode.
3. Client-supplied `X-User-Roles` is ignored in `REVERSE_PROXY` mode.
4. Roles remain resolved by `AuthorizationService` from `app_user_role` and bootstrap admin configuration.
5. Entity data scope remains resolved from application-owned Entity scope records.

## Verification

Backend validation:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

Expected result:

```text
BUILD SUCCESS
```

Repository checks:

```powershell
git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/
git diff --check
git status --short
```

## Risks And Limits

1. Header spoofing remains possible if the backend is exposed directly to clients.
2. This stage does not validate source IP or trusted proxy network ranges.
3. This stage does not implement JWT/OIDC bearer validation.
4. Failed authentication audit events are still a future stage.
5. Frontend production current-user behavior still depends on the existing `/api/security/me` endpoint and future auth-boundary refinement.

## Close Recommendation

Close AUTH-002A when backend tests and repository checks pass, no migration or frontend login code is added, and no PDF/OCR/build output is staged.
