# Smart Study Scheduler — 接口文档

面向 Dify / AI Agent 的 MCP 后端 API 说明。

**Base URL：** `http://localhost:50060`  
**前缀：** `/api/mcp`  
**时间格式：** `yyyy-MM-dd HH:mm:ss`（时区 `Asia/Shanghai`）  
**在线调试：** [Swagger UI](http://localhost:50060/swagger-ui.html)

---

## 统一响应

所有接口返回 JSON：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

| code | 含义 |
|------|------|
| 200 | 成功 |
| 400 | 参数校验失败 |
| 409 | 时间冲突等业务冲突 |
| 500 | 资源不存在或其他业务/系统错误 |

- 失败时 `data` 一般为 `null`
- **列表字段无数据时返回 `[]`，不返回 `null`**

---

## 接口一览

| # | 方法 | 路径 | 说明 |
|---|------|------|------|
| 1 | GET | `/api/mcp/dict/items` | 课程 / 考试字典（含别名） |
| 2 | GET | `/api/mcp/info/detail` | 课程或考试详情 |
| 3 | GET | `/api/mcp/schedule/free-time` | 计算空闲时间段 |
| 4 | POST | `/api/mcp/plan/item` | 新增学习计划 |

---

## 1. 获取字典

```
GET /api/mcp/dict/items
```

**参数：** 无

**成功 `data` 示例：**

```json
{
  "courses": [
    {
      "id": 1,
      "code": "COURSE-SE",
      "name": "软件工程导论",
      "aliases": ["软工导论", "软件工程"]
    }
  ],
  "exams": [
    {
      "id": 1,
      "code": "EXAM-SE-FINAL",
      "name": "软件工程导论期末考试",
      "aliases": ["软工期末"]
    }
  ]
}
```

---

## 2. 获取详情

```
GET /api/mcp/info/detail
```

| 参数 | 必填 | 说明 |
|------|:----:|------|
| id | 是 | 字典接口返回的主键 ID |
| type | 是 | `COURSE` 或 `EXAM` |

**请求示例：**

```
GET /api/mcp/info/detail?id=1&type=COURSE
```

### type = COURSE

```json
{
  "id": 1,
  "code": "COURSE-SE",
  "name": "软件工程导论",
  "teacher": "赵教授",
  "location": "教学楼 A205",
  "aliases": ["软工导论"],
  "timeSlots": [
    {
      "dayOfWeek": 1,
      "startTime": "08:00",
      "endTime": "09:40"
    }
  ]
}
```

> `dayOfWeek`：1=周一 … 7=周日

### type = EXAM

```json
{
  "id": 1,
  "code": "EXAM-SE-FINAL",
  "name": "软件工程导论期末考试",
  "examTime": "2026-06-18 14:00:00",
  "aliases": ["软工期末"]
}
```

---

## 3. 获取空闲时间

```
GET /api/mcp/schedule/free-time
```

根据**课程表、考试、已有学习计划**动态计算空闲时段（默认每日可排程窗口 `08:00`–`22:00`）。

| 参数 | 必填 | 说明 |
|------|:----:|------|
| startDate | 否 | `yyyy-MM-dd`，默认今天 |
| endDate | 否 | `yyyy-MM-dd`，默认 startDate 起 6 天内 |

**请求示例：**

```
GET /api/mcp/schedule/free-time?startDate=2026-05-26&endDate=2026-05-30
```

**成功 `data` 示例（数组）：**

```json
[
  {
    "date": "2026-05-26",
    "startTime": "2026-05-26 12:00:00",
    "endTime": "2026-05-26 14:00:00",
    "durationMinutes": 120
  }
]
```

---

## 4. 新增学习计划

```
POST /api/mcp/plan/item
Content-Type: application/json
```

### 请求体

| 字段 | 必填 | 说明 |
|------|:----:|------|
| title | 是 | 计划标题 |
| description | 否 | 描述 |
| startTime | 是 | 开始时间 |
| endTime | 是 | 结束时间（须晚于 startTime） |
| status | 否 | 见下表，默认 `PENDING` |
| resources | 否 | 学习资源列表 |
| resources[].resourceName | 是* | 有 resources 条目时必填 |
| resources[].resourceUrl | 否 | 链接可选 |

**status 可选值：**

| 值 | 含义 |
|----|------|
| PENDING | 待开始 |
| IN_PROGRESS | 进行中 |
| COMPLETED | 已完成 |
| CANCELLED | 已取消 |

**冲突规则：** 与已有计划、课程上课时段、考试时间重叠时返回 `409`。

### 请求示例

```json
{
  "title": "复习软件测试",
  "description": "等价类与边界值",
  "startTime": "2026-06-01 19:00:00",
  "endTime": "2026-06-01 21:00:00",
  "resources": [
    {
      "resourceName": "测试讲义",
      "resourceUrl": "https://example.com/st/slides"
    },
    {
      "resourceName": "课堂笔记"
    }
  ]
}
```

### 成功 `data` 示例

```json
{
  "itemCode": "PLAN-1982736283746570240",
  "title": "复习软件测试",
  "description": "等价类与边界值",
  "startTime": "2026-06-01 19:00:00",
  "endTime": "2026-06-01 21:00:00",
  "status": "PENDING",
  "resources": [
    {
      "resourceName": "测试讲义",
      "resourceUrl": "https://example.com/st/slides"
    },
    {
      "resourceName": "课堂笔记",
      "resourceUrl": null
    }
  ]
}
```

> `itemCode` 由系统自动生成（雪花 ID），无需客户端传入。

---

## 错误响应示例

**参数错误 400**

```json
{
  "code": 400,
  "message": "title: 标题不能为空",
  "data": null
}
```

**时间冲突 409**

```json
{
  "code": 409,
  "message": "学习计划时间与已有计划冲突",
  "data": null
}
```

**资源不存在 500**

```json
{
  "code": 500,
  "message": "课程不存在",
  "data": null
}
```

---

## 快速 curl 自测

```bash
# 字典
curl "http://localhost:50060/api/mcp/dict/items"

# 课程详情
curl "http://localhost:50060/api/mcp/info/detail?id=1&type=COURSE"

# 空闲时间
curl "http://localhost:50060/api/mcp/schedule/free-time?startDate=2026-05-26&endDate=2026-05-30"

# 新增计划
curl -X POST "http://localhost:50060/api/mcp/plan/item" \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"晚间复习\",\"startTime\":\"2026-06-01 19:00:00\",\"endTime\":\"2026-06-01 21:00:00\"}"
```
