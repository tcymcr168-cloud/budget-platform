# PERF-004 Grouped Query Pagination

## Stage Goal

`PERF-004` adds paged backend and frontend contracts for budget summary and Budget vs Actual variance results. Existing unpaged endpoints remain available for compatibility. This stage does not change migrations, CSV export, ERP integration, BI charts, consolidation reporting, PDF/OCR assets, or secrets.

## Endpoints Added

Summary:

```http
GET /api/budget-query/summary/page
```

Variance:

```http
GET /api/budget-query/variance/page
```

Both endpoints return:

```text
ApiResponse<PageResponse<T>>
```

Existing endpoints remain unchanged:

```http
GET /api/budget-query/summary
GET /api/budget-query/variance
```

## Page Parameters

| Parameter | Default | Constraint |
| --- | --- | --- |
| `page` | `0` | Must be `>= 0` |
| `size` | `25` | Must be `1..100` |
| `direction` | Endpoint default | `ASC` or `DESC` |

Summary sort allow-list:

| Sort | Default | Notes |
| --- | --- | --- |
| `memberCode` | Yes | Preserves current deterministic grouped result order |
| `totalAmount` | No | Useful for amount-focused review |
| `lineCount` | No | Useful for density-focused review |

Variance sort allow-list:

| Sort | Default | Notes |
| --- | --- | --- |
| `accountCode` | Yes | Preserves current deterministic order |
| `entityCode` | No | Ownership-oriented review |
| `timeCode` | No | Period-oriented review |
| `budgetAmount` | No | Budget-heavy review |
| `actualAmount` | No | Actual-heavy review |
| `varianceAmount` | No | Exception review |
| `variancePercent` | No | Handles null percent values explicitly |

## Frontend Behavior

1. Summary uses paged state and calls `/api/budget-query/summary/page`.
2. Variance uses paged state and calls `/api/budget-query/variance/page`.
3. Query submit actions fetch page `0`.
4. Previous and Next controls navigate grouped result pages.
5. Workspace and budget model changes reset facts, summary, and variance pages.
6. Facts pagination from `PERF-003` remains unchanged.
7. CSV export remains unchanged.

## Tests

Backend targeted validation:

```text
mvn "-Dtest=com.budgetplatform.budgetquery.api.BudgetQueryControllerIntegrationTests" test
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
```

Full backend validation:

```text
mvn test
Tests run: 74, Failures: 0, Errors: 0, Skipped: 0
```

Frontend validation:

```text
pnpm type-check
pnpm lint
pnpm build
```

All passed.

## Limits

1. Summary and variance pagination still happen after service-layer grouping.
2. The database still loads facts for the model before filtering and grouping.
3. CSV export remains uncapped until `PERF-005`.
4. No PostgreSQL execution-plan validation is included in this stage.
5. No browser automation was introduced.

## Next Recommendation

Proceed to `PERF-005`: CSV export cap and truncation signal. That stage should prevent unbounded text export while preserving the lightweight CSV preview pattern.
