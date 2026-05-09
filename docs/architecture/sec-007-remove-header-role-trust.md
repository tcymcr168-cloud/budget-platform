# SEC-007 Remove Header Role Trust

## Goal

SEC-007 removes request header roles from the default trusted authorization path. The backend may still read `X-User-Id` in `DEV_HEADER` mode for local MVP continuity, but `X-User-Roles` is now ignored unless explicitly enabled by configuration.

The production direction is:

1. Authentication identifies the user.
2. Workspace roles come from `app_user_role`.
3. Entity scope comes from `app_user_entity_scope`.
4. A bootstrap admin user list can unlock initial global administration before a production IdP is connected.

## Configuration

```yaml
budget-platform:
  auth:
    mode: ${BUDGET_PLATFORM_AUTH_MODE:DEV_HEADER}
    allow-header-roles: ${BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES:false}
    reverse-proxy-user-header: ${BUDGET_PLATFORM_AUTH_REVERSE_PROXY_USER_HEADER:X-Forwarded-User}
    bootstrap-admin-users: ${BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS:}
```

| Setting | Default | Purpose |
| --- | --- | --- |
| `mode` | `DEV_HEADER` | Keeps current MVP identity source explicit |
| `allow-header-roles` | `false` | Prevents client supplied roles from being trusted by default |
| `reverse-proxy-user-header` | `X-Forwarded-User` | Reserved for later reverse proxy mode |
| `bootstrap-admin-users` | empty | Explicit global admin allow-list for bootstrap only |

The test profile keeps `allow-header-roles=true` to preserve existing integration-test fixtures while the production default is tightened.

## Runtime Behavior

### Current User Context

`CurrentUserContextResolver` continues to normalize the caller user id from `X-User-Id` in `DEV_HEADER` mode.

`X-User-Roles` is parsed only when:

1. auth mode is `DEV_HEADER`, and
2. `budget-platform.auth.allow-header-roles=true`.

When `allow-header-roles=false`, the current context has an authenticated user id but no request supplied roles.

### Workspace Authorization

`AuthorizationService.rolesForWorkspace` now treats persisted roles as the baseline and merges request roles only when `allow-header-roles=true`.

With the default configuration, Workspace permissions come from:

1. `app_user`
2. `app_user_role`
3. `app_user_entity_scope`

Request header roles are not used.

### Bootstrap Global Admin

Some global endpoints do not have a Workspace id yet, such as Workspace creation and initial security user management. SEC-007 adds `bootstrap-admin-users` as an explicit allow-list for this bootstrap path.

This list:

1. Is empty by default.
2. Is normalized case-insensitively.
3. Does not grant Workspace roles.
4. Does not replace persisted Workspace authorization.
5. Should be removed or minimized once production authentication and initial admin provisioning are implemented.

## What Changed

| Area | Before SEC-007 | After SEC-007 |
| --- | --- | --- |
| Header role default | Trusted by default | Ignored by default |
| Workspace role resolution | DB roles plus header roles | DB roles only unless explicitly enabled |
| Global bootstrap admin | Header `BUDGET_ADMIN` | Header role only if explicitly enabled, or configured bootstrap user |
| Test fixture compatibility | Header roles | Preserved in `application-test.yml` |

## Non-Goals

SEC-007 does not implement:

1. JWT validation.
2. OAuth/OIDC login.
3. Password storage.
4. Frontend login pages.
5. Database schema changes.
6. External identity provider calls.
7. Secrets management.

## Validation

Expected validation:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

SEC-007 validation result:

1. Tests run: 45.
2. Failures: 0.
3. Errors: 0.
4. Skipped: 0.
5. Flyway successfully validated and applied 7 migrations in the H2 test schema.

## Risks And Limits

1. Production authentication is still not complete; `JWT` and `REVERSE_PROXY` modes remain failure-closed placeholders from SEC-006.
2. Local operators must set `BUDGET_PLATFORM_AUTH_BOOTSTRAP_ADMIN_USERS` or temporarily set `BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES=true` for controlled bootstrap usage.
3. Frontend still sends internal identity headers from SEC-004; this is acceptable only for local/internal MVP mode until SEC-008 replaces the UX boundary.
4. Existing older architecture documents mention request header roles as the initial MVP mechanism; SEC-007 supersedes that behavior for defaults.

## Close Recommendation

SEC-007 can close when:

1. `allow-header-roles` defaults to false.
2. `AuthorizationService` ignores request roles unless explicitly enabled.
3. Bootstrap admin users are explicit configuration.
4. Backend tests pass.
5. No migration, frontend UI, PDF/OCR, ERP, BI, or consolidation scope is introduced.
