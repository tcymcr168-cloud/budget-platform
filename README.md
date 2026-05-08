# Budget Platform

Web Native 企业级全面预算管理平台。项目参考 SAP BPC 的模型、维度、成员、层级、Category、Version、Input Schedule、预算填报、查询汇总、实际数导入和预算与实际同源事实数据思想，自研一套更透明、更轻量、更可治理的预算管理系统。

## 项目原则

本项目吸收 SAP BPC 的成熟思想，但明确规避以下复杂形态：

1. 不依赖 Excel 插件。
2. 不实现黑盒 Data Manager。
3. 不暴露复杂 Script Logic。
4. 不照搬难懂的 Work Status 切片锁定。
5. 不提前引入复杂多维权限矩阵。
6. 不提前建设 BI 图表、合并报表或 ERP 直连。
7. 不在未批准阶段开发预算与实际差异分析。

## 当前 MVP 能力

已完成的主线能力：

| 阶段 | 能力 |
| --- | --- |
| BUD-001 | 后端与前端治理基础、统一错误、审计接口、测试基线 |
| BUD-003 / BUD-004 | 元数据后端与前端：Workspace、Dimension、Member |
| BUD-005 | 预算模型管理：模型创建、维度绑定、启用和停用 |
| BUD-006 | 预算模板管理：模板创建、行列轴配置、启用和停用 |
| BUD-007 | 预算填报：任务、草稿、提交、退回、通过、锁定 |
| BUD-008 | 预算查询：事实明细、基础汇总、CSV 文本导出 |
| BUD-009 | 实际数导入：CSV 校验、批次、行级错误、提交写入同源事实表 |
| BUD-010 | 预算与实际差异分析：表格型 Budget vs Actual 对比 |
| REVIEW-001 | MVP 源码审查与状态一致性硬化 |

当前仍未进入：

1. ERP 直连。
2. BI 图表和仪表盘。
3. 合并报表。
4. 复杂权限矩阵。

## 技术栈

| 层 | 技术 |
| --- | --- |
| 后端 | Java 17、Spring Boot、Spring Data JPA、Flyway |
| 数据库 | PostgreSQL 目标环境，H2 用于测试 |
| 前端 | React、Vite、TypeScript |
| 包管理 | pnpm |
| 文档 | Markdown |

## 目录结构

```text
backend/                       Spring Boot 后端
frontend/                      React/Vite 前端
docs/architecture/             架构与阶段设计文档
docs/product/                  BPC 知识抽取与产品设计文档
docs/source/bpc-pdf/           本地 SAP BPC PDF 原始资料，禁止提交 PDF 原文
tools/                         资料抽取和辅助工具
AGENTS.md                      项目阶段规则与 Codex 工作协议
PROJECT_STEP_RECORD.md         项目阶段推进记录
```

## 本地验证命令

后端：

```powershell
cd C:\codex\budget-platform\backend
mvn test
```

前端：

```powershell
cd C:\codex\budget-platform\frontend
pnpm type-check
pnpm lint
pnpm build
```

资料保护检查：

```powershell
cd C:\codex\budget-platform
git check-ignore docs/source/bpc-pdf/*.pdf docs/source/bpc-pdf/*.PDF docs/source/bpc-ocr-cache/ docs/source/bpc-ocr-text/ docs/source/bpc-ocr-output/ frontend/dist/ frontend/node_modules/ backend/target/
```

## PDF 与 OCR 资料保护

`docs/source/bpc-pdf` 仅用于本地资料分析。不得提交：

1. SAP BPC PDF 原文。
2. 扫描页截图。
3. OCR 全文。
4. 可还原原始资料的大段摘录。

允许提交的只有索引、结构化摘要、术语抽取、产品取舍和引用定位。

## 阶段记录

所有阶段推进结果必须记录在 `PROJECT_STEP_RECORD.md`，每个阶段至少包含：

1. 阶段编号和目标。
2. 修改文件。
3. 验证命令与结果。
4. 风险与未解决问题。
5. 是否建议关闭阶段。
6. 下一阶段建议。

## 当前下一步

当前主线已完成到 BUD-010，并完成 REVIEW-001 源码审查。后续建议优先补齐认证授权、持久化审计、查询分页与性能治理、模板版本治理和端到端测试。

当前治理推进已完成 `SEC-003F` 实际数导入 API 授权接入，安全基线采用“角色 + Entity 数据范围 + 流程责任人”的模型，避免过早建设复杂多维权限矩阵。SEC-003 已保护安全管理 API 与预算查询读取 API，SEC-003B 已保护元数据读写 API，SEC-003C 已保护预算模型读写 API，SEC-003D 已保护预算模板读写 API，SEC-003E 已保护预算填报读写和流程动作 API，SEC-003F 已保护实际数导入读写 API；生产登录、JWT/SSO 和前端安全体验仍需后续阶段推进。
