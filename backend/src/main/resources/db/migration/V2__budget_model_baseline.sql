CREATE TABLE budget_model (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL,
    code VARCHAR(64) NOT NULL,
    name VARCHAR(160) NOT NULL,
    description TEXT,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_budget_model_workspace FOREIGN KEY (workspace_id) REFERENCES budget_workspace (id),
    CONSTRAINT uk_budget_model_workspace_code UNIQUE (workspace_id, code)
);

CREATE TABLE budget_model_dimension (
    id UUID PRIMARY KEY,
    budget_model_id UUID NOT NULL,
    dimension_id UUID NOT NULL,
    dimension_role VARCHAR(32) NOT NULL,
    is_required_for_entry BOOLEAN NOT NULL,
    display_order INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_budget_model_dimension_model FOREIGN KEY (budget_model_id) REFERENCES budget_model (id),
    CONSTRAINT fk_budget_model_dimension_dimension FOREIGN KEY (dimension_id) REFERENCES dimension (id),
    CONSTRAINT uk_budget_model_dimension UNIQUE (budget_model_id, dimension_id)
);

CREATE INDEX idx_budget_model_workspace ON budget_model (workspace_id, status);
CREATE INDEX idx_budget_model_dimension_model ON budget_model_dimension (budget_model_id, display_order);
