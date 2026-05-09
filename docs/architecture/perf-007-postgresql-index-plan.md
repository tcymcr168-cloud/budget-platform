# PERF-007 PostgreSQL Index And Explain Plan Governance

## Stage Goal

`PERF-007` defines the PostgreSQL index and execution-plan governance for budget fact queries after `PERF-006` pushed common filters into repository-level specifications.

This is a design-only stage. It does not add a Flyway migration, alter production schema, modify backend code, modify frontend code, or introduce BI, ERP, consolidation, async exports, or new business modules.

## Current Schema Baseline

Existing `fact_value` indexes:

| Index | Columns | Source |
| --- | --- | --- |
| `uk_fact_value_submission_account` | `submission_task_id`, `account_member_id` | `V4__budget_submission_baseline.sql` |
| `idx_fact_value_task_status` | `submission_task_id`, `value_status` | `V4__budget_submission_baseline.sql` |
| `idx_fact_value_model_scope` | `budget_model_id`, `entity_member_id`, `time_member_id`, `category_member_id`, `version_member_id` | `V4__budget_submission_baseline.sql` |
| `idx_fact_value_import_batch` | `import_batch_id` | `V5__actual_import_baseline.sql` |

Current query shape from `PERF-006`:

1. Required `budget_model_id`.
2. Optional `entity_member_id`.
3. Optional `time_member_id`.
4. Optional `category_member_id`.
5. Optional `version_member_id`.
6. Optional `value_status`.
7. Optional Entity scope list for non-admin users.
8. Sort by `updated_at`, then `id`.

## Observations

`idx_fact_value_model_scope` supports exact or prefix filtering on budget model and dimension scope, but it does not include:

1. `value_status`.
2. `updated_at`.
3. `id` tie-breaker.

That means the database may still need extra filtering or sorting for the common facts/page and CSV export paths.

## Candidate Indexes

### Candidate A: Default Facts Page

```sql
CREATE INDEX idx_fact_value_model_updated
    ON fact_value (budget_model_id, updated_at DESC, id DESC);
```

Use case:

1. Admin facts page without dimension filters.
2. CSV export with only model/status omitted.
3. Stable newest-first review.

Risk:

1. Does not help status-heavy queries as much as a status-aware index.
2. Adds write overhead to all fact inserts/updates.

### Candidate B: Status-Aware Facts Page

```sql
CREATE INDEX idx_fact_value_model_status_updated
    ON fact_value (budget_model_id, value_status, updated_at DESC, id DESC);
```

Use case:

1. Most UI reads currently pass `status=APPROVED` or inspect a bounded status.
2. CSV export usually uses status filters.
3. Keeps sort aligned with page order.

Risk:

1. If status is omitted often, this may be less useful than Candidate A.
2. Needs plan comparison against Candidate A before both are accepted.

### Candidate C: Scope-And-Status Facts Page

```sql
CREATE INDEX idx_fact_value_model_scope_status_updated
    ON fact_value (
        budget_model_id,
        entity_member_id,
        time_member_id,
        category_member_id,
        version_member_id,
        value_status,
        updated_at DESC,
        id DESC
    );
```

Use case:

1. Highly filtered facts page.
2. Entity-scoped non-admin users.
3. CSV export with complete dimensional context.

Risk:

1. Wide index.
2. Potential overlap with existing `idx_fact_value_model_scope`.
3. May not be worth the write overhead until real cardinality is known.

## Recommended Sequence

1. Do not add all candidates at once.
2. Start with plan capture using current schema.
3. Compare Candidate A and Candidate B on representative data.
4. Prefer one narrow index first.
5. Defer Candidate C until entity-scoped workloads show a measurable need.

Initial likely choice for a future migration:

```sql
CREATE INDEX idx_fact_value_model_status_updated
    ON fact_value (budget_model_id, value_status, updated_at DESC, id DESC);
```

Reason: current UI and tests commonly query approved facts, and this index aligns with the default paging sort.

## Explain Plan Checklist

For PostgreSQL validation, capture:

```sql
EXPLAIN (ANALYZE, BUFFERS)
SELECT *
FROM fact_value
WHERE budget_model_id = :budget_model_id
  AND value_status = 'APPROVED'
ORDER BY updated_at DESC, id DESC
LIMIT 25 OFFSET 0;
```

Also capture:

```sql
EXPLAIN (ANALYZE, BUFFERS)
SELECT *
FROM fact_value
WHERE budget_model_id = :budget_model_id
  AND entity_member_id = :entity_member_id
  AND time_member_id = :time_member_id
  AND category_member_id = :category_member_id
  AND version_member_id = :version_member_id
  AND value_status = 'APPROVED'
ORDER BY updated_at DESC, id DESC
LIMIT 25 OFFSET 0;
```

Record:

1. Whether index scan or bitmap index scan is used.
2. Whether an explicit sort remains.
3. Rows scanned vs rows returned.
4. Shared buffer hits/reads.
5. Planning and execution time.
6. Impact on inserts/updates if benchmark data exists.

## Migration Gate

A future migration stage should proceed only if:

1. Query plan evidence shows avoidable sort or scan cost.
2. The chosen index is narrow enough for MVP write volume.
3. H2 compatibility is preserved for tests.
4. `mvn test` passes after Flyway migration.
5. The stage record includes rollback notes.

## Stage Closure Recommendation

Close `PERF-007` when this plan, README, and `PROJECT_STEP_RECORD.md` are updated, and repository protection checks confirm no schema migration, PDF/OCR, build output, or dependency directory is staged.
