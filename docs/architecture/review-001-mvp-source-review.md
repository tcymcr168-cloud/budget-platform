# REVIEW-001 MVP 已实现模块源码审查与治理收口

## 阶段定位

REVIEW-001 对 BUD-001 至 BUD-009 已实现的 MVP 主线进行源码审查、边界扫描、测试复核和治理收口。本阶段不进入 BUD-010 预算与实际差异分析，不新增业务模块，不删除文件。

## 审查范围

| 范围 | 内容 |
| --- | --- |
| 后端模块 | `common`、`metadata`、`budgetmodel`、`budgettemplate`、`budgetsubmission`、`budgetquery`、`budgetactual` |
| 前端模块 | `metadata`、`budgetModels`、`budgetTemplates`、`submissions`、`budgetQuery`、`actualImports` |
| 数据库 migration | `V1` 至 `V5` |
| 文档 | `docs/architecture`、`PROJECT_STEP_RECORD.md` |
| 边界 | PDF/OCR 保护、删除接口、ERP/BI/合并报表/差异分析越界扫描 |

## 阶段计划

| 项 | 内容 |
| --- | --- |
| 输入资料 | `AGENTS.md`、`PROJECT_STEP_RECORD.md`、BUD-001 至 BUD-009 架构文档、当前源码和 Git 状态 |
| 允许修改 | 当前已实现模块的小范围缺陷修复、对应测试、审查报告、`PROJECT_STEP_RECORD.md` |
| 禁止修改 | BUD-010 差异分析、ERP 直连、BI 图表、合并报表、删除文件、PDF/OCR 原文 |
| 验证命令 | `mvn test`、`pnpm type-check`、`pnpm lint`、`pnpm build`、`git status --short`、`git check-ignore`、边界关键词扫描 |

## 审查发现与处理

### 已修复：Actual 导入在多个 CUSTOM 维度下可能失败

问题：`ActualImportService` 在构造 `DimensionType -> dimensionId` 映射时，原实现会把模型所有绑定维度放入 `Collectors.toMap`。如果模型绑定多个 `CUSTOM` 维度，会出现重复 key 并抛出异常，导致 Actual CSV 导入无法校验。

处理：只收集 Actual 导入必需的 `ACCOUNT`、`ENTITY`、`TIME`、`CATEGORY`、`VERSION` 五类维度，忽略当前导入不消费的 `CUSTOM` 维度。

测试：新增 `validatesActualCsvWhenModelHasMultipleCustomDimensions`，覆盖模型绑定多个 CUSTOM 维度时仍可校验 Actual CSV。

### 已修复：填报退回后事实值状态未同步退回

问题：填报任务从 `SUBMITTED` 退回到 `RETURNED` 后，关联事实值仍保持 `SUBMITTED`。这会让按事实状态查询时看到已退回任务的旧提交数据。

处理：`SubmissionService.returnTask` 在任务退回时同步将该任务事实值状态更新为 `DRAFT`。

测试：增强 `savesSubmitsReturnsApprovesAndLocksSubmissionTask`，验证退回后事实值状态为 `DRAFT`，再提交、通过和锁定仍正常。

## 边界扫描结果

| 检查项 | 结果 |
| --- | --- |
| 删除接口 | 未发现 `@DeleteMapping` 或删除业务接口 |
| ERP 直连 | 未发现实现 |
| BI 图表 | 未发现实现；关键词误命中来自 `BigDecimal` 和 `Binding` |
| 合并报表 | 未发现实现 |
| 预算与实际差异分析 | 未发现实现 |
| 临时入口页 | 未发现 |
| PDF 原文 | 未修改，仍由 `.gitignore` 排除 |
| OCR 全文 | 未提交，缓存目录仍由 `.gitignore` 排除 |
| 构建产物 | `frontend/dist`、`backend/target` 仍被排除 |
| README | 存在历史本地修改，本阶段不纳入提交 |

## 测试结果

| 命令 | 结果 |
| --- | --- |
| `mvn test` | 通过；Tests run: 23, Failures: 0, Errors: 0, Skipped: 0 |
| `pnpm type-check` | 通过 |
| `pnpm lint` | 通过 |
| `pnpm build` | 通过 |

## 残余风险

1. 认证、授权、数据范围和持久化审计仍是后续独立阶段，不应伪装成已完成。
2. 查询仍是 MVP 服务层过滤，未做分页和数据库条件下推。
3. Actual 导入仍是固定表头 CSV，不支持转换规则表、撤销、冲销或覆盖治理。
4. 模板激活后结构仍可继续调整，后续需要模板版本或锁定策略。
5. BUD-010 差异分析未获明确批准前不得进入。

## 关闭建议

建议关闭 REVIEW-001。

关闭理由：已完成源码审查、边界扫描、两处小缺陷修复、测试复核和文档沉淀；未进入 BUD-010 或任何越界功能。
