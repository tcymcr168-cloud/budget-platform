# SEC-003 后端授权入口接入

## 阶段目标

SEC-003 在 SEC-001 安全模型和 SEC-002 安全基础数据结构之上，接入第一批后端授权入口。本阶段聚焦两个高风险面：

1. 安全管理 API：用户、角色、Entity 数据范围的管理接口必须由管理员调用。
2. 预算查询 API：事实明细、汇总、CSV 导出和预算与实际差异分析必须经过角色校验和 Entity 数据范围过滤。

本阶段不引入生产登录、JWT、SSO、密码策略或前端安全管理界面；这些内容后置到独立阶段。

## 设计边界

| 项 | 本阶段处理 |
| --- | --- |
| 安全管理 API 管理员保护 | 是 |
| 预算查询读取角色校验 | 是 |
| Entity 数据范围过滤 | 是 |
| 预算与实际差异分析读取授权 | 是 |
| 元数据写接口授权 | 否，后续阶段 |
| 模型、模板、填报、实际数导入写接口授权 | 否，后续阶段 |
| 前端登录或令牌管理 | 否 |
| 生产认证、JWT、SSO | 否 |
| 新增数据库 migration | 否 |

## 授权服务

新增 `AuthorizationService` 作为后端统一授权入口，当前提供：

| 方法 | 用途 |
| --- | --- |
| `requireHeaderAdmin` | 要求请求头身份中包含 `BUDGET_ADMIN`，用于引导期安全管理 |
| `requireAdmin` | 校验请求头或持久化角色中是否包含 `BUDGET_ADMIN` |
| `requireAnyRole` | 校验用户在 Workspace 内是否具备任一允许角色 |
| `rolesForWorkspace` | 合并轻量请求头角色与持久化 Workspace 角色 |
| `readableEntityMemberIds` | 返回用户在 Workspace 内可读取的 Entity 成员范围 |

请求头角色仍是内部技术验证机制。它便于集成测试和早期管理引导，不代表生产认证完成。

## 安全管理 API

`SecurityController` 已接入授权：

| API | 授权规则 |
| --- | --- |
| 创建用户 | 请求头必须包含 `BUDGET_ADMIN` |
| 查询用户 | 请求头必须包含 `BUDGET_ADMIN` |
| 授予角色 | 调用方必须是目标 Workspace 的 `BUDGET_ADMIN` |
| 查询角色 | 指定 Workspace 时要求该 Workspace 管理员；未指定 Workspace 时要求请求头管理员 |
| 授予 Entity 范围 | 调用方必须是目标 Workspace 的 `BUDGET_ADMIN` |
| 查询 Entity 范围 | 指定 Workspace 时要求该 Workspace 管理员；未指定 Workspace 时要求请求头管理员 |
| 查询当前身份 `/me` | 仍用于诊断，不强制管理员 |

## 预算查询 API

`BudgetQueryController` 已解析 `X-User-Id` 与 `X-User-Roles`，并将身份上下文传入服务层。

| API | 授权规则 |
| --- | --- |
| `GET /api/budget-query/facts` | 要求读取角色，并按 Entity 范围过滤 |
| `GET /api/budget-query/summary` | 要求读取角色，并按 Entity 范围过滤 |
| `GET /api/budget-query/facts.csv` | 要求读取角色，并按 Entity 范围过滤 |
| `GET /api/budget-query/variance` | 要求读取角色，并按 Entity 范围过滤 |

读取允许角色：

1. `BUDGET_ADMIN`
2. `BUDGET_OWNER`
3. `BUDGET_REVIEWER`
4. `IMPORT_OPERATOR`
5. `READ_ONLY`

`BUDGET_ADMIN` 可读取 Workspace 内全部 Entity 数据。其他角色必须存在明确的 Entity 范围；没有 Entity 范围时返回空结果或因 Workspace 角色缺失返回 403。

## 错误码

`ErrorCode` 新增：

| 错误码 | HTTP 状态 | 场景 |
| --- | --- | --- |
| `UNAUTHORIZED` | 401 | 缺少认证上下文 |
| `FORBIDDEN` | 403 | 已有身份但缺少所需角色或未注册为安全用户 |

## 测试覆盖

本阶段新增或调整后端集成测试：

1. 安全管理接口在非管理员请求下返回 403。
2. 预算查询接口在管理员请求下保持原有明细、汇总、CSV 和差异分析能力。
3. `READ_ONLY` 用户仅能读取已授予 Entity 范围的事实数据。
4. 同一用户访问未授权 Workspace 时返回 403。
5. 实际数导入提交后的事实查询通过管理员上下文读取。

验证命令：

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

结果：通过；Tests run: 29, Failures: 0, Errors: 0, Skipped: 0。

## 风险与限制

1. 本阶段不是完整生产认证；请求头身份上下文仍属于内部技术验证机制。
2. 前端当前尚未统一注入身份请求头，直接使用部分受保护 API 可能收到 401 或 403，需后续前端阶段补齐。
3. 本阶段只保护安全管理和预算查询读取面，尚未覆盖元数据、模型、模板、填报、实际数导入等写接口。
4. Entity 范围目前仅按事实数据的 Entity 成员过滤，不做 Account、Time、Category、Version 组合式复杂权限矩阵。
5. 持久化审计仍需独立阶段实现。

## 关闭建议

建议关闭 SEC-003。

关闭理由：统一授权服务、第一批管理与查询入口授权、Entity 数据范围过滤和集成测试已经完成；本阶段未新增 migration，未修改前端 UI，未删除文件，未提交 PDF 或 OCR 全文，也未进入 ERP 直连、BI 图表或合并报表。

## 下一阶段建议

建议下一阶段进入 `SEC-003B`：后端业务写接口授权接入，逐步保护元数据、模型、模板、填报和实际数导入接口，避免一次性大范围改动。
