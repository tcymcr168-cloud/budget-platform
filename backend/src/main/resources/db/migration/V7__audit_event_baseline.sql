CREATE TABLE audit_event (
    id UUID PRIMARY KEY,
    actor_id VARCHAR(160),
    subject_type VARCHAR(80) NOT NULL,
    subject_id VARCHAR(120),
    action VARCHAR(48) NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    details_json TEXT NOT NULL
);

CREATE INDEX idx_audit_event_subject ON audit_event (subject_type, subject_id, occurred_at);
CREATE INDEX idx_audit_event_actor ON audit_event (actor_id, occurred_at);
CREATE INDEX idx_audit_event_action ON audit_event (action, occurred_at);
