CREATE TABLE actual_import_batch (
    id UUID PRIMARY KEY,
    budget_model_id UUID NOT NULL,
    file_name VARCHAR(240) NOT NULL,
    operator_user VARCHAR(120) NOT NULL,
    status VARCHAR(32) NOT NULL,
    total_rows INTEGER NOT NULL,
    valid_rows INTEGER NOT NULL,
    error_rows INTEGER NOT NULL,
    total_amount NUMERIC(19, 4) NOT NULL,
    error_report TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_actual_import_batch_model FOREIGN KEY (budget_model_id) REFERENCES budget_model (id)
);

CREATE TABLE actual_import_row (
    id UUID PRIMARY KEY,
    import_batch_id UUID NOT NULL,
    row_number INTEGER NOT NULL,
    account_code VARCHAR(64) NOT NULL,
    entity_code VARCHAR(64) NOT NULL,
    time_code VARCHAR(64) NOT NULL,
    category_code VARCHAR(64) NOT NULL,
    version_code VARCHAR(64) NOT NULL,
    amount NUMERIC(19, 4),
    row_status VARCHAR(32) NOT NULL,
    error_message TEXT,
    account_member_id UUID,
    entity_member_id UUID,
    time_member_id UUID,
    category_member_id UUID,
    version_member_id UUID,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_actual_import_row_batch FOREIGN KEY (import_batch_id) REFERENCES actual_import_batch (id),
    CONSTRAINT fk_actual_import_row_account FOREIGN KEY (account_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_actual_import_row_entity FOREIGN KEY (entity_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_actual_import_row_time FOREIGN KEY (time_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_actual_import_row_category FOREIGN KEY (category_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_actual_import_row_version FOREIGN KEY (version_member_id) REFERENCES dimension_member (id)
);

ALTER TABLE fact_value ALTER COLUMN budget_template_id DROP NOT NULL;
ALTER TABLE fact_value ALTER COLUMN submission_task_id DROP NOT NULL;
ALTER TABLE fact_value ADD COLUMN import_batch_id UUID;
ALTER TABLE fact_value ADD CONSTRAINT fk_fact_value_import_batch FOREIGN KEY (import_batch_id) REFERENCES actual_import_batch (id);

CREATE INDEX idx_actual_import_batch_model_status ON actual_import_batch (budget_model_id, status);
CREATE INDEX idx_actual_import_row_batch_status ON actual_import_row (import_batch_id, row_status);
CREATE INDEX idx_fact_value_import_batch ON fact_value (import_batch_id);
