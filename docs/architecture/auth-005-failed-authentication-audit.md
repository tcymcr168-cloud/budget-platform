# AUTH-005 Failed Authentication Audit

## Stage Goal

AUTH-005 records failed authentication events without introducing token storage, secrets, external identity provider calls, JWT/OIDC validation, database migrations, ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Implemented Scope

| Area | Decision |
| --- | --- |
| Audit action | Added `AUTH_FAILURE` to the existing audit action enum |
| Subject | Failed authentication events use `subjectType=authentication` and `subjectId=failure` |
| Actor | `actorId` remains null because the principal is not trusted on failure |
| Reverse proxy missing principal | Records `MISSING_REVERSE_PROXY_PRINCIPAL` |
| Reverse proxy header not configured | Records `REVERSE_PROXY_HEADER_NOT_CONFIGURED` |
| Missing HTTP request | Records `MISSING_HTTP_REQUEST` |
| Unsupported JWT mode | Records `JWT_NOT_IMPLEMENTED` |
| Frontend audit filter | `AUTH_FAILURE` is available in the audit action selector |

## Recorded Details

Authentication failure details are intentionally low sensitivity:

| Field | Meaning |
| --- | --- |
| `reason` | Stable failure category |
| `message` | Stable application error message |
| `authMode` | Current configured authentication mode |
| `headerName` | Configured trusted header name when relevant |
| `method` | HTTP method when a request is available |
| `path` | Request path when a request is available |
| `requestId` | `X-Request-Id` when provided |

The audit event does not store bearer tokens, raw IdP payloads, cookies, secrets, passwords, or PII-heavy identity claims.

## Verification

Backend validation:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

Frontend validation:

```powershell
cd C:\codex\budget-platform\frontend
pnpm type-check
pnpm lint
pnpm build
```

Repository checks:

```powershell
git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/
git diff --check
git status --short
```

## Risks And Limits

1. This stage records authentication failures raised by the current resolver boundary, not every possible authorization failure.
2. JWT/OIDC validation remains unimplemented, so JWT failures are limited to the configured-mode-not-implemented category.
3. Failed authentication events share the existing audit table; future retention policy can still segment security events operationally.
4. No raw token, cookie, password, or secret should be added to audit details in future stages.

## Close Recommendation

Close AUTH-005 when backend and frontend validations pass, failed reverse proxy authentication is covered by integration tests, and no migration, secret, raw token storage, PDF/OCR, ERP, BI, or consolidation scope is introduced.
