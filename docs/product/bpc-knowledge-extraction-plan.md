# BPC Knowledge Extraction Plan

阶段编号：PROJECT-BOOTSTRAP-001

本计划用于从 `C:\codex\budget-platform\docs\source\bpc-pdf` 的 SAP BPC 资料中建立项目知识包。所有抽取都必须遵守资料保护规则：不复制 PDF 原文，不提交 OCR 全文，不把扫描页或可还原原文的大段内容写入仓库。

## 抽取原则

1. 先建立术语和概念，再进入产品设计与模块路线图。
2. 每次只处理一个知识阶段，形成小而可审查的文档增量。
3. 抽取结果必须服务于自研平台设计，记录“吸收什么”和“规避什么”。
4. 对 SAP BPC 的复杂能力只做产品思想分析，不直接照搬复杂实现。
5. 当前预算平台优先预算主线，不在早期阶段引入合并报表、BI 图表、ERP 直连。

## 阶段计划

| 阶段编号 | 阶段名称 | 目标 | 主要来源 | 产出建议 |
| --- | --- | --- | --- | --- |
| BPC-KB-001 | 核心概念与术语 | 建立 BPC 知识包词汇表，覆盖环境、模型、维度、成员、类别、版本、模板、数据包、状态、流程等基础概念 | BPC420、BPC450、S4F80 | `docs/product/bpc-kb-001-core-terms.md` |
| BPC-KB-002 | 模型、维度、成员、层级 | 抽取模型与维度设计思想，识别自研平台的数据模型最小闭环 | BPC420、BPC450、S4F80 | `docs/product/bpc-kb-002-model-dimension.md` |
| BPC-KB-003 | 模板/Input Schedule | 分析 Input Schedule、模板、表单布局、数据提交体验，形成 Web Native 填报模板设计原则 | BPC420、BPC430 | `docs/product/bpc-kb-003-input-schedule.md` |
| BPC-KB-004 | 数据填报与 Work Status | 分析填报、保存、提交、审核、锁定、状态切片思想，抽取可简化的 Web 流程 | BPC420、BPC450、S4F80 | `docs/product/bpc-kb-004-input-work-status.md` |
| BPC-KB-005 | 查询、汇总、报表 | 分析查询、汇总、报表展示与计划读取方式，为基础查询和报表视图设计做准备 | BPC430、BPC450、s4f90 | `docs/product/bpc-kb-005-query-reporting.md` |
| BPC-KB-006 | 实际数导入/Data Manager | 分析 Data Manager、实际数导入、数据转换与同源数据思想，形成可观测、可审计的数据导入原则 | BPC420、S4F80、BPC450 | `docs/product/bpc-kb-006-actual-import.md` |
| BPC-KB-007 | 权限、状态、流程与协作 | 分析权限、状态、流程和协作机制，形成简化权限模型和流程治理原则 | BPC420、s4f90、BPC440 | `docs/product/bpc-kb-007-permission-workflow.md` |
| BPC-KB-008 | 用户痛点与自研规避原则 | 汇总 SAP BPC 复杂度来源和用户痛点，形成自研平台的反复杂度设计约束 | 全部 PDF | `docs/product/bpc-kb-008-pain-points.md` |
| BPC-KB-009 | 自研预算平台模块路线图 | 将知识包转化为自研平台模块路线图，明确近期、中期、远期边界 | BPC-KB-001 至 BPC-KB-008 | `docs/product/bpc-kb-009-platform-roadmap.md` |

## 抽取顺序

1. 先执行 BPC-KB-001，统一术语和中文命名。
2. 再执行 BPC-KB-002，建立预算平台核心元模型边界。
3. 再执行 BPC-KB-003 和 BPC-KB-004，形成填报模板与流程闭环。
4. 再执行 BPC-KB-005 和 BPC-KB-006，补充查询汇总与实际数导入。
5. 再执行 BPC-KB-007 和 BPC-KB-008，沉淀治理与规避原则。
6. 最后执行 BPC-KB-009，形成自研模块路线图。

## OCR 与引用策略

1. 本地可用工具不足时，应先建立 OCR 或可靠 PDF 文本抽取能力。
2. OCR 仅用于辅助理解，不得提交完整 OCR 文件。
3. 文档中引用 BPC 资料时，只记录文件名、页码或章节定位、结构化摘要和自研取舍。
4. 每个知识阶段必须记录可信度、未确认点和待复核页码。

## 阶段关闭标准

每个 BPC-KB 阶段必须满足：

1. 明确本阶段目标和输入来源。
2. 输出结构化摘要，而不是原文摘录。
3. 标注适合吸收的产品思想。
4. 标注需要规避或简化的 SAP BPC 痛点。
5. 给出对自研平台后续设计的影响。
6. 更新 `PROJECT_STEP_RECORD.md`。
