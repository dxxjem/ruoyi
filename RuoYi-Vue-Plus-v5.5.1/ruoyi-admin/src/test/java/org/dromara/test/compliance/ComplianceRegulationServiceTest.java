package org.dromara.test.compliance;

import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.bo.ComplianceRegulationBo;
import org.dromara.compliance.domain.vo.ComplianceRegulationVo;
import org.dromara.compliance.service.IComplianceRegulationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 法规库服务测试类
 *
 * @author compliance
 */
@SpringBootTest
@DisplayName("法规库服务测试")
@Tag("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComplianceRegulationServiceTest {

    @Autowired
    private IComplianceRegulationService regulationService;

    private static Long testRegulationId;

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
    @DisplayName("测试新增法规")
    public void testInsert() {
        System.out.println("========== 测试新增法规 ==========");

        // 准备测试数据（使用时间戳确保唯一性）
        String timestamp = String.valueOf(System.currentTimeMillis());
        ComplianceRegulationBo bo = new ComplianceRegulationBo();
        bo.setRegulationCode("REG-TEST-" + timestamp);
        bo.setRegulationName("测试法规-建筑设计防火规范-" + timestamp);
        bo.setCategory("建筑设计");
        bo.setEffectiveDate(new Date());
        bo.setStatus("0");
        bo.setFileUrl("/upload/test/fire_safety_regulation.pdf");
        bo.setFileSize(1024000L);
        bo.setRemark("这是一个测试法规记录");

        // 执行新增操作
        Long regulationId = regulationService.insert(bo);

        // 验证结果
        Assertions.assertNotNull(regulationId, "新增法规后应返回法规ID");
        testRegulationId = regulationId;

        System.out.println("新增法规成功，法规ID: " + regulationId);
    }

    @Test
    @Order(2)
    @DisplayName("测试根据ID查询法规")
    public void testSelectById() {
        System.out.println("========== 测试根据ID查询法规 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testRegulationId == null) {
            testInsert();
        }

        // 执行查询操作
        ComplianceRegulationVo vo = regulationService.selectById(testRegulationId);

        // 验证结果
        Assertions.assertNotNull(vo, "查询结果不应为空");
        Assertions.assertEquals(testRegulationId, vo.getRegulationId(), "法规ID应匹配");
        Assertions.assertTrue(vo.getRegulationCode().startsWith("REG-TEST-"), "法规编码应以REG-TEST-开头");
        Assertions.assertTrue(vo.getRegulationName().contains("测试法规-建筑设计防火规范"), "法规名称应包含测试标识");

        System.out.println("查询法规成功: " + vo);
    }

    @Test
    @Order(3)
    @DisplayName("测试分页查询法规列表")
    public void testSelectPageList() {
        System.out.println("========== 测试分页查询法规列表 ==========");

        // 准备查询条件
        ComplianceRegulationBo bo = new ComplianceRegulationBo();
        bo.setRegulationName("测试");
        PageQuery pageQuery = new PageQuery(10, 1);

        // 执行分页查询
        TableDataInfo<ComplianceRegulationVo> tableData = regulationService.selectPageList(bo, pageQuery);

        // 验证结果
        Assertions.assertNotNull(tableData, "分页结果不应为空");
        Assertions.assertNotNull(tableData.getRows(), "数据行不应为空");

        System.out.println("分页查询成功，共 " + tableData.getTotal() + " 条记录");
        tableData.getRows().forEach(System.out::println);
    }

    @Test
    @Order(4)
    @DisplayName("测试查询法规列表")
    public void testSelectList() {
        System.out.println("========== 测试查询法规列表 ==========");

        // 准备查询条件
        ComplianceRegulationBo bo = new ComplianceRegulationBo();
        bo.setCategory("建筑设计");
        bo.setStatus("0");

        // 执行列表查询
        List<ComplianceRegulationVo> list = regulationService.selectList(bo);

        // 验证结果
        Assertions.assertNotNull(list, "查询列表不应为空");

        System.out.println("列表查询成功，共 " + list.size() + " 条记录");
        list.forEach(System.out::println);
    }

    @Test
    @Order(5)
    @DisplayName("测试修改法规")
    public void testUpdate() {
        System.out.println("========== 测试修改法规 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testRegulationId == null) {
            testInsert();
        }

        // 查询当前记录获取实际编码
        ComplianceRegulationVo existing = regulationService.selectById(testRegulationId);

        // 准备修改数据
        ComplianceRegulationBo bo = new ComplianceRegulationBo();
        bo.setRegulationId(testRegulationId);
        bo.setRegulationCode(existing.getRegulationCode()); // 使用实际编码
        bo.setRegulationName("测试法规-建筑设计防火规范(已修改)");
        bo.setCategory("建筑设计");
        bo.setEffectiveDate(new Date());
        bo.setStatus("0");
        bo.setFileUrl("/upload/test/fire_safety_regulation_v2.pdf");
        bo.setFileSize(2048000L);
        bo.setRemark("这是一个测试法规记录-已修改");

        // 执行修改操作
        Boolean result = regulationService.update(bo);

        // 验证结果
        Assertions.assertTrue(result, "修改操作应返回成功");

        // 查询验证修改是否生效
        ComplianceRegulationVo vo = regulationService.selectById(testRegulationId);
        Assertions.assertTrue(vo.getRegulationName().contains("(已修改)"), "法规名称应已修改");

        System.out.println("修改法规成功");
    }

    @Test
    @Order(6)
    @DisplayName("测试校验法规编码唯一性")
    public void testCheckCodeUnique() {
        System.out.println("========== 测试校验法规编码唯一性 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testRegulationId == null) {
            testInsert();
        }

        // 查询当前记录获取实际编码
        ComplianceRegulationVo existing = regulationService.selectById(testRegulationId);

        // 准备测试数据 - 已存在的编码
        ComplianceRegulationBo bo1 = new ComplianceRegulationBo();
        bo1.setRegulationCode(existing.getRegulationCode());

        // 测试已存在的编码
        boolean result1 = regulationService.checkCodeUnique(bo1);
        Assertions.assertFalse(result1, "已存在的编码应返回false");
        System.out.println("已存在的编码检查: " + result1);

        // 准备测试数据 - 新的编码（使用时间戳）
        ComplianceRegulationBo bo2 = new ComplianceRegulationBo();
        bo2.setRegulationCode("REG-TEST-NEW-" + System.currentTimeMillis());

        // 测试新的编码
        boolean result2 = regulationService.checkCodeUnique(bo2);
        Assertions.assertTrue(result2, "新的编码应返回true");
        System.out.println("新的编码检查: " + result2);
    }

    @Test
    @Order(7)
    @DisplayName("测试批量删除法规")
    public void testDeleteByIds() {
        System.out.println("========== 测试批量删除法规 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testRegulationId == null) {
            testInsert();
        }

        // 执行删除操作
        regulationService.deleteByIds(Arrays.asList(testRegulationId));

        // 验证删除结果
        ComplianceRegulationVo vo = regulationService.selectById(testRegulationId);
        Assertions.assertNull(vo, "删除后查询应返回null");

        System.out.println("删除法规成功");
    }

    @Test
    @Order(8)
    @DisplayName("测试批量新增法规")
    public void testBatchInsert() {
        System.out.println("========== 测试批量新增法规 ==========");

        String timestamp = String.valueOf(System.currentTimeMillis());

        // 批量插入3条测试数据（使用时间戳确保唯一性）
        for (int i = 1; i <= 3; i++) {
            ComplianceRegulationBo bo = new ComplianceRegulationBo();
            bo.setRegulationCode("REG-BATCH-" + timestamp + "-" + String.format("%03d", i));
            bo.setRegulationName("批量测试法规-" + timestamp + "-" + i);
            bo.setCategory("测试分类");
            bo.setEffectiveDate(new Date());
            bo.setStatus("0");
            bo.setRemark("批量插入测试数据 " + i);

            Long regulationId = regulationService.insert(bo);
            Assertions.assertNotNull(regulationId, "批量新增应返回法规ID");
            System.out.println("批量插入第 " + i + " 条记录成功，ID: " + regulationId);
        }

        // 查询验证
        ComplianceRegulationBo bo = new ComplianceRegulationBo();
        bo.setRegulationName("批量测试");
        List<ComplianceRegulationVo> list = regulationService.selectList(bo);
        System.out.println("批量插入完成，共查询到 " + list.size() + " 条记录");
    }
}
