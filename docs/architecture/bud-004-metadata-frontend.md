# BUD-004: Metadata Frontend

阶段编号：BUD-004

生成日期：2026-05-07

本文件记录元数据前端阶段的实现范围、交互、验证和边界。本阶段只实现 Workspace、Dimension、Dimension Member 的基础管理界面，不进入预算模型、模板、填报、查询或导入。

## 1. 范围

本阶段实现：

1. Workspace 列表、创建、选择。
2. Dimension 列表、创建、选择。
3. Dimension Member 列表、创建、父节点选择。
4. 统一 API 类型和元数据 API client。
5. Vite `/api` proxy 到后端 `localhost:8080`。

本阶段不实现：

1. 元数据删除。
2. 成员拖拽排序。
3. 批量导入。
4. Budget Model UI。
5. Template UI。
6. Submission、Query、Import UI。

## 2. 前端结构

| 文件 | 职责 |
| --- | --- |
| `frontend/src/features/metadata/metadataApi.ts` | 元数据 API 类型和调用函数 |
| `frontend/src/App.tsx` | 元数据管理工作台 |
| `frontend/src/styles.css` | 元数据工作台布局和表格样式 |
| `frontend/vite.config.ts` | `/api` 代理到后端 |

## 3. 交互

界面按三列组织：

1. Workspaces：创建和选择预算空间。
2. Dimensions：在选中 workspace 下创建和选择维度。
3. Members：在选中 dimension 下创建成员，并选择 parent。

该结构与后端当前 API 保持一致，避免提前引入路由、复杂状态管理或业务模块。

## 4. API

前端调用 BUD-003 API：

1. `GET /api/metadata/workspaces`
2. `POST /api/metadata/workspaces`
3. `GET /api/metadata/dimensions?workspaceId=`
4. `POST /api/metadata/dimensions`
5. `GET /api/metadata/dimensions/{dimensionId}/members`
6. `POST /api/metadata/dimensions/{dimensionId}/members`

## 5. 验证

执行：

1. `pnpm type-check`
2. `pnpm lint`
3. `pnpm build`
4. `mvn test`

本阶段还进行了本地预览检查：

1. 前端：`pnpm dev --host 127.0.0.1` 可启动并监听 `127.0.0.1:5173`。
2. 后端：`mvn spring-boot:run -Dspring-boot.run.profiles=test` 和追加 `-Dspring-boot.run.useTestClasspath=true` 均未成功启动本地预览。

后端预览失败的真实原因是 `spring-boot:run` 仍使用主配置连接 PostgreSQL，Flyway 获取连接时报 `SQL State 28P01`，本机用户 `budget_platform` 密码认证失败。该问题不影响 `mvn test`，因为集成测试已通过 `src/test/resources/application-test.yml` 使用 H2 PostgreSQL 模式验证。

## 6. 边界

本阶段未新增：

1. 后端业务扩展。
2. migration。
3. 预算模型页面。
4. 模板页面。
5. 填报页面。
6. 查询页面。
7. 导入页面。

## 7. 后续

建议进入 BUD-005：预算模型管理。

BUD-005 应基于当前元数据后端和前端，实现预算模型创建、维度绑定和启停。
