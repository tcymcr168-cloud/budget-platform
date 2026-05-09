# SEC-015 Grant Soft Revoke Schema And API

## Stage Goal

SEC-015 adds soft revoke support for Workspace role grants and Entity scope grants. Revoked grants remain in the database for traceability, while authorization and active grant lists only use `ACTIVE` grants.

This stage does not physically delete grants, add frontend UI, integrate external identity providers, add ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Implemented Scope

| Area | Decision |
| --- | --- |
| Migration | `V8__security_grant_soft_revoke.sql` |
| Grant status | `ACTIVE` / `REVOKED` |
| Role revoke API | `POST /api/security/users/{userId}/roles/{roleId}/revoke` |
| Entity scope revoke API | `POST /api/security/users/{userId}/entity-scopes/{scopeId}/revoke` |
| Revoke request | Optional short `reason` |
| Audit | `ACCESS_CHANGE` on role/scope grant subject |
| Re-grant after revoke | Reactivates the existing grant row |
| Active authorization | Only `ACTIVE` grants participate in role/scope checks |

## Data Model

SEC-015 adds these columns to both `app_user_role` and `app_user_entity_scope`:

| Column | Purpose |
| --- | --- |
| `status` | Current grant status: `ACTIVE` or `REVOKED` |
| `revoked_at` | Timestamp when the grant was revoked |
| `revoked_by` | Actor user id that revoked the grant |
| `revoked_reason` | Short operator-provided reason |

The existing unique constraints are retained. When an identical grant is created after revoke, the application reactivates the existing row instead of inserting a duplicate. This keeps H2 and PostgreSQL behavior simple and avoids database-specific partial unique indexes at this stage.

## Authorization Behavior

1. `AuthorizationService.rolesForWorkspace` reads only active role grants.
2. `AuthorizationService.canReadEntity` and `readableEntityMemberIds` read only active Entity scope grants.
3. `SecurityService.currentUser` exposes only active application roles and Entity scopes.
4. Security management list endpoints return only active grants.

## Audit Behavior

Role and Entity scope grant lifecycle events all use `ACCESS_CHANGE`.

Audit details include the username, workspace, grant target, status, and revoke reason where applicable. Historical audit rows are not changed when a grant is revoked or reactivated.

## Non-Goals

1. Do not expose physical grant delete endpoints.
2. Do not add frontend revoke controls.
3. Do not add bulk revoke.
4. Do not cascade revoke budget facts, submissions, imports, templates, metadata, or audit events.
5. Do not add complex multi-dimensional permission matrices.

## Verification

Backend validation:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

Repository checks:

```powershell
git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/
git diff --check
git status --short
```

## Risks And Limits

1. Active list endpoints intentionally hide revoked grants; audit remains the current traceability source.
2. Re-granting a revoked grant reuses the same grant id.
3. Frontend revoke and disable controls still need SEC-016.
4. Audit query polish for lifecycle events remains a later stage.

## Close Recommendation

Close SEC-015 when migration, soft revoke domain model, revoke endpoints, active-only authorization, reactivation behavior, audit coverage, tests, README, and project record are complete.
