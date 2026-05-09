# PERF-005 CSV Export Cap And Truncation Signal

## Stage Goal

`PERF-005` adds an explicit row cap to the lightweight budget facts CSV export. The goal is to keep `/api/budget-query/facts.csv` useful for operational inspection while preventing it from becoming an unbounded report export path.

This stage does not introduce an async report engine, BI charts, ERP integration, consolidation reporting, database migration, or new business module.

## Scope

Allowed changes:

1. Add a bounded `limit` parameter to facts CSV export.
2. Return response headers that tell callers whether the CSV was truncated.
3. Surface the returned row count in the React CSV preview.
4. Cover cap and invalid limit behavior in integration tests.

Out of scope:

1. Full report export service.
2. Background jobs or downloadable files.
3. BI charting.
4. ERP direct connection.
5. Consolidation reporting.
6. PDF or OCR processing.

## Backend Contract

Endpoint:

```text
GET /api/budget-query/facts.csv
```

Parameters:

| Parameter | Default | Maximum | Notes |
| --- | --- | --- | --- |
| `limit` | `1000` | `5000` | Must be between `1` and `5000` |

Response headers:

| Header | Meaning |
| --- | --- |
| `X-Budget-Platform-Export-Truncated` | `true` when matching facts exceeded `limit` |
| `X-Budget-Platform-Export-Total-Rows` | Total rows matched by filters before export cap |
| `X-Budget-Platform-Export-Returned-Rows` | Rows included in the CSV body |

Invalid limits return `400 BAD_REQUEST`.

## Frontend Behavior

The React workspace still renders the CSV as a text preview. It now reads export headers and displays:

1. Returned rows over total rows.
2. A capped marker when the backend reports truncation.
3. A notice that distinguishes normal export from capped export.

The frontend does not create files, run background exports, or add report scheduling.

## Test Coverage

Backend integration tests cover:

1. Existing CSV export still returns `text/csv`.
2. Non-truncated export returns `false`, `1`, and `1` in export headers.
3. `limit=1` truncates a two-row result set and returns truncation headers.
4. `limit=5001` returns `400 BAD_REQUEST`.

Frontend validation covers TypeScript, lint, and production build.

## Risks And Follow-Ups

1. CSV export still materializes query rows in service memory before applying the export cap; repository-level query pushdown remains a later performance stage.
2. CSV remains a text preview, not a governed report artifact.
3. Browser-level smoke automation is still recommended for the full query/export workflow.

## Stage Closure Recommendation

Close `PERF-005` when backend targeted/full tests pass, frontend type-check/lint/build pass, `.gitignore` protection checks pass, and Git status confirms no PDF, OCR cache, dependency directory, or build output is staged.
