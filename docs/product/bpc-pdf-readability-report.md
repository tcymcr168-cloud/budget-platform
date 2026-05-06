# BPC PDF Readability Report

阶段编号：FOUNDATION-002

生成日期：2026-05-06

本报告只记录 OCR 与 PDF 文本抽取工具链验证结果，不进行正式 BPC 知识抽取，不复制 PDF 原文，不提交 OCR 全文。

## 1. 工具链状态

当前 Codex/PowerShell 父进程仍未加载 FOUNDATION-001 写入的用户级 PATH。普通命令直接执行时仍出现：

| 命令 | 当前普通命令状态 |
| --- | --- |
| `git --version` | 失败：`The term 'git' is not recognized` |
| `gh --version` | 失败：`The term 'gh' is not recognized` |
| `node -v` | 失败：命中 Codex 内嵌 `node.exe`，启动错误 `拒绝访问` |
| `pnpm -v` | 失败：`The term 'pnpm' is not recognized` |
| `java -version` | 失败：`The term 'java' is not recognized` |
| `mvn -version` | 失败：`The term 'mvn' is not recognized` |
| `python --version` | 失败：Microsoft Store alias 提示 |
| `py --version` | 成功：`Python 3.12.10` |

本阶段后续命令均显式加载机器 PATH 与用户 PATH，等价于新终端环境。建议重启 Codex 或重新打开 PowerShell 后继续后续阶段。

加载 PATH 后验证结果：

| 工具 | 结果 |
| --- | --- |
| Git | `git version 2.54.0.windows.1` |
| GitHub CLI | `gh version 2.92.0 (2026-04-28)` |
| Node.js | `v24.15.0` |
| pnpm | `10.33.3` |
| Java | OpenJDK `17.0.19` Temurin |
| Maven | Apache Maven `3.9.11` |
| py | `Python 3.12.10` |
| python | `Python 3.12.10` |
| pip | `pip 25.0.1` |

## 2. Python 包状态

| 包 | 状态 |
| --- | --- |
| PyMuPDF | 已安装，版本 `1.27.2.3` |
| Pillow | 已安装，版本 `12.2.0` |
| pytesseract | 已安装，版本 `0.3.13` |

## 3. Tesseract 状态

Tesseract OCR 已通过 winget 安装。

| 项 | 结果 |
| --- | --- |
| 命令 | `tesseract --version` |
| 版本 | `tesseract v5.4.0.20240606` |
| 默认路径 | `C:\Program Files\Tesseract-OCR\tesseract.exe` |
| PATH | 已追加到用户级 PATH；当前 Codex 进程仍需显式加载或重启 |

## 4. PDF 清单

资料目录：`docs/source/bpc-pdf`

| 序号 | PDF 文件名 | 页数 |
| --- | --- | ---: |
| 1 | `1.英文版官方标准PA教材-BPC420 SAP Business Planning and Consolidation, Version for SAP NetWeaver Standard Administration and Planning Configuration共379页 2018年编著 PDF版.pdf` | 379 |
| 2 | `2.英文版官方标准PA教材-BPC430 SAP Business Planning and Consolidation 11.0 version for SAP BW4HANA Reporting and Planning共231页 2018年编著 PDF版.pdf` | 231 |
| 3 | `3.英文版官方标准PA教材-BPC440  SAP Business Planning and Consolidation Consolidation共267页 2018年编著 PDF版.pdf` | 267 |
| 4 | `4.英文版官方标准PA教材-BPC450_col17 SAP Embedded Business Planning and Consolidation 11.0 version for SAP BW4HANA共290页 2018年编著 PDF版.pdf` | 290 |
| 5 | `5.英文版官方标准PA教材-S4F80 SAP BPC Optimized for SAP S4HANA共230页 2018年编著 PDF版.pdf` | 230 |
| 6 | `6.英文版官方标准PA教材-s4f90 SAP Business Planning and Consolidation Embedded Consolidation共343页 2018年编著 PDF版.pdf` | 343 |

## 5. 抽样策略

脚本：`tools/extract_bpc_pdf_text.py`

执行命令：

```powershell
py tools/extract_bpc_pdf_text.py --sample
```

抽样页规则：

1. 前 5 页。
2. 中间 5 页。
3. 最后 5 页。
4. 页码去重后处理。

直接文本有效阈值：每页直接提取文本不少于 500 字符。低于该阈值判定为疑似扫描页并执行 OCR。

## 6. 每个 PDF 的抽样 OCR 结果

| 序号 | 页数 | 抽样页数 | 直接文本有效页 | 疑似扫描页 | OCR 成功页 | OCR 失败页 | 抽样总字符数 | 建议全量 OCR |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: | --- |
| 1 | 379 | 15 | 0 | 15 | 15 | 0 | 13298 | 是 |
| 2 | 231 | 15 | 0 | 15 | 15 | 0 | 18734 | 是 |
| 3 | 267 | 15 | 0 | 15 | 15 | 0 | 17325 | 是 |
| 4 | 290 | 15 | 0 | 15 | 15 | 0 | 16791 | 是 |
| 5 | 230 | 15 | 0 | 15 | 15 | 0 | 16715 | 是 |
| 6 | 343 | 15 | 0 | 15 | 15 | 0 | 17164 | 是 |

