# PERF-002 Budget Facts Pagination Backend

## Stage Goal

`PERF-002` adds the first backend pagination capability for budget facts while preserving the existing `/api/budget-query/facts` list endpoint for frontend compatibility. This stage does not change frontend runtime code, migrations, summary query, variance query, CSV export, ERP integration, BI charts, consolidation reporting, PDF/OCR assets, or secrets.

## Endpoint Added

```http
GET /api/budget-query/facts/page
```

The endpoint returns:

```text
ApiResponse<PageResponse<FactQueryResponse>>
```

The original endpoint remains unchanged:

```http
GET /api/budget-query/facts
```

It still returns:

```text
ApiResponse<List<FactQueryResponse>>
```

This compatibility bridge avoids breaking the current React query screen before `PERF-003` updates the frontend.

## Query Parameters

The paged endpoint supports the same business filters as `/facts`:

| Parameter | Required | Notes |
| --- | --- | --- |
| `budgetModelId` | Yes | Budget model scope |
| `entityMemberId` | No | Entity filter; still constrained by user data scope |
| `timeMemberId` | No | Time filter |
| `categoryMemberId` | No | Category filter |
| `versionMemberId` | No | Version filter |
| `status` | No | Fact value status |

Pagination parameters:

| Parameter | Default | Constraint |
| --- | --- | --- |
| `page` | `0` | Must be `>= 0` |
| `size` | `25` | Must be between `1` and `100` |
| `sort` | `updatedAt` | Only `updatedAt` is enabled in this first stage |
| `direction` | `DESC` | `ASC` or `DESC` |

Unsupported sort fields and invalid directions return `400 BAD_REQUEST`.

## Backend Implementation

| File | Purpose |
| --- | --- |
| `PageRequestSpec` | Central page/size/sort/direction validation and in-memory slice helper |
| `PageResponse.fromList` | Creates `PageResponse<T>` from an already materialized bounded page |
| `FactValue.getUpdatedAt` | Exposes stable sort source for fact pagination |
| `BudgetQueryController.queryFactsPage` | Adds compatibility-safe paged facts endpoint |
| `BudgetQueryService.queryFactsPage` | Filters by current authorization/data-scope rules, sorts by `updatedAt`, slices, and maps to response rows |

The first implementation still performs service-side filtering because repository pushdown is intentionally reserved for `PERF-006`. The immediate value is a bounded API response and validated request contract without breaking existing UI behavior.

## Authorization And Scope

The paged endpoint uses the same authorization path as `/facts`:

1. Resolve current user context.
2. Load budget model.
3. Require one of the existing budget read roles.
4. Apply Entity data scope for non-admin users.

No new role model, permission matrix, token storage, or workflow rule is introduced.

## Test Coverage

`BudgetQueryControllerIntegrationTests` now covers:

1. Existing `/facts` list behavior remains unchanged.
2. `/facts/page` returns one item, default `page=0`, `size=25`, `totalElements=1`, `totalPages=1`.
3. Negative page is rejected.
4. Size above `100` is rejected.
5. Unsupported sort field is rejected.
6. Unsupported sort direction is rejected.

Validation results:

```text
mvn "-Dtest=com.budgetplatform.budgetquery.api.BudgetQueryControllerIntegrationTests" test
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
```

```text
mvn test
Tests run: 71, Failures: 0, Errors: 0, Skipped: 0
```

## Limits

1. `/facts/page` still uses service-side filtering and slicing.
2. Only `updatedAt` sort is enabled in this stage.
3. The frontend still uses `/facts` until `PERF-003`.
4. Summary, variance, and CSV export are intentionally unchanged.
5. No PostgreSQL execution-plan validation was performed in this stage.

## Next Recommendation

Proceed to `PERF-003`: update the frontend budget facts query to call `/api/budget-query/facts/page`, consume `PageResponse<FactQueryRow>`, and add simple previous/next controls while keeping summary and variance unchanged.
