# BPC-KB-001: Core Concepts And Terms

阶段编号：BPC-KB-001

生成日期：2026-05-06

本文件是 SAP BPC 核心概念与术语抽取。内容基于本地 OCR 缓存和 PDF 页码定位生成，只保留结构化摘要、术语解释和自研取舍，不复制 PDF 原文或 OCR 全文。

## 1. 来源与可信度

| 来源 | 用途 | OCR 状态 |
| --- | --- | --- |
| BPC420 Standard Administration and Planning Configuration | 核心术语、模型、维度、成员、模板、Work Status、Data Manager、Script Logic | 全量 OCR 成功，379 页 |
| BPC430 Reporting and Planning | 报表、查询、成员、维度、输入计划相关术语 | 全量 OCR 成功，231 页 |
| BPC440 Consolidation | 合并相关术语、Business Rule、Work Status 参考 | 全量 OCR 成功，267 页 |
| BPC450 Embedded BPC for BW/4HANA | Embedded BPC、模型、Work Status、Data Access 参考 | 全量 OCR 成功，290 页 |
| S4F80 BPC Optimized for S/4HANA | S/4 优化、Actual、Work Status、模型参考 | 全量 OCR 成功，230 页 |
| s4f90 Embedded Consolidation | Embedded Consolidation、Business Rule、Consolidation 参考 | 全量 OCR 成功，343 页 |

可信度说明：OCR 全量成功，但仍可能存在识别误差。后续 BPC-KB 阶段需要继续页码复核。本文中的“来源页”均指 OCR 定位页码。

## 2. 核心概念总览

SAP BPC 的产品思想可以抽象为一个“模型驱动的多维预算与合并平台”：

1. 用 Environment/Model 组织业务场景和数据域。
2. 用 Dimension/Member/Hierarchy 描述预算口径。
3. 用 Category/Version/Time 区分预算、实际、预测和周期。
4. 用 Input Schedule 和 Report 承载填报与查询体验。
5. 用 Work Status、Data Access、Process 管控协作与状态。
6. 用 Data Manager、Transformation、Conversion 承载数据导入与转换。
7. 用 Script Logic、Business Rule 等机制承载复杂计算和合并规则。

自研平台应吸收前 4 类作为预算管理主线，同时谨慎简化第 5 至第 7 类，避免变成黑盒流程、复杂脚本和复杂权限矩阵。

## 3. 术语表

