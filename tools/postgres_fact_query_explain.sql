\set ON_ERROR_STOP on
\timing on

\if :{?budget_model_id}
\else
\echo 'Required psql variable budget_model_id is missing.'
\echo 'Example: psql "...connection..." -v budget_model_id=00000000-0000-0000-0000-000000000000 -f tools/postgres_fact_query_explain.sql'
\quit 1
\endif

\if :{?value_status}
\else
\set value_status APPROVED
\endif

\if :{?limit_rows}
\else
\set limit_rows 25
\endif

\if :{?offset_rows}
\else
\set offset_rows 0
\endif

\echo 'Fact query indexes'
SELECT
    indexname,
    indexdef
FROM pg_indexes
WHERE schemaname = current_schema()
  AND tablename = 'fact_value'
ORDER BY indexname;

\echo 'Fact rows for selected model/status'
SELECT
    budget_model_id,
    value_status,
    count(*) AS rows
FROM fact_value
WHERE budget_model_id = :'budget_model_id'::uuid
  AND value_status = :'value_status'
GROUP BY budget_model_id, value_status;

\echo 'EXPLAIN: status-filtered facts page'
EXPLAIN (ANALYZE, BUFFERS)
SELECT
    id,
    budget_model_id,
    budget_template_id,
    submission_task_id,
    account_member_id,
    entity_member_id,
    time_member_id,
    category_member_id,
    version_member_id,
    amount,
    value_status,
    source_type,
    updated_at
FROM fact_value
WHERE budget_model_id = :'budget_model_id'::uuid
  AND value_status = :'value_status'
ORDER BY updated_at DESC, id DESC
LIMIT :limit_rows OFFSET :offset_rows;

\if :{?entity_member_id}
\if :{?time_member_id}
\if :{?category_member_id}
\if :{?version_member_id}
\echo 'EXPLAIN: fully scoped facts page'
EXPLAIN (ANALYZE, BUFFERS)
SELECT
    id,
    budget_model_id,
    budget_template_id,
    submission_task_id,
    account_member_id,
    entity_member_id,
    time_member_id,
    category_member_id,
    version_member_id,
    amount,
    value_status,
    source_type,
    updated_at
FROM fact_value
WHERE budget_model_id = :'budget_model_id'::uuid
  AND entity_member_id = :'entity_member_id'::uuid
  AND time_member_id = :'time_member_id'::uuid
  AND category_member_id = :'category_member_id'::uuid
  AND version_member_id = :'version_member_id'::uuid
  AND value_status = :'value_status'
ORDER BY updated_at DESC, id DESC
LIMIT :limit_rows OFFSET :offset_rows;
\else
\echo 'Skipping scoped query: version_member_id is missing.'
\endif
\else
\echo 'Skipping scoped query: category_member_id is missing.'
\endif
\else
\echo 'Skipping scoped query: time_member_id is missing.'
\endif
\else
\echo 'Skipping scoped query: entity_member_id is missing.'
\endif
