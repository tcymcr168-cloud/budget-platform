# AUDIT-004 Frontend Audit Query MVP

## Goal

AUDIT-004 adds a minimal read-only frontend for persisted audit events. It reuses the existing audit query API and does not add BI dashboards, exports, report builders, delete operations, retention jobs, or backend changes.

## Implemented Scope

| Area | MVP behavior |
| --- | --- |
| Filter | Actor, subject type, subject id, action |
| Pagination | Previous and next page controls |
| Results | Time, actor, subject, action, details JSON |
| API client | Typed frontend client for `/api/audit/events` |
| Security | Reuses backend audit authorization |

## Non-Goals

AUDIT-004 does not implement:

1. CSV export.
2. BI charts or dashboards.
3. Audit retention or archive.
4. Delete or purge operations.
5. Failed authentication audit writes.
6. Backend endpoint changes.
7. Database migrations.
8. ERP or consolidation features.

## Frontend Files

| File | Responsibility |
| --- | --- |
| `frontend/src/features/audit/auditApi.ts` | Typed query client and audit event/page types |
| `frontend/src/App.tsx` | Adds read-only audit search and result panels |
| `frontend/src/styles.css` | Adds audit layout and form styles |

## Validation

```powershell
cd C:\codex\budget-platform\frontend
pnpm type-check
pnpm lint
pnpm build
```

AUDIT-004 validation result:

1. `pnpm type-check`: passed.
2. `pnpm lint`: passed.
3. `pnpm build`: passed.
4. Production build output remained under ignored `frontend/dist`.

## Risks And Limits

1. Access depends on the existing backend audit authorization and current authentication mode.
2. `detailsJson` is displayed as text to avoid inventing a schema-specific report layer.
3. There is no export or dashboard view by design.
4. Failed authentication events remain a future security/audit implementation stage.

## Close Recommendation

AUDIT-004 can close when the read-only audit query UI, typed API client, frontend validation, repository boundary checks, README, and stage record are complete, with no backend migration, PDF/OCR, secret, ERP, BI, or consolidation scope introduced.
