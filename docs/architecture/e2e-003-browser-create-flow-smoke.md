# E2E-003 Browser Create Flow Smoke

## Stage Goal

`E2E-003` extends the Playwright browser baseline with a create-flow smoke for the formal React/Vite workbench. It validates that the UI can create a workspace, create a budget model in that workspace, and create a template in that model through the existing API client and form handlers.

This stage does not add business features, backend runtime code, database migrations, BI charts, ERP integration, consolidation reporting, PDF/OCR processing, or temporary entry pages.

## Smoke Coverage

Test file:

```text
frontend/e2e/create-flow-smoke.spec.ts
```

Covered path:

1. Render the formal workbench app through Playwright's Vite webServer.
2. Mock same-origin `/api/*` backend routes with the platform `{ success: true, data }` envelope.
3. Create `AUTO_WS` through the Workspaces panel.
4. Create `AUTO_MODEL` through the Budget Models panel after the workspace is selected.
5. Create `AUTO_TEMPLATE` through the Templates panel after the model is selected.
6. Assert each created item is visible in its owning panel.

The test keeps state in memory inside the Playwright route handler. It does not require a backend server, database reset, or temporary HTML entry.

## Contract Notes

The smoke intentionally exercises these frontend contracts:

1. `POST /api/metadata/workspaces` returns the created workspace.
2. `GET /api/metadata/workspaces` reflects the newly created workspace.
3. `POST /api/budget-models` returns the created budget model.
4. `GET /api/budget-models?workspaceId=...` reflects the newly created model.
5. `POST /api/budget-templates` returns the created template.
6. `GET /api/budget-templates?budgetModelId=...` reflects the newly created template.

## Validation

Commands:

```powershell
cd C:\codex\budget-platform\frontend
pnpm e2e
pnpm type-check
pnpm lint
pnpm build
```

Observed result:

1. Playwright Chromium: 2 tests passed.
2. TypeScript app and E2E checks passed.
3. ESLint passed.
4. Vite production build passed.

## Limits

1. Backend behavior is mocked; API correctness remains covered by backend integration and API smoke tests.
2. The smoke covers creation UI flow only, not backend persistence.
3. Dimension binding, submission task, actual import, and variance browser workflows remain future browser-smoke candidates.
4. Mobile viewport coverage remains future work.

## Next Recommendations

1. Add a backend-backed browser smoke after a stable local test profile and data reset strategy are available.
2. Add a browser smoke for template axis binding only after the model-dimension fixture strategy is explicit.
3. Keep browser mocks aligned with the shared API envelope and current endpoint response shapes.
