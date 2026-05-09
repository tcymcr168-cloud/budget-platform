# SEC-006 Trusted Principal Backend Adapter

## Stage Goal

SEC-006 introduces a minimal backend authentication mode adapter around the existing `CurrentUserContextResolver`. The purpose is to make the current internal header mode explicit and to prevent future production modes from silently trusting caller-supplied role headers.

This stage does not implement JWT validation, OAuth/OIDC flows, reverse-proxy network trust, frontend login screens, password storage, secrets, migrations, or external identity provider integration.

## Implemented Scope

| Area | Decision |
| --- | --- |
| Auth mode enum | `DEV_HEADER`, `JWT`, `REVERSE_PROXY` |
| Configuration | `budget-platform.auth.mode`, `allow-header-roles`, `reverse-proxy-user-header` |
| Default mode | `DEV_HEADER` for local MVP compatibility |
| Header role control | `allow-header-roles` controls whether `X-User-Roles` is parsed |
| JWT mode | Explicitly rejected until a real adapter is implemented |
| Reverse proxy mode | Explicitly rejected until trusted gateway rules are implemented |
| Tests | Resolver tests cover header role parsing, disabled header roles, and unsupported JWT mode |

## Configuration

```yaml
budget-platform:
  auth:
    mode: ${BUDGET_PLATFORM_AUTH_MODE:DEV_HEADER}
    allow-header-roles: ${BUDGET_PLATFORM_AUTH_ALLOW_HEADER_ROLES:true}
    reverse-proxy-user-header: ${BUDGET_PLATFORM_AUTH_REVERSE_PROXY_USER_HEADER:X-Forwarded-User}
```

## Behavior

### `DEV_HEADER`

`DEV_HEADER` reads:

1. `X-User-Id`
2. `X-User-Roles` only when `allow-header-roles=true`

This remains the local and test default.

### `JWT`

`JWT` currently fails closed with `UNAUTHORIZED`. This prevents operators from selecting JWT mode before signature, issuer, audience, and expiry validation exist.

### `REVERSE_PROXY`

`REVERSE_PROXY` currently fails closed with `UNAUTHORIZED`. This prevents trusting gateway headers before network boundary and header overwrite rules exist.

## Design Notes

1. Existing controllers remain unchanged because they already depend on `CurrentUserContextResolver`.
2. Existing business authorization remains unchanged.
3. This stage creates the production seam without pulling in Spring Security or JWT dependencies.
4. Future `SEC-007` can turn off role header trust by default and load roles only from `app_user_role`.

## Verification

Backend validation:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

Expected result:

```text
Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Risks And Limits

1. Production authentication is not complete.
2. `DEV_HEADER` still allows header roles by default for MVP continuity.
3. JWT and reverse-proxy modes intentionally reject requests until future stages implement real validation.
4. Audit actor ids still come from the resolved current user context.

## Closure Recommendation

Close SEC-006 when backend tests, repository ignore checks, diff checks, documentation, and stage record updates pass.
