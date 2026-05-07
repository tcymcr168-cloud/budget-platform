# BUD-006: Budget Template Management

阶段编号：BUD-006

生成日期：2026-05-07

本文件记录预算模板管理阶段的实现范围、数据结构、API、前端交互、验证和边界。本阶段只实现单页 Web 模板定义和轴配置，不进入预算填报执行、事实数据写入、查询汇总或实际数导入。

## 1. 范围

本阶段实现：

1. Budget Template 创建、查询和状态切换。
2. Template Axis 配置，支持 ROW、COLUMN、FILTER。
3. Axis 引用 Budget Model 已绑定维度。
4. 激活模板前校验至少有一个 ROW 轴和一个 COLUMN 轴。
5. Flyway V3 预算模板基础表。
6. 后端集成测试和前端模板管理 UI。

本阶段不实现：

1. 填报任务。
2. 草稿保存或提交。
3. 事实数据表写入。
4. 查询、汇总或报表。
5. 实际数导入。
6. Excel 插件、复杂公式、脚本逻辑或多 sheet。

## 2. 数据结构

新增 `V3__budget_template_baseline.sql`：

1. `budget_template`
   - 归属 `budget_model`。
   - `code` 在同一 budget model 内唯一。
   - 状态为 `DRAFT`、`ACTIVE`、`INACTIVE`。
2. `budget_template_axis`
   - 归属 `budget_template`。
   - 引用 `budget_model_dimension`，确保轴只能使用模型已绑定维度。
   - 轴类型为 ROW、COLUMN、FILTER。
   - `member_selector` 先支持 `ALL_LEAF`、`ALL_MEMBERS`、`ROOT_ONLY` 等简单集合语义。

该结构吸收 BPC Input Schedule 的多维视图思想：模板保存的是模型、维度和布局配置，不保存预算事实数据。

## 3. API

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/budget-templates` | 创建模板 |
| GET | `/api/budget-templates?budgetModelId=` | 查询模型下模板 |
| GET | `/api/budget-templates/{budgetTemplateId}` | 查询模板详情 |
| POST | `/api/budget-templates/{budgetTemplateId}/axes` | 新增模板轴 |
| GET | `/api/budget-templates/{budgetTemplateId}/axes` | 查询模板轴 |
| POST | `/api/budget-templates/{budgetTemplateId}/activate` | 激活模板 |
| POST | `/api/budget-templates/{budgetTemplateId}/deactivate` | 停用模板 |

## 4. 后端规则

1. 只能为 `ACTIVE` budget model 创建模板。
2. Template code 在同一模型内唯一，并规范为大写。
3. Axis 引用的 model dimension 必须属于模板绑定的同一 budget model。
4. 同一模板不能重复使用同一 model dimension。
5. 激活模板前必须至少配置一个 ROW 轴和一个 COLUMN 轴。
6. 本阶段不提供删除、复制、复杂公式或写数接口。

## 5. 前端交互

前端在预算模型管理区下新增模板管理区：

1. 在选中 budget model 下创建和选择模板。
2. 从当前模型已绑定维度中选择未使用绑定作为模板轴。
3. 设置轴类型 ROW、COLUMN、FILTER。
4. 设置成员集合：All leaf、All members、Root only。
5. 支持激活和停用模板。

UI 继续采用单页工作台，不引入路由和复杂状态管理。

## 6. 验证

执行：

1. `mvn test`
2. `pnpm type-check`
3. `pnpm lint`
4. `pnpm build`
5. `git status --short`
6. `git check-ignore`

验证结果：

1. 后端 14 个测试通过，含模板创建、轴配置、激活、草稿模型拒绝、跨模型轴拒绝和缺少列轴拒绝。
2. 前端 type-check、lint、build 通过。

## 7. 边界

本阶段未新增：

1. Submission / Task API。
2. Fact Value API。
3. Query API/UI。
4. Import API/UI。
5. BI 图表。
6. ERP 直连。
7. 合并报表。
8. 删除接口。

## 8. 后续

建议进入 BUD-007：预算填报基础版。

BUD-007 应基于已激活模板生成填报任务、草稿和提交状态，首次引入事实数据写入，但仍不进入预算执行差异分析、BI 或实际数导入。
