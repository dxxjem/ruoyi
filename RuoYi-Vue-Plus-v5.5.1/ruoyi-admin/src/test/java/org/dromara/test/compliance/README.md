# Compliance 模块测试用例说明文档

## 概述

本文档描述了智能文档合规性检查平台（compliance模块）的增删改查测试用例。

## 测试文件列表

| 测试类 | 服务接口 | 测试方法数 | 说明 |
|--------|----------|-----------|------|
| ComplianceRegulationServiceTest.java | IComplianceRegulationService | 8 | 法规库服务测试 |
| ComplianceRuleServiceTest.java | IComplianceRuleService | 8 | 规则配置服务测试 |
| ComplianceDocumentServiceTest.java | IComplianceDocumentService | 7 | 待检查文档服务测试 |
| ComplianceCheckTaskServiceTest.java | IComplianceCheckTaskService | 8 | 检查任务服务测试 |
| ComplianceCheckResultServiceTest.java | IComplianceCheckResultService | 8 | 检查结果服务测试 |

---

## 1. ComplianceRegulationServiceTest - 法规库服务测试

### 测试方法详情

| 方法名 | 功能 | 数据操作 |
|--------|------|----------|
| testInsert() | 测试新增法规 | INSERT |
| testSelectById() | 测试根据ID查询法规 | SELECT |
| testSelectPageList() | 测试分页查询法规列表 | SELECT |
| testSelectList() | 测试查询法规列表 | SELECT |
| testUpdate() | 测试修改法规 | UPDATE |
| testCheckCodeUnique() | 测试校验法规编码唯一性 | SELECT |
| testDeleteByIds() | 测试批量删除法规 | DELETE |
| testBatchInsert() | 测试批量新增法规 | INSERT |

### 测试数据示例

```java
ComplianceRegulationBo bo = new ComplianceRegulationBo();
bo.setRegulationCode("REG-TEST-001");
bo.setRegulationName("测试法规-建筑设计防火规范");
bo.setCategory("建筑设计");
bo.setEffectiveDate(new Date());
bo.setStatus("0");
bo.setFileUrl("/upload/test/fire_safety_regulation.pdf");
bo.setFileSize(1024000L);
bo.setRemark("这是一个测试法规记录");
```

---

## 2. ComplianceRuleServiceTest - 规则配置服务测试

### 测试方法详情

| 方法名 | 功能 | 数据操作 |
|--------|------|----------|
| testInsert() | 测试新增规则 | INSERT |
| testSelectById() | 测试根据ID查询规则 | SELECT |
| testSelectPageList() | 测试分页查询规则列表 | SELECT |
| testSelectList() | 测试查询规则列表 | SELECT |
| testUpdate() | 测试修改规则 | UPDATE |
| testCheckCodeUnique() | 测试校验规则编码唯一性 | SELECT |
| testDeleteByIds() | 测试批量删除规则 | DELETE |
| testBatchInsertDifferentTypes() | 测试批量新增不同类型规则 | INSERT |

### 测试数据示例

```java
// 硬规则测试数据
ComplianceRuleBo bo = new ComplianceRuleBo();
bo.setRuleCode("RULE-HARD-001");
bo.setRuleName("防火间距检查规则");
bo.setRuleType("HARD");
bo.setCategory("消防安全");
bo.setKeywords(Arrays.asList("防火间距", "不应小于", "米"));
bo.setDescription("检查建筑防火间距是否符合标准");
bo.setSeverity("HIGH");
bo.setStatus("0");
bo.setRuleOrder(1);

// 语义规则测试数据
ComplianceRuleBo ragRule = new ComplianceRuleBo();
ragRule.setRuleCode("RULE-RAG-001");
ragRule.setRuleName("建筑耐火等级语义检查");
ragRule.setRuleType("RAG");
ragRule.setCategory("消防安全");
ragRule.setDescription("通过语义分析检查建筑耐火等级");
ragRule.setSeverity("MEDIUM");
ragRule.setStatus("0");
```

---

## 3. ComplianceDocumentServiceTest - 待检查文档服务测试

### 测试方法详情

| 方法名 | 功能 | 数据操作 |
|--------|------|----------|
| testInsert() | 测试新增待检查文档 | INSERT |
| testSelectById() | 测试根据ID查询文档 | SELECT |
| testSelectPageList() | 测试分页查询文档列表 | SELECT |
| testSelectList() | 测试查询文档列表 | SELECT |
| testUpdate() | 测试修改文档 | UPDATE |
| testDeleteByIds() | 测试批量删除文档 | DELETE |
| testBatchInsertDifferentStatus() | 测试批量新增不同状态文档 | INSERT |

