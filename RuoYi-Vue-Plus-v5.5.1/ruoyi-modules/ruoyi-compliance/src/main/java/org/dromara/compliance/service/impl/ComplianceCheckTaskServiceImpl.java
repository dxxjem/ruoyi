package org.dromara.compliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.ComplianceCheckTask;
import org.dromara.compliance.domain.bo.ComplianceCheckTaskBo;
import org.dromara.compliance.domain.vo.ComplianceCheckTaskVo;
import org.dromara.compliance.mapper.ComplianceCheckTaskMapper;
import org.dromara.compliance.service.IComplianceCheckTaskService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 检查任务 服务层实现
 *
 * @author compliance
 */
@RequiredArgsConstructor
@Service
public class ComplianceCheckTaskServiceImpl implements IComplianceCheckTaskService {

    private final ComplianceCheckTaskMapper baseMapper;

    /**
     * 分页查询检查任务列表
     */
    @Override
    public TableDataInfo<ComplianceCheckTaskVo> selectPageList(ComplianceCheckTaskBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ComplianceCheckTask> lqw = buildQueryWrapper(bo);
        Page<ComplianceCheckTaskVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 查询检查任务信息
     */
    @Override
    public ComplianceCheckTaskVo selectById(Long taskId) {
        return baseMapper.selectVoById(taskId);
    }

    /**
     * 查询检查任务列表
     */
    @Override
    public List<ComplianceCheckTaskVo> selectList(ComplianceCheckTaskBo bo) {
        LambdaQueryWrapper<ComplianceCheckTask> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增检查任务
     */
    @Override
    public Long insert(ComplianceCheckTaskBo bo) {
        ComplianceCheckTask task = MapstructUtils.convert(bo, ComplianceCheckTask.class);
        baseMapper.insert(task);
        return task.getTaskId();
    }

    /**
     * 修改检查任务
     */
    @Override
    public Boolean update(ComplianceCheckTaskBo bo) {
        ComplianceCheckTask task = MapstructUtils.convert(bo, ComplianceCheckTask.class);
        return baseMapper.updateById(task) > 0;
    }

    /**
     * 批量删除检查任务信息
     */
    @Override
    public void deleteByIds(List<Long> taskIds) {
        baseMapper.deleteBatchIds(taskIds);
    }

    /**
     * 更新任务状态
     */
    @Override
    public Boolean updateStatus(Long taskId, String status, Integer progress) {
        ComplianceCheckTask task = new ComplianceCheckTask();
        task.setTaskId(taskId);
        task.setStatus(status);
        if (progress != null) {
            task.setProgress(progress);
        }
        return baseMapper.updateById(task) > 0;
    }

    private LambdaQueryWrapper<ComplianceCheckTask> buildQueryWrapper(ComplianceCheckTaskBo bo) {
        LambdaQueryWrapper<ComplianceCheckTask> lqw = Wrappers.lambdaQuery();
        lqw.like(bo.getTaskName() != null, ComplianceCheckTask::getTaskName, bo.getTaskName());
        lqw.eq(bo.getDocumentId() != null, ComplianceCheckTask::getDocumentId, bo.getDocumentId());
        lqw.eq(bo.getCheckType() != null, ComplianceCheckTask::getCheckType, bo.getCheckType());
        lqw.eq(bo.getStatus() != null, ComplianceCheckTask::getStatus, bo.getStatus());
        lqw.orderByDesc(ComplianceCheckTask::getCreateTime);
        return lqw;
    }
}
