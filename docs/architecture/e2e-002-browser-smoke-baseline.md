# E2E-002 Browser Smoke Baseline

## Stage Goal

`E2E-002` establishes the first browser-level smoke test for the React/Vite workbench. It validates that the app renders, consumes API responses through the shared HTTP layer, and can run the paged facts query UI path.

This stage does not add business features, backend runtime code, database migrations, BI charts, ERP integration, consolidation reporting, PDF/OCR processing, or temporary entry pages.

## Tooling

The frontend now uses Playwright as a development dependency:

```powershell
cd C:\codex\budget-platform\frontend
pnpm e2e
```

The Playwright config starts the formal Vite app through the project `pnpm dev` command. It does not use a temporary HTML page or one-off preview entry.

Generated Playwright output is ignored:

1. `frontend/playwright-report/`
2. `frontend/test-results/`

## Smoke Coverage

Test file:

```text
frontend/e2e/workbench-smoke.spec.ts
```

Covered path:

1. Render `Budget Platform` workbench shell.
2. Mock same-origin `/api/*` backend routes with `success: true` API envelopes.
3. Load workspace, dimensions, members, budget model, budget template, submission task, and existing fact value.
4. Run the `Fact Query` UI action.
5. Verify the `Results` table displays the paged fact row.

The smoke test intentionally uses route mocks instead of requiring a local backend server. Backend API integration remains covered by `E2E-001`.

## Failure And Fix Record

First failure:

```text
Failed to load module script: Expected a JavaScript-or-Wasm module script but the server responded with a MIME type of "application/json".
```

Cause: the initial route glob matched Vite source modules under `/src/shared/api/...`, so module requests were fulfilled as JSON.

Fix: route only requests whose URL pathname starts with `/api/`.

Second failure:

```text
Request failed with status 200
```

Cause: mocked API responses returned `{ data }` without the platform envelope field `success: true`.

Fix: the test `api()` helper now returns `{ success: true, data }`.

Selector hardening:

1. Repeated labels such as workspace code use `.first()` for initial presence checks.
2. Query results are scoped to the `Results` region before checking table cells.

## Validation

Commands:

```powershell
cd C:\codex\budget-platform\frontend
pnpm e2e
pnpm type-check
pnpm lint
pnpm build
```

Expected result:

1. Playwright smoke passes in Chromium.
2. TypeScript checks application and E2E TS files.
3. ESLint includes Playwright config and E2E files.
4. Vite production build still succeeds.

## Limits

1. The test is a browser smoke baseline, not full workflow coverage.
2. Backend behavior is mocked; API correctness remains covered by backend integration and API smoke tests.
3. The smoke does not yet cover create/submit/import flows in the browser.
4. The smoke does not test mobile viewport behavior.

## Next Recommendations

1. Add a browser smoke for create workspace/model/template only after deciding test data reset strategy.
2. Add a backend-backed browser smoke only after a stable local profile is available.
3. Keep Playwright mocks aligned with the shared API envelope to catch frontend contract drift.
