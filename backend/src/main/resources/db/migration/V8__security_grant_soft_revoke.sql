ALTER TABLE app_user_role ADD COLUMN status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE';
ALTER TABLE app_user_role ADD COLUMN revoked_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE app_user_role ADD COLUMN revoked_by VARCHAR(120);
ALTER TABLE app_user_role ADD COLUMN revoked_reason VARCHAR(240);

ALTER TABLE app_user_role
    ALTER COLUMN status DROP DEFAULT;

ALTER TABLE app_user_entity_scope ADD COLUMN status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE';
ALTER TABLE app_user_entity_scope ADD COLUMN revoked_at TIMESTAMP WITH TIME ZONE;
ALTER TABLE app_user_entity_scope ADD COLUMN revoked_by VARCHAR(120);
ALTER TABLE app_user_entity_scope ADD COLUMN revoked_reason VARCHAR(240);

ALTER TABLE app_user_entity_scope
    ALTER COLUMN status DROP DEFAULT;

CREATE INDEX idx_app_user_role_status ON app_user_role (status);
CREATE INDEX idx_app_user_entity_scope_status ON app_user_entity_scope (status);
