# AUDIT-003 Core Write Audit Coverage

## Stage Goal

AUDIT-003 closes the MVP audit coverage gap for core configuration writes by adding audit events to metadata, budget model, and budget template write operations.

This stage does not add new business features, frontend screens, audit exports, retention jobs, or delete capabilities.

## Covered Operations

| Module | Operation | Subject Type | Action |
| --- | --- | --- | --- |
| Metadata | Create workspace | `budget_workspace` | `CREATE` |
| Metadata | Create dimension | `dimension` | `CREATE` |
| Metadata | Create dimension member | `dimension_member` | `CREATE` |
| Metadata | Update dimension member | `dimension_member` | `UPDATE` |
| Budget model | Create model | `budget_model` | `CREATE` |
| Budget model | Bind dimension | `budget_model_dimension` | `CREATE` |
| Budget model | Activate/deactivate | `budget_model` | `STATUS_CHANGE` |
| Budget template | Create template | `budget_template` | `CREATE` |
| Budget template | Add axis | `budget_template_axis` | `CREATE` |
| Budget template | Activate/deactivate | `budget_template` | `STATUS_CHANGE` |

## Design Notes

1. Services keep using the existing `AuditService` interface.
2. Actor id is taken from the current request context.
3. Details stay compact: ids, codes, status, role/type, and display order where relevant.
4. Existing authorization remains unchanged; audit is recorded only after write validation succeeds.
5. Existing integration tests now assert audit records for representative metadata, model, and template writes.

## Verification

Backend validation:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

Expected result:

```text
Tests run: 38, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Risks And Limits

1. Audit events are still not exposed in a frontend screen.
2. Retention, archive, and field masking remain future governance stages.
3. Failed write attempts are not audited in this stage; only successful writes generate records.
4. There are still no delete operations in the product, so delete audit behavior remains out of scope.

## Closure Recommendation

Close AUDIT-003 when the full backend test suite passes and repository checks confirm no PDF/OCR/build artifacts are staged.
