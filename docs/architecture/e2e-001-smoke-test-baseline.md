# E2E-001 Smoke Test Baseline

## Stage Goal

`E2E-001` establishes a repeatable API smoke baseline for the current MVP path. It does not add business features, frontend runtime code, migrations, BI charts, consolidation, ERP integration, PDF processing, or OCR output.

The first smoke path is intentionally API-level instead of browser-level. The current risk is cross-module contract drift between metadata, budget models, templates, submissions, actual imports, and variance query. A MockMvc smoke test gives fast feedback without introducing a large browser automation dependency at this stage.

## Covered Path

The smoke test is implemented in `backend/src/test/java/com/budgetplatform/e2e/MvpApiSmokeIntegrationTests.java`.

It verifies:

1. `GET /api/security/me` authenticates the test admin under the `DEV_HEADER` test profile.
2. Metadata APIs can create a workspace, core dimensions, and members.
3. Budget model APIs can create a model, bind dimensions, and activate the model.
4. Budget template APIs can create a template, configure row and column axes, and activate the template.
5. Submission APIs can create a task, save a budget amount, submit it, and approve it.
6. Actual import APIs can validate and commit an actual CSV row.
7. Variance query API returns one comparable row with expected budget, actual, variance amount, and variance percent.

## Test Data Shape

The fixture creates one planning slice:

| Dimension | Smoke member |
| --- | --- |
| Account | Travel expense |
| Entity | Operations |
| Time | 2026-05 |
| Category | Budget and Actual |
| Version | Working and Final |

The test writes a budget value of `1000.00`, imports an actual value of `850.00`, and expects a variance amount of `-150.00` with a variance percent of `-15.0000`.

## Validation Commands

Targeted test:

```powershell
cd C:\codex\budget-platform\backend
mvn "-Dtest=com.budgetplatform.e2e.MvpApiSmokeIntegrationTests" test
```

Full backend suite:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

Repository checks:

```powershell
cd C:\codex\budget-platform
git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/
git diff --check
git status --short
```

## Validation Result

`mvn test` passed with `69` tests, `0` failures, `0` errors, and `0` skipped tests.

The targeted test also passed after using the PowerShell-safe quoted Maven property syntax:

```powershell
mvn "-Dtest=com.budgetplatform.e2e.MvpApiSmokeIntegrationTests" test
```

An earlier unquoted form failed in PowerShell:

```powershell
mvn -Dtest=com.budgetplatform.e2e.MvpApiSmokeIntegrationTests test
```

The real Maven error was `Unknown lifecycle phase ".budgetplatform.e2e.MvpApiSmokeIntegrationTests"`. The failure was command-line parsing, not a test failure.

## Scope Guardrails

This stage did not modify:

1. Frontend runtime code.
2. Backend production code.
3. Database migrations.
4. PDF or OCR source material.
5. ERP integration.
6. BI charts.
7. Consolidation reporting.

## Limitations

1. The baseline is API-level and does not prove browser rendering or user interaction.
2. The test runs on H2 under the Spring test profile, not on a live PostgreSQL instance.
3. The authentication mode is the existing `DEV_HEADER` test profile, not a real gateway or IdP.
4. The smoke path covers one happy path and should be expanded later with browser smoke and selected negative paths.

## Next Recommendation

Proceed to `PERF-001`: query pagination and performance governance. That stage should first design the query limits and sort/page contract before changing endpoints, so the smoke baseline remains stable while query behavior becomes safer for growing data volumes.