| 术语 | 中文建议名 | BPC 含义摘要 | 自研平台取舍 | 主要来源 |
| --- | --- | --- | --- | --- |
| Environment | 环境 / 应用集 | BPC 中较高层级的组织容器，用于承载模型、维度、权限和配置边界。 | 自研平台可映射为租户或预算应用空间；早期 MVP 建议只保留“预算空间”概念，避免多环境复杂治理。 | BPC420 p5, p19-p24；BPC450 p5, p19；s4f90 p29-p31，OCR |
| Model | 模型 | 承载一组维度、数据结构和业务用途的多维数据模型。 | 必须吸收。自研平台核心应以 Budget Model 组织预算/实际事实数据和维度组合。 | BPC420 p19-p22；BPC440 p10-p17；BPC450 p13-p14；s4f90 p20-p21，OCR |
| Dimension | 维度 | 描述数据分析和填报口径的主数据轴，如 Account、Entity、Time、Category。 | 必须吸收。应建立统一维度定义、维度类型、属性和层级，不依赖 Excel 插件。 | BPC420 p19-p22；BPC430 p16-p21；BPC440 p16-p17，OCR |
| Member | 成员 | 维度中的具体取值，例如某个科目、组织、期间或类别。 | 必须吸收。成员应支持编码、名称、启停、属性和层级归属。 | BPC420 p20-p22, p31；BPC430 p5-p21，OCR |
| Hierarchy | 层级 | 维度成员之间的父子关系，用于汇总、导航和权限口径。 | 必须吸收。MVP 先支持单主层级与基础汇总，避免过早引入多层级复杂权限联动。 | BPC420 p20-p22, p56-p57；BPC430 p47, p67；BPC450 p110-p127，OCR |
| Account | 科目 | 常见核心维度，用于表达财务科目、指标或账户结构。 | 必须吸收。自研平台中 Account 是预算指标和实际数同源事实的关键维度。 | BPC420 p19-p20, p30-p31；BPC440 p16-p18，OCR |
| Entity | 组织 / 实体 | 常见核心维度，用于表达公司、部门、成本中心或合并实体。 | 必须吸收。早期建议用组织维度承载填报责任和汇总口径。 | BPC420 p20, p31, p55-p56；S4F80 p96，OCR |
| Time | 时间 | 周期维度，用于年度、季度、月份等预算与实际期间。 | 必须吸收。预算、实际、预测都应共享统一期间维度。 | BPC420 p11-p12, p19, p37；BPC430 p38-p39，OCR |
| Category | 类别 | 区分 Actual、Budget、Forecast 等数据类别的核心维度。 | 必须吸收。自研平台应明确 Category，不把预算、实际、预测拆成孤立表。 | BPC420 p55-p56, p75；BPC440 p25-p39；S4F80 p41-p44，OCR |
| Version | 版本 | 表示版本、场景、发布批次或模型版本的概念。 | 必须吸收但需简化。MVP 可用 Version 表示预算版本，Category 表示数据类别。 | BPC420 p1, p12, p22；BPC450 p11-p14；s4f90 p10-p12，OCR |
| Actual | 实际数 | 实际业务或财务结果数据，通常用于与预算/预测同口径比较。 | 必须吸收。实际数导入要与预算共享事实模型和维度口径。 | S4F80 p3, p19-p21, p50；BPC420 p12, p147；BPC450 p49, p66，OCR |
| Budget | 预算 | 计划编制产生的数据类别或场景。 | 必须吸收。Budget 应作为 Category 或业务场景进入统一事实数据。 | BPC420 p11-p12, p55-p56；BPC430 p18, p23，OCR |
| Forecast | 预测 | 滚动预测或预测版本相关数据。 | 中期吸收。MVP 先保留 Category/Version 能力，预测流程可后置。 | BPC420 p11-p12, p55-p56；BPC430 p15, p39；BPC450 p141，OCR |
| Input Schedule | 输入计划 / 填报模板 | BPC 中面向用户填报的表单或计划输入界面。 | 必须吸收思想，但规避 Excel 插件依赖。自研平台应做 Web Native 模板与填报。 | BPC420 p18, p32, p41, p133, p320；BPC430 p57, p82, p209，OCR |
| Report | 报表 / 查询 | 用于读取、展示、汇总和分析多维数据。 | 必须吸收基础查询与汇总，不提前建设复杂 BI 图表。 | BPC430 多处高频；BPC420 p11-p15；BPC440 p50-p60，OCR |
| Work Status | 工作状态 | BPC 中用于控制数据区域提交、锁定、审核或状态推进的机制。 | 吸收“状态治理”思想，规避难懂的切片锁定。自研平台先做简单填报状态和锁定范围。 | BPC420 p82-p83, p109；BPC450 p98-p99；S4F80 p146-p147，OCR |
| Data Access | 数据访问 | 与用户、角色、维度成员和数据区域访问相关的控制。 | 吸收最小权限思想，规避复杂多维权限矩阵。MVP 先以组织/模板/版本授权为主。 | BPC420 p90, p97-p98；BPC450 p56, p192；s4f90 p97，OCR |
| Data Manager | 数据管理器 | BPC 中用于导入、转换、执行数据处理包的能力集合。 | 只吸收“可配置导入流程”思想，必须规避黑盒执行。自研导入要可预览、可审计、可追踪。 | BPC420 p13, p25-p26, p73, p78；BPC430 p12-p25，OCR |
| Transformation | 转换 | 数据导入时字段、维度、成员或结构映射过程。 | 必须吸收。自研实际数导入应有显式映射配置和校验报告。 | BPC420 p75-p76, p161-p162；BPC440 p50-p54，OCR |
| Conversion | 转换规则 / 换算 | 通常与导入过程中的转换、映射或换算规则相关。 | 吸收为导入映射规则的一部分，避免隐藏在脚本和黑盒任务中。 | BPC420 p175-p177；BPC440 p52-p54；s4f90 p111，OCR |
| Script Logic | 脚本逻辑 | BPC 中用于表达计算、分摊、派生等复杂逻辑的脚本机制。 | 作为规避对象。自研平台早期不引入通用脚本语言，先用可解释的规则配置和服务端代码。 | BPC420 p115-p121；BPC430 p108；BPC440 p49-p54，OCR |
| Business Rule | 业务规则 | BPC 中承载特定业务计算、合并或处理逻辑的规则。 | 只吸收“规则显式化”思想，不在预算 MVP 引入复杂合并规则引擎。 | BPC440 p16, p29-p30；s4f90 p21-p25；BPC420 p256，OCR |
| Consolidation | 合并 | BPC 的合并报表与集团合并能力域。 | 当前不进入实现。仅作为远期边界和术语背景，避免过早建设合并报表。 | BPC440 p1, p5-p8；s4f90 p1, p5-p9；BPC420 p9-p12，OCR |
| Process | 流程 | BPC 中与业务流程、任务推进、协作控制相关的概念。 | 吸收轻量流程思想。MVP 先做填报状态流，不做复杂审批流。 | BPC420 p16, p29；BPC440 p47；S4F80 p146-p147，OCR |

