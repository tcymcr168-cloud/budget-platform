# AUTH-003 Current User API And Actor Trust

## Stage Goal

AUTH-003 hardens the current-user contract after the reverse proxy adapter. It makes `/api/security/me` expose the authentication mode and application-owned authorization summary while preserving the rule that budget roles and Entity scope are not trusted from client headers.

This stage does not implement JWT/OIDC, frontend login, database migrations, secrets, external identity provider calls, ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Implemented Scope

| Area | Decision |
| --- | --- |
| Current user auth mode | `/api/security/me` now returns `authMode` |
| Header roles | Returned in `roles` only as resolver context; reverse proxy mode returns an empty set |
| Application roles | `/api/security/me` returns persisted `applicationRoles` for registered users |
| Entity scopes | `/api/security/me` returns persisted `entityScopes` for registered users |
| Unregistered authenticated user | Still returns authenticated principal with empty app authorization summary |
| Audit actor | Reverse proxy integration test verifies actor id comes from trusted proxy principal |

## Response Contract

The current-user response includes:

| Field | Meaning |
| --- | --- |
| `userId` | Resolved principal id or username |
| `roles` | Resolver-local roles; empty in reverse proxy mode |
| `authenticated` | Whether a principal is present |
| `authMode` | `DEV_HEADER`, `REVERSE_PROXY`, or future modes |
| `applicationRoles` | Workspace roles persisted in `app_user_role` |
| `entityScopes` | Entity data scopes persisted in `app_user_entity_scope` |

## Trust Rules

1. In `REVERSE_PROXY` mode, `X-User-Id` and `X-User-Roles` are ignored.
2. Application roles and Entity scopes come from platform tables.
3. Audit actor ids use the resolved `CurrentUserContext.userId()`.
4. Unknown but authenticated principals are visible to `/api/security/me`, but have no application roles or Entity scopes until registered.

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

1. `/api/security/me` does not force user registration; unregistered users receive an empty authorization summary.
2. The frontend still needs a production current-user display pass.
3. Failed authentication audit events remain a later stage.
4. JWT/OIDC bearer validation remains a later optional route.

## Close Recommendation

Close AUTH-003 when backend tests pass, reverse proxy current-user behavior is covered, audit actor trust is verified, and no migration, PDF/OCR, secret, ERP, BI, or consolidation scope is introduced.
