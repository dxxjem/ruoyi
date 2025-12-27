package org.dromara.test.compliance;

import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.bo.ComplianceCheckTaskBo;
import org.dromara.compliance.domain.bo.ComplianceDocumentBo;
import org.dromara.compliance.domain.vo.ComplianceCheckTaskVo;
import org.dromara.compliance.service.IComplianceCheckTaskService;
import org.dromara.compliance.service.IComplianceDocumentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 检查任务服务测试类
 *
 * @author compliance
 */
@SpringBootTest
@DisplayName("检查任务服务测试")
@Tag("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComplianceCheckTaskServiceTest {

    @Autowired
    private IComplianceCheckTaskService taskService;

    @Autowired
    private IComplianceDocumentService documentService;

    private static Long testTaskId;
    private static Long testDocumentId;

    @BeforeEach
    public void setUp() {
        System.out.println("@BeforeEach ================== 开始测试");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("@AfterEach ================== 结束测试");
    }

    @Test
    @Order(1)
    @DisplayName("测试新增检查任务")
    public void testInsert() {
        System.out.println("========== 测试新增检查任务 ==========");

        // 先创建一个测试文档
        ComplianceDocumentBo docBo = new ComplianceDocumentBo();
        String timestamp = String.valueOf(System.currentTimeMillis());
        docBo.setDocumentName("测试文档-消防规范检查-" + timestamp + ".pdf");
        docBo.setFileUrl("/upload/test/fire_safety_" + timestamp + ".pdf");
        docBo.setFileSize(5120000L);
        docBo.setFileType("PDF");
        docBo.setStatus("PENDING");
        docBo.setUploader(1L);
        docBo.setRemark("用于检查任务测试的文档");

        testDocumentId = documentService.insert(docBo);
        Assertions.assertNotNull(testDocumentId, "新增文档后应返回文档ID");
        System.out.println("新增测试文档成功，文档ID: " + testDocumentId);

        // 准备测试数据
        ComplianceCheckTaskBo bo = new ComplianceCheckTaskBo();
        bo.setTaskName("测试任务-建筑设计图消防规范合规检查-" + timestamp);
        bo.setDocumentId(testDocumentId);  // 使用刚创建的文档ID
        bo.setRuleIds("[1,2,3]");  // 规则ID列表JSON格式
        bo.setCheckType("HARD");
        bo.setStatus("PENDING");
        bo.setProgress(0);
        bo.setResultSummary(null);
        bo.setErrorMessage(null);

        // 执行新增操作
        Long taskId = taskService.insert(bo);

        // 验证结果
        Assertions.assertNotNull(taskId, "新增任务后应返回任务ID");
        testTaskId = taskId;

        System.out.println("新增检查任务成功，任务ID: " + taskId);
    }

    @Test
    @Order(2)
    @DisplayName("测试根据ID查询任务")
    public void testSelectById() {
        System.out.println("========== 测试根据ID查询任务 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testTaskId == null) {
            testInsert();
        }

        // 执行查询操作
        ComplianceCheckTaskVo vo = taskService.selectById(testTaskId);

        // 验证结果
        Assertions.assertNotNull(vo, "查询结果不应为空");
        Assertions.assertEquals(testTaskId, vo.getTaskId(), "任务ID应匹配");
        Assertions.assertTrue(vo.getTaskName().contains("建筑设计图消防规范合规检查"), "任务名称应包含关键词");
        Assertions.assertEquals("HARD", vo.getCheckType(), "检查类型应匹配");
        Assertions.assertEquals("PENDING", vo.getStatus(), "状态应匹配");

        System.out.println("查询检查任务成功: " + vo);
    }

    @Test
    @Order(3)
    @DisplayName("测试分页查询任务列表")
    public void testSelectPageList() {
        System.out.println("========== 测试分页查询任务列表 ==========");

        // 准备查询条件
        ComplianceCheckTaskBo bo = new ComplianceCheckTaskBo();
        bo.setCheckType("HARD");
        PageQuery pageQuery = new PageQuery(1, 10);

        // 执行分页查询
        TableDataInfo<ComplianceCheckTaskVo> tableData = taskService.selectPageList(bo, pageQuery);

        // 验证结果
        Assertions.assertNotNull(tableData, "分页结果不应为空");
        Assertions.assertNotNull(tableData.getRows(), "数据行不应为空");

        System.out.println("分页查询成功，共 " + tableData.getTotal() + " 条记录");
        tableData.getRows().forEach(System.out::println);
    }

    @Test
    @Order(4)
    @DisplayName("测试查询任务列表")
    public void testSelectList() {
        System.out.println("========== 测试查询任务列表 ==========");

        // 准备查询条件
        ComplianceCheckTaskBo bo = new ComplianceCheckTaskBo();
        bo.setStatus("PENDING");

        // 执行列表查询
        List<ComplianceCheckTaskVo> list = taskService.selectList(bo);

        // 验证结果
        Assertions.assertNotNull(list, "查询列表不应为空");

        System.out.println("列表查询成功，共 " + list.size() + " 条记录");
        list.forEach(System.out::println);
    }

    @Test
    @Order(5)
    @DisplayName("测试修改任务")
    public void testUpdate() {
        System.out.println("========== 测试修改任务 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testTaskId == null) {
            testInsert();
        }

        // 查询当前任务获取实际文档ID
        ComplianceCheckTaskVo existing = taskService.selectById(testTaskId);

        // 准备修改数据
        ComplianceCheckTaskBo bo = new ComplianceCheckTaskBo();
        bo.setTaskId(testTaskId);
        bo.setTaskName("测试任务-建筑设计图消防规范合规检查(已修改)");
        bo.setDocumentId(existing.getDocumentId()); // 使用实际文档ID
        bo.setRuleIds("[1,2,3,4]");  // 修改规则列表
        bo.setCheckType("HARD");
        bo.setStatus("RUNNING");  // 修改状态
        bo.setProgress(50);  // 修改进度
        bo.setResultSummary(null);
        bo.setErrorMessage(null);

        // 执行修改操作
        Boolean result = taskService.update(bo);

        // 验证结果
        Assertions.assertTrue(result, "修改操作应返回成功");

        // 查询验证修改是否生效
        ComplianceCheckTaskVo vo = taskService.selectById(testTaskId);
        Assertions.assertTrue(vo.getTaskName().contains("(已修改)"), "任务名称应已修改");
        Assertions.assertEquals("RUNNING", vo.getStatus(), "状态应已修改");
        Assertions.assertEquals(50, vo.getProgress(), "进度应已修改");

        System.out.println("修改检查任务成功");
    }

    @Test
    @Order(6)
    @DisplayName("测试更新任务状态")
    public void testUpdateStatus() {
        System.out.println("========== 测试更新任务状态 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testTaskId == null) {
            testInsert();
        }

        // 执行状态更新操作
        Boolean result = taskService.updateStatus(testTaskId, "COMPLETED", 100);

        // 验证结果
        Assertions.assertTrue(result, "状态更新操作应返回成功");

        // 查询验证状态是否生效
        ComplianceCheckTaskVo vo = taskService.selectById(testTaskId);
        Assertions.assertEquals("COMPLETED", vo.getStatus(), "状态应已更新为COMPLETED");
        Assertions.assertEquals(100, vo.getProgress(), "进度应已更新为100");

        System.out.println("更新任务状态成功");
    }

    @Test
    @Order(7)
    @DisplayName("测试批量删除任务")
    public void testDeleteByIds() {
        System.out.println("========== 测试批量删除任务 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testTaskId == null) {
            testInsert();
        }

        // 执行删除操作
        taskService.deleteByIds(Arrays.asList(testTaskId));

        // 验证删除结果
        ComplianceCheckTaskVo vo = taskService.selectById(testTaskId);
        Assertions.assertNull(vo, "删除后查询应返回null");

        System.out.println("删除检查任务成功");
    }

    @Test
    @Order(8)
    @DisplayName("测试模拟任务执行流程")
    public void testTaskExecutionFlow() {
        System.out.println("========== 测试模拟任务执行流程 ==========");

        // 先创建一个测试文档
        ComplianceDocumentBo docBo = new ComplianceDocumentBo();
        String timestamp = String.valueOf(System.currentTimeMillis());
        docBo.setDocumentName("测试文档-施工图-" + timestamp + ".pdf");
        docBo.setFileUrl("/upload/test/construction_" + timestamp + ".pdf");
        docBo.setFileSize(3072000L);
        docBo.setFileType("PDF");
        docBo.setStatus("PENDING");
        docBo.setUploader(1L);
        docBo.setRemark("用于流程测试的文档");

        Long docId = documentService.insert(docBo);
        Assertions.assertNotNull(docId, "新增文档后应返回文档ID");
        System.out.println("新增测试文档成功，文档ID: " + docId);

        // 创建新任务
        ComplianceCheckTaskBo bo = new ComplianceCheckTaskBo();
        bo.setTaskName("测试任务-施工图合规检查-流程测试-" + timestamp);
        bo.setDocumentId(docId);  // 使用刚创建的文档ID
        bo.setRuleIds("[1,2]");
        bo.setCheckType("HARD");
        bo.setStatus("PENDING");
        bo.setProgress(0);

        Long taskId = taskService.insert(bo);
        Assertions.assertNotNull(taskId, "任务创建成功");

        // 模拟任务状态变化: PENDING -> RUNNING -> COMPLETED
        System.out.println("任务创建: PENDING");

        taskService.updateStatus(taskId, "RUNNING", 30);
        System.out.println("任务开始执行: RUNNING, 进度30%");

        taskService.updateStatus(taskId, "RUNNING", 60);
        System.out.println("任务执行中: RUNNING, 进度60%");

        taskService.updateStatus(taskId, "RUNNING", 90);
        System.out.println("任务即将完成: RUNNING, 进度90%");

        taskService.updateStatus(taskId, "COMPLETED", 100);
        System.out.println("任务完成: COMPLETED, 进度100%");

        // 验证最终状态
        ComplianceCheckTaskVo finalVo = taskService.selectById(taskId);
        Assertions.assertEquals("COMPLETED", finalVo.getStatus(), "最终状态应为COMPLETED");
        Assertions.assertEquals(100, finalVo.getProgress(), "最终进度应为100");

        System.out.println("任务执行流程测试完成");
    }
}
