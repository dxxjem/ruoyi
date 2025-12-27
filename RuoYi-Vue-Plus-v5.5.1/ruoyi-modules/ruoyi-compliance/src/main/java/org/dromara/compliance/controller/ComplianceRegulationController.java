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
import org.dromara.compliance.domain.bo.ComplianceRegulationBo;
import org.dromara.compliance.domain.vo.ComplianceRegulationVo;
import org.dromara.compliance.service.IComplianceRegulationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 法规库 信息操作处理
 *
 * @author compliance
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/compliance/regulation")
public class ComplianceRegulationController extends BaseController {

    private final IComplianceRegulationService regulationService;

    /**
     * 获取法规库列表
     */
    @SaCheckPermission("compliance:regulation:list")
    @GetMapping("/list")
    public TableDataInfo<ComplianceRegulationVo> list(ComplianceRegulationBo bo, PageQuery pageQuery) {
        return regulationService.selectPageList(bo, pageQuery);
    }

    /**
     * 导出法规库列表
     */
    @Log(title = "法规管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("compliance:regulation:export")
    @PostMapping("/export")
    public void export(ComplianceRegulationBo bo, HttpServletResponse response) {
        List<ComplianceRegulationVo> list = regulationService.selectList(bo);
        ExcelUtil.exportExcel(list, "法规数据", ComplianceRegulationVo.class, response);
    }

    /**
     * 根据法规编号获取详细信息
     *
     * @param regulationId 法规ID
     */
    @SaCheckPermission("compliance:regulation:query")
    @GetMapping(value = "/{regulationId}")
    public R<ComplianceRegulationVo> getInfo(@PathVariable Long regulationId) {
        return R.ok(regulationService.selectById(regulationId));
    }

    /**
     * 新增法规库
     */
    @SaCheckPermission("compliance:regulation:add")
    @Log(title = "法规管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody ComplianceRegulationBo bo) {
        if (!regulationService.checkCodeUnique(bo)) {
            return R.fail("新增法规'" + bo.getRegulationName() + "'失败，法规编码已存在");
        }
        regulationService.insert(bo);
        return R.ok();
    }

    /**
     * 修改法规库
     */
    @SaCheckPermission("compliance:regulation:edit")
    @Log(title = "法规管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody ComplianceRegulationBo bo) {
        if (!regulationService.checkCodeUnique(bo)) {
            return R.fail("修改法规'" + bo.getRegulationName() + "'失败，法规编码已存在");
        }
        regulationService.update(bo);
        return R.ok();
    }

    /**
     * 删除法规库
     *
     * @param regulationIds 法规ID串
     */
    @SaCheckPermission("compliance:regulation:remove")
    @Log(title = "法规管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{regulationIds}")
    public R<Void> remove(@PathVariable Long[] regulationIds) {
        regulationService.deleteByIds(List.of(regulationIds));
        return R.ok();
    }
}
