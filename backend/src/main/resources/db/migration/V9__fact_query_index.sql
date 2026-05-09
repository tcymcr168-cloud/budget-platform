CREATE INDEX idx_fact_value_model_status_updated ON fact_value (
    budget_model_id,
    value_status,
    updated_at DESC,
    id DESC
);
