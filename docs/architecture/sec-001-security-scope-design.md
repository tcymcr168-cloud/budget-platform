# SEC-001 认证、角色与数据范围设计

## 当前状态说明

SEC-001 是早期安全设计文档，其中 `X-User-Roles` 作为 MVP 技术验证方案被保留为历史说明。当前默认策略已由 `SEC-007` 和 `SEC-008` 收敛：后端默认不信任请求头角色，前端生产构建不自动注入身份或角色请求头。生产认证必须继续遵守 `SEC-005`、`SEC-009` 的可信 Principal 与 Token 运维边界。

## 阶段定位

SEC-001 定义预算平台的认证、角色、操作权限和数据范围模型。本阶段只做治理设计，不修改后端或前端业务代码，不新增 migration，不删除文件。

设计目标是在企业级预算管理所需的安全边界和 SAP BPC 复杂权限矩阵之间取一个可解释的中间层：角色控制“能做什么”，数据范围控制“能看和处理哪些 Entity”，流程责任人控制“当前任务谁能推进”。

## 当前安全现状

| 领域 | 当前状态 | 风险 |
| --- | --- | --- |
| 身份认证 | 未实现登录态或 token 校验 | 任意调用方可访问所有接口 |
| 角色权限 | 未实现系统角色 | 无法区分管理员、填报人、审核人、导入人和只读用户 |
| 数据范围 | 未实现 Entity 范围约束 | 查询、导入、填报可跨组织访问 |
| 流程责任人 | `ownerUser`、`reviewerUser` 已在填报任务保存 | 只是字段记录，未参与授权 |
| 导入操作人 | `operatorUser` 已在 Actual 导入批次保存 | 只是字段记录，未参与授权 |
| 审计 | 已有 `AuditService` 接口和 `NoopAuditService` | 审计不持久化，无法追踪权限变更和访问行为 |

## 设计原则

1. 先显式角色，再数据范围，不做隐式黑盒权限。
2. 角色只表达职责，不绑定复杂维度交叉矩阵。
3. 数据范围 MVP 只按 Workspace + Entity 成员控制，不扩展到 Account、Time、Category、Version 的组合权限。
4. 流程责任人只约束填报任务状态动作，不替代系统角色。
5. 查询和差异分析默认必须经过数据范围过滤。
6. Actual 导入必须限制到用户可操作的 Entity 范围。
7. 审计持久化进入后续 `AUDIT-001`，但 SEC 实现阶段必须保留审计事件上下文。

## 角色模型

| 角色代码 | 角色名称 | 核心职责 |
| --- | --- | --- |
| `BUDGET_ADMIN` | 预算管理员 | 管理工作区、模型、模板、任务、锁定和用户范围 |
| `METADATA_MANAGER` | 元数据管理员 | 维护维度、成员、层级和基础口径 |
| `TEMPLATE_DESIGNER` | 模板设计人 | 维护预算模板、行列轴和筛选轴 |
| `BUDGET_OWNER` | 预算责任人 | 保存草稿并提交本人负责的填报任务 |
| `BUDGET_REVIEWER` | 预算审核人 | 审核、退回本人负责的填报任务 |
| `IMPORT_OPERATOR` | 实际数导入人 | 校验和提交可访问 Entity 范围内的 Actual 文件 |
| `READ_ONLY` | 只读用户 | 在授权 Entity 范围内查询事实、汇总和差异 |

## 操作权限矩阵

| 能力 | BUDGET_ADMIN | METADATA_MANAGER | TEMPLATE_DESIGNER | BUDGET_OWNER | BUDGET_REVIEWER | IMPORT_OPERATOR | READ_ONLY |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Workspace 管理 | 是 | 否 | 否 | 否 | 否 | 否 | 否 |
| 维度与成员维护 | 是 | 是 | 否 | 否 | 否 | 否 | 否 |
| 预算模型维护 | 是 | 否 | 否 | 否 | 否 | 否 | 否 |
| 模板维护 | 是 | 否 | 是 | 否 | 否 | 否 | 否 |
| 创建填报任务 | 是 | 否 | 否 | 否 | 否 | 否 | 否 |
| 保存草稿 | 是 | 否 | 否 | 仅本人任务 | 否 | 否 | 否 |
| 提交任务 | 是 | 否 | 否 | 仅本人任务 | 否 | 否 | 否 |
| 退回/通过任务 | 是 | 否 | 否 | 否 | 仅本人任务 | 否 | 否 |
| 锁定任务 | 是 | 否 | 否 | 否 | 否 | 否 | 否 |
| 查询事实与汇总 | 是 | 否 | 否 | 范围内 | 范围内 | 范围内 | 范围内 |
| 查询预算实际差异 | 是 | 否 | 否 | 范围内 | 范围内 | 范围内 | 范围内 |
| Actual 导入校验/提交 | 是 | 否 | 否 | 否 | 否 | 范围内 | 否 |
| 用户角色与范围维护 | 是 | 否 | 否 | 否 | 否 | 否 | 否 |

## 数据范围模型

### 范围对象

MVP 数据范围以 Entity 成员为核心：

1. 每个用户可以在一个 Workspace 下拥有多个 Entity 成员范围。
2. 如果授权到父级 Entity，则默认包含其子级成员。
3. 如果用户拥有 `BUDGET_ADMIN`，默认拥有该 Workspace 的全 Entity 范围。
4. 非管理员如果没有范围，则只能访问本人作为 Owner 或 Reviewer 的填报任务；查询结果为空。

### 范围判定

