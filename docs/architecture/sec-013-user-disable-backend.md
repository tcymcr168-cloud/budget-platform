# SEC-013 User Disable Backend MVP

## Stage Goal

SEC-013 implements the backend MVP for disabling and enabling security users by reusing the existing `app_user.status` field. It does not add migrations, role revoke, Entity scope revoke, physical delete, frontend UI, external identity integration, ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Implemented Scope

| Area | Decision |
| --- | --- |
| Disable user | `POST /api/security/users/{userId}/disable` |
| Enable user | `POST /api/security/users/{userId}/enable` |
| Request body | Optional short `reason` field |
| Persistence | Reuses existing `app_user.status` |
| Audit | Records `STATUS_CHANGE` on `app_user` |
| Self disable | Rejected with `BAD_REQUEST` |
| Authorization | Existing `requireHeaderAdmin` gate |

## Non-Goals

1. Do not revoke Workspace roles.
2. Do not revoke Entity scopes.
3. Do not add soft revoke schema.
4. Do not add frontend buttons.
5. Do not physically delete security records.
6. Do not integrate with external IdP account lifecycle.

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

1. Inactive-user authorization enforcement is intentionally deferred to SEC-014.
2. Existing role and Entity scope grants remain visible for inactive users.
3. Enable action is available for recovery and is audited.
4. The endpoint uses action-style `POST`, not `DELETE`, to avoid physical deletion semantics.

## Close Recommendation

Close SEC-013 when backend tests pass, disable/enable actions and audit are covered, and no migration, physical delete, frontend UI, PDF/OCR, secret, ERP, BI, or consolidation scope is introduced.