### 测试数据示例

```java
ComplianceDocumentBo bo = new ComplianceDocumentBo();
bo.setDocumentName("建筑设计图-消防规范检查.pdf");
bo.setFileUrl("/upload/documents/building_design_fire_safety.pdf");
bo.setFileSize(5120000L);
bo.setFileType("PDF");
bo.setStatus("PENDING");  // 可选: PENDING, PROCESSING, COMPLETED, FAILED
bo.setUploader(1L);
bo.setRemark("需要进行消防规范合规性检查的建筑设计图");
bo.setCreateDept(100L);
```

### 文档状态说明

| 状态 | 说明 |
|------|------|
| PENDING | 待处理 |
| PROCESSING | 处理中 |
| COMPLETED | 已完成 |
| FAILED | 失败 |

---

## 4. ComplianceCheckTaskServiceTest - 检查任务服务测试

### 测试方法详情

| 方法名 | 功能 | 数据操作 |
|--------|------|----------|
| testInsert() | 测试新增检查任务 | INSERT |
| testSelectById() | 测试根据ID查询任务 | SELECT |
| testSelectPageList() | 测试分页查询任务列表 | SELECT |
| testSelectList() | 测试查询任务列表 | SELECT |
| testUpdate() | 测试修改任务 | UPDATE |
| testUpdateStatus() | 测试更新任务状态 | UPDATE |
| testDeleteByIds() | 测试批量删除任务 | DELETE |
| testTaskExecutionFlow() | 测试模拟任务执行流程 | INSERT + UPDATE |

### 测试数据示例

```java
ComplianceCheckTaskBo bo = new ComplianceCheckTaskBo();
bo.setTaskName("建筑设计图消防规范合规检查任务");
bo.setDocumentId(1L);
bo.setRuleIds("[1,2,3]");  // JSON数组格式
bo.setCheckType("HARD");    // HARD 或 RAG
bo.setStatus("PENDING");    // PENDING, RUNNING, COMPLETED, FAILED
bo.setProgress(0);          // 0-100
```

### 任务执行流程测试

该测试模拟完整的任务状态流转：
```
PENDING (进度 0%)
    ↓
RUNNING (进度 30%)
    ↓
RUNNING (进度 60%)
    ↓
RUNNING (进度 90%)
    ↓
COMPLETED (进度 100%)
```

---

## 5. ComplianceCheckResultServiceTest - 检查结果服务测试

### 测试方法详情

| 方法名 | 功能 | 数据操作 |
|--------|------|----------|
| testSelectListByTaskId() | 测试根据任务ID查询结果列表 | SELECT |
| testSelectPageListByTaskId() | 测试分页查询任务结果列表 | SELECT |
| testSelectById() | 测试根据ID查询结果 | SELECT |
| testDeleteByIds() | 测试批量删除结果 | DELETE |
| testSelectMultipleTaskResults() | 测试查询多个任务的结果 | SELECT |
| testCheckResultDataStructure() | 测试验证结果数据结构 | SELECT |
| testSelectPageListBoundaryConditions() | 测试分页边界条件 | SELECT |

### 结果数据结构

```java
{
    "resultId": 1,
    "taskId": 100,
    "documentId": 10,
    "ruleId": 5,
    "ruleName": "防火间距检查规则",
    "ruleType": "HARD",
    "violated": true,
    "severity": "HIGH",
    "location": "{\"page\": 12, \"line\": 5}",
    "matchedContent": "防火间距不应小于10米",
    "violationDesc": "防火间距不满足规范要求",
    "suggestion": "请调整建筑间距，确保防火间距不小于10米"
}
```

---

## 测试执行前准备

### 1. 数据库准备

#### MySQL 数据库
确保以下数据库和表已创建：

```sql
-- 数据库: ry
-- 表:
--   compliance_document      - 待检查文档表
--   compliance_check_task    - 检查任务表
--   compliance_check_result  - 检查结果明细表
```

#### PostgreSQL 数据库
确保以下数据库和表已创建：

```sql
-- 数据库: compliance_vector_db
-- 扩展: CREATE EXTENSION IF NOT EXISTS vector;
-- 表:
--   compliance_regulation    - 法规库表
--   compliance_rule          - 规则配置表
--   compliance_document_chunk - 文档切块表
```

### 2. 配置文件

确保 `application-dev.yml` 中配置了正确的数据源：

