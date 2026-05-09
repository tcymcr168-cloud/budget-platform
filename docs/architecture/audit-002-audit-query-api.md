# AUDIT-002 Audit Query API

## Current Status

AUDIT-002 originally described audit reads as requiring `BUDGET_ADMIN` from the request header context. Current default authorization is superseded by `SEC-007`: request header roles are ignored unless explicitly enabled, and audit access should be evaluated through persisted roles or a controlled bootstrap admin path.

## Stage Goal

AUDIT-002 adds a minimal read-only audit query API for governance troubleshooting. It exposes persisted audit events with simple filters and pagination while avoiding BI dashboards, report builders, export pipelines, or delete capabilities.

## Endpoint

```http
GET /api/audit/events
```

Required headers:

| Header | Requirement |
| --- | --- |
| `X-User-Id` | Authenticated internal user context |
| `X-User-Roles` | Must include `BUDGET_ADMIN` |

Optional query parameters:

| Parameter | Purpose |
| --- | --- |
| `actorId` | Filter by acting user id |
| `subjectType` | Filter by subject category |
| `subjectId` | Filter by subject id |
| `action` | Filter by `AuditAction` enum |
| `page` | 0-based page number, default `0` |
| `size` | Page size, default `25`, maximum `100` |

## Response Shape

The endpoint returns the standard `ApiResponse` envelope with `PageResponse<AuditEventResponse>` data:

| Field | Purpose |
| --- | --- |
| `items` | Current page audit events |
| `page` | Current 0-based page |
| `size` | Page size |
| `totalElements` | Total matched rows |
| `totalPages` | Total pages |

Each audit event contains id, actor, subject, action, timestamp, and `detailsJson`.

## Security Decision

Audit search is restricted to `BUDGET_ADMIN` in the request header context. This keeps the first read model conservative and avoids prematurely designing multi-tenant audit visibility rules.

## Verification

Backend validation:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

Expected result:

```text
Tests run: 38, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Risks And Limits

1. No frontend page is provided in this stage.
2. No CSV export is provided; export should be a later explicit governance task.
3. `detailsJson` is returned as JSON text rather than a parsed object to keep the first API stable and minimal.
4. Audit search currently uses request-header admin validation; production identity remains a later security stage.

## Closure Recommendation

Close AUDIT-002 when query, authorization, invalid-parameter tests, full backend tests, and repository boundary checks pass.
