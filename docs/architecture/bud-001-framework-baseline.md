# BUD-001: Framework Baseline

阶段编号：BUD-001

生成日期：2026-05-06

本文件记录项目治理与基础框架阶段的工程基线。本阶段只建立横切能力，不进入元数据、预算模型、模板、填报、查询或导入业务模块。

## 1. 后端基线

后端新增横切包：

| 包 | 职责 |
| --- | --- |
| `com.budgetplatform.common.api` | 统一 API 响应、错误码、应用异常、全局异常处理 |
| `com.budgetplatform.common.audit` | 基础审计事件、审计动作、审计服务接口 |
| `com.budgetplatform.common.web` | 工程健康检查 |

统一响应结构：

```json
{
  "success": true,
  "data": {},
  "error": null
}
```

统一错误结构：

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "BAD_REQUEST",
    "message": "Invalid request.",
    "traceId": "trace-001"
  }
}
```

## 2. 前端基线

前端新增 `src/shared/api`：

1. `types.ts` 定义 `ApiResponse`、`ApiError`、`ApiRequestOptions`。
2. `http.ts` 封装基础 JSON 请求、`Accept` 头、`X-Request-Id` 和统一错误抛出。

该层只提供共享基础能力，不绑定任何预算业务模块。

## 3. 审计基线

审计接口先定义事件契约：

1. actorId
2. subjectType
3. subjectId
4. action
5. occurredAt
6. details

持久化后置到审计存储或具体业务阶段。本阶段使用 `NoopAuditService` 作为 Spring Bean，避免业务模块在早期散落临时审计逻辑。

## 4. 健康检查

新增 `GET /api/health`。

该接口只用于工程基线验证，不承载业务状态。

## 5. 验证命令

1. `mvn test`
2. `pnpm type-check`
3. `pnpm lint`
4. `pnpm build`
5. `git check-ignore`

## 6. 边界

本阶段不创建：

1. 元数据 API。
2. 预算模型 API。
3. 模板 API。
4. 填报 API。
5. 查询 API。
6. 导入 API。
7. migration 或数据库表。

## 7. 下一阶段

建议进入 BUD-002：元数据模型设计。

BUD-002 应先输出逻辑模型、物理模型候选、约束、索引和 ADR，再进入后端实现。
