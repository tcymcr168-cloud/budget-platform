# SEC-009 Session And Token Operations

## Goal

SEC-009 defines operational rules for future production authentication. It does not implement JWT, OAuth/OIDC, cookies, sessions, login pages, secrets, or reverse proxy trust. The purpose is to prevent the next implementation stage from inventing token behavior ad hoc.

## Target Authentication Modes

| Mode | Session carrier | Backend responsibility |
| --- | --- | --- |
| JWT bearer | `Authorization: Bearer <token>` | Validate signature, issuer, audience, expiry, clock skew, subject |
| OIDC browser session | Secure cookie or bearer token from gateway | Validate session or upstream-authenticated principal |
| Reverse proxy | Trusted gateway principal header | Trust only after gateway network and header overwrite rules are enforced |
| Dev header | `X-User-Id` in Vite dev/local tools | Local only; never a production authentication mode |

Application roles remain owned by this platform through `app_user_role`; authentication tokens must not be treated as budget authorization matrices.

## Token Validation Rules

Future JWT/OIDC implementation must validate:

1. Signature with configured JWKS or pinned public key.
2. `issuer` against a configured allow-list.
3. `audience` against the budget platform API audience.
4. `exp` and `nbf` with bounded clock skew.
5. Subject claim mapped to normalized `app_user.username` or a future external subject field.
6. Token algorithm allow-list; reject `none` and unexpected algorithms.
7. Token size limit before parsing to avoid oversized request abuse.

Recommended starting values:

| Control | Initial rule |
| --- | --- |
| Clock skew | 60 seconds |
| Access token lifetime | 5 to 15 minutes, controlled by IdP |
| Refresh token | Prefer IdP/session layer; do not store refresh tokens in this application until explicitly designed |
| JWKS cache | Cache with issuer TTL, refresh on key miss, fail closed on unavailable key refresh |
| User binding | Token subject must map to an active `app_user` |

## Browser Session Rules

If browser cookies are used later:

1. Use `HttpOnly`, `Secure`, and `SameSite=Lax` or stricter.
2. Never store bearer tokens in `localStorage` or `sessionStorage`.
3. Keep CSRF protection in scope for cookie-authenticated write requests.
4. Logout must clear application cookies and, when IdP supports it, redirect to IdP logout.
5. Session expiry must surface as `UNAUTHORIZED` with a stable error payload.

If bearer tokens are used later:

1. The frontend should not persist tokens in browser storage.
2. Token refresh should be delegated to the IdP SDK or gateway when possible.
3. API requests should attach bearer tokens through a single HTTP client boundary.
4. Role override UI must remain absent from production builds.

## CORS And Gateway Rules

Production authentication must define:

1. Allowed origins per environment.
2. Whether credentials are allowed.
3. Accepted authentication headers.
4. Rejection of direct client-supplied `X-User-Roles`.
5. For reverse proxy mode, stripping or overwriting all inbound identity headers before forwarding to the backend.
6. TLS termination owner and whether backend-to-proxy traffic is private.

## Failed Authentication Audit

The audit model already records business write events. Future authentication implementation should add security events without mixing them into budget facts:

| Event | Minimum fields |
| --- | --- |
| Token validation failure | timestamp, request id, failure reason category, issuer if parseable |
| Unknown user | timestamp, request id, normalized username or subject hash |
| Disabled user | timestamp, request id, username |
| Expired session | timestamp, request id, username if known |
| Logout | timestamp, request id, username |

Do not store raw tokens, secrets, or full PII-heavy IdP payloads in audit details.

## Secret And Key Operations

Future stages must follow these rules:

1. No secrets in Git.
2. Use environment variables or a managed secret store.
3. Document every required secret name, owner, rotation interval, and fallback behavior.
4. Key rotation must allow old and new signing keys during transition.
5. Missing or invalid key configuration must fail application startup or fail authentication closed.
6. Test keys must be separate from production keys.

## Error Contract

Authentication and authorization errors should remain stable:

| Case | HTTP | Error code |
| --- | --- | --- |
| Missing authentication | 401 | `UNAUTHORIZED` |
| Invalid token/session | 401 | `UNAUTHORIZED` |
| Authenticated but inactive user | 403 | `FORBIDDEN` |
| Authenticated but missing role/scope | 403 | `FORBIDDEN` |

The frontend should display concise errors and avoid exposing sensitive token validation internals.

## Non-Goals

SEC-009 does not implement:

1. Spring Security.
2. JWT/OIDC libraries.
3. Login/logout UI.
4. Cookie sessions.
5. Password accounts.
6. Database migrations.
7. External service calls.
8. Secrets or environment-specific credentials.
9. ERP, BI, or consolidation features.

## Verification

This is a documentation governance stage. Required repository checks:

```powershell
git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/
git diff --check
git status --short
```

## Close Recommendation

SEC-009 can close when the operational rules are documented, README and stage record are updated, repository boundary checks pass, and no code, migration, PDF/OCR, secret, external service, ERP, BI, or consolidation scope is introduced.
