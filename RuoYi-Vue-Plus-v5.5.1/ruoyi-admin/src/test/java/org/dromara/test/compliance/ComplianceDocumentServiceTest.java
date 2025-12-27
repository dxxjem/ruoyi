package org.dromara.test.compliance;

import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.bo.ComplianceDocumentBo;
import org.dromara.compliance.domain.vo.ComplianceDocumentVo;
import org.dromara.compliance.service.IComplianceDocumentService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 待检查文档服务测试类
 *
 * @author compliance
 */
@SpringBootTest
@DisplayName("待检查文档服务测试")
@Tag("local")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComplianceDocumentServiceTest {

    @Autowired
    private IComplianceDocumentService documentService;

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
    @DisplayName("测试新增待检查文档")
    public void testInsert() {
        System.out.println("========== 测试新增待检查文档 ==========");

        // 准备测试数据
        ComplianceDocumentBo bo = new ComplianceDocumentBo();
        bo.setDocumentName("建筑设计图-消防规范检查.pdf");
        bo.setFileUrl("/upload/documents/building_design_fire_safety.pdf");
        bo.setFileSize(5120000L);
        bo.setFileType("PDF");
        bo.setStatus("PENDING");
        bo.setUploader(1L);
        bo.setRemark("需要进行消防规范合规性检查的建筑设计图");
        bo.setCreateDept(100L);

        // 执行新增操作
        Long documentId = documentService.insert(bo);

        // 验证结果
        Assertions.assertNotNull(documentId, "新增文档后应返回文档ID");
        testDocumentId = documentId;

        System.out.println("新增文档成功，文档ID: " + documentId);
    }

    @Test
    @Order(2)
    @DisplayName("测试根据ID查询文档")
    public void testSelectById() {
        System.out.println("========== 测试根据ID查询文档 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testDocumentId == null) {
            testInsert();
        }

        // 执行查询操作
        ComplianceDocumentVo vo = documentService.selectById(testDocumentId);

        // 验证结果
        Assertions.assertNotNull(vo, "查询结果不应为空");
        Assertions.assertEquals(testDocumentId, vo.getDocumentId(), "文档ID应匹配");
        Assertions.assertEquals("建筑设计图-消防规范检查.pdf", vo.getDocumentName(), "文档名称应匹配");
        Assertions.assertEquals("PDF", vo.getFileType(), "文件类型应匹配");
        Assertions.assertEquals("PENDING", vo.getStatus(), "状态应匹配");

        System.out.println("查询文档成功: " + vo);
    }

    @Test
    @Order(3)
    @DisplayName("测试分页查询文档列表")
    public void testSelectPageList() {
        System.out.println("========== 测试分页查询文档列表 ==========");

        // 准备查询条件
        ComplianceDocumentBo bo = new ComplianceDocumentBo();
        bo.setFileType("PDF");
        PageQuery pageQuery = new PageQuery(10, 1);

        // 执行分页查询
        TableDataInfo<ComplianceDocumentVo> tableData = documentService.selectPageList(bo, pageQuery);

        // 验证结果
        Assertions.assertNotNull(tableData, "分页结果不应为空");
        Assertions.assertNotNull(tableData.getRows(), "数据行不应为空");

        System.out.println("分页查询成功，共 " + tableData.getTotal() + " 条记录");
        tableData.getRows().forEach(System.out::println);
    }

    @Test
    @Order(4)
    @DisplayName("测试查询文档列表")
    public void testSelectList() {
        System.out.println("========== 测试查询文档列表 ==========");

        // 准备查询条件
        ComplianceDocumentBo bo = new ComplianceDocumentBo();
        bo.setStatus("PENDING");

        // 执行列表查询
        List<ComplianceDocumentVo> list = documentService.selectList(bo);

        // 验证结果
        Assertions.assertNotNull(list, "查询列表不应为空");

        System.out.println("列表查询成功，共 " + list.size() + " 条记录");
        list.forEach(System.out::println);
    }

    @Test
    @Order(5)
    @DisplayName("测试修改文档")
    public void testUpdate() {
        System.out.println("========== 测试修改文档 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testDocumentId == null) {
            testInsert();
        }

        // 准备修改数据
        ComplianceDocumentBo bo = new ComplianceDocumentBo();
        bo.setDocumentId(testDocumentId);
        bo.setDocumentName("建筑设计图-消防规范检查(已修改).pdf");
        bo.setFileUrl("/upload/documents/building_design_fire_safety_v2.pdf");
        bo.setFileSize(6144000L);
        bo.setFileType("PDF");
        bo.setStatus("PROCESSING");
        bo.setUploader(1L);
        bo.setRemark("需要进行消防规范合规性检查的建筑设计图(已修改)");
        bo.setCreateDept(100L);

        // 执行修改操作
        Boolean result = documentService.update(bo);

        // 验证结果
        Assertions.assertTrue(result, "修改操作应返回成功");

        // 查询验证修改是否生效
        ComplianceDocumentVo vo = documentService.selectById(testDocumentId);
        Assertions.assertEquals("建筑设计图-消防规范检查(已修改).pdf", vo.getDocumentName(), "文档名称应已修改");
        Assertions.assertEquals("PROCESSING", vo.getStatus(), "状态应已修改");

        System.out.println("修改文档成功");
    }

    @Test
    @Order(6)
    @DisplayName("测试批量删除文档")
    public void testDeleteByIds() {
        System.out.println("========== 测试批量删除文档 ==========");

        // 如果没有测试ID，先插入一条数据
        if (testDocumentId == null) {
            testInsert();
        }

        // 执行删除操作
        documentService.deleteByIds(Arrays.asList(testDocumentId));

        // 验证删除结果
        ComplianceDocumentVo vo = documentService.selectById(testDocumentId);
        Assertions.assertNull(vo, "删除后查询应返回null");

        System.out.println("删除文档成功");
    }

    @Test
    @Order(7)
    @DisplayName("测试批量新增不同状态的文档")
    public void testBatchInsertDifferentStatus() {
        System.out.println("========== 测试批量新增不同状态的文档 ==========");

        // 插入待处理文档
        ComplianceDocumentBo doc1 = new ComplianceDocumentBo();
        doc1.setDocumentName("标书文档-待检查.pdf");
        doc1.setFileUrl("/upload/documents/tender_doc_1.pdf");
        doc1.setFileSize(2048000L);
        doc1.setFileType("PDF");
        doc1.setStatus("PENDING");
        doc1.setUploader(1L);
        doc1.setRemark("待检查的标书文档");

        Long docId1 = documentService.insert(doc1);
        Assertions.assertNotNull(docId1, "新增待处理文档应返回文档ID");
        System.out.println("新增待处理文档成功，ID: " + docId1);

        // 插入处理中文档
        ComplianceDocumentBo doc2 = new ComplianceDocumentBo();
        doc2.setDocumentName("施工图纸-检查中.pdf");
        doc2.setFileUrl("/upload/documents/construction_drawing.pdf");
        doc2.setFileSize(3072000L);
        doc2.setFileType("PDF");
        doc2.setStatus("PROCESSING");
        doc2.setUploader(1L);
        doc2.setRemark("正在检查的施工图纸");

        Long docId2 = documentService.insert(doc2);
        Assertions.assertNotNull(docId2, "新增处理中文档应返回文档ID");
        System.out.println("新增处理中文档成功，ID: " + docId2);

        // 插入已完成文档
        ComplianceDocumentBo doc3 = new ComplianceDocumentBo();
        doc3.setDocumentName("设计说明-已完成.pdf");
        doc3.setFileUrl("/upload/documents/design_description.pdf");
        doc3.setFileSize(1024000L);
        doc3.setFileType("PDF");
        doc3.setStatus("COMPLETED");
        doc3.setUploader(1L);
        doc3.setRemark("已完成检查的设计说明");

        Long docId3 = documentService.insert(doc3);
        Assertions.assertNotNull(docId3, "新增已完成文档应返回文档ID");
        System.out.println("新增已完成文档成功，ID: " + docId3);

        // 查询验证
        List<ComplianceDocumentVo> pendingList = documentService.selectList(new ComplianceDocumentBo());
        System.out.println("批量插入完成，当前共有 " + pendingList.size() + " 条文档记录");
    }
}
