# SEC-003C 预算模型 API 授权接入

## 阶段目标

SEC-003C 在 SEC-003 和 SEC-003B 的授权模式基础上，只针对预算模型模块接入授权。本阶段保护 Budget Model 的创建、查询、维度绑定、激活和停用接口，不扩展到模板、填报或实际数导入。

## 授权策略

| API | 授权规则 |
| --- | --- |
| `POST /api/budget-models` | Workspace 内 `BUDGET_ADMIN` 或 `METADATA_MANAGER` |
| `GET /api/budget-models?workspaceId=` | Workspace 内任一读取角色 |
| `GET /api/budget-models/{budgetModelId}` | Model 所属 Workspace 内任一读取角色 |
| `POST /api/budget-models/{budgetModelId}/dimensions` | Model 所属 Workspace 内 `BUDGET_ADMIN` 或 `METADATA_MANAGER` |
| `GET /api/budget-models/{budgetModelId}/dimensions` | Model 所属 Workspace 内任一读取角色 |
| `POST /api/budget-models/{budgetModelId}/activate` | Model 所属 Workspace 内 `BUDGET_ADMIN` 或 `METADATA_MANAGER` |
| `POST /api/budget-models/{budgetModelId}/deactivate` | Model 所属 Workspace 内 `BUDGET_ADMIN` 或 `METADATA_MANAGER` |

读取角色包括：

1. `BUDGET_ADMIN`
2. `METADATA_MANAGER`
3. `TEMPLATE_DESIGNER`
4. `BUDGET_OWNER`
5. `BUDGET_REVIEWER`
6. `IMPORT_OPERATOR`
7. `READ_ONLY`

## 实现方式

`BudgetModelController` 解析 `X-User-Id` 与 `X-User-Roles`，并把 `CurrentUserContext` 传入 `BudgetModelService`。

`BudgetModelService` 在加载 Workspace 或 Budget Model 后执行授权：

1. Model 创建、维度绑定、激活和停用采用模型写权限。
2. Model 清单、详情和绑定维度清单采用模型读权限。
3. 授权判断仍复用 `AuthorizationService.requireAnyRole`。
4. 原有业务校验顺序基本保持不变：先确认对象存在和权限，再执行重复编码、跨 Workspace 绑定、必需维度等业务校验。

## 测试覆盖

本阶段更新依赖预算模型夹具的后端集成测试，为模型准备和状态切换步骤增加管理员请求头。

新增预算模型授权测试：

1. 不带身份读取模型绑定维度返回 401。
2. `READ_ONLY` 可读取模型绑定维度。
3. `READ_ONLY` 不能创建预算模型，返回 403。

验证命令：

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

结果：通过；Tests run: 31, Failures: 0, Errors: 0, Skipped: 0。

## 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不是生产认证。
2. 前端尚未统一注入身份请求头，预算模型页面直接调用受保护 API 时可能返回 401 或 403。
3. 本阶段只保护预算模型模块；预算模板、预算填报和实际数导入写接口仍待后续阶段。
4. 写权限暂限定为 `BUDGET_ADMIN` 和 `METADATA_MANAGER`，避免预算填报角色修改模型结构。

## 关闭建议

建议关闭 SEC-003C。

关闭理由：预算模型 API 已完成读写授权接入，所有后端测试通过；未新增 migration，未删除文件，未修改前端 UI，未提交 PDF/OCR 全文，也未进入 ERP 直连、BI 图表或合并报表。

## 下一阶段建议

建议下一阶段进入 `SEC-003D`：预算模板 API 授权接入，只处理模板模块。
