# SEC-010 Security Document Consistency

## Goal

SEC-010 reconciles early security-stage documents with the current authentication and authorization posture. It keeps the historical stage records intact while making the current default behavior explicit for future readers.

## Current Security Baseline

The current baseline after SEC-005 to SEC-009 is:

1. `DEV_HEADER` is an explicit development/local mode.
2. `X-User-Roles` is ignored by default on the backend.
3. Workspace roles default to `app_user_role`.
4. Entity scope defaults to `app_user_entity_scope`.
5. Frontend production builds do not auto-inject `X-User-Id` or `X-User-Roles`.
6. JWT/OIDC/reverse proxy authentication is still a future implementation stage and must follow SEC-009 operational rules.

## Updated Historical Documents

| Document | Consistency update |
| --- | --- |
| `sec-001-security-scope-design.md` | Added current-status note that request header roles were MVP-only |
| `sec-002-security-backend-baseline.md` | Added current-status note about SEC-006/SEC-007/SEC-008 behavior |
| `sec-003-backend-authorization-entry-points.md` | Added current-status note for persisted roles and bootstrap admin path |
| `sec-003b-metadata-authorization.md` | Added current-status note for metadata bootstrap admin behavior |
| `sec-003c-budget-model-authorization.md` | Added current-status note for persisted Workspace role authorization |
| `sec-003d-budget-template-authorization.md` | Added current-status note for persisted Workspace role authorization |
| `sec-003e-budget-submission-authorization.md` | Added current-status note for role, Entity scope, and workflow responsibility |
| `sec-003f-actual-import-authorization.md` | Added current-status note for actual import authorization |
| `sec-004-frontend-security-context.md` | Added current-status note that SEC-008 supersedes production behavior |
| `audit-002-audit-query-api.md` | Added current-status note that SEC-007 supersedes request-header admin default |

## Non-Goals

SEC-010 does not:

1. Rewrite historical stage decisions.
2. Change backend or frontend behavior.
3. Add migrations.
4. Add authentication libraries.
5. Touch secrets or external services.
6. Add ERP, BI, or consolidation features.

## Verification

This is a documentation consistency stage. Required checks:

```powershell
git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/
git diff --check
git status --short
```

## Close Recommendation

SEC-010 can close when historical security documents clearly point to SEC-007/SEC-008/SEC-009 for current behavior, repository boundary checks pass, and no code, migration, PDF/OCR, secret, external service, ERP, BI, or consolidation scope is introduced.
