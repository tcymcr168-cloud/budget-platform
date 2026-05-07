CREATE TABLE budget_template (
    id UUID PRIMARY KEY,
    budget_model_id UUID NOT NULL,
    code VARCHAR(64) NOT NULL,
    name VARCHAR(160) NOT NULL,
    description TEXT,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_budget_template_model FOREIGN KEY (budget_model_id) REFERENCES budget_model (id),
    CONSTRAINT uk_budget_template_model_code UNIQUE (budget_model_id, code)
);

CREATE TABLE budget_template_axis (
    id UUID PRIMARY KEY,
    budget_template_id UUID NOT NULL,
    model_dimension_id UUID NOT NULL,
    axis_type VARCHAR(32) NOT NULL,
    member_selector VARCHAR(64) NOT NULL,
    display_order INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_budget_template_axis_template FOREIGN KEY (budget_template_id) REFERENCES budget_template (id),
    CONSTRAINT fk_budget_template_axis_model_dimension FOREIGN KEY (model_dimension_id) REFERENCES budget_model_dimension (id),
    CONSTRAINT uk_budget_template_axis_dimension UNIQUE (budget_template_id, model_dimension_id)
);

CREATE INDEX idx_budget_template_model ON budget_template (budget_model_id, status);
CREATE INDEX idx_budget_template_axis_template ON budget_template_axis (budget_template_id, axis_type, display_order);
