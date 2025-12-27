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
import org.dromara.compliance.domain.bo.ComplianceCheckTaskBo;
import org.dromara.compliance.domain.vo.ComplianceCheckTaskVo;
import org.dromara.compliance.service.IComplianceCheckTaskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查任务 信息操作处理
 *
 * @author compliance
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/compliance/task")
public class ComplianceCheckTaskController extends BaseController {

    private final IComplianceCheckTaskService taskService;

    /**
     * 获取检查任务列表
     */
    @SaCheckPermission("compliance:task:list")
    @GetMapping("/list")
    public TableDataInfo<ComplianceCheckTaskVo> list(ComplianceCheckTaskBo bo, PageQuery pageQuery) {
        return taskService.selectPageList(bo, pageQuery);
    }

    /**
     * 导出检查任务列表
     */
    @Log(title = "任务管理", businessType = BusinessType.EXPORT)
    @SaCheckPermission("compliance:task:export")
    @PostMapping("/export")
    public void export(ComplianceCheckTaskBo bo, HttpServletResponse response) {
        List<ComplianceCheckTaskVo> list = taskService.selectList(bo);
        ExcelUtil.exportExcel(list, "任务数据", ComplianceCheckTaskVo.class, response);
    }

    /**
     * 根据任务编号获取详细信息
     *
     * @param taskId 任务ID
     */
    @SaCheckPermission("compliance:task:query")
    @GetMapping(value = "/{taskId}")
    public R<ComplianceCheckTaskVo> getInfo(@PathVariable Long taskId) {
        return R.ok(taskService.selectById(taskId));
    }

    /**
     * 新增检查任务
     */
    @SaCheckPermission("compliance:task:add")
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@Validated @RequestBody ComplianceCheckTaskBo bo) {
        taskService.insert(bo);
        return R.ok();
    }

    /**
     * 修改检查任务
     */
    @SaCheckPermission("compliance:task:edit")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> edit(@Validated @RequestBody ComplianceCheckTaskBo bo) {
        taskService.update(bo);
        return R.ok();
    }

    /**
     * 删除检查任务
     *
     * @param taskIds 任务ID串
     */
    @SaCheckPermission("compliance:task:remove")
    @Log(title = "任务管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{taskIds}")
    public R<Void> remove(@PathVariable Long[] taskIds) {
        taskService.deleteByIds(List.of(taskIds));
        return R.ok();
    }
}
