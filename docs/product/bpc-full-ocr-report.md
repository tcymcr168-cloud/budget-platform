# BPC Full OCR Report

阶段编号：FOUNDATION-003

生成日期：2026-05-06

本报告记录 SAP BPC PDF 全量 OCR 执行结果。OCR 全文、逐页缓存和统计 JSON 均位于 `docs/source` 下的 ignored 目录，不进入 Git 提交。

## 1. 执行范围

资料目录：`docs/source/bpc-pdf`

执行脚本：`tools/extract_bpc_pdf_text.py`

执行方式：

1. 先按 `--full --pdf-index N` 对 6 个 PDF 分批执行 OCR。
2. 再执行 `--full` 汇总统计。
3. 启用逐页缓存：`docs/source/bpc-ocr-cache/`。
4. 输出全文缓存：`docs/source/bpc-ocr-text/*.full.txt`。
5. 输出统计 JSON：`docs/source/bpc-ocr-output/bpc-pdf-ocr-stats.json`。

以上输出目录均被 `.gitignore` 排除。

## 2. 全量 OCR 汇总

| 指标 | 结果 |
| --- | ---: |
| PDF 数量 | 6 |
| 总页数 | 1740 |
| 直接文本有效页 | 3 |
| 疑似扫描页 | 1737 |
| OCR 成功页 | 1737 |
| OCR 失败页 | 0 |
| 统计字符数 | 1897239 |

结论：6 个 PDF 基本都需要 OCR 才能形成可用文本基础；本次全量 OCR 成功率满足后续 BPC-KB 文档抽取的前置条件。

## 3. 单文件结果

| 序号 | PDF | 页数 | 直接文本有效页 | 疑似扫描页 | OCR 成功页 | OCR 失败页 | 字符数 |
| --- | --- | ---: | ---: | ---: | ---: | ---: | ---: |
| 1 | BPC420 Standard Administration and Planning Configuration | 379 | 0 | 379 | 379 | 0 | 413962 |
| 2 | BPC430 Reporting and Planning | 231 | 0 | 231 | 231 | 0 | 239032 |
| 3 | BPC440 Consolidation | 267 | 0 | 267 | 267 | 0 | 296021 |
| 4 | BPC450 Embedded BPC for BW/4HANA | 290 | 3 | 287 | 287 | 0 | 329725 |
| 5 | S4F80 BPC Optimized for S/4HANA | 230 | 0 | 230 | 230 | 0 | 241677 |
| 6 | s4f90 Embedded Consolidation | 343 | 0 | 343 | 343 | 0 | 376822 |

## 4. 关键词命中摘要

| 主题词 | 主要命中来源 |
| --- | --- |
| Model | 全部 PDF 均命中；BPC420、BPC440、BPC450、s4f90 较高 |
| Dimension | 全部 PDF 均命中；BPC420、BPC440、BPC430、s4f90 较高 |
| Member | 全部 PDF 均命中；BPC430、BPC420 较高 |
| Hierarchy | 全部 PDF 均命中；BPC420 较高 |
| Input Schedule | BPC420、BPC430、BPC440、s4f90 命中；适合作为模板主题入口 |
| Report | 全部 PDF 均命中；BPC430、BPC420、BPC440 较高 |
| Work Status | 全部 PDF 均命中；BPC420、S4F80、BPC450 较高 |
| Data Manager | BPC420、BPC440、BPC430 命中；适合实际数导入主题 |
| Transformation / Conversion | BPC420、BPC440 命中较高；适合导入转换主题 |
| Actual / Budget / Forecast | 多数 PDF 命中；S4F80 对 Actual 较有参考价值 |
| Data Access | BPC420、BPC450、s4f90 命中；适合权限主题 |
| Script Logic | BPC420 命中较高；适合作为自研规避复杂逻辑来源 |
| Business Rule | BPC440、s4f90 命中较高；合并相关内容仅作远期参考 |
| Consolidation | s4f90、BPC420、BPC440 命中较高；当前不进入合并报表实现 |

## 5. 质量判断

1. OCR 运行稳定，未发现失败页。
2. 关键词命中覆盖预算平台核心主题，支持进入 BPC-KB-001。
3. OCR 文本仍可能包含识别错误，BPC-KB 阶段必须引用 PDF、页码和 OCR 来源，并使用结构化摘要，不复制原文。
4. 合并相关 PDF 只作为产品思想和远期边界参考，不作为当前业务实现入口。

## 6. 后续建议

建议进入 BPC-KB-001：SAP BPC 核心概念与术语抽取。

BPC-KB-001 应只写产品知识文档，不写业务代码。每个术语尽量标注来源 PDF 和页码；如仅由 OCR 支撑，应标注“来源：OCR”。
