# ADR-0001: Technology Stack Baseline

状态：Accepted

日期：2026-05-06

## 背景

本项目需要建设 Web Native 企业级全面预算管理平台。平台必须支持模型、维度、成员、层级、模板、填报、查询、实际数导入和预算实际同源事实数据，同时避免过早引入分布式复杂度、插件复杂度和黑盒计算逻辑。

当前工具链已经在 FOUNDATION-001 阶段验证：Java 17、Maven、Node.js、pnpm、Python 和 PostgreSQL 客户端可用，Docker 暂不可用。

## 决策

采用以下技术栈作为 MVP 架构基线：

1. 后端：Java 17 + Spring Boot 3.x + Maven。
2. 前端：React + TypeScript + Vite + pnpm。
3. 数据库：PostgreSQL 16。
4. 数据访问：Spring Data JPA 起步；复杂查询在 BUD-008 前再评估是否引入 jOOQ 或 SQL Mapper。
5. 数据迁移：Flyway，仅在明确允许 migration 的开发阶段启用。
6. 部署形态：MVP 使用模块化单体后端、单前端应用和 PostgreSQL。

## 理由

1. Java 17 和 Spring Boot 适合企业级权限、审计、事务和可测试服务边界。
2. React + TypeScript + Vite 适合构建 Web Native 预算模板、填报表格和查询界面。
3. PostgreSQL 能承载关系型元数据、事实坐标、审计日志和导入批次。
4. 模块化单体可以先稳定领域边界，避免 MVP 被微服务拆分拖慢。
5. Spring Data JPA 能快速完成元数据 CRUD，复杂查询保留后续演进空间。

## 后果

正向影响：

1. 后续 DEV-000 可以按明确技术栈创建基础工程。
2. 后端、前端和数据库测试命令可标准化。
3. 架构允许后续扩展复杂查询能力，但不在 MVP 早期引入。

代价和风险：

1. 动态维度和层级汇总可能超出 JPA 的表达舒适区。
2. PostgreSQL 查询性能需要在 BUD-002 和 BUD-008 设计索引、坐标哈希和查询计划。
3. Docker 暂不可用时，本地数据库启动方式需要在 DEV-000 或 BUD-001 单独确认。

## 非目标

本 ADR 不批准以下能力进入 MVP：

1. 微服务拆分。
2. BI 图表平台。
3. ERP 直连。
4. Excel / Office 插件。
5. 通用脚本引擎。

## 验证

本 ADR 属于文档阶段决策。ARCH-001 验证命令为：

1. `git status --short`
2. `git check-ignore` for PDF and OCR cache paths
3. 禁止范围检查：`backend/src`、`frontend/src`、migration
