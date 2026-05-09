# SEC-003F 实际数导入 API 授权接入

## 当前状态说明

SEC-003F 中的 `X-User-Roles` 说明属于早期 MVP 调用上下文。当前默认策略已由 `SEC-007` 与 `SEC-008` 收敛：生产构建不自动注入身份头，后端默认只从持久化 Workspace 角色和 Entity 范围判断实际数导入授权。

## 阶段目标

SEC-003F 在 SEC-003E 的授权模式基础上，只针对实际数导入模块接入授权。本阶段保护 Actual CSV 校验、提交、批次查询和行级结果查询接口，不扩展到 ERP 直连、复杂 Data Manager 或生产认证。

## 授权策略

| API | 授权规则 |
| --- | --- |
| `POST /api/actual-imports/validate` | 模型所属 Workspace 内 `BUDGET_ADMIN` 或 `IMPORT_OPERATOR` |
| `POST /api/actual-imports/{batchId}/commit` | 批次所属 Workspace 内 `BUDGET_ADMIN` 或 `IMPORT_OPERATOR` |
| `GET /api/actual-imports/batches?budgetModelId=` | 模型所属 Workspace 内 `BUDGET_ADMIN`、`IMPORT_OPERATOR`、`BUDGET_OWNER`、`BUDGET_REVIEWER` 或 `READ_ONLY` |
| `GET /api/actual-imports/batches/{batchId}/rows` | 批次所属 Workspace 内同上读取角色 |

## 实现方式

`ActualImportController` 解析 `X-User-Id` 与 `X-User-Roles`，并把 `CurrentUserContext` 传入 `ActualImportService`。

`ActualImportService` 在加载 Budget Model 或 Import Batch 后执行授权：

1. CSV Validate 和 Commit 属于导入写操作，要求 `BUDGET_ADMIN` 或 `IMPORT_OPERATOR`。
2. 批次列表和行级结果属于导入读取操作，允许管理员、导入员和业务读取角色。
3. 授权判断复用 `AuthorizationService.requireAnyRole`。
4. 原有导入校验保持不变：固定表头、成员编码匹配、Category=ACTUAL、金额和重复坐标校验。

## 测试覆盖

本阶段更新实际数导入和预算差异测试，为 Actual Validate 和 Commit 增加管理员请求头。

新增实际数导入授权测试：

1. `READ_ONLY` 不能执行 Actual CSV Validate，返回 403。
2. 原有合法导入、错误导入和自定义维度场景在管理员上下文下保持通过。

验证命令：

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

结果：通过；Tests run: 34, Failures: 0, Errors: 0, Skipped: 0。

## 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不是生产认证。
2. 批次和行级结果读取当前按 Workspace 角色控制，尚未按行级 Entity 范围过滤。
3. 本阶段不引入 ERP 直连，不实现黑盒 Data Manager，也不实现外部编码转换表。
4. 前端尚未统一注入身份请求头，Actual 导入页面直接调用受保护 API 时可能返回 401 或 403。

## 关闭建议

建议关闭 SEC-003F。

关闭理由：实际数导入 API 已完成读写授权接入，所有后端测试通过；未新增 migration，未删除文件，未修改前端 UI，未提交 PDF/OCR 全文，也未进入 ERP 直连、BI 图表或合并报表。

## 下一阶段建议

建议下一阶段进入 `SEC-004`：前端安全上下文接入，为现有页面统一注入请求头并处理 401/403。
