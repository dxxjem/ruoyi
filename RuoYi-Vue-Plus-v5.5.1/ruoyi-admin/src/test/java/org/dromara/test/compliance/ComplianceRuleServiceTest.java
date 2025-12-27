package org.dromara.test.compliance;

import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.bo.ComplianceRuleBo;
import org.dromara.compliance.domain.vo.ComplianceRuleVo;
import org.dromara.compliance.service.IComplianceRuleService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 规则配置服务测试类
 *
 * @author compliance
 */
@SpringBootTest
@DisplayName("规则配置服务测试")
@Tag("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComplianceRuleServiceTest {

    @Autowired
    private IComplianceRuleService ruleService;

    private static Long testRuleId;

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
    @DisplayName("测试新增规则")
    public void testInsert() {
        System.out.println("========== 测试新增规则 ==========");

        // 准备测试数据（使用时间戳确保唯一性）
        String timestamp = String.valueOf(System.currentTimeMillis());
        ComplianceRuleBo bo = new ComplianceRuleBo();
        bo.setRuleCode("RULE-TEST-" + timestamp);
        bo.setRuleName("测试规则-防火间距检查-" + timestamp);
        bo.setRuleType("HARD");
        bo.setCategory("消防安全");
        bo.setKeywords(Arrays.asList("防火间距", "不应小于", "米"));
        bo.setDescription("检查建筑防火间距是否符合标准");
        bo.setSeverity("HIGH");
        bo.setStatus("0");
        bo.setRuleOrder(1);

        // 执行新增操作
        Long ruleId = ruleService.insert(bo);

        // 验证结果
        Assertions.assertNotNull(ruleId, "新增规则后应返回规则ID");
        testRuleId = ruleId;

        System.out.println("新增规则成功，规则ID: " + ruleId);
    }

    @Test
    @Order(2)
    @DisplayName("测试根据ID查询规则")
    public void testSelectById() {
        System.out.println("========== 测试根据ID查询规则 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testRuleId == null) {
            testInsert();
        }

        // 执行查询操作
        ComplianceRuleVo vo = ruleService.selectById(testRuleId);

        // 验证结果
        Assertions.assertNotNull(vo, "查询结果不应为空");
        Assertions.assertEquals(testRuleId, vo.getRuleId(), "规则ID应匹配");
        Assertions.assertTrue(vo.getRuleCode().startsWith("RULE-TEST-"), "规则编码应以RULE-TEST-开头");
        Assertions.assertTrue(vo.getRuleName().contains("测试规则-防火间距检查"), "规则名称应包含测试标识");
        Assertions.assertEquals("HARD", vo.getRuleType(), "规则类型应匹配");

        System.out.println("查询规则成功: " + vo);
    }

    @Test
    @Order(3)
    @DisplayName("测试分页查询规则列表")
    public void testSelectPageList() {
        System.out.println("========== 测试分页查询规则列表 ==========");

        // 准备查询条件
        ComplianceRuleBo bo = new ComplianceRuleBo();
        bo.setRuleType("HARD");
        PageQuery pageQuery = new PageQuery(10, 1);

        // 执行分页查询
        TableDataInfo<ComplianceRuleVo> tableData = ruleService.selectPageList(bo, pageQuery);

        // 验证结果
        Assertions.assertNotNull(tableData, "分页结果不应为空");
        Assertions.assertNotNull(tableData.getRows(), "数据行不应为空");

        System.out.println("分页查询成功，共 " + tableData.getTotal() + " 条记录");
        tableData.getRows().forEach(System.out::println);
    }

    @Test
    @Order(4)
    @DisplayName("测试查询规则列表")
    public void testSelectList() {
        System.out.println("========== 测试查询规则列表 ==========");

        // 准备查询条件
        ComplianceRuleBo bo = new ComplianceRuleBo();
        bo.setCategory("消防安全");
        bo.setStatus("0");

        // 执行列表查询
        List<ComplianceRuleVo> list = ruleService.selectList(bo);

        // 验证结果
        Assertions.assertNotNull(list, "查询列表不应为空");

        System.out.println("列表查询成功，共 " + list.size() + " 条记录");
        list.forEach(System.out::println);
    }

    @Test
    @Order(5)
    @DisplayName("测试修改规则")
    public void testUpdate() {
        System.out.println("========== 测试修改规则 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testRuleId == null) {
            testInsert();
        }

        // 查询当前记录获取实际编码
        ComplianceRuleVo existing = ruleService.selectById(testRuleId);

        // 准备修改数据
        ComplianceRuleBo bo = new ComplianceRuleBo();
        bo.setRuleId(testRuleId);
        bo.setRuleCode(existing.getRuleCode()); // 使用实际编码
        bo.setRuleName("测试规则-防火间距检查(已修改)");
        bo.setRuleType("HARD");
        bo.setCategory("消防安全");
        bo.setKeywords(Arrays.asList("防火间距", "不应小于", "米", "建筑高度"));
        bo.setDescription("检查建筑防火间距是否符合标准(已修改)");
        bo.setSeverity("MEDIUM");
        bo.setStatus("0");
        bo.setRuleOrder(2);

        // 执行修改操作
        Boolean result = ruleService.update(bo);

        // 验证结果
        Assertions.assertTrue(result, "修改操作应返回成功");

        // 查询验证修改是否生效
        ComplianceRuleVo vo = ruleService.selectById(testRuleId);
        Assertions.assertTrue(vo.getRuleName().contains("(已修改)"), "规则名称应已修改");
        Assertions.assertEquals("MEDIUM", vo.getSeverity(), "严重程度应已修改");

        System.out.println("修改规则成功");
    }

    @Test
    @Order(6)
    @DisplayName("测试校验规则编码唯一性")
    public void testCheckCodeUnique() {
        System.out.println("========== 测试校验规则编码唯一性 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testRuleId == null) {
            testInsert();
        }

        // 查询当前记录获取实际编码
        ComplianceRuleVo existing = ruleService.selectById(testRuleId);

        // 准备测试数据 - 已存在的编码
        ComplianceRuleBo bo1 = new ComplianceRuleBo();
        bo1.setRuleCode(existing.getRuleCode());

        // 测试已存在的编码
        boolean result1 = ruleService.checkCodeUnique(bo1);
        Assertions.assertFalse(result1, "已存在的编码应返回false");
        System.out.println("已存在的编码检查: " + result1);

        // 准备测试数据 - 新的编码（使用时间戳）
        ComplianceRuleBo bo2 = new ComplianceRuleBo();
        bo2.setRuleCode("RULE-NEW-TEST-" + System.currentTimeMillis());

        // 测试新的编码
        boolean result2 = ruleService.checkCodeUnique(bo2);
        Assertions.assertTrue(result2, "新的编码应返回true");
        System.out.println("新的编码检查: " + result2);
    }

    @Test
    @Order(7)
    @DisplayName("测试批量删除规则")
    public void testDeleteByIds() {
        System.out.println("========== 测试批量删除规则 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testRuleId == null) {
            testInsert();
        }

        // 执行删除操作
        ruleService.deleteByIds(Arrays.asList(testRuleId));

        // 验证删除结果
        ComplianceRuleVo vo = ruleService.selectById(testRuleId);
        Assertions.assertNull(vo, "删除后查询应返回null");

        System.out.println("删除规则成功");
    }

    @Test
    @Order(8)
    @DisplayName("测试批量新增不同类型的规则")
    public void testBatchInsertDifferentTypes() {
        System.out.println("========== 测试批量新增不同类型的规则 ==========");

        String timestamp = String.valueOf(System.currentTimeMillis());

        // 插入硬规则
        ComplianceRuleBo hardRule = new ComplianceRuleBo();
        hardRule.setRuleCode("RULE-TEST-HARD-" + timestamp);
        hardRule.setRuleName("测试规则-疏散通道检查-" + timestamp);
        hardRule.setRuleType("HARD");
        hardRule.setCategory("消防安全");
        hardRule.setKeywords(Arrays.asList("疏散通道", "净宽度", "不应小于"));
        hardRule.setDescription("检查疏散通道净宽度是否符合标准");
        hardRule.setSeverity("HIGH");
        hardRule.setStatus("0");
        hardRule.setRuleOrder(1);

        Long hardRuleId = ruleService.insert(hardRule);
        Assertions.assertNotNull(hardRuleId, "新增硬规则应返回规则ID");
        System.out.println("新增硬规则成功，ID: " + hardRuleId);

        // 插入语义规则
        ComplianceRuleBo ragRule = new ComplianceRuleBo();
        ragRule.setRuleCode("RULE-TEST-RAG-" + timestamp);
        ragRule.setRuleName("测试规则-建筑耐火等级语义检查-" + timestamp);
        ragRule.setRuleType("RAG");
        ragRule.setCategory("消防安全");
        ragRule.setDescription("通过语义分析检查建筑耐火等级");
        ragRule.setSeverity("MEDIUM");
        ragRule.setStatus("0");
        ragRule.setRuleOrder(2);

        Long ragRuleId = ruleService.insert(ragRule);
        Assertions.assertNotNull(ragRuleId, "新增语义规则应返回规则ID");
        System.out.println("新增语义规则成功，ID: " + ragRuleId);

        // 查询验证
        ComplianceRuleBo bo = new ComplianceRuleBo();
        bo.setCategory("消防安全");
        List<ComplianceRuleVo> list = ruleService.selectList(bo);
        System.out.println("批量插入完成，共查询到 " + list.size() + " 条记录");
    }
}
