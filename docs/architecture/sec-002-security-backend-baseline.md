# SEC-002 后端安全基础

## 当前状态说明

SEC-002 记录的是后端安全底座建立时的 MVP 请求头身份上下文。当前默认策略已由 `SEC-006`、`SEC-007` 和 `SEC-008` 收敛：`X-User-Roles` 只有在显式配置 `allow-header-roles=true` 时才参与后端授权，前端生产构建不会自动发送 `X-User-Id` 或 `X-User-Roles`。

## 阶段定位

SEC-002 基于 SEC-001 的安全设计，实现后端安全基础数据模型、管理 API 和轻量请求头身份上下文。本阶段只建立安全底座，不把授权判断接入所有业务接口；业务接口授权后置到 SEC-003。

## 实现范围

| 范围 | 本阶段处理 |
| --- | --- |
| 用户 | 新增 `app_user` 表、实体、仓储、创建与查询 API |
| 角色 | 新增 `app_user_role` 表、角色枚举、授予与查询 API |
| Entity 数据范围 | 新增 `app_user_entity_scope` 表、授予与查询 API |
| 当前用户上下文 | 支持从 `X-User-Id` 与 `X-User-Roles` 请求头解析 |
| 授权拦截 | 不进入；SEC-003 逐模块接入 |
| 登录/JWT/SSO | 不进入；AUTH-001 单独处理 |
| 删除或撤销 | 不进入；延续当前无删除接口基线 |

## 数据结构

新增 migration：

`V6__security_baseline.sql`

### app_user

保存平台用户基础身份：

1. `username` 统一转为小写；
2. `display_name` 用于界面展示；
3. `email` 可选；
4. `status` 当前为 `ACTIVE` / `INACTIVE`。

### app_user_role

保存用户在 Workspace 下的角色：

1. `user_id`；
2. `workspace_id`；
3. `role_code`；
4. 唯一约束：`user_id + workspace_id + role_code`。

### app_user_entity_scope

保存用户在 Workspace 下可访问的 Entity 成员范围：

1. `user_id`；
2. `workspace_id`；
3. `entity_member_id`；
4. `include_descendants`；
5. 唯一约束：`user_id + workspace_id + entity_member_id`。

## 角色枚举

`SecurityRoleCode` 包含：

1. `BUDGET_ADMIN`
2. `METADATA_MANAGER`
3. `TEMPLATE_DESIGNER`
4. `BUDGET_OWNER`
5. `BUDGET_REVIEWER`
6. `IMPORT_OPERATOR`
7. `READ_ONLY`

## API

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `POST` | `/api/security/users` | 创建用户 |
| `GET` | `/api/security/users` | 查询用户 |
| `POST` | `/api/security/users/{userId}/roles` | 授予 Workspace 角色 |
| `GET` | `/api/security/users/{userId}/roles?workspaceId=` | 查询用户角色 |
| `POST` | `/api/security/users/{userId}/entity-scopes` | 授予 Entity 数据范围 |
| `GET` | `/api/security/users/{userId}/entity-scopes?workspaceId=` | 查询用户 Entity 数据范围 |
| `GET` | `/api/security/me` | 返回请求头解析出的轻量身份上下文 |

## 校验规则

1. 用户名唯一，保存前统一小写。
2. 角色授予必须引用存在的用户和 Workspace。
3. 同一用户在同一 Workspace 下同一角色不能重复授予。
4. Entity 范围必须引用存在的用户、Workspace 和 Entity 成员。
5. Entity 范围成员必须属于 `ENTITY` 类型维度。
6. Entity 范围成员必须属于同一 Workspace。
7. 同一用户在同一 Workspace 下同一 Entity 范围不能重复授予。

## 当前用户上下文

本阶段新增轻量 `CurrentUserContextResolver`：

| 请求头 | 说明 |
| --- | --- |
| `X-User-Id` | 当前用户标识 |
| `X-User-Roles` | 逗号分隔角色 |

该机制仅用于内部技术验证，不等同生产认证。生产登录、JWT 或 SSO 必须进入 `AUTH-001`。

## 测试覆盖

新增 `SecurityControllerIntegrationTests`：

1. 创建用户；
2. 授予 Workspace 角色；
3. 授予 Entity 范围；
4. 查询用户角色与范围；
5. 拒绝重复角色授权；
6. 拒绝非 Entity 成员作为 Entity 范围；
7. 从请求头解析当前用户上下文。

## 风险与限制

1. 本阶段新增 migration `V6__security_baseline.sql`，但尚未把授权判断接入业务接口。
2. 本阶段管理 API 尚未要求 `BUDGET_ADMIN`，SEC-003 才接入授权拦截。
3. 请求头身份上下文不具备生产安全性。
4. 未提供删除、撤销或禁用角色/范围接口；后续如需撤销，应单独设计停用或有效期策略。
5. 审计仍由现有 Noop 审计承接，持久化后置到 `AUDIT-001`。

## 关闭建议

如果 `mvn test`、`git check-ignore`、`git status --short` 和 `git diff --check` 通过，并确认未提交 PDF/OCR 全文、构建产物或删除文件，则建议关闭 SEC-002。
