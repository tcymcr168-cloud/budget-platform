# BPC Source Index

阶段编号：PROJECT-BOOTSTRAP-001

资料目录：`C:\codex\budget-platform\docs\source\bpc-pdf`

本文件只记录 SAP BPC PDF 的目录索引、可读性判断、主题判断和后续抽取计划。不复制 PDF 原文，不保存 OCR 全文。

## 索引方法

当前环境没有可用的 `pdfinfo`、`pdftotext`、`qpdf`、`mutool` 或 Python PDF 解析工具。索引采用 PowerShell 读取 PDF 字节并统计 PDF 对象标记的方式完成：

1. 页数使用 `/Type /Page` 对象标记进行估算。
2. 可读性使用文本对象标记、图片对象标记、原始关键词命中情况进行启发式判断。
3. 主题依据文件名、课程编号和 SAP BPC 课程常见内容进行初步判断。

由于所有文件体积较大且图片对象数量高，本阶段判断为索引级结论，后续正式知识抽取建议引入 OCR 或可用 PDF 文本抽取工具复核。

## PDF 文件清单

| 序号 | PDF 文件名 | 大小 | 页数判断 | 是否能直接提取文本 | 初步主题判断 | 扫描版判断 | 是否需要 OCR |
| --- | --- | ---: | ---: | --- | --- | --- | --- |
| 1 | `1.英文版官方标准PA教材-BPC420 SAP Business Planning and Consolidation, Version for SAP NetWeaver Standard Administration and Planning Configuration共379页 2018年编著 PDF版.pdf` | 195.4 MB | 381 个 Page 标记，文件名标注 379 页 | 不能稳定直接提取正文；仅发现少量文本对象和元数据命中 | BPC Standard 管理、计划配置、模型、维度、成员、层级、环境、模板和计划配置 | 疑似扫描图像为主 | 需要 |
| 2 | `2.英文版官方标准PA教材-BPC430 SAP Business Planning and Consolidation 11.0 version for SAP BW4HANA Reporting and Planning共231页 2018年编著 PDF版.pdf` | 111.4 MB | 231 页 | 不能稳定直接提取正文；语义关键词原始命中弱 | BPC 11.0 for BW/4HANA 报表、查询、计划数据访问与展示 | 疑似扫描图像为主 | 需要 |
| 3 | `3.英文版官方标准PA教材-BPC440  SAP Business Planning and Consolidation Consolidation共267页 2018年编著 PDF版.pdf` | 137.4 MB | 267 页 | 不能稳定直接提取正文；语义关键词原始命中弱 | BPC Consolidation，合并相关流程、规则、数据收集与报表思想 | 疑似扫描图像为主 | 需要 |
| 4 | `4.英文版官方标准PA教材-BPC450_col17 SAP Embedded Business Planning and Consolidation 11.0 version for SAP BW4HANA共290页 2018年编著 PDF版.pdf` | 143.6 MB | 290 页 | 不能稳定直接提取正文；仅可识别少量文本结构 | Embedded BPC，BW/4HANA 嵌入式计划、模型、查询和集成方式 | 疑似扫描图像为主 | 需要 |
| 5 | `5.英文版官方标准PA教材-S4F80 SAP BPC Optimized for SAP S4HANA共230页 2018年编著 PDF版.pdf` | 121.0 MB | 230 页 | 不能稳定直接提取正文；语义关键词原始命中弱 | S/4HANA Optimized BPC，预算与实际同源、S/4 集成和计划数据模型 | 疑似扫描图像为主 | 需要 |
| 6 | `6.英文版官方标准PA教材-s4f90 SAP Business Planning and Consolidation Embedded Consolidation共343页 2018年编著 PDF版.pdf` | 177.6 MB | 343 页 | 不能稳定直接提取正文；语义关键词原始命中弱 | Embedded Consolidation，嵌入式合并、状态、流程、权限和报表思想 | 疑似扫描图像为主 | 需要 |

## 可读性统计摘要

| 序号 | Page 标记 | Image 标记 | Text BT 标记 | SAP 原始命中 | Dimension 原始命中 | 判断 |
| --- | ---: | ---: | ---: | ---: | ---: | --- |
| 1 | 381 | 6858 | 738 | 83 | 0 | 图片对象极多，正文语义不可稳定直接抽取 |
| 2 | 231 | 5977 | 290 | 37 | 0 | 图片对象极多，正文语义不可稳定直接抽取 |
| 3 | 267 | 8642 | 485 | 40 | 0 | 图片对象极多，正文语义不可稳定直接抽取 |
| 4 | 290 | 4788 | 586 | 55 | 0 | 图片对象极多，正文语义不可稳定直接抽取 |
| 5 | 230 | 3293 | 341 | 35 | 0 | 图片对象极多，正文语义不可稳定直接抽取 |
| 6 | 343 | 8656 | 513 | 44 | 0 | 图片对象极多，正文语义不可稳定直接抽取 |

## 主题来源建议

| 主题 | 优先来源 PDF | 辅助来源 PDF | 说明 |
| --- | --- | --- | --- |
| 核心概念与术语 | 1, 4, 5 | 2, 6 | BPC Standard、Embedded BPC 和 S/4 Optimized BPC 可共同抽取术语体系 |
| 模型、维度、成员、层级 | 1, 4 | 5 | BPC420 适合抽取 Standard 模型思想，BPC450 适合抽取 Embedded 模型差异 |
| 模板/Input Schedule | 1 | 2, 4 | BPC420 更适合抽取计划模板、填报表单和配置思想 |
| 数据填报与 Work Status | 1 | 4, 5 | BPC420 适合作为 Work Status 和填报流程来源 |
| 查询、汇总、报表 | 2 | 4, 6 | BPC430 明确面向 Reporting and Planning |
| 实际数导入/Data Manager | 1, 5 | 4 | Standard 的 Data Manager 思路与 S/4 同源思想需对比分析 |
| 权限、状态、流程与协作 | 1, 6 | 3, 4 | Standard 权限和 Embedded Consolidation 的流程状态可作为对比来源 |
| 用户痛点与自研规避原则 | 1, 2, 4, 5 | 3, 6 | 从复杂 Excel 插件、黑盒任务、权限矩阵和状态锁定中抽取规避原则 |
| 自研模块路线图 | 1, 2, 4, 5 | 3, 6 | 优先预算管理主线，合并能力仅作为远期参考，不进入当前开发范围 |

## 后续处理建议

1. 后续 BPC 知识包阶段应先建立 OCR 或可靠文本抽取能力，再进行章节级摘录。
2. OCR 结果不得完整提交，只保留概念摘要、主题标签、页码定位和自研取舍。
3. BPC440 和 s4f90 涉及合并思想，可作为远期参考；当前阶段不得引入合并报表业务实现。
4. 文件 1 的对象页数与文件名标注页数不一致，后续应通过专业 PDF 工具复核。
