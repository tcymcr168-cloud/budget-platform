# BUD-007: Budget Submission Baseline

阶段编号：BUD-007

生成日期：2026-05-07

本文件记录预算填报基础版的实现范围、数据结构、API、状态流、前端交互、验证和边界。本阶段首次引入统一事实数据写入，但只服务预算填报草稿与提交流程，不进入查询汇总、实际数导入、预算执行差异分析或 BI。

## 1. 范围

本阶段实现：

1. Submission Task 创建和查询。
2. 填报范围：Template + Entity + Time + Category + Version。
3. 预算填报值保存为草稿。
4. 状态流转：NOT_STARTED、DRAFT、SUBMITTED、RETURNED、APPROVED、LOCKED。
5. Fact Value 同源事实数据基础表。
6. 后端集成测试和前端填报基础 UI。

本阶段不实现：

1. 查询、汇总、报表。
2. 实际数导入。
3. 预算执行差异分析。
4. BI 图表。
5. ERP 直连。
6. 多级审批流。
7. 删除、解锁或重开。

## 2. 数据结构

新增 `V4__budget_submission_baseline.sql`：

1. `submission_task`
   - 绑定 `budget_template` 和 `budget_model`。
   - 以 Entity、Time、Category、Version 定义填报范围。
   - 保存 Owner、Reviewer、状态和退回原因。
   - 同一模板和同一范围唯一。
2. `fact_value`
   - 绑定 `budget_model`、`budget_template` 和 `submission_task`。
   - 使用 Account + Entity + Time + Category + Version 表达费用预算事实坐标。
   - `source_type` 当前为 `BUDGET_SUBMISSION`，为后续 Actual 导入同源扩展预留。
   - `value_status` 随任务状态推进。

## 3. API

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/submissions/tasks` | 创建填报任务 |
| GET | `/api/submissions/tasks?budgetTemplateId=` | 查询模板下任务 |
| GET | `/api/submissions/tasks/{taskId}` | 查询任务详情 |
| POST | `/api/submissions/tasks/{taskId}/values` | 保存或覆盖 Account 填报值 |
| GET | `/api/submissions/tasks/{taskId}/values` | 查询任务填报值 |
| POST | `/api/submissions/tasks/{taskId}/submit` | 提交任务 |
| POST | `/api/submissions/tasks/{taskId}/return` | 退回任务 |
| POST | `/api/submissions/tasks/{taskId}/approve` | 审核通过 |
| POST | `/api/submissions/tasks/{taskId}/lock` | 锁定任务 |

## 4. 状态规则

| 动作 | 允许来源状态 | 目标状态 | 事实值状态 |
| --- | --- | --- | --- |
| 创建任务 | 无 | NOT_STARTED | 无 |
| 保存值 | NOT_STARTED / DRAFT / RETURNED | DRAFT | DRAFT |
| 提交 | DRAFT / RETURNED | SUBMITTED | SUBMITTED |
| 退回 | SUBMITTED | RETURNED | 保持原状态，重新保存后回 DRAFT |
| 通过 | SUBMITTED | APPROVED | APPROVED |
| 锁定 | APPROVED | LOCKED | LOCKED |

已提交、已通过、已锁定任务不可继续保存填报值。

## 5. 校验规则

1. 只能基于 `ACTIVE` 模板创建填报任务。
2. Entity、Time、Category、Version 成员必须属于对应维度类型。
3. 上述成员所在维度必须绑定到模板所属预算模型。
4. Account 填报值必须来自模型绑定的 Account 维度。
5. 提交前必须至少有一条填报值。
6. 同一任务和同一 Account 只保留一条事实值，重复保存表示覆盖草稿。

## 6. 前端交互

前端新增填报基础区：

1. 在选中模板下按 Entity、Time、Category、Version 创建任务。
2. 选择 Account 并录入金额，保存为草稿。
3. 查看任务下已保存事实值。
4. 支持 Submit、Return、Approve、Lock。
5. 退回时可录入退回原因。

UI 仍保持单页工作台，不引入流程设计器或复杂权限矩阵。

## 7. 验证

执行：

1. `mvn test`
2. `pnpm type-check`
3. `pnpm lint`
4. `pnpm build`
5. `git status --short`
6. `git check-ignore`

验证结果：

1. 后端 18 个测试通过，含填报保存、提交、退回、重提、通过、锁定、已提交不可编辑、非激活模板拒绝和成员维度校验。
2. 前端 type-check、lint、build 通过。

## 8. 边界

本阶段未新增：

1. Query API/UI。
2. Import API/UI。
3. Budget vs Actual 差异分析。
4. BI 图表。
5. ERP 直连。
6. 合并报表。
7. 删除接口。

## 9. 后续

建议进入 BUD-008：预算查询与基础汇总。

BUD-008 应基于 `fact_value` 实现表格查询、基础明细和轻量汇总，不做 BI 图表、不做预算执行差异分析。
