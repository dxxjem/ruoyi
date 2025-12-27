package org.dromara.compliance.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.compliance.domain.vo.ComplianceCheckResultVo;
import org.dromara.compliance.service.IComplianceCheckResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查结果明细 信息操作处理
 *
 * @author compliance
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/compliance/check/result")
public class ComplianceCheckResultController extends BaseController {

    private final IComplianceCheckResultService resultService;

    /**
     * 获取检查结果明细列表
     *
     * @param taskId 任务ID
     */
    @SaCheckPermission("compliance:result:list")
    @GetMapping("/list/{taskId}")
    public TableDataInfo<ComplianceCheckResultVo> list(@PathVariable Long taskId, PageQuery pageQuery) {
        return resultService.selectPageListByTaskId(taskId, pageQuery);
    }

    /**
     * 导出检查结果明细列表
     */
    @Log(title = "结果管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("compliance:result:export")
    @PostMapping("/export/{taskId}")
    public void export(@PathVariable Long taskId, HttpServletResponse response) {
        List<ComplianceCheckResultVo> list = resultService.selectListByTaskId(taskId);
        ExcelUtil.exportExcel(list, "检查结果数据", ComplianceCheckResultVo.class, response);
    }

    /**
     * 根据结果编号获取详细信息
     *
     * @param resultId 结果ID
     */
    @SaCheckPermission("compliance:result:query")
    @GetMapping(value = "/info/{resultId}")
    public R<ComplianceCheckResultVo> getInfo(@PathVariable Long resultId) {
        return R.ok(resultService.selectById(resultId));
    }

    /**
     * 删除检查结果明细
     *
     * @param resultIds 结果ID串
     */
    @SaCheckPermission("compliance:result:remove")
    @Log(title = "结果管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{resultIds}")
    public R<Void> remove(@PathVariable Long[] resultIds) {
        resultService.deleteByIds(List.of(resultIds));
        return R.ok();
    }
}
