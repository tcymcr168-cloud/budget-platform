# PERF-006 Repository Query Pushdown

## Stage Goal

`PERF-006` moves common budget fact filters from service-side stream filtering into repository-level JPA specifications. The first implementation focuses on fact rows used by `/api/budget-query/facts/page` and `/api/budget-query/facts.csv`, while preserving existing response contracts and authorization behavior.

This stage does not add database migrations, BI charts, ERP integration, consolidation reporting, async exports, or new business modules.

## Baseline Problem

Before this stage, budget fact query methods loaded all fact rows for a budget model and then applied filters in Java:

1. Entity member.
2. Time member.
3. Category member.
4. Version member.
5. Fact status.
6. Entity data scope for non-admin users.

That was acceptable for MVP correctness, but it made paged endpoints and capped CSV export still perform unbounded model-level reads.

## Implementation

`FactValueRepository` now extends `JpaSpecificationExecutor<FactValue>`.

`BudgetQueryService` builds one shared fact specification with:

1. Required `budgetModel.id`.
2. Optional entity, time, category, version, and status filters.
3. Entity data-scope predicate for non-admin users.
4. An always-false predicate when a non-admin user has no readable entity scope.

`/api/budget-query/facts/page` now uses:

```text
factValueRepository.findAll(specification, PageRequest)
```

`/api/budget-query/facts.csv` now uses the same specification and a first-page request with `limit`, so the database performs the capped row retrieval and count.

The legacy `/api/budget-query/facts` path and service-side summary path also reuse the same filtered fact retrieval helper. Summary and variance aggregation contracts are not changed in this stage.

## Sorting

Fact row sorting remains intentionally narrow:

| Sort field | Direction | Tie-breaker |
| --- | --- | --- |
| `updatedAt` | `ASC` or `DESC` | `id` in the same direction |

Business-field sorts such as account code or amount remain out of scope until joins and index strategy are designed explicitly.

## Test Coverage

Budget query integration tests cover:

1. Existing facts query behavior.
2. Existing facts page defaults.
3. Repository-backed paging with `size=1`, total rows, and page 1.
4. Existing invalid page parameter behavior.
5. Existing CSV cap and invalid limit behavior.
6. Existing Entity data-scope behavior.

## Risks And Limits

1. H2 integration tests validate query correctness, but PostgreSQL execution plans are still not captured.
2. Summary aggregation still happens in service memory after filtered row retrieval.
3. Variance aggregation remains service-side and still reads model rows before variance-side filtering.
4. No index migration was added; PostgreSQL indexing should be designed in a separate stage.

## Stage Closure Recommendation

Close `PERF-006` when targeted budget query tests and full backend tests pass, repository guardrails pass, and Git status confirms no migration, PDF/OCR, dependency directory, or build output is staged.
