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
import org.dromara.compliance.domain.bo.ComplianceRuleBo;
import org.dromara.compliance.domain.vo.ComplianceRuleVo;
import org.dromara.compliance.service.IComplianceRuleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 规则配置 信息操作处理
 *
 * @author compliance
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/compliance/rule")
public class ComplianceRuleController extends BaseController {

    private final IComplianceRuleService ruleService;

    /**
     * 获取规则配置列表
     */
    @SaCheckPermission("compliance:rule:list")
    @GetMapping("/list")
    public TableDataInfo<ComplianceRuleVo> list(ComplianceRuleBo bo, PageQuery pageQuery) {
        return ruleService.selectPageList(bo, pageQuery);
    }

    /**
     * 导出规则配置列表
     */
    @Log(title = "规则管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("compliance:rule:export")
    @PostMapping("/export")
    public void export(ComplianceRuleBo bo, HttpServletResponse response) {
        List<ComplianceRuleVo> list = ruleService.selectList(bo);
        ExcelUtil.exportExcel(list, "规则数据", ComplianceRuleVo.class, response);
    }

    /**
     * 根据规则编号获取详细信息
     *
     * @param ruleId 规则ID
     */
    @SaCheckPermission("compliance:rule:query")
    @GetMapping(value = "/{ruleId}")
    public R<ComplianceRuleVo> getInfo(@PathVariable Long ruleId) {
        return R.ok(ruleService.selectById(ruleId));
    }

    /**
     * 新增规则配置
     */
    @SaCheckPermission("compliance:rule:add")
    @Log(title = "规则管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody ComplianceRuleBo bo) {
        if (!ruleService.checkCodeUnique(bo)) {
            return R.fail("新增规则'" + bo.getRuleName() + "'失败，规则编码已存在");
        }
        ruleService.insert(bo);
        return R.ok();
    }

    /**
     * 修改规则配置
     */
    @SaCheckPermission("compliance:rule:edit")
    @Log(title = "规则管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody ComplianceRuleBo bo) {
        if (!ruleService.checkCodeUnique(bo)) {
            return R.fail("修改规则'" + bo.getRuleName() + "'失败，规则编码已存在");
        }
        ruleService.update(bo);
        return R.ok();
    }

    /**
     * 删除规则配置
     *
     * @param ruleIds 规则ID串
     */
    @SaCheckPermission("compliance:rule:remove")
    @Log(title = "规则管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ruleIds}")
    public R<Void> remove(@PathVariable Long[] ruleIds) {
        ruleService.deleteByIds(List.of(ruleIds));
        return R.ok();
    }
}
