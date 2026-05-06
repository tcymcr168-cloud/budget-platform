# BPC-KB-008: Pain Points And Design Guardrails

阶段编号：BPC-KB-008

生成日期：2026-05-06

本文件基于 BPC-KB-001 至 BPC-KB-007 的资料抽取结果，归纳 SAP BPC 对自研 Web Native 全面预算平台的核心痛点、规避原则和产品护栏。内容只保留结构化摘要和设计取舍，不复制 PDF 原文或 OCR 全文。

## 1. 本阶段结论

自研平台不能只“复刻 BPC 功能名”，而要吸收其预算管理底层思想，同时主动规避造成复杂、黑盒和用户依赖插件的实现形态。

必须吸收的主线：

1. 模型驱动。
2. 维度驱动。
3. 成员与层级驱动汇总。
4. Category / Version 分离。
5. 模板绑定模型与维度口径。
6. 填报状态与责任范围。
7. Actual 与 Budget 同源事实数据。

必须规避的主线：

1. Excel / Office 插件依赖。
2. 黑盒 Data Manager。
3. 复杂 Script Logic。
4. 难懂 Work Status 切片锁定。
5. 复杂多维权限矩阵。
6. 过早建设 BI 图表、合并报表、ERP 直连和预算执行差异分析。

## 2. 来源定位

| 痛点或护栏 | 来源 |
| --- | --- |
| Excel / EPM Add-in 依赖 | BPC-KB-003；BPC420 p135-p150；BPC430 p43-p61, p90, p95, p113-p117，OCR |
| 黑盒 Data Manager | BPC-KB-006；BPC420 p160-p179, p187-p195；BPC440 p75-p83，OCR |
| Transformation / Conversion 复杂语法 | BPC-KB-006；BPC420 p175-p179, p187-p192, p209-p212；BPC440 p75-p79，OCR |
| Script Logic / Dynamic Calculation 复杂化 | BPC-KB-001；BPC420 p115-p121, p282-p285；BPC430 p107-p109；BPC440 p49-p54，OCR |
| Work Status 切片锁定 | BPC-KB-004；BPC420 p82-p83, p290-p296；BPC450 p245-p250；S4F80 p146-p149, p170-p173，OCR |
| Data Access 多维权限矩阵 | BPC-KB-007；BPC420 p90-p104；BPC450 p260-p266；s4f90 p330-p333，OCR |
| BPF 流程复杂化 | BPC-KB-007；BPC420 p299-p309；BPC440 p246-p264；S4F80 p170-p184；s4f90 p334-p342，OCR |
| 过早合并报表 | BPC-KB-001；BPC440 p1-p8, p246-p264；s4f90 p1-p9，OCR |
| Actual 与 Budget 同源 | BPC-KB-002、BPC-KB-006；S4F80 p19, p50, p58-p68, p72, p103；BPC450 p49, p66，OCR |

## 3. 痛点归纳

### 3.1 插件依赖导致体验碎片化

BPC 的 EPM Add-in、Workbook、Worksheet、Member Recognition、EPM Report Editor 说明其用户体验大量依赖 Excel / Office 插件。

自研护栏：

1. 主工作台必须是 Web Native。
2. Excel 只可作为导入导出辅助，不作为系统主入口。
3. 模板、报表、权限、状态和审计必须在 Web 中完成。
4. 不做本地插件安装、宏、Office 版本兼容和本地文件分发。

### 3.2 Data Manager 黑盒导致导入不可解释

BPC 的 Data Manager、Package、Process Chain、Transformation File、Conversion File 能支撑复杂导入，但业务用户难以理解执行细节。

自研护栏：

1. 导入必须拆成 Upload、Parse、Mapping、Conversion、Validate、Preview、Commit。
2. 每一步必须有状态、日志和错误明细。
3. 校验通过前不得写正式事实数据。
4. 不暴露复杂文件语法给业务用户。
5. 导入配置必须可视化、可复用、可审计。

### 3.3 Script Logic 和动态计算会形成影子系统

BPC 的 Script Logic、Business Rule、Dynamic Calculation、Local Member 能解决复杂场景，但容易让核心业务逻辑散落在脚本和报表里。

自研护栏：

1. MVP 不提供通用脚本语言。
2. 计算逻辑必须先以显式规则、校验或服务端代码实现。
3. 报表展示计算不得写回事实数据。
4. 复杂分摊、跨模型公式、合并规则后置。

### 3.4 Work Status 难以解释

BPC Work Status 用于控制数据区域状态和锁定，但与维度切片、Owner、Reviewer、权限联动后复杂度很高。

