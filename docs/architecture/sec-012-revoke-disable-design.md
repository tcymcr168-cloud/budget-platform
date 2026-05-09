# SEC-012 Authorization Revoke And User Disable Design

## Stage Goal

SEC-012 defines the minimum governance design for revoking Workspace roles, revoking Entity scopes, and disabling users. This is a design stage only.

This stage does not implement delete endpoints, physical deletion, database migrations, backend code, frontend code, external identity integrations, JWT/OIDC validation, ERP integration, BI charts, consolidation reports, PDF handling, or OCR outputs.

## Current State

| Area | Current capability | Gap |
| --- | --- | --- |
| User lifecycle | Create and list `app_user` | No disable/enable action |
| Workspace roles | Grant and list `app_user_role` | No revoke action |
| Entity scopes | Grant and list `app_user_entity_scope` | No revoke action |
| Audit | Records grants and auth failures | No revoke/disable events yet |
| Frontend | Can create users and grant roles/scopes | Cannot revoke or disable |

## Design Principles

1. Do not physically delete security records in the MVP user flow.
2. Prefer explicit disable/revoke actions with audit over silent deletion.
3. Keep authorization simple: Workspace roles and Entity scopes remain app-owned.
4. Avoid complex multi-dimensional permission matrices.
5. Keep rollback possible: disabled users and revoked grants should be inspectable in audit.
6. Do not remove historical audit actor references.
7. Do not cascade-delete budget facts, submissions, imports, templates, or metadata.

## Recommended Data Model Direction

The current tables already include user status and grant timestamps. Two possible implementation paths exist:

| Path | Description | Migration needed | Recommendation |
| --- | --- | --- | --- |
| A: Physical delete grants | Delete `app_user_role` and `app_user_entity_scope` rows on revoke | No | Not recommended for MVP governance because it loses current-state history outside audit |
| B: Soft revoke grants | Add `status`, `revoked_at`, `revoked_by`, `revoked_reason` to grant tables | Yes | Recommended when implementation enters SEC-013 |
| C: Minimal no-migration revoke | Delete grant rows but require `ACCESS_CHANGE` audit details | No | Acceptable only if schema changes are deferred |

Recommended path: implement user disable first with the existing `app_user.status`, then implement soft revoke with a small migration in a separate approved implementation stage.

## User Disable Rules

| Rule | Decision |
| --- | --- |
| Disable action | Set `app_user.status=INACTIVE` |
| Enable action | Optional later action to set `ACTIVE` |
| Authorization | `BUDGET_ADMIN` bootstrap or Workspace admin depending endpoint scope |
| Effect on login/current user | Authenticated inactive users should receive `FORBIDDEN` for protected business operations |
| Existing audit history | Remains untouched |
| Existing role/scope rows | Remain, but inactive user cannot use them |

Current code already has `SecurityUserStatus`, but authorization currently does not reject inactive users. Implementation should add this check before role/scope evaluation.

## Role Revoke Rules

| Rule | Decision |
| --- | --- |
| Revoke target | One role grant id or `(userId, workspaceId, roleCode)` |
| Authorization | `BUDGET_ADMIN` for the target Workspace |
| Audit action | `ACCESS_CHANGE` |
| Audit details | username, workspaceId, roleCode, revokeReason |
| Physical delete | Avoid in recommended implementation |
| Soft revoke fields | `status`, `revoked_at`, `revoked_by`, `revoked_reason` |

## Entity Scope Revoke Rules

| Rule | Decision |
| --- | --- |
| Revoke target | One Entity scope grant id |
| Authorization | `BUDGET_ADMIN` for the target Workspace |
| Audit action | `ACCESS_CHANGE` |
| Audit details | username, workspaceId, entityMemberId, includeDescendants, revokeReason |
| Physical delete | Avoid in recommended implementation |
| Soft revoke fields | `status`, `revoked_at`, `revoked_by`, `revoked_reason` |

## API Design Candidate

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/security/users/{userId}/disable` | Disable user |
| `POST` | `/api/security/users/{userId}/enable` | Re-enable user |
| `POST` | `/api/security/users/{userId}/roles/{roleId}/revoke` | Revoke one Workspace role |
| `POST` | `/api/security/users/{userId}/entity-scopes/{scopeId}/revoke` | Revoke one Entity scope |

Use `POST` action endpoints instead of `DELETE` for MVP governance because the project requires explicit deletion authorization and audit-friendly semantics.

Suggested request body:

```json
{
  "reason": "Role no longer needed"
}
```

## Frontend Design Candidate

1. Add disable/enable button to selected user panel.
2. Add revoke action to each listed Workspace role row.
3. Add revoke action to each listed Entity scope row.
4. Require a short reason input before submitting action.
5. Refresh current user, user list, roles, scopes, and audit results after success.
6. Do not add bulk revoke in MVP.

## Minimum Implementation Split

| Stage | Scope |
| --- | --- |
| `SEC-013` | Backend user disable/enable using existing `app_user.status`, no migration |
| `SEC-014` | Backend inactive-user authorization enforcement and tests |
| `SEC-015` | Grant soft revoke schema and backend APIs; migration allowed in that stage |
| `SEC-016` | Frontend disable/revoke MVP |
| `AUDIT-005` | Audit query polish for security lifecycle events, no BI |

## Acceptance Criteria For Future Implementation

1. Inactive users cannot perform protected operations.
2. Disable/enable actions are audited.
3. Role/scope revoke actions are audited.
4. No budget facts, submissions, templates, imports, metadata, or audit records are deleted.
5. Frontend does not expose physical delete as a normal workflow.
6. Tests cover disabled user, revoked role, revoked Entity scope, duplicate grant, and audit details.

## Risks And Limits

1. Soft revoke requires migration, so it must be a separate implementation stage.
2. User disable can be implemented first without schema changes, but role/scope grants would remain visible.
3. If physical delete is ever chosen as a temporary path, the phase must explicitly document the tradeoff and audit coverage.
4. This design does not integrate with external IdP account disabling; production IdP lifecycle remains outside the current application boundary.

## Close Recommendation

Close SEC-012 when the revoke/disable design, implementation split, README, project step record, and repository checks are complete, with no code, migration, PDF/OCR, secret, ERP, BI, or consolidation scope introduced.
