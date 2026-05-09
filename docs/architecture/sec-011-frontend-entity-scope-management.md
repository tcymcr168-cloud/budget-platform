# SEC-011 Frontend Entity Scope Management MVP

## Goal

SEC-011 adds a minimal frontend entry point for security user, Workspace role, and Entity scope administration. It uses the existing backend security API and does not add delete/revoke actions, migrations, new backend endpoints, complex permission matrices, or production login.

## Implemented Scope

| Area | MVP behavior |
| --- | --- |
| Users | Create user, list users, select current security user |
| Workspace roles | Grant one role to the selected user in the selected Workspace |
| Entity scopes | Grant one Entity member scope to the selected user in the selected Workspace |
| Existing data reuse | Reuses selected Workspace and loaded Entity members from metadata |
| Read model | Lists current roles and Entity scopes for the selected user and Workspace |

## Non-Goals

SEC-011 does not implement:

1. Delete or revoke role/scope actions.
2. User disable/enable flows.
3. Bulk grants.
4. Complex Account/Time/Category/Version permission matrices.
5. Backend endpoint changes.
6. Database migrations.
7. Production login, JWT, OAuth/OIDC, or secrets.
8. ERP, BI, or consolidation features.

## Frontend Files

| File | Responsibility |
| --- | --- |
| `frontend/src/features/security/securityApi.ts` | Typed client for `/api/security` users, roles, and Entity scopes |
| `frontend/src/App.tsx` | Adds compact security administration panels |
| `frontend/src/styles.css` | Adds layout and form styles for the security panels |

## Operational Notes

The backend default from SEC-007 does not trust `X-User-Roles`. Manual local testing needs one of:

1. `BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS` containing the current dev user, or
2. explicit local/test `BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES=true`.

This keeps the frontend management screen useful for MVP governance while avoiding a production role-header bypass.

## Validation

```powershell
cd C:\codex\budget-platform\frontend
pnpm type-check
pnpm lint
pnpm build
```

SEC-011 validation result:

1. `pnpm type-check`: passed.
2. `pnpm lint`: passed.
3. `pnpm build`: passed.
4. Production build output remained under ignored `frontend/dist`.

## Risks And Limits

1. There is no revoke path yet; duplicate grants will be rejected by backend uniqueness constraints.
2. This screen relies on existing security API authorization and is not a replacement for production authentication.
3. Entity member choices depend on metadata for the currently selected Workspace being loaded.
4. Future stages should add revoke/disable behavior only after an explicit deletion or deactivation design.

## Close Recommendation

SEC-011 can close when the frontend API client, compact management UI, styles, frontend validation, repository boundary checks, README, and stage record are complete, with no backend migration, PDF/OCR, secret, ERP, BI, or consolidation scope introduced.
