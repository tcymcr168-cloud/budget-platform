# BUD-005: Budget Model Management

阶段编号：BUD-005

生成日期：2026-05-07

本文件记录预算模型管理阶段的实现范围、数据结构、API、前端交互、验证和边界。本阶段只实现预算模型创建、维度绑定和启停，不进入模板、填报、查询、实际数导入或预算执行差异分析。

## 1. 范围

本阶段实现：

1. Budget Model 创建、查询和状态切换。
2. Budget Model 与 Dimension 的绑定。
3. 激活前校验必需维度：Account、Entity、Time、Category、Version。
4. Flyway V2 预算模型基础表。
5. 后端集成测试和前端预算模型管理 UI。

本阶段不实现：

1. 模板 / Input Schedule。
2. 预算填报。
3. 查询、汇总或报表。
4. 实际数导入。
5. 预算执行差异分析。
6. 维度解绑、删除或复杂权限。

## 2. 数据结构

新增 `V2__budget_model_baseline.sql`：

1. `budget_model`
   - 归属 `budget_workspace`。
   - `code` 在同一 workspace 内唯一。
   - 状态为 `DRAFT`、`ACTIVE`、`INACTIVE`。
2. `budget_model_dimension`
   - 绑定 `budget_model` 与 `dimension`。
   - 记录维度角色 `dimension_role`，来源于维度类型。
   - 记录是否填报必需和显示顺序。

该结构延续 BPC 的模型驱动思想：预算模型不是一张固定业务表，而是一个治理容器，绑定一组可复用维度后再进入模板和填报。

## 3. API

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/budget-models` | 创建预算模型 |
| GET | `/api/budget-models?workspaceId=` | 查询 workspace 下预算模型 |
| GET | `/api/budget-models/{budgetModelId}` | 查询预算模型详情 |
| POST | `/api/budget-models/{budgetModelId}/dimensions` | 绑定维度 |
| GET | `/api/budget-models/{budgetModelId}/dimensions` | 查询已绑定维度 |
| POST | `/api/budget-models/{budgetModelId}/activate` | 激活预算模型 |
| POST | `/api/budget-models/{budgetModelId}/deactivate` | 停用预算模型 |

## 4. 后端规则

1. Budget Model code 在同一 workspace 内唯一，并规范为大写。
2. 绑定维度必须属于同一 workspace。
3. 同一模型不能重复绑定同一维度。
4. 激活模型前必须绑定 Account、Entity、Time、Category、Version 五类维度。
5. 本阶段不提供删除接口，避免误删元数据骨架。

## 5. 前端交互

前端在元数据工作台下新增预算模型管理区：

1. 在选中 workspace 下创建和选择预算模型。
2. 从当前 workspace 的维度列表中选择未绑定维度。
3. 绑定维度时可设置是否填报必需和显示顺序。
4. 支持激活和停用模型。

UI 继续保持工作台式布局，不引入路由、复杂状态管理或阶段外模块。

## 6. 验证

执行：

1. `mvn test`
2. `pnpm type-check`
3. `pnpm lint`
4. `pnpm build`
5. `git status --short`
6. `git check-ignore`

验证结果：

1. 后端 10 个测试通过，含预算模型创建、绑定、激活、重复编码、跨 workspace 绑定和缺失必需维度校验。
2. 前端 type-check、lint、build 通过。

## 7. 边界

本阶段未新增：

1. Template API/UI。
2. Submission API/UI。
3. Query API/UI。
4. Import API/UI。
5. BI 图表。
6. ERP 直连。
7. 合并报表。
8. 删除接口。

## 8. 后续

建议进入 BUD-006：预算模板管理。

BUD-006 应基于已激活预算模型和绑定维度，设计模板基础结构和模板字段布局，不进入预算填报执行。
