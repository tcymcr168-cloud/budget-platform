# AUDIT-005 Security Lifecycle Audit UX

## Stage Goal

AUDIT-005 improves the existing audit frontend for security lifecycle review. It adds quick filters for user status, role access, Entity access, and authentication failures, and formats audit details JSON for easier inspection.

This stage does not add BI charts, dashboards, alerting, external services, backend schema, new audit tables, ERP integration, consolidation reports, PDF handling, or OCR outputs.

## Implemented Scope

| Area | Decision |
| --- | --- |
| Quick filters | User status, role access, Entity access, auth failures |
| Details display | Pretty-prints valid JSON in the audit result table |
| API behavior | Reuses existing audit search endpoint |
| Data model | No schema changes |
| Security lifecycle coverage | Focuses on `STATUS_CHANGE`, `ACCESS_CHANGE`, and `AUTH_FAILURE` |

## UX Boundary

This remains an operational table, not a BI dashboard. The goal is faster inspection for administrators who need to verify disable, revoke, regrant, and failed authentication events.

The quick filters map to existing fields:

| Preset | Subject type | Action |
| --- | --- | --- |
| User status | `app_user` | `STATUS_CHANGE` |
| Role access | `app_user_role` | `ACCESS_CHANGE` |
| Entity access | `app_user_entity_scope` | `ACCESS_CHANGE` |
| Auth failures | `authentication` | `AUTH_FAILURE` |

## Non-Goals

1. Do not add charts or visual analytics.
2. Do not add alerting or notification integrations.
3. Do not add new backend query endpoints.
4. Do not change audit retention or archival.
5. Do not expose physical delete workflows.

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

1. Audit details are still stored as JSON text and rendered read-only.
2. Presets cover current security lifecycle events only.
3. There is no export or saved-search feature in this stage.
4. Alerting and retention policy remain later operational work.

## Close Recommendation

Close AUDIT-005 when frontend validation, repository protection, and boundary scans pass, with no BI chart, external alerting, schema, build artifact, PDF, OCR, or secret committed.
