# AUDIT-001 Persistent Audit Baseline

## Stage Goal

AUDIT-001 replaces the previous no-op audit implementation with a minimal persistent audit baseline. The goal is to retain security-sensitive write history without introducing a large workflow engine, BI audit dashboard, or external log service.

## Scope

| Area | Decision |
| --- | --- |
| Storage | New `audit_event` table managed by Flyway `V7__audit_event_baseline.sql` |
| Implementation | `JpaAuditService` persists `AuditEvent` through Spring Data JPA |
| Details | Structured event details are serialized to `details_json` |
| Covered existing business writes | Budget submission task/value transitions and actual import validation/commit via existing audit call sites |
| Covered security writes | Security user creation, role grant, and entity-scope grant |
| Excluded | Audit query API, audit UI, alerting, retention jobs, delete events, external SIEM integration |

## Data Model

| Column | Purpose |
| --- | --- |
| `id` | Audit event UUID |
| `actor_id` | Acting user id when available |
| `subject_type` | Business subject category such as `submission_task`, `actual_import_batch`, `app_user` |
| `subject_id` | Subject identifier as text |
| `action` | `CREATE`, `UPDATE`, `STATUS_CHANGE`, `IMPORT`, or `ACCESS_CHANGE` |
| `occurred_at` | Event timestamp |
| `details_json` | Compact structured context |

Indexes are optimized for common MVP support lookups:

1. Subject timeline: `subject_type`, `subject_id`, `occurred_at`.
2. Actor timeline: `actor_id`, `occurred_at`.
3. Action timeline: `action`, `occurred_at`.

## Design Notes

1. The audit service remains behind the existing `AuditService` interface, so controllers and business services do not depend on persistence details.
2. The original `NoopAuditService` remains as a plain class, but is no longer a Spring bean.
3. Existing submission and actual import audit call sites now use the current user context as actor where available.
4. Security management writes record `ACCESS_CHANGE` for role and entity-scope grants.
5. Audit failures currently fail the surrounding transaction. That is intentional for MVP governance: protected writes should not silently proceed if audit persistence is broken.

## Verification

Backend validation:

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

Expected result:

```text
Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Risks And Limits

1. There is no audit read API or frontend view yet.
2. The audit table stores details as JSON text; later stages can add JSONB-specific indexing for PostgreSQL if needed.
3. Metadata, budget model, and budget template writes are not yet fully instrumented; this stage covers security writes and existing audit-enabled business writes.
4. No retention or archive policy exists yet.

## Closure Recommendation

Close AUDIT-001 when `mvn test` passes, Flyway validates seven migrations, audit persistence tests pass, and repository checks confirm no PDF/OCR/build artifacts are staged.
