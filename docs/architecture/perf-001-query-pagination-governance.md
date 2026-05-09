# PERF-001 Query Pagination And Performance Governance

## Stage Goal

`PERF-001` defines the pagination, sorting, row-limit, and performance guardrails for MVP query endpoints. It is a design and governance stage only. It does not change runtime API behavior, frontend code, backend production code, database migrations, PDF/OCR assets, ERP integration, BI charts, or consolidation reporting.

## Current Baseline

Audit query already has a conservative page contract:

| Area | Current state |
| --- | --- |
| Endpoint | `GET /api/audit/events` |
| Response | `ApiResponse<PageResponse<AuditEventResponse>>` |
| Page parameters | `page`, `size` |
| Default size | `25` |
| Max size | `100` |
| Sort | `occurredAt DESC` |
| Data access | Repository-level `Pageable` |

Budget query endpoints are still MVP list endpoints:

| Endpoint | Current response | Current risk |
| --- | --- | --- |
| `GET /api/budget-query/facts` | `ApiResponse<List<FactQueryResponse>>` | Reads all facts for a model, then filters in service memory |
| `GET /api/budget-query/summary` | `ApiResponse<List<FactSummaryResponse>>` | Reads all facts for a model, then aggregates in service memory |
| `GET /api/budget-query/facts.csv` | `text/csv` | Exports all matching facts without an explicit export cap |
| `GET /api/budget-query/variance` | `ApiResponse<List<BudgetActualVarianceResponse>>` | Reads all facts for a model, then filters and aggregates in service memory |

## Governance Decisions

1. All list-like query endpoints must have explicit limits before the platform is used with production-sized data.
2. New list endpoints should return `PageResponse<T>` unless they are intentionally bounded lookup lists.
3. Existing budget query endpoints should migrate in small compatibility-aware stages instead of one large breaking change.
4. Query default page size should be `25`; maximum page size should be `100`, aligned with the audit query baseline.
5. API page indexes are `0`-based.
6. Unsupported sort fields must fail with `400 BAD_REQUEST` instead of silently changing query semantics.
7. CSV export must not become an unbounded report engine; it needs an explicit row cap and a later asynchronous export design if larger exports are required.
8. Query work must remain table-oriented. This stage does not authorize BI charts, dashboards, consolidation reporting, ERP direct connection, or advanced budget execution analytics.

## Target Contracts

### Shared Page Parameters

| Parameter | Default | Constraint | Notes |
| --- | --- | --- | --- |
| `page` | `0` | `>= 0` | 0-based page index |
| `size` | `25` | `1..100` | Max must stay centralized and tested |
| `sort` | endpoint-specific | allow-list only | Format: one logical field |
| `direction` | endpoint-specific | `ASC` or `DESC` | Reject unknown values |

### Budget Facts

Target endpoint shape:

```http
GET /api/budget-query/facts?page=0&size=25&sort=updatedAt&direction=DESC
```

Target response:

```text
ApiResponse<PageResponse<FactQueryResponse>>
```

Allowed sort fields:

| Logical field | Default | Notes |
| --- | --- | --- |
| `updatedAt` | yes | Stable newest-first fact review |
| `accountCode` | no | Requires join-aware implementation |
| `entityCode` | no | Requires join-aware implementation |
| `timeCode` | no | Requires join-aware implementation |
| `categoryCode` | no | Requires join-aware implementation |
| `versionCode` | no | Requires join-aware implementation |
| `amount` | no | Useful for exception review |

Implementation sequence should start with `updatedAt DESC` only, then add business sort fields after repository queries are made explicit and tested.

### Budget Summary

Summary returns grouped rows. A grouped query can still grow beyond a screen, so it needs a bounded response even if the aggregation itself remains service-side for MVP.

Target endpoint shape:

```http
GET /api/budget-query/summary?groupBy=ACCOUNT&page=0&size=25&sort=memberCode&direction=ASC
```

Target response:

```text
ApiResponse<PageResponse<FactSummaryResponse>>
```

Allowed sort fields:

| Logical field | Default | Notes |
| --- | --- | --- |
| `memberCode` | yes | Current deterministic order |
| `totalAmount` | no | Useful for spend/revenue review |
| `lineCount` | no | Useful for density review |

The first implementation can keep service-side aggregation but must slice the final grouped rows into a `PageResponse`. A later database aggregation stage can push grouping into SQL when volume justifies it.

### Budget Vs Actual Variance

Variance returns grouped Account + Entity + Time rows and should be bounded independently from facts.

Target endpoint shape:

```http
GET /api/budget-query/variance?page=0&size=25&sort=accountCode&direction=ASC
```

Target response:

```text
ApiResponse<PageResponse<BudgetActualVarianceResponse>>
```

Allowed sort fields:

| Logical field | Default | Notes |
| --- | --- | --- |
| `accountCode` | yes | Preserve current deterministic order |
| `entityCode` | no | Useful when reviewing ownership |
| `timeCode` | no | Useful for period review |
| `varianceAmount` | no | Useful for exception review |
| `variancePercent` | no | Null handling must be explicit |
| `actualAmount` | no | Useful for actual-heavy review |
| `budgetAmount` | no | Useful for budget-heavy review |

The first implementation should preserve current default order: `accountCode ASC`, then `entityCode ASC`, then `timeCode ASC`.

### CSV Export

CSV export remains a lightweight verification tool, not a report engine.

Target guardrails:

| Decision | Value |
| --- | --- |
| Default export cap | `1000` rows |
| Maximum export cap | `5000` rows |
| Default sort | `updatedAt DESC` |
| If more rows exist | Return a clear footer/comment or response header in a later implementation |
| Larger exports | Separate async export stage, not part of MVP query endpoint |

`PERF-001` does not implement the cap. It only defines the guardrail so a later implementation can change behavior intentionally and test it.

## Implementation Roadmap

| Stage | Scope | Runtime change |
| --- | --- | --- |
| `PERF-002` | Backend shared page/sort request validation helper and budget facts pagination | Yes, one endpoint family |
| `PERF-003` | Frontend facts query pagination UI and API type update | Yes, frontend only for facts |
| `PERF-004` | Summary and variance `PageResponse` contracts | Yes, backend and frontend for grouped query results |
| `PERF-005` | CSV export cap and user-visible truncation signal | Yes, backend and frontend |
| `PERF-006` | Repository-level query pushdown for common filters | Yes, backend query performance |

Each implementation stage must keep E2E-001 smoke passing or update it deliberately if the response shape changes.

## Testing Requirements

Implementation stages must cover:

1. Default page and size.
2. Negative page rejection.
3. Size below 1 rejection.
4. Size above max rejection.
5. Unsupported sort rejection.
6. Stable default order.
7. Authorization behavior unchanged.
8. Entity data scope unchanged.
9. E2E smoke compatibility or deliberate update.

When frontend behavior changes, run:

```powershell
cd C:\codex\budget-platform\frontend
pnpm type-check
pnpm lint
pnpm build
```

When backend behavior changes, run:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

## Risks And Limits

1. Changing `List<T>` to `PageResponse<T>` is a contract change for existing frontend calls.
2. Service-side grouped pagination still reads the full fact set; it controls response size but not database load.
3. True performance work needs repository-level filter pushdown and PostgreSQL execution-plan validation.
4. CSV caps can surprise users if not clearly surfaced in the UI.
5. Sort allow-lists must not expose arbitrary entity field names.

## Stage Conclusion

`PERF-001` should close when this design, README pointer, and `PROJECT_STEP_RECORD.md` are updated and repository guardrails pass. Runtime implementation should start with `PERF-002`, limited to backend budget facts pagination and shared request validation.
