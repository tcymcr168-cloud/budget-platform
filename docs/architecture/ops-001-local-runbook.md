# OPS-001 Local Runbook

## Goal

OPS-001 documents the current local run and verification workflow for the budget platform. It is a handoff and daily-inspection runbook, not a deployment manual and not a production operations guide.

## Repository Location

```powershell
cd C:\codex\budget-platform
```

Expected Git state:

```powershell
git status --short
git branch --show-current
git remote -v
```

Current expected branch:

```text
main
```

Expected remote:

```text
https://github.com/tcymcr168-cloud/budget-platform.git
```

## Required Toolchain

| Tool | Purpose |
| --- | --- |
| Git | Source control |
| GitHub CLI | GitHub authentication and repository operations |
| Java 17 | Spring Boot backend |
| Maven | Backend build and tests |
| Node.js | Frontend tooling |
| pnpm | Frontend package scripts |
| Python 3.12 | PDF/OCR tooling |
| PostgreSQL | Target database |

Docker is not required for current local validation.

## Backend

Default backend configuration:

```yaml
server:
  port: 8080

spring:
  datasource:
    url: ${BUDGET_PLATFORM_DB_URL:jdbc:postgresql://localhost:5432/budget_platform}
    username: ${BUDGET_PLATFORM_DB_USERNAME:budget_platform}
    password: ${BUDGET_PLATFORM_DB_PASSWORD:budget_platform}

budget-platform:
  auth:
    mode: ${BUDGET_PLATFORM_AUTH_MODE:DEV_HEADER}
    allow-header-roles: ${BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES:false}
    bootstrap-admin-users: ${BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS:}
```

Run backend tests:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

For local manual API testing, set one of the following:

```powershell
$env:BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS='admin@example.com'
```

or, for controlled local-only compatibility with old fixtures:

```powershell
$env:BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES='true'
```

Do not use header role trust as a production authentication pattern.

## Frontend

Run frontend validation:

```powershell
cd C:\codex\budget-platform\frontend
pnpm type-check
pnpm lint
pnpm build
```

Run the local dev server:

```powershell
cd C:\codex\budget-platform\frontend
pnpm dev
```

The development identity selector is available only in Vite dev mode by default. Disable it during local hardening checks with:

```powershell
$env:VITE_ENABLE_DEV_SECURITY_CONTEXT='false'
```

Production builds do not automatically inject `X-User-Id` or `X-User-Roles`.

## Daily Inspection Checklist

Run these checks before reporting project health:

```powershell
cd C:\codex\budget-platform
git status --short
git log --oneline -6
git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/
git diff --check
```

Backend:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

Frontend:

```powershell
cd C:\codex\budget-platform\frontend
pnpm type-check
pnpm lint
pnpm build
```

## Protected Local Materials

Never commit:

1. `docs/source/bpc-pdf/*.pdf`
2. `docs/source/bpc-pdf/*.PDF`
3. OCR cache/text/output directories
4. `backend/target`
5. `frontend/dist`
6. `frontend/node_modules`
7. secrets, tokens, or environment-specific credentials

## Current Governance Notes

1. `X-User-Roles` is ignored by backend default unless explicitly enabled.
2. Workspace roles are owned by `app_user_role`.
3. Entity scope is owned by `app_user_entity_scope`.
4. Production JWT/OIDC/reverse-proxy authentication remains future work and must follow SEC-009.
5. The frontend now has MVP screens for security user/role/Entity scope management and read-only audit query.
6. No delete/revoke path exists yet by design.

## Troubleshooting

| Symptom | Likely cause | First check |
| --- | --- | --- |
| `401 UNAUTHORIZED` | Missing current user context | Confirm auth mode and frontend dev context |
| `403 FORBIDDEN` | Missing role, scope, or bootstrap admin | Check `app_user_role`, Entity scope, or bootstrap env var |
| Frontend build output appears in Git | Ignore rules or staging mistake | Run `git check-ignore frontend/dist/` and `git status --short` |
| PDF appears in Git status | Ignore rules or forced add mistake | Run `git check-ignore docs/source/bpc-pdf/*.pdf` |
| Maven test migration failure | Flyway/schema mismatch | Inspect exact migration error before changing schema |

## Close Recommendation

OPS-001 can close when this runbook, README, and stage record are updated, repository boundary checks pass, and no code, migration, PDF/OCR, secret, ERP, BI, or consolidation scope is introduced.
