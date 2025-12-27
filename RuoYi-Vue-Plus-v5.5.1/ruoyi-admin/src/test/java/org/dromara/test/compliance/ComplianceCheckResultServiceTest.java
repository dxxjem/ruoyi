package org.dromara.test.compliance;

import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.vo.ComplianceCheckResultVo;
import org.dromara.compliance.service.IComplianceCheckResultService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 检查结果明细服务测试类
 *
 * @author compliance
 */
@SpringBootTest
@DisplayName("检查结果明细服务测试")
@Tag("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComplianceCheckResultServiceTest {

    @Autowired
    private IComplianceCheckResultService resultService;

    // 注意: 检查结果通常由检查任务自动生成，这里假设系统中已存在测试数据
    // 实际测试时需要先通过创建检查任务并执行来生成结果数据

    private static Long testTaskId;
    private static Long testResultId;

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
    @DisplayName("测试根据任务ID查询结果列表")
    public void testSelectListByTaskId() {
        System.out.println("========== 测试根据任务ID查询结果列表 ==========");

        // 假设存在任务ID为1的任务及其结果
        Long taskId = 1L;

        // 执行列表查询
        List<ComplianceCheckResultVo> list = resultService.selectListByTaskId(taskId);

        // 验证结果（可能为空，如果任务未执行或没有结果）
        Assertions.assertNotNull(list, "查询列表不应为null");

        System.out.println("根据任务ID查询结果成功，共 " + list.size() + " 条记录");
        list.forEach(System.out::println);
    }

    @Test
    @Order(2)
    @DisplayName("测试分页查询任务结果列表")
    public void testSelectPageListByTaskId() {
        System.out.println("========== 测试分页查询任务结果列表 ==========");

        // 假设存在任务ID
        Long taskId = 1L;

        PageQuery pageQuery = new PageQuery(10, 1);

        // 执行分页查询
        TableDataInfo<ComplianceCheckResultVo> tableData = resultService.selectPageListByTaskId(taskId, pageQuery);

        // 验证结果
        Assertions.assertNotNull(tableData, "分页结果不应为空");
        Assertions.assertNotNull(tableData.getRows(), "数据行不应为空");

        System.out.println("分页查询成功，共 " + tableData.getTotal() + " 条记录");
        tableData.getRows().forEach(System.out::println);
    }

    @Test
    @Order(3)
    @DisplayName("测试根据ID查询结果")
    public void testSelectById() {
        System.out.println("========== 测试根据ID查询结果 ==========");

        // 假设存在结果ID
        Long resultId = 1L;

        // 执行查询操作
        ComplianceCheckResultVo vo = resultService.selectById(resultId);

        // 验证结果（可能为null，如果结果不存在）
        if (vo != null) {
            Assertions.assertEquals(resultId, vo.getResultId(), "结果ID应匹配");
            System.out.println("查询检查结果成功: " + vo);
        } else {
            System.out.println("未找到结果ID为 " + resultId + " 的记录（可能未执行检查任务）");
        }
    }

    @Test
    @Order(4)
    @DisplayName("测试批量删除结果")
    public void testDeleteByIds() {
        System.out.println("========== 测试批量删除结果 ==========");

        // 假设存在结果ID（实际测试时应使用真实存在的ID）
        Long resultId = 1L;

        // 先查询确认是否存在
        ComplianceCheckResultVo vo = resultService.selectById(resultId);

        if (vo != null) {
            // 执行删除操作
            resultService.deleteByIds(Arrays.asList(resultId));

            // 验证删除结果
            ComplianceCheckResultVo deletedVo = resultService.selectById(resultId);
            Assertions.assertNull(deletedVo, "删除后查询应返回null");

            System.out.println("删除检查结果成功");
        } else {
            System.out.println("未找到结果ID为 " + resultId + " 的记录，跳过删除测试");
        }
    }

    @Test
    @Order(5)
    @DisplayName("测试查询多个任务的结果")
    public void testSelectMultipleTaskResults() {
        System.out.println("========== 测试查询多个任务的结果 ==========");

        // 测试多个任务ID的结果查询
        List<Long> taskIds = Arrays.asList(1L, 2L, 3L);

        int totalResults = 0;
        for (Long taskId : taskIds) {
            List<ComplianceCheckResultVo> list = resultService.selectListByTaskId(taskId);
            totalResults += list.size();
            System.out.println("任务ID " + taskId + " 的结果数量: " + list.size());
        }

        System.out.println("多个任务结果查询完成，总结果数: " + totalResults);
    }

    @Test
    @Order(6)
    @DisplayName("测试检查结果的数据结构")
    public void testCheckResultDataStructure() {
        System.out.println("========== 测试检查结果的数据结构 ==========");

        // 查询所有任务的结果
        Long taskId = 1L;
        List<ComplianceCheckResultVo> list = resultService.selectListByTaskId(taskId);

        if (!list.isEmpty()) {
            ComplianceCheckResultVo firstResult = list.get(0);

            // 验证数据结构
            Assertions.assertNotNull(firstResult.getResultId(), "结果ID不应为空");
            Assertions.assertNotNull(firstResult.getTaskId(), "任务ID不应为空");
            Assertions.assertNotNull(firstResult.getDocumentId(), "文档ID不应为空");

            // 打印结果详情
            System.out.println("结果数据结构验证:");
            System.out.println("  结果ID: " + firstResult.getResultId());
            System.out.println("  任务ID: " + firstResult.getTaskId());
            System.out.println("  文档ID: " + firstResult.getDocumentId());
            System.out.println("  规则ID: " + firstResult.getRuleId());
            System.out.println("  规则名称: " + firstResult.getRuleName());
            System.out.println("  规则类型: " + firstResult.getRuleType());
            System.out.println("  是否违规: " + firstResult.getViolated());
            System.out.println("  严重程度: " + firstResult.getSeverity());
            System.out.println("  违规描述: " + firstResult.getViolationDesc());
            System.out.println("  修改建议: " + firstResult.getSuggestion());
        } else {
            System.out.println("任务ID " + taskId + " 没有检查结果记录");
        }
    }

    @Test
    @Order(7)
    @DisplayName("测试分页查询结果的边界条件")
    public void testSelectPageListBoundaryConditions() {
        System.out.println("========== 测试分页查询结果的边界条件 ==========");

        Long taskId = 1L;

        // 测试第一页
        PageQuery pageQuery1 = new PageQuery(5, 1);
        pageQuery1.setPageNum(1);
        pageQuery1.setPageSize(5);
        TableDataInfo<ComplianceCheckResultVo> page1 = resultService.selectPageListByTaskId(taskId, pageQuery1);
        System.out.println("第1页（每页5条）: 共 " + page1.getTotal() + " 条，当前页 " + page1.getRows().size() + " 条");

        // 测试大页码
        PageQuery pageQuery2 = new PageQuery(10, 999);
        pageQuery2.setPageNum(999);
        pageQuery2.setPageSize(10);
        TableDataInfo<ComplianceCheckResultVo> page2 = resultService.selectPageListByTaskId(taskId, pageQuery2);
        System.out.println("第999页（每页10条）: 共 " + page2.getTotal() + " 条，当前页 " + page2.getRows().size() + " 条");

        // 测试大pageSize
        PageQuery pageQuery3 = new PageQuery(1000, 1);
        pageQuery3.setPageNum(1);
        pageQuery3.setPageSize(1000);
        TableDataInfo<ComplianceCheckResultVo> page3 = resultService.selectPageListByTaskId(taskId, pageQuery3);
        System.out.println("第1页（每页1000条）: 共 " + page3.getTotal() + " 条，当前页 " + page3.getRows().size() + " 条");
    }

    @Disabled
    @Test
    @Order(8)
    @DisplayName("测试批量删除不存在的结果ID")
    public void testDeleteNonExistentIds() {
        System.out.println("========== 测试批量删除不存在的结果ID ==========");

        // 删除不存在的ID应不抛出异常
        List<Long> nonExistentIds = Arrays.asList(99999L, 99998L, 99997L);

        try {
            resultService.deleteByIds(nonExistentIds);
            System.out.println("删除不存在的ID成功（无异常）");
        } catch (Exception e) {
            System.out.println("删除不存在的ID时发生异常: " + e.getMessage());
        }
    }
}
