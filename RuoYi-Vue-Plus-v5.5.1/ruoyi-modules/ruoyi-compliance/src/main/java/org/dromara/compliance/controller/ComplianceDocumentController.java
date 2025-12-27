package org.dromara.compliance.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.compliance.domain.bo.ComplianceDocumentBo;
import org.dromara.compliance.domain.vo.ComplianceDocumentVo;
import org.dromara.compliance.service.IComplianceDocumentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 待检查文档 信息操作处理
 *
 * @author compliance
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/compliance/document")
public class ComplianceDocumentController extends BaseController {

    private final IComplianceDocumentService documentService;

    /**
     * 获取待检查文档列表
     */
    @SaCheckPermission("compliance:document:list")
    @GetMapping("/list")
    public TableDataInfo<ComplianceDocumentVo> list(ComplianceDocumentBo bo, PageQuery pageQuery) {
        return documentService.selectPageList(bo, pageQuery);
    }

    /**
     * 导出待检查文档列表
     */
    @Log(title = "文档管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("compliance:document:export")
    @PostMapping("/export")
    public void export(ComplianceDocumentBo bo, HttpServletResponse response) {
        List<ComplianceDocumentVo> list = documentService.selectList(bo);
        ExcelUtil.exportExcel(list, "文档数据", ComplianceDocumentVo.class, response);
    }

    /**
     * 根据文档编号获取详细信息
     *
     * @param documentId 文档ID
     */
    @SaCheckPermission("compliance:document:query")
    @GetMapping(value = "/{documentId}")
    public R<ComplianceDocumentVo> getInfo(@PathVariable Long documentId) {
        return R.ok(documentService.selectById(documentId));
    }

    /**
     * 新增待检查文档
     */
    @SaCheckPermission("compliance:document:add")
    @Log(title = "文档管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody ComplianceDocumentBo bo) {
        documentService.insert(bo);
        return R.ok();
    }

    /**
     * 修改待检查文档
     */
    @SaCheckPermission("compliance:document:edit")
    @Log(title = "文档管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody ComplianceDocumentBo bo) {
        documentService.update(bo);
        return R.ok();
    }

    /**
     * 删除待检查文档
     *
     * @param documentIds 文档ID串
     */
    @SaCheckPermission("compliance:document:remove")
    @Log(title = "文档管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{documentIds}")
    public R<Void> remove(@PathVariable Long[] documentIds) {
        documentService.deleteByIds(List.of(documentIds));
        return R.ok();
    }
}
