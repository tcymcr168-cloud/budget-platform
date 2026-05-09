# PERF-008 Fact Query Index Migration

## Stage Goal

`PERF-008` adds one narrow Flyway migration for the most common budget facts query pattern after `PERF-006` introduced repository-level filtering and `PERF-007` defined index governance.

This stage intentionally adds only one index and does not modify backend runtime code, frontend code, PDF/OCR assets, BI charts, ERP integration, consolidation reporting, or async exports.

## Migration

File:

```text
backend/src/main/resources/db/migration/V9__fact_query_index.sql
```

Index:

```sql
CREATE INDEX idx_fact_value_model_status_updated ON fact_value (
    budget_model_id,
    value_status,
    updated_at DESC,
    id DESC
);
```

## Why This Index

The current facts page and CSV export paths commonly filter by:

1. `budget_model_id`.
2. `value_status`.

They also sort by:

1. `updated_at DESC`.
2. `id DESC`.

This index is narrower than a full dimensional scope index and directly supports the default facts page and status-filtered CSV export paths.

## Out Of Scope

1. Wide dimensional scope indexes.
2. Summary or variance database aggregation.
3. Query sort by account/entity/time/category/version code.
4. BI charts or dashboards.
5. ERP direct connection.
6. Consolidation reporting.
7. Async export jobs.

## Validation

Required validation:

1. Targeted budget query integration tests.
2. Full backend `mvn test`.
3. Flyway migration validation under H2 test profile.
4. `.gitignore` protection for PDF, OCR, build output, and dependency directories.
5. `git diff --check`.

## Rollback Note

If the index causes unexpected write overhead in PostgreSQL, rollback should be a controlled migration in a later stage:

```sql
DROP INDEX idx_fact_value_model_status_updated;
```

Do not manually drop the index outside Flyway in governed environments.

## Remaining Work

1. Capture PostgreSQL `EXPLAIN (ANALYZE, BUFFERS)` on representative data.
2. Decide whether entity-scoped workloads need a second, wider index.
3. Keep summary and variance aggregation optimization as separate stages.
