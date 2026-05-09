# SEC-005 Production Authentication Boundary

## Stage Goal

SEC-005 defines the production authentication boundary for the budget platform. It converts the current internal request-header security context into a staged production route without directly integrating an external identity provider in this stage.

This is a design stage only. It does not add JWT libraries, OAuth clients, SSO configuration, password storage, secrets, migrations, frontend login screens, or external service calls.

## Current State

| Area | Current Implementation | Production Gap |
| --- | --- | --- |
| Caller identity | `X-User-Id` request header | Header can be forged by any caller |
| Caller roles | `X-User-Roles` request header plus persisted workspace roles | Header role bypass is possible until trusted authentication exists |
| User registry | `app_user` table | No binding to external subject id or login session |
| Data scope | Workspace role and Entity scope tables | Depends on trusted user identity |
| Frontend | Internal security context selector | Not a login experience |
| Audit | Persistent `audit_event` table | Actor trust depends on current request header |

## Production Authentication Principles

1. Backend must be the trust boundary. The frontend can display identity, but must not be trusted to assert roles.
2. Roles and Entity scope remain application-owned. The identity provider authenticates the person; the budget platform authorizes budget actions.
3. The first production authentication stage should disable client-supplied role headers for normal browser/API traffic.
4. External identity integration must be isolated behind a small adapter so JWT, SSO, or reverse-proxy identity can be swapped later.
5. Local development must keep a clearly gated internal identity mode, never enabled accidentally in production.
6. Audit actor ids must come from the trusted authenticated principal once production auth is enabled.
7. No password storage should be introduced unless there is a deliberate local-account stage with hashing, rotation, and lockout rules.

## Target Identity Contract

The application should standardize on a trusted authenticated principal:

| Field | Purpose |
| --- | --- |
| `principalId` | Stable external subject id or local username |
| `username` | Normalized application username |
| `displayName` | Human-readable display name |
| `email` | Optional email |
| `authSource` | `LOCAL_DEV_HEADER`, `JWT`, `OIDC`, or `REVERSE_PROXY` |
| `roles` | Application roles resolved from `app_user_role`, not from client input |
| `requestId` | Trace id for audit and diagnostics |

The existing `CurrentUserContext` can evolve toward this contract without changing every business service at once.

## Recommended Production Route

### SEC-006: Trusted Principal Backend Adapter

Create a backend-only authentication adapter that resolves identity from one configured mode:

| Mode | Environment | Behavior |
| --- | --- | --- |
| `dev-header` | Local only | Accepts `X-User-Id`; ignores `X-User-Roles` except for bootstrap tests |
| `jwt` | Production candidate | Validates Bearer JWT signature, issuer, audience, expiry |
| `reverse-proxy` | Enterprise gateway candidate | Trusts headers only from a configured gateway network |

Output should remain a `CurrentUserContext`-compatible object.

### SEC-007: Remove Role Trust From Request Headers

After trusted principal resolution exists:

1. Resolve user by `username` or external subject mapping.
2. Load roles from `app_user_role`.
3. Keep `BUDGET_ADMIN` bootstrap only behind a local/test profile.
4. Reject direct `X-User-Roles` usage in production profile.

### SEC-008: Frontend Login Boundary

Add a real frontend authentication boundary:

1. Replace the internal role selector with current-user display.
2. Call `/api/security/me` to show the resolved user.
3. Redirect to IdP login or show unauthenticated state depending on selected auth mode.
4. Do not expose role override controls in production builds.

### SEC-009: Session And Token Operations

Define operational controls:

1. Token expiry and refresh behavior.
2. Logout behavior.
3. Clock skew tolerance.
4. CORS and cookie policy if browser cookies are used.
5. Failed authentication audit events.

## Explicit Non-Goals

The following are out of scope for SEC-005:

1. Implementing Spring Security.
2. Adding JWT or OAuth dependencies.
3. Creating login pages.
4. Storing passwords.
5. Calling GitHub, Azure AD, LDAP, SAP, ERP, or any external identity provider.
6. Adding secrets or environment-specific credentials.
7. Changing database schema.
8. Reworking existing business authorization rules.

## Security Decisions

| Decision | Rationale |
| --- | --- |
| Keep app-owned authorization | Budget responsibility and Entity scope are business concepts, not IdP groups |
| Avoid production trust in `X-User-Roles` | Client-supplied roles are suitable only for internal technical validation |
| Prefer external IdP over local passwords | Reduces password handling and enterprise rollout risk |
| Keep dev-header mode explicit | Maintains local test speed without weakening production posture |
| Design before implementation | Prevents accidental lock-in to one IdP or token shape |

## Migration Path From Current Headers

| Step | Current | Future |
| --- | --- | --- |
| User identity | `X-User-Id` | Authenticated principal subject |
| Roles | `X-User-Roles` plus DB roles | DB roles only |
| Entity scope | DB table | DB table unchanged |
| Frontend context | Manual selector | `/api/security/me` and IdP session |
| Audit actor | Header user id | Trusted principal id |

## Acceptance Criteria

SEC-005 is complete when:

1. The production authentication trust boundary is documented.
2. The staged implementation route is documented.
3. JWT/SSO/reverse-proxy options are separated from application authorization.
4. No business code, frontend code, migration, secrets, or external service integration is added.
5. Repository checks confirm no PDF/OCR/build artifacts are staged.

## Closure Recommendation

Close SEC-005 after documentation, README, project step record, and repository boundary checks pass.
