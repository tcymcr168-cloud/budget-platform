# BUD-010 预算与实际差异分析基线

## 阶段定位

BUD-010 在 BUD-008 查询与 BUD-009 实际数导入的基础上，建立最小版 Budget vs Actual 差异分析。本阶段只做同源事实表上的表格型差异查询，不引入 BI 图表、仪表盘、ERP 直连、合并报表或复杂权限矩阵。

## 设计目标

1. 复用 `fact_value` 同源事实表，不新增差异结果表。
2. 由用户显式选择预算 Category 与实际 Category，避免隐式黑盒口径。
3. 支持预算 Version 与实际 Version 可选过滤。
4. 按 Account + Entity + Time 聚合预算金额与实际金额。
5. 输出预算金额、实际金额、差异金额和差异率。
6. 默认仅纳入 `APPROVED` 与 `LOCKED` 状态事实，避免草稿和提交中数据污染分析。

## API

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `GET` | `/api/budget-query/variance` | 查询预算与实际差异表 |

## 查询参数

| 参数 | 必填 | 说明 |
| --- | --- | --- |
| `budgetModelId` | 是 | 预算模型 |
| `budgetCategoryMemberId` | 是 | 预算 Category 成员 |
| `actualCategoryMemberId` | 是 | 实际 Category 成员 |
| `budgetVersionMemberId` | 否 | 预算 Version 成员 |
| `actualVersionMemberId` | 否 | 实际 Version 成员 |
| `entityMemberId` | 否 | Entity 过滤 |
| `timeMemberId` | 否 | Time 过滤 |
| `status` | 否 | 事实状态；不传时默认纳入 `APPROVED` 与 `LOCKED` |

## 返回字段

| 字段 | 说明 |
| --- | --- |
| `accountMemberId` / `accountCode` / `accountName` | Account 坐标 |
| `entityMemberId` / `entityCode` / `entityName` | Entity 坐标 |
| `timeMemberId` / `timeCode` / `timeName` | Time 坐标 |
| `budgetAmount` | 预算金额合计 |
| `actualAmount` | 实际金额合计 |
| `varianceAmount` | `actualAmount - budgetAmount` |
| `variancePercent` | `varianceAmount / budgetAmount * 100`；预算为 0 时返回 `null` |
| `budgetLineCount` / `actualLineCount` | 两侧事实行数 |

## 前端实现

前端在现有工作台新增 Budget vs Actual 区域：

1. 选择 Entity、Time、预算 Category、实际 Category、预算 Version、实际 Version 和状态。
2. 点击 Analyze 调用 `/api/budget-query/variance`。
3. 展示表格型差异结果：Account、Entity、Time、Budget、Actual、Variance、Variance %、Lines。
4. 不引入图表组件，不新增临时入口页。

## 测试覆盖

新增后端集成测试覆盖：

1. 创建完整五维模型。
2. 创建 Budget Category 与 Actual Category。
3. 写入一条已审核预算事实。
4. 通过 Actual CSV 导入并提交一条实际事实。
5. 查询 `/api/budget-query/variance`，验证预算金额、实际金额、差异额、差异率和两侧行数。

## 风险与限制

1. 当前聚合粒度固定为 Account + Entity + Time，不支持用户自定义 group by。
2. 当前差异率以预算为分母，预算为 0 时返回 `null`。
3. 当前仍不做图表、趋势、钻取或执行分析看板。
4. 当前未实现权限、数据范围、期间锁定和审计持久化增强。
5. 查询仍是 MVP 服务层过滤，后续需要分页、索引和数据库条件下推。

## 关闭建议

如果 `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build` 均通过，并确认未提交 PDF/OCR 全文、构建产物、ERP 直连、BI 图表或合并报表，则建议关闭 BUD-010。
