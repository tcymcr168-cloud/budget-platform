# BUD-008 预算查询与基础汇总基线

## 阶段定位

BUD-008 在 BUD-007 已形成的 `fact_value` 统一事实表之上，提供预算数据的只读明细查询、基础维度汇总和 CSV 文本导出。

本阶段不新增数据库 migration，不写实际数导入，不做预算与实际差异分析，不做 BI 图表，不做 ERP 直连，不做合并报表。

## 设计目标

1. 以预算模型作为查询入口，保持模型驱动。
2. 以 Account、Entity、Time、Category、Version 作为当前 MVP 查询坐标。
3. 支持按 Entity、Time、Category、Version 和事实状态过滤。
4. 支持按 Account、Entity、Time、Category、Version 做基础金额汇总。
5. 提供轻量 CSV 文本导出，便于核对数据，不引入报表引擎。

## 后端实现

### 新增模块

`com.budgetplatform.budgetquery`

### API

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `GET` | `/api/budget-query/facts` | 查询事实明细 |
| `GET` | `/api/budget-query/summary` | 按指定维度基础汇总 |
| `GET` | `/api/budget-query/facts.csv` | 导出当前过滤条件下的 CSV 文本 |

### 查询参数

| 参数 | 必填 | 说明 |
| --- | --- | --- |
| `budgetModelId` | 是 | 查询所属预算模型 |
| `entityMemberId` | 否 | 过滤实体成员 |
| `timeMemberId` | 否 | 过滤期间成员 |
| `categoryMemberId` | 否 | 过滤类别成员 |
| `versionMemberId` | 否 | 过滤版本成员 |
| `status` | 否 | 过滤事实值状态 |
| `groupBy` | 汇总接口必填 | 汇总维度：`ACCOUNT`、`ENTITY`、`TIME`、`CATEGORY`、`VERSION` |

### 数据来源

本阶段只读取 `fact_value`，来源类型当前为 `BUDGET_SUBMISSION`。后续 BUD-009 引入实际数导入时，可以继续复用同一事实表思想，但不得在本阶段提前实现 Actual 导入或差异分析。

## 前端实现

前端在现有 React/Vite 工作台新增预算查询区域：

1. 使用预算模型作为查询上下文。
2. 提供 Entity、Time、Category、Version、Status 和 Group By 过滤控件。
3. 提供明细结果表。
4. 提供基础汇总表。
5. 提供 CSV 文本预览。

界面仍保持工作台形态，不新增临时入口页，不引入图表组件。

## 测试覆盖

后端新增 `BudgetQueryControllerIntegrationTests`，通过 API 组合创建完整 fixture：

1. workspace；
2. 五个系统维度；
3. 维度成员；
4. budget model；
5. model dimension binding；
6. active model；
7. budget template；
8. template axes；
9. active template；
10. submission task；
11. fact value；
12. submit；
13. approve。

随后验证：

1. 明细查询可返回 approved fact；
2. 按 Account 汇总金额正确；
3. CSV 导出包含表头和事实行。

## 风险与限制

1. 当前查询使用按模型读取后在服务层过滤，适合 MVP 小数据量验证；后续需要按常用筛选条件下推数据库查询。
2. 汇总为基础金额求和，不处理层级递归汇总、币种、单位、符号翻转或复杂账户属性。
3. CSV 是轻量文本导出，不是正式报表包。
4. 当前没有鉴权上下文，查询权限需要在权限阶段补齐。
5. 本阶段没有 Budget vs Actual 对比，避免提前进入 BUD-010 范围。

## 关闭建议

如果 `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build` 均通过，并确认未新增 migration、未提交 PDF/OCR 全文、未进入 BI 或差异分析，则建议关闭 BUD-008。
