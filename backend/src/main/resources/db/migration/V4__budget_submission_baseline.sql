CREATE TABLE submission_task (
    id UUID PRIMARY KEY,
    budget_template_id UUID NOT NULL,
    budget_model_id UUID NOT NULL,
    entity_member_id UUID NOT NULL,
    time_member_id UUID NOT NULL,
    category_member_id UUID NOT NULL,
    version_member_id UUID NOT NULL,
    owner_user VARCHAR(120) NOT NULL,
    reviewer_user VARCHAR(120) NOT NULL,
    status VARCHAR(32) NOT NULL,
    return_reason TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_submission_task_template FOREIGN KEY (budget_template_id) REFERENCES budget_template (id),
    CONSTRAINT fk_submission_task_model FOREIGN KEY (budget_model_id) REFERENCES budget_model (id),
    CONSTRAINT fk_submission_task_entity FOREIGN KEY (entity_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_submission_task_time FOREIGN KEY (time_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_submission_task_category FOREIGN KEY (category_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_submission_task_version FOREIGN KEY (version_member_id) REFERENCES dimension_member (id),
    CONSTRAINT uk_submission_scope UNIQUE (
        budget_template_id,
        entity_member_id,
        time_member_id,
        category_member_id,
        version_member_id
    )
);

CREATE TABLE fact_value (
    id UUID PRIMARY KEY,
    budget_model_id UUID NOT NULL,
    budget_template_id UUID NOT NULL,
    submission_task_id UUID NOT NULL,
    account_member_id UUID NOT NULL,
    entity_member_id UUID NOT NULL,
    time_member_id UUID NOT NULL,
    category_member_id UUID NOT NULL,
    version_member_id UUID NOT NULL,
    amount NUMERIC(19, 4) NOT NULL,
    value_status VARCHAR(32) NOT NULL,
    source_type VARCHAR(32) NOT NULL,
    note TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_fact_value_model FOREIGN KEY (budget_model_id) REFERENCES budget_model (id),
    CONSTRAINT fk_fact_value_template FOREIGN KEY (budget_template_id) REFERENCES budget_template (id),
    CONSTRAINT fk_fact_value_task FOREIGN KEY (submission_task_id) REFERENCES submission_task (id),
    CONSTRAINT fk_fact_value_account FOREIGN KEY (account_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_fact_value_entity FOREIGN KEY (entity_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_fact_value_time FOREIGN KEY (time_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_fact_value_category FOREIGN KEY (category_member_id) REFERENCES dimension_member (id),
    CONSTRAINT fk_fact_value_version FOREIGN KEY (version_member_id) REFERENCES dimension_member (id),
    CONSTRAINT uk_fact_value_submission_account UNIQUE (submission_task_id, account_member_id)
);

CREATE INDEX idx_submission_task_template_status ON submission_task (budget_template_id, status);
CREATE INDEX idx_fact_value_task_status ON fact_value (submission_task_id, value_status);
CREATE INDEX idx_fact_value_model_scope ON fact_value (
    budget_model_id,
    entity_member_id,
    time_member_id,
    category_member_id,
    version_member_id
);