```yaml
spring:
  datasource:
    dynamic:
      primary: master
      strict: true
      datasource:
        # MySQL主数据源
        master:
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: jdbc:mysql://localhost:3306/ry
          username: root
          password: your_password

        # PostgreSQL向量数据源
        vector:
          driver-class-name: org.postgresql.Driver
          url: jdbc:postgresql://localhost:5432/compliance_vector_db
          username: postgres
          password: your_password
```

---

## 执行测试

### 执行单个测试类

```bash
cd D:\test\ruoyi\RuoYi-Vue-Plus-v5.5.1

# 测试法规库服务
mvn test -Dtest=ComplianceRegulationServiceTest -pl ruoyi-admin

# 测试规则配置服务
mvn test -Dtest=ComplianceRuleServiceTest -pl ruoyi-admin

# 测试文档服务
mvn test -Dtest=ComplianceDocumentServiceTest -pl ruoyi-admin

# 测试任务服务
mvn test -Dtest=ComplianceCheckTaskServiceTest -pl ruoyi-admin

# 测试结果服务
mvn test -Dtest=ComplianceCheckResultServiceTest -pl ruoyi-admin
```

### 执行所有 compliance 测试

```bash
mvn test -Dtest=org.dromara.test.compliance.** -pl ruoyi-admin
```

### 执行特定测试方法

```bash
# 只执行新增测试
mvn test -Dtest=ComplianceRegulationServiceTest#testInsert -pl ruoyi-admin

# 只执行查询测试
mvn test -Dtest=ComplianceRegulationServiceTest#testSelectById -pl ruoyi-admin
```

---

## 测试验证要点

### 1. 增删改查完整性验证

每个服务测试都包含完整的 CRUD 操作：

| 操作 | 验证内容 |
|------|----------|
| CREATE | 插入后返回ID，查询可验证数据存在 |
| READ | 根据ID、列表、分页查询都能正确返回数据 |
| UPDATE | 更新后查询可验证数据已修改 |
| DELETE | 删除后查询返回null |
| UNIQUE | 唯一性校验（编码唯一性）正确工作 |

### 2. 数据一致性验证

- 插入的数据与查询结果一致
- 更新后的数据与预期一致
- 删除操作级联正确（如删除任务同时删除结果）
- 分页查询总数正确

### 3. 业务逻辑验证

- 任务状态流转：PENDING → RUNNING → COMPLETED
- 文档状态变更：PENDING → PROCESSING → COMPLETED
- 规则类型：HARD（硬规则）和 RAG（语义规则）
- 严重程度：HIGH、MEDIUM、LOW

---

## 测试报告示例

### ComplianceRegulationServiceTest 执行结果

```
@BeforeEach ================== 开始测试
========== 测试新增法规 ==========
新增法规成功，法规ID: 1234567890123456789
@AfterEach ================== 结束测试

@BeforeEach ================== 开始测试
========== 测试根据ID查询法规 ==========
查询法规成功: ComplianceRegulationVo(regulationId=1234567890123456789, ...)
@AfterEach ================== 结束测试

...（省略其他测试方法）

Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
```

---

## 常见问题

### 1. 数据库连接失败

**问题**: `java.sql.SQLException: Access denied for user`

**解决**: 检查 `application-dev.yml` 中的数据库用户名和密码

### 2. PostgreSQL 数组类型处理

**问题**: `column "keywords" is of type text[] but expression is of type character varying`

**解决**: 确保使用了 `ComplianceStringArrayTypeHandler` 处理 PostgreSQL 数组类型

### 3. 测试顺序问题

**问题**: 测试方法执行顺序不符合预期

**解决**: 使用 `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)` 和 `@Order` 注解控制顺序

### 4. 数据残留问题

**问题**: 测试数据未清理导致后续测试失败

**解决**: 每次测试后在 `@AfterEach` 或最后一个测试方法中清理数据

---

## 总结

本测试套件涵盖了 compliance 模块所有核心服务的增删改查功能：

- ✅ 5 个服务接口，共 39 个测试方法
- ✅ 完整的 CRUD 操作覆盖
- ✅ 业务逻辑和状态流转测试
- ✅ 边界条件和异常场景测试
- ✅ 自动数据创建和清理

通过这些测试用例，可以验证：
1. 所有服务的基本增删改查功能正常
2. 数据一致性和完整性得到保证
3. 业务逻辑（如状态流转）正确执行
4. 与数据库的交互（MySQL 和 PostgreSQL）正常工作
