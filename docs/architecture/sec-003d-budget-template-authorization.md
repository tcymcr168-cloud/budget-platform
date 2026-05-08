# SEC-003D 预算模板 API 授权接入

## 阶段目标

SEC-003D 在 SEC-003C 的授权模式基础上，只针对预算模板模块接入授权。本阶段保护 Budget Template 的创建、查询、轴配置、激活和停用接口，不扩展到预算填报或实际数导入。

## 授权策略

| API | 授权规则 |
| --- | --- |
| `POST /api/budget-templates` | 模板所属模型 Workspace 内 `BUDGET_ADMIN` 或 `TEMPLATE_DESIGNER` |
| `GET /api/budget-templates?budgetModelId=` | 模板所属模型 Workspace 内任一读取角色 |
| `GET /api/budget-templates/{budgetTemplateId}` | 模板所属 Workspace 内任一读取角色 |
| `POST /api/budget-templates/{budgetTemplateId}/axes` | 模板所属 Workspace 内 `BUDGET_ADMIN` 或 `TEMPLATE_DESIGNER` |
| `GET /api/budget-templates/{budgetTemplateId}/axes` | 模板所属 Workspace 内任一读取角色 |
| `POST /api/budget-templates/{budgetTemplateId}/activate` | 模板所属 Workspace 内 `BUDGET_ADMIN` 或 `TEMPLATE_DESIGNER` |
| `POST /api/budget-templates/{budgetTemplateId}/deactivate` | 模板所属 Workspace 内 `BUDGET_ADMIN` 或 `TEMPLATE_DESIGNER` |

读取角色包括：

1. `BUDGET_ADMIN`
2. `METADATA_MANAGER`
3. `TEMPLATE_DESIGNER`
4. `BUDGET_OWNER`
5. `BUDGET_REVIEWER`
6. `IMPORT_OPERATOR`
7. `READ_ONLY`

## 实现方式

`BudgetTemplateController` 解析 `X-User-Id` 与 `X-User-Roles`，并把 `CurrentUserContext` 传入 `BudgetTemplateService`。

`BudgetTemplateService` 在加载 Budget Model 或 Budget Template 后执行授权：

1. 模板创建、轴配置、激活和停用采用模板写权限。
2. 模板清单、详情和轴清单采用模板读权限。
3. 授权判断复用 `AuthorizationService.requireAnyRole`。
4. 原有模板业务校验保持不变：只允许 ACTIVE 模型建模板、轴必须来自同一模型、激活前必须有 ROW 和 COLUMN。

## 测试覆盖

本阶段更新依赖模板夹具的后端集成测试，为模板创建、轴配置、激活和停用增加管理员请求头。

新增预算模板授权测试：

1. 不带身份读取模板轴返回 401。
2. `READ_ONLY` 可读取模板轴。
3. `READ_ONLY` 不能创建模板，返回 403。

验证命令：

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

结果：通过；Tests run: 32, Failures: 0, Errors: 0, Skipped: 0。

## 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不是生产认证。
2. 前端尚未统一注入身份请求头，模板页面直接调用受保护 API 时可能返回 401 或 403。
3. 本阶段只保护模板模块；预算填报和实际数导入写接口仍待后续阶段。
4. 写权限暂限定为 `BUDGET_ADMIN` 和 `TEMPLATE_DESIGNER`，避免填报角色修改模板结构。

## 关闭建议

建议关闭 SEC-003D。

关闭理由：预算模板 API 已完成读写授权接入，所有后端测试通过；未新增 migration，未删除文件，未修改前端 UI，未提交 PDF/OCR 全文，也未进入 ERP 直连、BI 图表或合并报表。

## 下一阶段建议

建议下一阶段进入 `SEC-003E`：预算填报 API 授权接入，只处理填报模块。
