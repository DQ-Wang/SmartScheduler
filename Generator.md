# Smart Study Scheduler

智能学习任务调度系统（Smart Study Scheduler）后端项目。

---

# 项目简介

本项目是一个面向 AI Agent / Dify 工作流的智能学习任务调度系统后端服务。

系统核心职责：

- 提供课程 / 考试元数据查询
- 提供用户空闲时间分析
- 提供学习计划任务落库能力
- 作为 Dify 与 MySQL 之间的中间层（MCP Backend）

支持后续扩展：

- AI 自动排程
- 日历同步
- 消息通知
- 学习行为分析
- RAG 知识库接入

---

# 技术栈

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Validation
- Spring Data JPA
- MySQL 8
- Lombok
- MapStruct
- Swagger/OpenAPI
- Docker
- Maven
- Hutool

---

# 一、项目架构设计要求（必须严格遵守）

Cursor 生成代码时必须遵循以下工程规范。

---

## 1.1 高内聚低耦合

必须采用：

- Controller
- Service
- Repository / Mapper
- Domain
- DTO
- VO
- Converter
- Config
- Common
- Exception
- Infrastructure

分层架构。

严禁：

- Controller 直接操作数据库
- Service 返回 Entity
- 魔法字符串
- SQL 写在业务代码中
- 工具类污染业务层

---

## 1.2 必须采用 DDD + Clean Architecture 思想

推荐结构：

```text
com.scheduler
├── common
├── config
├── infrastructure
├── modules
│   ├── course
│   ├── exam
│   ├── schedule
│   ├── plan
│   └── user
```

每个 module 内部：

```text
course
├── controller
├── service
├── domain
├── repository
├── mapper
├── dto
├── vo
├── convert
└── enums
```

---

## 1.3 接口统一响应结构

所有接口统一返回：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

定义：

```java
Result<T>
```

统一响应包装类。

---

## 1.4 全局异常处理

必须实现：

```java
@RestControllerAdvice
```

统一处理：

- 参数异常
- 业务异常
- 数据不存在
- 系统异常

返回统一 JSON。

---

## 1.5 时间处理规范（重点）

系统统一：

```text
Asia/Shanghai
```

时间格式：

```text
yyyy-MM-dd HH:mm:ss
```

必须使用：

```java
LocalDateTime
```

禁止：

```java
Date
```

---

## 1.6 空数据规范（非常重要）

任何列表接口：

必须返回：

```json
[]
```

严禁：

```json
null
```

---

## 1.7 API 文档要求

必须集成：

- springdoc-openapi
- Swagger UI

接口需包含：

- 接口描述
- 参数说明
- 示例响应

---

# 二、数据库设计要求

数据库名称：

```sql
smart_scheduler
```

---

# 三、数据库表设计

---

## 3.1 课程表（courses）

```sql
CREATE TABLE courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(64) UNIQUE NOT NULL,
    standard_name VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);
```

---

## 3.2 课程别名表（course_aliases）

```sql
CREATE TABLE course_aliases (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id BIGINT,
    alias_name VARCHAR(255)
);
```

---

## 3.3 考试表（exams）

```sql
CREATE TABLE exams (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exam_code VARCHAR(64) UNIQUE NOT NULL,
    standard_name VARCHAR(255),
    exam_time DATETIME
);
```

---

## 3.4 考试别名表（exam_aliases）

```sql
CREATE TABLE exam_aliases (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    exam_id BIGINT,
    alias_name VARCHAR(255)
);
```

---

## 3.5 时间槽表（time_slots）

用于课程排课。

```sql
CREATE TABLE time_slots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    relation_type VARCHAR(20),
    relation_code VARCHAR(64),
    day_of_week INT,
    start_time TIME,
    end_time TIME
);
```

relation_type：

```text
COURSE
EXAM
PLAN
```

---

## 3.6 学习计划表（plan_items）

