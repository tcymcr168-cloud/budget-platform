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

本地运行、认证 bootstrap 和日常巡视步骤详见 `docs/architecture/ops-001-local-runbook.md`。当前治理巡视详见 `docs/architecture/ops-002-governance-readiness-check.md`。MVP API smoke 测试基线详见 `docs/architecture/e2e-001-smoke-test-baseline.md`。查询分页与性能治理详见 `docs/architecture/perf-001-query-pagination-governance.md`，预算 facts 后端分页详见 `docs/architecture/perf-002-budget-facts-pagination-backend.md`，前端 facts 分页适配详见 `docs/architecture/perf-003-frontend-facts-pagination.md`。

生产认证实施拆分详见 `docs/architecture/auth-001-production-auth-implementation-plan.md`。反向代理可信身份后端适配详见 `docs/architecture/auth-002a-reverse-proxy-auth-adapter.md`，当前用户与审计 actor 信任收敛详见 `docs/architecture/auth-003-current-user-actor-trust.md`，前端当前用户边界详见 `docs/architecture/auth-004-frontend-current-user-boundary.md`，失败认证审计详见 `docs/architecture/auth-005-failed-authentication-audit.md`，部署与密钥运维详见 `docs/architecture/auth-006-deployment-secret-operations-runbook.md`，JWT/OIDC bearer 校验设计详见 `docs/architecture/auth-007-jwt-oidc-bearer-validation-design.md`，JWT 配置基线详见 `docs/architecture/auth-008-jwt-config-baseline.md`，JWT bearer 后端适配详见 `docs/architecture/auth-009-jwt-bearer-adapter.md`，前端 bearer 边界决策详见 `docs/architecture/auth-010-frontend-bearer-boundary.md`，生产认证 smoke 与回滚详见 `docs/architecture/auth-011-auth-deployment-smoke-rollback.md`。

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

当前主线已完成到 BUD-010，并完成 REVIEW-001 源码审查。后续建议优先按 `PERF-004` 小步治理 summary 和 variance 分页，再推进模板版本治理和浏览器端 smoke 测试。

当前治理推进已完成 `PERF-003` 前端预算 facts 分页适配，安全基线采用“角色 + Entity 数据范围 + 流程责任人”的模型，避免过早建设复杂多维权限矩阵。SEC-003 已保护安全管理 API 与预算查询读取 API，SEC-003B 已保护元数据读写 API，SEC-003C 已保护预算模型读写 API，SEC-003D 已保护预算模板读写 API，SEC-003E 已保护预算填报读写和流程动作 API，SEC-003F 已保护实际数导入读写 API，SEC-004 已为前端统一注入内部身份请求头并展示 401/403 授权错误，SEC-005 已明确生产 JWT/SSO/反向代理身份边界与后续阶段路线，SEC-006 已将当前请求头模式收拢到可配置认证模式并让未实现的 JWT 模式失败关闭，SEC-007 已让 `X-User-Roles` 默认失效并改由 `app_user_role` 作为 Workspace 角色来源，SEC-008 已让前端生产构建停止自动注入 `X-User-Id`/`X-User-Roles` 并隐藏开发身份选择器，SEC-009 已明确未来 JWT/OIDC/反向代理认证的 token 生命周期、CORS/cookie、失败认证审计和密钥轮换边界，SEC-010 已标注早期请求头角色文档的历史状态，SEC-011 已提供安全用户、Workspace 角色和 Entity 范围授权的最小前端入口，SEC-012 已明确用户禁用、角色撤销和 Entity 范围撤销的最小设计与后续实现拆分，SEC-013 已复用 `app_user.status` 提供 disable/enable action endpoints 并记录审计，SEC-014 已在 `AuthorizationService` 中拦截已注册 inactive 用户继续使用 bootstrap、请求头角色、Workspace 角色或 Entity 范围授权，SEC-015 已为 Workspace 角色授权和 Entity 范围授权新增 `ACTIVE`/`REVOKED` 软撤销状态、撤销 action endpoints、审计和重新 grant 激活旧记录能力，SEC-016 已在现有安全管理界面提供用户 disable/enable、角色 revoke、Entity scope revoke 和 reason 输入，AUDIT-001 已落地 `audit_event` 持久化审计表，AUDIT-002 已提供 BUDGET_ADMIN 只读审计查询与分页，AUDIT-003 已覆盖元数据、预算模型和预算模板写操作审计，AUDIT-004 已提供只读审计筛选与分页查看前端，AUDIT-005 已提供安全生命周期快捷筛选和 JSON details 可读化展示，OPS-001 已沉淀本地运行、认证 bootstrap、资料保护和巡视检查手册，OPS-002 已完成后端/前端验证、资料保护和阶段记录一致性检查；AUTH-001 至 AUTH-011 已完成生产认证路线、反向代理可信 principal、当前用户边界、失败认证审计、部署运维文档、JWT/OIDC bearer 校验设计、JWT 配置基线、JWT bearer 后端适配、前端 bearer 边界决策和生产认证 smoke/rollback 手册；E2E-001 已用 MockMvc 串联元数据、预算模型、预算模板、预算填报、实际数导入和差异查询主路径；PERF-001 已明确预算 facts、summary、variance 和 CSV export 的分页、排序、默认限制与后续实现路线；PERF-002 已新增兼容型 `/api/budget-query/facts/page` 后端分页接口和共享分页校验；PERF-003 已让前端 facts 查询消费 paged endpoint 并提供 previous/next 分页控制。

授权撤销与用户禁用设计详见 `docs/architecture/sec-012-revoke-disable-design.md`。
用户禁用/启用后端 MVP 详见 `docs/architecture/sec-013-user-disable-backend.md`。
inactive 用户授权拦截详见 `docs/architecture/sec-014-inactive-user-authorization.md`。
授权软撤销 schema/API 详见 `docs/architecture/sec-015-grant-soft-revoke-api.md`。
前端禁用/启用与撤销 MVP 详见 `docs/architecture/sec-016-frontend-disable-revoke-mvp.md`。
安全生命周期审计体验优化详见 `docs/architecture/audit-005-security-lifecycle-audit-ux.md`。
