# SEC-014 Inactive User Authorization Enforcement

## Stage Goal

SEC-014 enforces inactive-user blocking in the backend authorization layer. It reuses the existing `app_user.status` field and the disable/enable actions from SEC-013.

This stage does not add migrations, grant revocation schema, frontend UI, physical delete endpoints, external identity integration, ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Implemented Scope

| Area | Decision |
| --- | --- |
| Enforcement point | `AuthorizationService` |
| Registered inactive user | Rejected with `FORBIDDEN` before role or scope evaluation |
| Bootstrap admin | Still allowed when not registered, preserving initial setup path |
| Registered inactive bootstrap admin | Rejected with `FORBIDDEN` |
| Header roles | Cannot bypass inactive status when the user exists in `app_user` |
| Schema | No migration; reuses `app_user.status` |

## Behavior

Protected operations already pass through `AuthorizationService` via `requireHeaderAdmin`, `requireAdmin`, `requireAnyRole`, `rolesForWorkspace`, `canReadEntity`, or `readableEntityMemberIds`.

SEC-014 adds a status check in two places:

1. `requireHeaderAdmin` checks whether the current principal is a registered inactive user before allowing bootstrap or trusted request roles.
2. `rolesForWorkspace` loads the registered user once and rejects inactive users before returning persisted or trusted request roles.

This keeps the rule centralized and avoids scattering inactive-user checks across budget, metadata, template, submission, query, actual import, audit, and security controllers.

## Non-Goals

1. Do not revoke Workspace roles.
2. Do not revoke Entity scopes.
3. Do not add grant status fields.
4. Do not add frontend disable/enable controls.
5. Do not block unregistered bootstrap administrators during first setup.
6. Do not implement JWT/OIDC bearer validation.

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

1. Existing role and Entity scope grants remain stored and visible for inactive users.
2. Reactivating a user immediately restores their existing grants unless later soft revoke stages remove or deactivate grants.
3. External IdP account disabling is still outside the current application boundary.
4. User-facing frontend disable/enable workflow is still deferred to SEC-016.

## Close Recommendation

Close SEC-014 when inactive-user blocking is implemented in `AuthorizationService`, tests pass for inactive bootstrap and inactive header-role users, and no migration, physical delete, frontend UI, PDF/OCR, secret, ERP, BI, or consolidation scope is introduced.
