# SEC-008 Frontend Authentication Boundary

## Goal

SEC-008 tightens the frontend authentication boundary. The React application no longer sends internal identity headers in production builds. The existing user and role selector remains available only in Vite development mode so the MVP can still be tested without pretending that the selector is a production login system.

This stage does not implement IdP login, JWT storage, refresh tokens, cookies, logout, password handling, or a new authentication screen.

## Runtime Behavior

| Runtime | Dev identity selector | `X-User-Id` auto header | `X-User-Roles` auto header |
| --- | --- | --- | --- |
| Vite dev, default | Visible | Sent | Sent |
| Vite dev, `VITE_ENABLE_DEV_SECURITY_CONTEXT=false` | Hidden | Not sent | Not sent |
| Production build | Hidden | Not sent | Not sent |

The frontend shared HTTP client is now the single enforcement point:

1. It only injects `X-User-Id` and `X-User-Roles` when `import.meta.env.DEV` is true.
2. Development injection can be disabled by `VITE_ENABLE_DEV_SECURITY_CONTEXT=false`.
3. Production builds do not auto-inject either header.

## Why This Boundary Matters

SEC-007 made the backend stop trusting request role headers by default. SEC-008 aligns the frontend with that backend posture:

1. The browser no longer looks like a source of production roles.
2. The development role picker is clearly limited to development builds.
3. Future production authentication can replace the boundary with `/api/security/me`, IdP redirect, or reverse proxy session behavior without keeping role override controls.

## Files

| File | Responsibility |
| --- | --- |
| `frontend/src/shared/api/http.ts` | Gates internal identity header injection behind Vite dev mode |
| `frontend/src/App.tsx` | Hides the development identity selector outside dev mode |
| `frontend/src/vite-env.d.ts` | Documents the optional dev-only environment flag |

## Non-Goals

SEC-008 does not add:

1. JWT or OAuth/OIDC libraries.
2. Token persistence.
3. Password login.
4. Logout or session refresh.
5. Backend authentication changes.
6. Database migrations.
7. Entity-scope management UI.
8. ERP, BI, or consolidation features.

## Validation

```powershell
cd C:\codex\budget-platform\frontend
pnpm type-check
pnpm lint
pnpm build
```

SEC-008 validation result:

1. `pnpm type-check`: passed.
2. `pnpm lint`: passed.
3. `pnpm build`: passed.
4. Production bundle was generated under ignored `frontend/dist`.

## Risks And Limits

1. Production authentication is still not implemented; production builds will depend on a future backend trusted principal adapter and login/session boundary.
2. Local development remains able to send role headers by default to preserve MVP testing speed.
3. Because SEC-007 disables backend header role trust by default, local non-test environments may need backend `BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES=true` or bootstrap admin configuration for full manual testing.
4. `/api/security/me` exists on the backend, but this stage does not redesign the frontend around it.

## Close Recommendation

SEC-008 can close when:

1. The shared HTTP client gates identity headers behind dev mode.
2. The role selector is not rendered in production builds.
3. Frontend type-check, lint, and build pass.
4. No backend business logic, migration, PDF/OCR content, external IdP, secret, ERP, BI, or consolidation scope is introduced.
