# SEC-004 Frontend Security Context

## Stage Goal

SEC-004 connects the existing React/Vite MVP to the backend authorization baseline by adding an internal security context to the shared HTTP client and the main application shell.

This stage does not implement production login, JWT, SSO, password policy, session management, or a complex permission administration screen.

## Scope

| Area | Decision |
| --- | --- |
| Frontend HTTP client | Automatically sends `X-User-Id` and `X-User-Roles` unless a caller explicitly overrides the headers |
| Default context | `admin@example.com` with `BUDGET_ADMIN` for local MVP continuity |
| UI control | Compact top-level security context controls for user id and role selection |
| Error display | 401/403 backend errors are surfaced with their authorization code |
| Backend logic | Unchanged |
| Database migration | Unchanged |

## Request Header Contract

The frontend uses the same internal technical contract introduced by SEC-003:

| Header | Purpose |
| --- | --- |
| `X-User-Id` | Current acting user id |
| `X-User-Roles` | Comma-separated role codes |

Role codes remain aligned with the backend enum:

1. `BUDGET_ADMIN`
2. `METADATA_MANAGER`
3. `TEMPLATE_DESIGNER`
4. `BUDGET_OWNER`
5. `BUDGET_REVIEWER`
6. `IMPORT_OPERATOR`
7. `READ_ONLY`

## Design Notes

1. The shared `requestJson` function is the single injection point, so existing metadata, model, template, submission, query, actual import, and variance screens receive the same context without module-by-module duplication.
2. The application shell keeps a simple internal context selector because the project has not entered production authentication.
3. Backend authorization remains the source of truth. The frontend selector is only a caller context switch for MVP testing and governance demos.
4. `401` and `403` payloads are shown with the backend error code to make security failures diagnosable during local testing.

## Verification

Required validation for this stage:

```powershell
cd C:\codex\budget-platform\frontend
pnpm type-check
pnpm lint
pnpm build
```

Repository boundary validation:

```powershell
cd C:\codex\budget-platform
git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/
git diff --check
git status --short
```

## Risks And Limits

1. This is not production authentication. Any client can still forge headers until a real authentication layer is introduced.
2. The current role picker supports one role at a time in the UI, while the HTTP client and backend support comma-separated roles.
3. Entity-scope assignment is not yet exposed in the frontend; scope-sensitive roles still require backend security data setup.
4. Persistent audit is still planned for a later audit stage.

## Closure Recommendation

Close SEC-004 when the frontend type check, lint, build, repository ignore checks, and stage record update all pass.
