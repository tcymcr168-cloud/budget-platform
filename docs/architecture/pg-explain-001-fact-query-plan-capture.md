# PG-EXPLAIN-001 Fact Query Plan Capture

## Stage Goal

`PG-EXPLAIN-001` turns the `PERF-007` PostgreSQL execution-plan checklist into a repeatable local script for the `fact_value` facts page query shape after `PERF-008` added `idx_fact_value_model_status_updated`.

This stage does not modify backend runtime code, frontend code, Flyway migrations, PDF/OCR assets, BI charts, ERP integration, consolidation reporting, or business features.

## Local PostgreSQL Status

Observed on 2026-05-09:

| Check | Result |
| --- | --- |
| `psql --version` | `psql (PostgreSQL) 16.13` |
| Windows service | `postgresql-x64-16` is `Running` |
| Project default URL | `jdbc:postgresql://localhost:5432/budget_platform` |
| Project default username | `budget_platform` |
| Default credential probe | Failed: password authentication failed for user `budget_platform` |

The repository must not store database passwords. Use `PGPASSWORD`, `.pgpass`, or an operator-provided connection string when running the script.

## Script

File:

```text
tools/postgres_fact_query_explain.sql
```

Minimal usage:

```powershell
cd C:\codex\budget-platform
$env:PGPASSWORD = '<local password>'
psql -h localhost -p 5432 -U budget_platform -d budget_platform `
  -v budget_model_id='<budget-model-uuid>' `
  -v value_status='APPROVED' `
  -v limit_rows=25 `
  -v offset_rows=0 `
  -f tools/postgres_fact_query_explain.sql
```

Fully scoped usage:

```powershell
cd C:\codex\budget-platform
$env:PGPASSWORD = '<local password>'
psql -h localhost -p 5432 -U budget_platform -d budget_platform `
  -v budget_model_id='<budget-model-uuid>' `
  -v entity_member_id='<entity-member-uuid>' `
  -v time_member_id='<time-member-uuid>' `
  -v category_member_id='<category-member-uuid>' `
  -v version_member_id='<version-member-uuid>' `
  -v value_status='APPROVED' `
  -v limit_rows=25 `
  -v offset_rows=0 `
  -f tools/postgres_fact_query_explain.sql
```

Do not commit local passwords, `.pgpass`, captured query output containing sensitive data, or local database dumps.

## What The Script Captures

1. Current `fact_value` index definitions from `pg_indexes`.
2. Row count for the selected `budget_model_id` and `value_status`.
3. `EXPLAIN (ANALYZE, BUFFERS)` for the status-filtered facts page query:
   `budget_model_id + value_status + updated_at DESC + id DESC + LIMIT/OFFSET`.
4. Optional `EXPLAIN (ANALYZE, BUFFERS)` for the fully scoped facts page query when all dimension member ids are supplied.

## Review Checklist

When reading the output, record:

1. Whether `idx_fact_value_model_status_updated` is used.
2. Whether PostgreSQL still performs an explicit `Sort`.
3. Rows scanned versus rows returned.
4. Shared buffer hits and reads.
5. Planning time and execution time.
6. Whether the fully scoped query prefers `idx_fact_value_model_scope` or the status-aware index.

## Data Readiness

The script is most useful when the database contains representative facts:

1. At least one budget model with approved facts.
2. Enough rows to make scan and sort costs visible.
3. A mix of statuses such as `DRAFT`, `SUBMITTED`, `APPROVED`, and `LOCKED`.
4. A scoped Entity/Time/Category/Version combination that exists in `fact_value`.

If local data is sparse, treat the output as a smoke check only and defer performance conclusions.

## Risks And Limits

1. The local default credential currently fails authentication, so actual plan capture requires operator-provided credentials.
2. The script runs `EXPLAIN ANALYZE`, which executes the selected read queries. It does not mutate data.
3. A small local database may not produce production-like plans.
4. Captured output may include row counts and schema details; review before sharing outside the project.

## Next Recommendations

1. Run the script against a local or staging database with representative facts.
2. Store only summarized plan findings in project docs, not raw sensitive output.
3. If plans still show avoidable sort or broad scans, create a separate performance stage for the next index or query-shape adjustment.