| 操作 | 范围判定 |
| --- | --- |
| 创建填报任务 | `BUDGET_ADMIN` 可为任何 Entity 创建 |
| 保存/提交填报 | 用户必须是任务 `ownerUser` 或 `BUDGET_ADMIN` |
| 退回/通过填报 | 用户必须是任务 `reviewerUser` 或 `BUDGET_ADMIN` |
| 锁定填报 | 仅 `BUDGET_ADMIN` |
| 查询事实 | 事实的 Entity 必须在用户范围内，管理员例外 |
| 查询汇总 | 汇总前先做 Entity 范围过滤 |
| 查询差异 | Budget 与 Actual 两侧事实均先做 Entity 范围过滤 |
| Actual 导入 | CSV 每行 Entity 必须在用户范围内，管理员例外 |

## 身份上下文

后续实现阶段建议引入轻量 `SecurityContext`：

| 字段 | 说明 |
| --- | --- |
| `userId` | 用户唯一标识 |
| `displayName` | 显示名 |
| `roles` | 当前用户角色集合 |
| `workspaceIds` | 可访问工作区 |
| `entityMemberIds` | 当前工作区下可访问 Entity 成员范围 |
| `requestId` | 请求追踪 ID |

SEC-002 可先使用请求头承载 MVP 身份上下文，避免过早接入外部身份提供商：

| 请求头 | 说明 |
| --- | --- |
| `X-User-Id` | 当前用户 ID |
| `X-User-Roles` | 逗号分隔角色，如 `BUDGET_ADMIN,READ_ONLY` |

这种方式只适合内部技术验证。生产化登录、密码、SSO、JWT、会话管理应进入后续独立阶段。

## 建议数据模型

SEC 实现阶段建议新增以下表：

### app_user

| 字段 | 说明 |
| --- | --- |
| `id` | UUID 主键 |
| `username` | 登录名或外部身份 ID |
| `display_name` | 显示名 |
| `email` | 邮箱 |
| `status` | `ACTIVE` / `INACTIVE` |
| `created_at` / `updated_at` | 时间戳 |

### app_user_role

| 字段 | 说明 |
| --- | --- |
| `id` | UUID 主键 |
| `user_id` | 用户 |
| `workspace_id` | 工作区 |
| `role_code` | 角色代码 |
| `created_at` | 创建时间 |

唯一约束：`user_id + workspace_id + role_code`。

### app_user_entity_scope

| 字段 | 说明 |
| --- | --- |
| `id` | UUID 主键 |
| `user_id` | 用户 |
| `workspace_id` | 工作区 |
| `entity_member_id` | Entity 成员 |
| `include_descendants` | 是否包含下级 |
| `created_at` | 创建时间 |

唯一约束：`user_id + workspace_id + entity_member_id`。

## API 设计建议

| 方法 | 路径 | 说明 | 角色 |
| --- | --- | --- | --- |
| `POST` | `/api/security/users` | 创建用户 | `BUDGET_ADMIN` |
| `GET` | `/api/security/users?workspaceId=` | 查询用户 | `BUDGET_ADMIN` |
| `POST` | `/api/security/users/{userId}/roles` | 授予角色 | `BUDGET_ADMIN` |
| `GET` | `/api/security/users/{userId}/roles?workspaceId=` | 查询角色 | `BUDGET_ADMIN` |
| `POST` | `/api/security/users/{userId}/entity-scopes` | 授予 Entity 范围 | `BUDGET_ADMIN` |
| `GET` | `/api/security/users/{userId}/entity-scopes?workspaceId=` | 查询 Entity 范围 | `BUDGET_ADMIN` |
| `GET` | `/api/security/me` | 查看当前身份上下文 | 任意已识别用户 |

SEC-002 MVP 可暂不实现删除或撤销接口，延续项目当前“避免删除接口”的治理基线。后续如需撤销角色或范围，应采用停用或有效期策略，独立阶段设计。

## 与现有模块的集成策略

| 模块 | 集成策略 |
| --- | --- |
| 元数据 | 创建和维护需 `METADATA_MANAGER` 或 `BUDGET_ADMIN` |
| 预算模型 | 维护需 `BUDGET_ADMIN` |
| 预算模板 | 维护需 `TEMPLATE_DESIGNER` 或 `BUDGET_ADMIN` |
| 预算填报 | Owner/Reviewer 字段与当前用户匹配；管理员可代办 |
| 预算查询 | 按 Entity 范围过滤事实 |
| Actual 导入 | 校验阶段逐行验证 Entity 范围 |
| 差异分析 | 基于已过滤事实计算，不绕过查询范围 |
| 审计 | 每次角色、范围和关键业务状态变更记录 actor |

## 后续阶段拆分

| 阶段 | 范围 |
| --- | --- |
| `SEC-002` | 后端安全基础：角色枚举、身份上下文、用户/角色/Entity 范围数据模型与管理 API |
| `SEC-003` | 后端授权接入：将角色与数据范围接入元数据、模型、模板、填报、查询、导入和差异接口 |
| `SEC-004` | 前端安全管理：用户、角色和 Entity 范围配置 UI |
| `AUDIT-001` | 持久化审计：替换 Noop 审计，记录安全与业务关键事件 |
| `AUTH-001` | 生产认证：登录、JWT 或 SSO 接入 |

## 验收标准

SEC-001 完成条件：

1. 明确角色模型。
2. 明确操作权限边界。
3. 明确 Entity 数据范围规则。
4. 明确不照搬复杂多维权限矩阵。
5. 明确后续实现阶段的数据模型、API 和接入顺序。
6. 更新 `PROJECT_STEP_RECORD.md`。

## 关闭建议

如果文档检查、Git 忽略保护和阶段记录更新通过，则建议关闭 SEC-001，进入 SEC-002 后端安全基础。