结论：6 个 PDF 的抽样页均未达到直接文本有效阈值，全部需要依赖 OCR 才能形成可用文本基础。

## 7. 关键词命中情况

| 序号 | 关键词命中摘要 |
| --- | --- |
| 1 | Model 9；Dimension 7；Member 7；Hierarchy 7；Report 5；Work Status 1；Data Manager 1；Transformation 5；Conversion 4；Budget 2；Forecast 1；Script Logic 1；Business Rule 1；Consolidation 24 |
| 2 | Model 11；Dimension 8；Member 18；Hierarchy 1；Report 51；Consolidation 7 |
| 3 | Model 4；Dimension 10；Member 5；Hierarchy 1；Report 3；Work Status 9；Actual 1；Data Access 1；Business Rule 1；Consolidation 13 |
| 4 | Model 2；Dimension 3；Member 10；Hierarchy 1；Work Status 1；Actual 2；Consolidation 1 |
| 5 | Model 5；Dimension 5；Report 6；Work Status 1；Actual 1；Consolidation 1 |
| 6 | Model 5；Report 4；Work Status 2；Conversion 1；Data Access 2；Business Rule 3；Consolidation 27 |

未命中或抽样命中较弱的关键词不代表 PDF 全文不存在对应内容，只代表本阶段抽样页未覆盖或 OCR 未稳定识别。

## 8. OCR 质量判断

1. OCR 工具链可用，6 个 PDF 的 90 个抽样页 OCR 全部成功。
2. 抽样文本能够命中 SAP BPC 关键术语，具备后续知识抽取基础。
3. 直接文本层不足以支撑正式知识抽取，后续应以 OCR 文本为主，直接文本为辅。
4. 当前 OCR 只使用英文 `eng` 语言包，适合英文教材正文；中文文件名不影响处理。
5. OCR 样本文本位于 `docs/source/bpc-ocr-text`，统计 JSON 位于 `docs/source/bpc-ocr-output`，均被 `.gitignore` 排除。

## 9. 是否建议全量 OCR

FOUNDATION-002 时建议进入全量 OCR 准备阶段，但未自动执行全量 OCR。

FOUNDATION-003 已执行全量 OCR，并生成 `docs/product/bpc-full-ocr-report.md`。全量 OCR 全文和缓存仍位于 `.gitignore` 排除目录中，不进入 Git。

FOUNDATION-002 原建议包括：

1. 全量 OCR 输出目录与清理策略。
2. 是否按 PDF 分批执行。
3. OCR 全文是否只保留本地，不进入 Git。
4. 从 OCR 全文抽取结构化摘要的最小格式。
5. OCR 质量抽检和页码引用规则。

## 10. 是否建议进入 BPC-KB-001

FOUNDATION-002 阶段暂不建议直接进入正式 BPC-KB-001。

FOUNDATION-003 全量 OCR 完成后，已具备进入 BPC-KB-001 的基础。BPC-KB-001 仍必须只写结构化摘要和术语抽取，不复制 PDF 原文或 OCR 全文。

## 11. 风险与限制

1. 当前 Codex 进程 PATH 未刷新，后续阶段前建议重启 Codex 或终端。
2. OCR 结果可能包含识别错误，不能直接作为正式知识包，需要人工结构化整理和页码校验。
3. 本阶段只抽样 90 页，不代表全量页面质量完全一致。
4. PDF 文件较大，全量 OCR 可能耗时较长，应分批执行并保留中断恢复机制。
5. OCR 缓存目录已被忽略，后续如果需要复用 OCR 结果，应明确本地保留策略。

## 12. 意外 BPC 知识文档处理建议

当前发现 4 个未跟踪 BPC 知识文档：

| 文件 | 检查结果 | 处理 |
| --- | --- | --- |
| `docs/product/bpc-core-concepts.md` | 原为 0 字节空文件 | 已标记为 DRAFT 占位，不作为正式知识包 |
| `docs/product/bpc-data-manager-actual-import.md` | 原为 0 字节空文件 | 已标记为 DRAFT 占位，不作为正式知识包 |
| `docs/product/bpc-pain-points-and-design-guardrails.md` | 原为 0 字节空文件 | 已标记为 DRAFT 占位，不作为正式知识包 |
| `docs/product/bpc-template-input-schedule.md` | 原为 0 字节空文件 | 已标记为 DRAFT 占位，不作为正式知识包 |

建议后续正式 BPC-KB 阶段重新生成或替换这些草稿文件，不在 FOUNDATION-002 扩写。
