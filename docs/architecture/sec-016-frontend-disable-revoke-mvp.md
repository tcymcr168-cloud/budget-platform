# SEC-016 Frontend Disable And Revoke MVP

## Stage Goal

SEC-016 adds the minimum frontend operations for user disable/enable and grant revoke workflows in the existing security management view.

This stage does not add new backend schema, bulk revoke, complex approval workflows, physical delete controls, external identity integration, ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Implemented Scope

| Area | Decision |
| --- | --- |
| User lifecycle | Selected user can be disabled or enabled |
| Workspace roles | Active role rows expose a revoke action |
| Entity scopes | Active scope rows expose a revoke action |
| Reason input | One shared optional reason field for lifecycle/revoke actions |
| API client | Adds disable, enable, role revoke, and Entity scope revoke calls |
| Refresh behavior | Refreshes users, grants, and current-user summary after lifecycle/revoke actions |

## UX Boundary

The implementation stays inside the existing operational security administration surface. It avoids a marketing-style flow, modal-heavy interaction, bulk actions, or approval workflow complexity.

The single reason input is intentionally simple. It supports the backend audit trail without introducing a separate form or per-row state machine.

## Non-Goals

1. Do not add bulk disable or bulk revoke.
2. Do not expose physical delete.
3. Do not add approval workflow.
4. Do not show revoked grants in a history grid.
5. Do not add new backend endpoints or migrations.
6. Do not change authentication mode or JWT/OIDC behavior.

## Verification

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

1. The frontend lists only active grants; revoked grant history remains available through audit.
2. The reason input is shared across disable/enable and revoke actions.
3. Backend still prevents self-disable; the frontend does not attempt to duplicate every backend guard.
4. JWT/OIDC bearer validation remains outside this stage.

## Close Recommendation

Close SEC-016 when frontend type-check, lint, build, repository protection, and boundary scans pass, with no physical delete controls, no generated build artifacts committed, and no PDF/OCR source committed.
