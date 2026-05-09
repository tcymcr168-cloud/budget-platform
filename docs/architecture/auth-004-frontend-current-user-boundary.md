# AUTH-004 Frontend Current User Boundary

## Stage Goal

AUTH-004 makes the frontend consume the backend trusted current-user contract from `/api/security/me`. It displays the resolved user, auth mode, application role count, and Entity scope count while keeping development identity overrides gated behind Vite development mode.

This stage does not implement login, logout, JWT/OIDC token handling, token storage, cookies, external identity provider calls, database migrations, ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Implemented Scope

| Area | Decision |
| --- | --- |
| API client | Added typed `getCurrentUser()` for `/api/security/me` |
| Current user state | App shell stores and refreshes backend-resolved current user |
| Production display | Shows backend `userId`, `authMode`, application role count, Entity scope count |
| Dev identity controls | Remain visible only when `isDevSecurityContextEnabled()` is true |
| Header injection | Still controlled only by `frontend/src/shared/api/http.ts` and Vite dev mode |

## Trust Boundary

1. The frontend displays identity but does not assert production roles.
2. Production builds do not auto-inject `X-User-Id` or `X-User-Roles`.
3. The current-user summary comes from `/api/security/me`.
4. Role and Entity scope counts are backend application authorization summaries, not IdP group claims.

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

1. This is not a login implementation.
2. If the backend returns `UNAUTHORIZED`, the existing shared error banner is used.
3. The role and Entity scope display is intentionally summarized as counts in this MVP stage.
4. Failed authentication audit events remain a later backend stage.
5. JWT/OIDC bearer validation remains a later optional route.

## Close Recommendation

Close AUTH-004 when frontend type-check, lint, build, and repository boundary checks pass, and no production role override, token storage, migration, PDF/OCR, ERP, BI, or consolidation scope is introduced.
