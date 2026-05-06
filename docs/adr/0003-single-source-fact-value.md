# ADR-0003: Single Source Fact Value

状态：Accepted

日期：2026-05-06

## 背景

本项目必须吸收 SAP BPC 中 Category、Version、模型、维度和事实数据同源的思想。预算、实际数和预测不应被拆散到互相隔离的数据口径中，否则后续查询、汇总和预算实际对比会形成重复建模和口径漂移。

BPC-KB-002、BPC-KB-006、BPC-KB-008 和 BPC-KB-009 已明确 Actual、Budget、Forecast 应以 Category / Version 区分，并写入同源事实数据。

## 决策

MVP 采用同源事实数据模型：

1. `fact_value` 保存事实主记录、金额、来源、状态、批次和审计字段。
2. Account、Entity、Time、Category、Version 作为核心显式坐标。
3. 自定义维度坐标使用 `fact_value_axis` 承载。
4. 使用 `coordinate_hash` 表达同一模型下完整坐标的幂等键。
5. Budget、Actual、Forecast 不拆成不同事实表。
6. 模板和查询视图不拥有事实数据，只解析或读取 `fact_value`。

## 理由

1. 同源事实数据能让预算填报、实际数导入和查询汇总共享口径。
2. Category / Version 显式化后，预算、实际、预测可以在同一查询模型中比较。
3. 核心维度显式列能简化最常见查询；自定义维度轴表保留动态模型能力。
4. `coordinate_hash` 有助于导入幂等、重复校验和覆盖策略设计。

## 后果

正向影响：

1. BUD-007 填报和 BUD-009 实际数导入可以写入同一事实层。
2. BUD-008 查询与基础汇总可以基于统一事实口径实现。
3. BUD-010 预算与实际差异分析具备前置数据基础，但仍需用户明确批准后进入。

代价和风险：

1. 动态维度查询需要谨慎设计索引和查询路径。
2. 事实写入必须统一校验坐标完整性和权限状态。
3. 导入覆盖、冲销或版本化策略需要在 BUD-009 前进一步决策。

## 非目标

1. 本 ADR 不创建 migration。
2. 本 ADR 不固定最终物理表结构的全部字段。
3. 本 ADR 不批准提前开发预算执行差异分析。

## 验证

后续阶段验收时，应确认：

1. 填报、查询和导入都围绕 `fact_value` 建模。
2. Actual / Budget / Forecast 通过 Category / Version 表达。
3. 模板和报表不创建独立事实表。
