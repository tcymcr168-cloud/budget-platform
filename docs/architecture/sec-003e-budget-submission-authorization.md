# SEC-003E 预算填报 API 授权接入

## 阶段目标

SEC-003E 在 SEC-003D 的授权模式基础上，只针对预算填报模块接入授权。本阶段保护 Submission Task 的创建、查询、填报值保存、提交、退回、审批和锁定接口，不扩展到实际数导入或生产认证。

## 授权策略

| API | 授权规则 |
| --- | --- |
| `POST /api/submissions/tasks` | `BUDGET_ADMIN`、任务 owner 本人，或具备 `BUDGET_OWNER` 且有 Entity 范围 |
| `GET /api/submissions/tasks?budgetTemplateId=` | Workspace 内 `BUDGET_ADMIN`、`BUDGET_OWNER`、`BUDGET_REVIEWER` 或 `READ_ONLY` |
| `GET /api/submissions/tasks/{taskId}` | `BUDGET_ADMIN`、owner/reviewer 本人，或具备读取角色且有 Entity 范围 |
| `POST /api/submissions/tasks/{taskId}/values` | `BUDGET_ADMIN`、owner 本人，或具备 `BUDGET_OWNER` 且有 Entity 范围 |
| `GET /api/submissions/tasks/{taskId}/values` | 同任务详情读取规则 |
| `POST /api/submissions/tasks/{taskId}/submit` | `BUDGET_ADMIN`、owner 本人，或具备 `BUDGET_OWNER` 且有 Entity 范围 |
| `POST /api/submissions/tasks/{taskId}/return` | `BUDGET_ADMIN`、reviewer 本人，或具备 `BUDGET_REVIEWER` 且有 Entity 范围 |
| `POST /api/submissions/tasks/{taskId}/approve` | 同退回规则 |
| `POST /api/submissions/tasks/{taskId}/lock` | 同退回规则 |

## 实现方式

`SubmissionController` 解析 `X-User-Id` 与 `X-User-Roles`，并把 `CurrentUserContext` 传入 `SubmissionService`。

`SubmissionService` 在加载模板、任务或填报范围后执行授权：

1. `BUDGET_ADMIN` 拥有填报管理兜底权限。
2. `ownerUser` 可创建自己的任务、保存值和提交。
3. `reviewerUser` 可退回、审批和锁定。
4. `BUDGET_OWNER` 和 `BUDGET_REVIEWER` 可通过持久化角色与 Entity 范围执行对应动作。
5. `READ_ONLY` 等读取角色读取任务详情时必须通过 Entity 范围。

## 测试覆盖

本阶段更新填报与查询相关后端集成测试，为任务创建、填报、提交、退回、审批、锁定和读取增加管理员请求头。

新增填报授权测试：

1. 非 owner、非 reviewer、无对应角色的用户保存填报值返回 403。
2. 原有填报状态流转测试在管理员上下文下保持通过。

验证命令：

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

结果：通过；Tests run: 33, Failures: 0, Errors: 0, Skipped: 0。

## 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不是生产认证。
2. 任务清单接口当前只做 Workspace 读取角色校验，尚未按 Entity 范围裁剪列表；任务详情和值读取已接入 Entity 范围。
3. 前端尚未统一注入身份请求头，填报页面直接调用受保护 API 时可能返回 401 或 403。
4. 持久化审计仍使用现有轻量审计服务，生产审计后置到 `AUDIT-001`。

## 关闭建议

建议关闭 SEC-003E。

关闭理由：预算填报 API 已完成主要读写授权接入，所有后端测试通过；未新增 migration，未删除文件，未修改前端 UI，未提交 PDF/OCR 全文，也未进入 ERP 直连、BI 图表或合并报表。

## 下一阶段建议

建议下一阶段进入 `SEC-003F`：实际数导入 API 授权接入，只处理导入模块。
