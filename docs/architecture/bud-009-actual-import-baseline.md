# BUD-009 实际数导入基线

## 阶段定位

BUD-009 建立 Actual CSV 导入的最小闭环：上传 CSV 内容、按固定字段映射到模型维度、执行成员与金额校验、生成批次和行级错误、用户确认后写入同源 `fact_value`。

本阶段吸收 SAP BPC Data Manager 的导入批次、转换、校验、日志思想，但明确规避黑盒 Data Manager、复杂 Transformation/Conversion 文件语法、ERP 直连、BI 图表、合并报表和预算执行差异分析。

## 范围边界

| 项 | 本阶段处理 |
| --- | --- |
| CSV 导入 | 支持固定表头文本导入 |
| 字段映射 | 固定表头：`account,entity,time,category,version,amount` |
| 成员转换 | 直接按成员编码匹配当前模型绑定维度 |
| 校验 | 表头、成员、Category=ACTUAL、金额、批次内重复坐标 |
| 预览 | 保存批次和行级校验结果 |
| 提交 | 无错误批次可提交，写入 `fact_value` |
| ERP 直连 | 不进入 |
| 差异分析 | 不进入 |
| BI 图表 | 不进入 |

## 数据结构

### actual_import_batch

记录一次实际数导入批次：

1. 预算模型；
2. 文件名；
3. 操作人；
4. 状态；
5. 总行数、有效行数、错误行数；
6. 有效总金额；
7. 错误报告；
8. 创建和更新时间。

### actual_import_row

记录批次内每一行的解析和校验结果：

1. 行号；
2. Account、Entity、Time、Category、Version 外部编码；
3. 金额；
4. 行状态；
5. 错误信息；
6. 匹配到的维度成员。

### fact_value 扩展

为了支持预算填报和实际数导入同源：

1. `budget_template_id` 允许为空；
2. `submission_task_id` 允许为空；
3. 新增 `import_batch_id`；
4. `source_type` 增加 `ACTUAL_IMPORT`。

预算填报事实仍带 `submission_task_id` 和 `budget_template_id`；实际数导入事实带 `import_batch_id`。

## API

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `POST` | `/api/actual-imports/validate` | 校验 CSV 并生成导入批次与行级结果 |
| `POST` | `/api/actual-imports/{batchId}/commit` | 提交无错误批次并写入事实表 |
| `GET` | `/api/actual-imports/batches?budgetModelId=` | 查询模型下导入批次 |
| `GET` | `/api/actual-imports/batches/{batchId}/rows` | 查询批次行级结果 |

## 导入状态

| 状态 | 说明 |
| --- | --- |
| `VALIDATED` | 已完成校验，可能有错误；有错误不可提交 |
| `COMMITTED` | 已写入同源事实表 |
| `FAILED` | 表头或文件结构等关键错误导致批次失败 |

## 前端实现

前端在现有工作台新增 Actual CSV Import 区域：

1. 输入文件名、操作人和 CSV 文本；
2. 点击 Validate 生成批次和行级结果；
3. 批次列表展示状态、有效行、错误行和总金额；
4. 行表展示每行坐标、金额、状态和错误；
5. 点击 Commit 将无错误批次写入 `fact_value`，随后刷新查询区。

前端不保存业务文件，不新增临时入口页，不引入图表。

## 测试覆盖

新增 `ActualImportControllerIntegrationTests`：

1. 创建完整五维模型并激活；
2. 校验合法 Actual CSV；
3. 提交批次；
4. 通过预算查询 API 验证 `sourceType=ACTUAL_IMPORT` 和 `importBatchId`；
5. 校验错误 CSV 可生成错误报告；
6. 错误批次提交被阻断。

## 失败与修复记录

首轮 `mvn test` 失败：

1. 失败测试：`ActualImportControllerIntegrationTests` 两个用例。
2. 真实错误：测试用贪婪正则从校验响应中提取了最后一个 `id`，实际拿到的是 `actual_import_row.id`，导致 commit 请求使用 row id。
3. 失败表现：`POST /api/actual-imports/{rowId}/commit` 返回 404，错误信息为 `Actual import batch was not found`。
4. 修复方式：将测试 ID 提取改为非贪婪匹配，确保获取响应中的 batch id。
5. 修复后：`mvn test` 通过，Tests run: 22, Failures: 0, Errors: 0, Skipped: 0。

## 风险与限制

1. 当前 CSV 仅支持固定表头，不提供可视化字段映射编辑器。
2. 成员转换只按成员编码精确匹配，不支持外部编码转换表。
3. Actual 分类强制要求 Category 成员编码为 `ACTUAL`。
4. 当前提交后不支持撤销、冲销或覆盖策略。
5. 当前不处理权限、期间关闭、锁定范围例外或审计持久化增强。
6. 查询会看到 Actual 与 Budget 同源事实，但本阶段不计算差异。

## 关闭建议

如果 `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build` 均通过，并确认未提交 PDF/OCR 全文、构建产物或 README 历史改动，则建议关闭 BUD-009。