自研护栏：

1. MVP 使用可解释的 Submission Status。
2. 状态范围固定为 Template + Entity + Time + Category + Version。
3. 不支持任意维度切片锁定。
4. 用户界面显示“为什么不能改”，而不是暴露锁定矩阵。
5. 重开和解锁必须有原因和审计。

### 3.5 多维权限矩阵维护成本高

BPC Data Access Profile 和 Task Profile 将功能权限、数据权限、团队和成员范围组合起来，灵活但难维护。

自研护栏：

1. 功能权限与数据权限分离。
2. 数据权限先以组织、模板、期间、类别、版本为核心。
3. 不做单元格级权限。
4. 不做任意维度成员交叉权限矩阵。
5. 权限和责任范围变更必须审计。

### 3.6 BPF 容易过早变成流程平台

BPC Business Process Flow 支持活动、实例、分配、状态、评论和跟踪，但完整流程配置会让预算 MVP 失焦。

自研护栏：

1. MVP 只做固定填报流程：草稿、提交、退回、通过、锁定。
2. Owner / Reviewer 是最小协作闭环。
3. 退回意见和操作审计必须有。
4. 多级审批、条件路由、流程设计器后置。

### 3.7 合并、BI、ERP 直连和差异分析不能提前

BPC 资料覆盖合并、报表、S/4 优化、Actual 等较大范围，但本项目当前目标是预算平台主线。

自研护栏：

1. 合并报表不进入 MVP。
2. BI 图表不进入 MVP。
3. ERP 直连不进入 MVP，先做文件导入。
4. 预算执行差异分析必须在用户明确批准后进入。
5. 先完成元数据、模板、填报、查询、实际数导入闭环。

## 4. 正向设计原则

| 原则 | 说明 |
| --- | --- |
| Web Native First | 所有核心业务操作必须在 Web 中完成 |
| Model First | 模型和维度定义数据口径，模板和报表只是视图 |
| Fact Value Single Source | Budget、Actual、Forecast 进入同源事实数据 |
| Explicit Workflow | 保存、提交、退回、通过、锁定必须显式 |
| Explainable Access | 权限必须能用角色、范围、责任解释 |
| Transparent Import | 导入必须可预览、可校验、可审计 |
| No Hidden Logic | 不允许脚本、报表公式或插件绕过服务端规则 |
| Stage Discipline | 阶段外能力不得提前实现 |

## 5. 模块级护栏

| 模块 | 必须做 | 禁止提前做 |
| --- | --- | --- |
| 元数据 | 模型、维度、成员、层级、Category、Version | 合并专用复杂维度、多父层级 |
| 模板 | Web 表格化填报、行列轴、筛选、校验 | Excel 插件、复杂跨模型公式 |
| 填报 | 保存、提交、退回、通过、锁定、审计 | 多级审批、流程设计器 |
| 查询 | 基础查询、层级汇总、钻取明细 | BI 仪表盘、预算执行差异分析 |
| 实际数导入 | 文件导入、映射、转换、校验、预览、批次审计 | ERP 直连、黑盒后台包 |
| 权限 | 角色、责任范围、数据范围、审计 | 任意维度交叉矩阵、单元格权限 |

## 6. 项目治理护栏

1. BPC-KB-001 至 BPC-KB-009 完成前不写业务代码。
2. ARCH-001 和 PRODUCT-001 完成前不创建后端/前端基础工程。
3. 不提交 PDF 原文、OCR 全文、扫描图片或大段摘录。
4. 每个阶段必须更新 `PROJECT_STEP_RECORD.md`。
5. 每次提交前必须确认未包含 `docs/source`、PDF、OCR 全文、构建产物、业务代码越界。
6. 仍保留 `README.md` 当前既有本地修改，除非进入 README 专门阶段。

## 7. 后续阶段输入

BPC-KB-009 应基于本阶段护栏制定模块路线图：

1. 优先完成预算主线闭环。
2. 把复杂能力明确放到后置阶段。
3. 把禁止提前建设的能力写入路线图边界。

ARCH-001 应基于本阶段护栏固定：

1. 同源事实数据架构。
2. 元数据与视图分离。
3. 状态、权限、审计的简化模型。
4. 导入批次与查询口径。

## 8. 待复核问题

1. OCR 页码可能与 PDF 阅读器页码存在偏移，关键页需后续抽样复核。
2. 哪些护栏需要做成自动化代码审查规则，应在 DEV-000 后补充。
3. README 的当前本地修改仍是历史遗留项，需要后续 README 阶段统一处理。
