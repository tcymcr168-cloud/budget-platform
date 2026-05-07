# BUD-003: Metadata Backend

阶段编号：BUD-003

生成日期：2026-05-07

本文件记录元数据后端阶段的实现范围、API、数据迁移、测试和边界。本阶段只实现元数据后端最小闭环，不实现前端元数据页面，不进入预算模型、模板、填报、查询或导入模块。

## 1. 范围

本阶段实现：

1. Workspace 基础创建和查询。
2. Dimension 基础创建、查询和按类型筛选。
3. Dimension Member 创建、查询、重命名、状态更新和单主父子层级维护。
4. Flyway V1 元数据基础表。
5. H2 PostgreSQL 模式集成测试。

本阶段不实现：

1. Budget Model API。
2. Template API。
3. Submission API。
4. Query API。
5. Import API。
6. 前端元数据管理页面。
7. 删除接口。

## 2. 依赖

新增后端依赖：

| 依赖 | 作用 |
| --- | --- |
| `spring-boot-starter-data-jpa` | JPA Entity、Repository、事务 |
| `flyway-core` | 数据库 migration |
| `postgresql` | PostgreSQL runtime driver |
| `h2` | 测试数据库 |

测试使用 `application-test.yml` 中的 H2 PostgreSQL 模式，验证 Flyway 与 JPA schema。

## 3. Migration

新增 `V1__metadata_baseline.sql`：

1. `budget_workspace`
2. `dimension`
3. `dimension_member`

索引：

1. `dimension(workspace_id, dimension_type)`
2. `dimension_member(dimension_id, parent_id)`

唯一约束：

1. `budget_workspace.code`
2. `dimension(workspace_id, code)`
3. `dimension_member(dimension_id, code)`

## 4. API

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/metadata/workspaces` | 创建预算空间 |
| GET | `/api/metadata/workspaces` | 查询预算空间列表 |
| POST | `/api/metadata/dimensions` | 创建维度 |
| GET | `/api/metadata/dimensions?workspaceId=&type=` | 查询维度列表 |
| GET | `/api/metadata/dimensions/{dimensionId}` | 查询维度详情 |
| POST | `/api/metadata/dimensions/{dimensionId}/members` | 创建成员 |
| GET | `/api/metadata/dimensions/{dimensionId}/members` | 查询成员列表 |
| PATCH | `/api/metadata/members/{memberId}` | 更新成员名称、父节点或状态 |

## 5. 规则

1. Workspace code 全局唯一，并规范为大写。
2. Dimension code 在同一 workspace 内唯一，并规范为大写。
3. Member code 在同一 dimension 内唯一，并规范为大写。
4. Member parent 必须属于同一 dimension。
5. Member 层级不允许循环。
6. Member 支持停用，但本阶段不做删除接口。
7. 单主层级通过 `parent_id` 表达。

## 6. 测试

后端新增集成测试：

1. 创建 workspace、dimension、root member 和 child member。
2. 重复 dimension code 返回 `CONFLICT`。
3. 跨维度 parent 返回 `BAD_REQUEST`。

基线测试：

1. Spring context loads。
2. 全局异常响应。
3. 健康检查。

## 7. 后续

建议进入 BUD-004：元数据前端。

BUD-004 应只实现元数据管理基础 UI，调用本阶段 API，不进入预算模型、模板、填报、查询或导入。