## 4. 自研平台核心命名建议

| 自研概念 | 对应 BPC 思想 | MVP 命名建议 |
| --- | --- | --- |
| 预算空间 | Environment | `Budget Workspace` / `预算空间` |
| 预算模型 | Model | `Budget Model` / `预算模型` |
| 维度 | Dimension | `Dimension` / `维度` |
| 维度成员 | Member | `Dimension Member` / `维度成员` |
| 成员层级 | Hierarchy | `Hierarchy` / `层级` |
| 数据类别 | Category | `Category` / `类别` |
| 预算版本 | Version | `Version` / `版本` |
| 填报模板 | Input Schedule | `Input Template` / `填报模板` |
| 填报任务 | Process + Work Status | `Submission Task` / `填报任务` |
| 填报状态 | Work Status | `Submission Status` / `填报状态` |
| 数据导入任务 | Data Manager | `Import Job` / `导入任务` |
| 导入映射 | Transformation / Conversion | `Import Mapping` / `导入映射` |

## 5. 必须吸收的设计原则

1. 模型驱动：预算数据不应散落在模板和报表中，应由模型定义维度组合和事实数据口径。
2. 维度驱动：科目、组织、期间、类别、版本等口径必须主数据化。
3. 成员与层级驱动汇总：汇总应来自层级定义，而不是模板里的硬编码单元格。
4. 类别和版本分离：Actual/Budget/Forecast 与预算版本要有清晰语义，不应混为一个字段。
5. 模板只是入口：填报模板服务于用户输入，但不能成为数据结构本身。
6. 实际数与预算同源：Actual 和 Budget 应进入共享事实模型，便于后续查询和差异分析。

## 6. 必须规避的复杂形态

1. Excel 插件依赖：Input Schedule 思想可以吸收，但实现必须 Web Native。
2. 黑盒 Data Manager：导入过程必须可预览、可校验、可审计。
3. 复杂 Script Logic：早期不要提供通用脚本能力，避免不可维护的预算逻辑。
4. Work Status 切片锁定复杂化：只吸收状态和锁定思想，MVP 做简单、可解释的提交状态。
5. 多维权限矩阵复杂化：先做组织、角色、模板、版本层面的清晰授权。
6. 过早建设合并报表：Consolidation 只作为远期参考。
7. 过早建设 BI 图表、ERP 直连和预算执行差异分析：这些能力必须等主线稳定后再进入。

## 7. 对后续阶段的影响

1. BPC-KB-002 应重点展开 Model、Dimension、Member、Hierarchy。
2. BPC-KB-003 应围绕 Input Schedule 抽取模板结构和 Web 化取舍。
3. BPC-KB-004 应围绕填报状态、Work Status 简化和协作边界展开。
4. BPC-KB-006 应围绕 Data Manager、Transformation、Conversion 抽取导入原则。
5. PRODUCT-001 和 ARCH-001 必须把“预算与实际同源事实数据”作为核心架构约束。

## 8. 待复核问题

1. OCR 页码与 PDF 阅读器页码可能存在封面或目录偏移，后续引用时需抽样复核。
2. BPC420 中部分 Consolidation 命中来自课程总称，不代表该文档主要讲合并实现。
3. Version 与 Category 在不同 BPC 语境中可能存在交叠，后续模型设计需明确自研语义。
4. Business Rule 在合并语境中更强，预算 MVP 阶段只保留规则显式化思想。
