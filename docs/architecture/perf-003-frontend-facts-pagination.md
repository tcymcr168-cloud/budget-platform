# PERF-003 Frontend Facts Pagination

## Stage Goal

`PERF-003` updates only the React budget facts query experience to consume the paged backend endpoint added in `PERF-002`. It does not change backend APIs, migrations, summary query, variance query, CSV export, PDF/OCR assets, ERP integration, BI charts, or consolidation reporting.

## Frontend Contract

The frontend now calls:

```http
GET /api/budget-query/facts/page
```

with:

| Parameter | Value |
| --- | --- |
| `page` | Current facts page, 0-based |
| `size` | `25` from local page state |
| business filters | Same Entity, Time, Category, Version, Status filters as before |

The response is consumed as:

```text
PageResponse<FactQueryRow>
```

## UX Behavior

1. The Query button fetches page `0` for the current filters.
2. The Results heading shows `totalElements` instead of only current-page row count.
3. Previous and Next buttons navigate facts pages.
4. Page display uses `Page current / total`.
5. Workspace or budget model changes reset facts pagination to an empty page.

Summary, variance, and CSV remain unchanged in this stage.

## Files Changed

| File | Change |
| --- | --- |
| `frontend/src/features/budgetQuery/budgetQueryApi.ts` | Adds `PageResponse`, `FactQueryPage`, `FactQueryPageInput`, and `queryFactsPage` |
| `frontend/src/App.tsx` | Replaces facts array state with paged state and adds Previous/Next controls |

## Validation

Frontend validation passed:

```text
pnpm type-check
```

```text
pnpm lint
```

```text
pnpm build
```

The build produced `frontend/dist` assets, which are ignored and must not be committed.

## Limits

1. The page size is fixed at `25` in this stage.
2. The only backend sort currently enabled is `updatedAt DESC`.
3. Summary, variance, and CSV export are still unpaged.
4. No browser automation was introduced.

## Next Recommendation

Proceed to `PERF-004`: summary and variance paged response contracts. That stage should be split carefully because it changes grouped query response shapes and requires both backend and frontend updates.
