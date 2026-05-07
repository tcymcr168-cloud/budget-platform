CREATE TABLE budget_workspace (
    id UUID PRIMARY KEY,
    code VARCHAR(64) NOT NULL,
    name VARCHAR(160) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uk_budget_workspace_code UNIQUE (code)
);

CREATE TABLE dimension (
    id UUID PRIMARY KEY,
    workspace_id UUID NOT NULL,
    code VARCHAR(64) NOT NULL,
    name VARCHAR(160) NOT NULL,
    dimension_type VARCHAR(32) NOT NULL,
    is_system BOOLEAN NOT NULL,
    status VARCHAR(32) NOT NULL,
    attributes_schema TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_dimension_workspace FOREIGN KEY (workspace_id) REFERENCES budget_workspace (id),
    CONSTRAINT uk_dimension_workspace_code UNIQUE (workspace_id, code)
);

CREATE TABLE dimension_member (
    id UUID PRIMARY KEY,
    dimension_id UUID NOT NULL,
    code VARCHAR(64) NOT NULL,
    name VARCHAR(160) NOT NULL,
    parent_id UUID,
    sort_order INTEGER,
    is_leaf BOOLEAN NOT NULL,
    status VARCHAR(32) NOT NULL,
    attributes TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_dimension_member_dimension FOREIGN KEY (dimension_id) REFERENCES dimension (id),
    CONSTRAINT fk_dimension_member_parent FOREIGN KEY (parent_id) REFERENCES dimension_member (id),
    CONSTRAINT uk_dimension_member_dimension_code UNIQUE (dimension_id, code)
);

CREATE INDEX idx_dimension_workspace_type ON dimension (workspace_id, dimension_type);
CREATE INDEX idx_dimension_member_parent ON dimension_member (dimension_id, parent_id);
