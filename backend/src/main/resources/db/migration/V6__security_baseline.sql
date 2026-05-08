CREATE TABLE app_user (
    id UUID PRIMARY KEY,
    username VARCHAR(120) NOT NULL,
    display_name VARCHAR(160) NOT NULL,
    email VARCHAR(240),
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uk_app_user_username UNIQUE (username)
);

CREATE TABLE app_user_role (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    workspace_id UUID NOT NULL,
    role_code VARCHAR(48) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_app_user_role_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_app_user_role_workspace FOREIGN KEY (workspace_id) REFERENCES budget_workspace (id),
    CONSTRAINT uk_app_user_role_user_workspace_role UNIQUE (user_id, workspace_id, role_code)
);

CREATE TABLE app_user_entity_scope (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    workspace_id UUID NOT NULL,
    entity_member_id UUID NOT NULL,
    include_descendants BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_app_user_entity_scope_user FOREIGN KEY (user_id) REFERENCES app_user (id),
    CONSTRAINT fk_app_user_entity_scope_workspace FOREIGN KEY (workspace_id) REFERENCES budget_workspace (id),
    CONSTRAINT fk_app_user_entity_scope_member FOREIGN KEY (entity_member_id) REFERENCES dimension_member (id),
    CONSTRAINT uk_app_user_entity_scope_user_workspace_member UNIQUE (user_id, workspace_id, entity_member_id)
);

CREATE INDEX idx_app_user_role_workspace_role ON app_user_role (workspace_id, role_code);
CREATE INDEX idx_app_user_entity_scope_workspace_member ON app_user_entity_scope (workspace_id, entity_member_id);