```sql
CREATE TABLE plan_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    item_code VARCHAR(64) UNIQUE,
    title VARCHAR(255),
    description TEXT,
    start_time DATETIME,
    end_time DATETIME,
    status VARCHAR(30),
    created_at DATETIME,
    updated_at DATETIME
);
```

---

## 3.7 学习资源表（plan_resources）

```sql
CREATE TABLE plan_resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_item_code VARCHAR(64),
    resource_name VARCHAR(255),
    resource_url TEXT
);
```

---

# 四、模拟数据要求（必须生成）

Cursor 必须自动生成。

---

## 4.1 课程模拟数据

至少：

- 数据库系统原理
- 离散数学
- 操作系统
- 软件工程导论
- 计算机网络

每门课包含：

- 课程别名
- 教师
- 地点
- 时间槽

---

## 4.2 考试模拟数据

至少：

- 数据库期末考试
- 离散数学期末考试

---

## 4.3 学习计划模拟数据

至少：

- 5 条学习计划
- 包含资源链接

---

# 五、接口开发要求

---

## 5.1 获取字典接口

```http
GET /api/mcp/dict/items
```

返回：

- 课程
- 考试
- aliases

---

## 5.2 获取详情接口

```http
GET /api/mcp/info/detail
```

参数：

```text
id
type
```

支持：

```text
COURSE
EXAM
```

---

## 5.3 获取空闲时间接口

```http
GET /api/mcp/schedule/free-time
```

逻辑：

系统自动根据：

- 课程时间
- 已有计划
- 考试安排

计算空闲时间。

要求：

- 使用时间区间算法
- 禁止写死数据

---

## 5.4 新增学习计划接口

```http
POST /api/mcp/plan/item
```

功能：

- 保存计划
- 保存资源
- 校验时间冲突
- 自动生成 item_code

---

# 六、核心工程要求（重点）

---

## 6.1 必须实现统一日志系统

使用：

```text
Slf4j + Logback
```

记录：

- 请求参数
- 响应耗时
- 异常日志

---

## 6.2 必须实现参数校验

使用：

```java
@Validated
@NotNull
@NotBlank
```

---

## 6.3 DTO 与 Entity 隔离

必须使用：

```text
MapStruct
```

禁止手写大量 set/get。

---

## 6.4 配置分环境

```text
application-dev.yml
application-test.yml
application-prod.yml
```

---

## 6.5 Docker 支持

生成：

```text
docker-compose.yml
```

包含：

- Spring Boot
- MySQL

---

# 七、返回示例规范

---

## 成功响应

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

---

## 失败响应

```json
{
  "code": 500,
  "message": "课程不存在",
  "data": null
}
```

---

# 八、代码质量要求（非常重要）

Cursor 生成代码时必须：

## 必须做到

- 代码可直接运行
- 完整 Maven 项目
- 包结构规范
- 无循环依赖
- 无硬编码
- 单一职责
- SOLID 原则
- RESTful 风格
- 可扩展
- 易维护
- 易二次开发

---

## 严禁出现

- 巨型 Service
- 巨型 Controller
- Utils 泛滥
- Mapper XML 混乱
- 业务逻辑写 SQL
- if-else 地狱
- 魔法值
- 复制粘贴代码

---

# 九、最终生成内容要求

Cursor 最终必须自动生成：

- 完整项目结构
- Maven 配置
- 所有 Entity / DTO / VO
- Controller
- Service
- Repository
- SQL 初始化脚本
- Swagger 配置
- Docker 配置
- README.md

---

# README 必须包含

- 启动方式
- Docker 启动
- 数据库初始化
- Swagger 地址
- API 示例
- 项目架构图

---

# 十、未来扩展预留（必须预留接口能力）

未来可能增加：

- AI 自动任务规划
- LangChain4j
- Dify Tool 调用
- WebSocket 实时通知
- RBAC 权限
- 日历同步
- OCR 导入课表
- RAG 学习资源推荐

因此代码结构必须提前具备扩展性。