package org.dromara.compliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.ComplianceCheckResult;
import org.dromara.compliance.domain.vo.ComplianceCheckResultVo;
import org.dromara.compliance.mapper.ComplianceCheckResultMapper;
import org.dromara.compliance.service.IComplianceCheckResultService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 检查结果明细 服务层实现
 *
 * @author compliance
 */
@RequiredArgsConstructor
@Service
public class ComplianceCheckResultServiceImpl implements IComplianceCheckResultService {

    private final ComplianceCheckResultMapper baseMapper;

    /**
     * 分页查询检查结果明细列表
     */
    @Override
    public TableDataInfo<ComplianceCheckResultVo> selectPageListByTaskId(Long taskId, PageQuery pageQuery) {
        LambdaQueryWrapper<ComplianceCheckResult> lqw = Wrappers.lambdaQuery();
        lqw.eq(ComplianceCheckResult::getTaskId, taskId);
        lqw.orderByDesc(ComplianceCheckResult::getCreateTime);
        Page<ComplianceCheckResultVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 查询检查结果明细列表
     */
    @Override
    public List<ComplianceCheckResultVo> selectListByTaskId(Long taskId) {
        LambdaQueryWrapper<ComplianceCheckResult> lqw = Wrappers.lambdaQuery();
        lqw.eq(ComplianceCheckResult::getTaskId, taskId);
        lqw.orderByDesc(ComplianceCheckResult::getCreateTime);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 查询检查结果明细信息
     */
    @Override
    public ComplianceCheckResultVo selectById(Long resultId) {
        return baseMapper.selectVoById(resultId);
    }

    /**
     * 批量删除检查结果明细信息
     */
    @Override
    public void deleteByIds(List<Long> resultIds) {
        baseMapper.deleteBatchIds(resultIds);
    }
}
