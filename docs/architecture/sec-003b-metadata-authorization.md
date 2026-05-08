# SEC-003B 元数据 API 授权接入

## 阶段目标

SEC-003B 在 SEC-003 统一授权服务基础上，只针对元数据模块接入授权。本阶段不扩展到预算模型、模板、填报或实际数导入写接口，避免一次性跨多个业务模块改造。

## 授权策略

| API | 授权规则 |
| --- | --- |
| `POST /api/metadata/workspaces` | 请求头必须包含 `BUDGET_ADMIN` |
| `GET /api/metadata/workspaces` | 请求头必须包含 `BUDGET_ADMIN` |
| `POST /api/metadata/dimensions` | Workspace 内 `BUDGET_ADMIN` 或 `METADATA_MANAGER` |
| `GET /api/metadata/dimensions` | Workspace 内任一读取角色 |
| `GET /api/metadata/dimensions/{dimensionId}` | Dimension 所属 Workspace 内任一读取角色 |
| `POST /api/metadata/dimensions/{dimensionId}/members` | Dimension 所属 Workspace 内 `BUDGET_ADMIN` 或 `METADATA_MANAGER` |
| `GET /api/metadata/dimensions/{dimensionId}/members` | Dimension 所属 Workspace 内任一读取角色 |
| `PATCH /api/metadata/members/{memberId}` | Member 所属 Workspace 内 `BUDGET_ADMIN` 或 `METADATA_MANAGER` |

读取角色包括：

1. `BUDGET_ADMIN`
2. `METADATA_MANAGER`
3. `TEMPLATE_DESIGNER`
4. `BUDGET_OWNER`
5. `BUDGET_REVIEWER`
6. `IMPORT_OPERATOR`
7. `READ_ONLY`

## 实现方式

`MetadataController` 只负责解析 `X-User-Id` 与 `X-User-Roles`，并把 `CurrentUserContext` 传入 `MetadataService`。

`MetadataService` 在业务对象加载后执行授权：

1. Workspace 创建和 Workspace 清单属于引导期管理操作，采用 `requireHeaderAdmin`。
2. Dimension 创建、Member 创建和 Member 更新采用元数据写权限。
3. Dimension 和 Member 查询采用元数据读权限。
4. 授权失败时统一返回 `UNAUTHORIZED` 或 `FORBIDDEN`。

## 测试覆盖

本阶段更新所有依赖元数据夹具的后端集成测试，为元数据准备步骤增加管理员请求头。

新增元数据授权测试：

1. 不带身份读取成员返回 401。
2. `READ_ONLY` 请求头可读取元数据成员。
3. `READ_ONLY` 不能创建 Dimension，返回 403。

验证命令：

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

结果：通过；Tests run: 30, Failures: 0, Errors: 0, Skipped: 0。

## 风险与限制

1. 请求头身份上下文仍是内部技术验证机制，不是生产认证。
2. Workspace 创建阶段仍依赖请求头管理员进行引导，后续生产登录完成后应收敛到正式身份源。
3. 本阶段只保护元数据 API；预算模型、模板、填报、实际数导入写接口仍待后续阶段逐步接入授权。
4. 元数据读权限未按 Entity 数据范围过滤，因为 Dimension/Member 属于口径主数据，不是事实数据。

## 关闭建议

建议关闭 SEC-003B。

关闭理由：元数据 API 已完成读写授权接入，所有后端测试通过；未新增 migration，未删除文件，未修改前端 UI，未提交 PDF/OCR 全文，也未进入 ERP 直连、BI 图表或合并报表。

## 下一阶段建议

建议下一阶段进入 `SEC-003C`：预算模型 API 授权接入，只处理预算模型模块。
