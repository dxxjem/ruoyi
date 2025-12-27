package org.dromara.compliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.ComplianceRule;
import org.dromara.compliance.domain.bo.ComplianceRuleBo;
import org.dromara.compliance.domain.vo.ComplianceRuleVo;
import org.dromara.compliance.mapper.ComplianceRuleMapper;
import org.dromara.compliance.service.IComplianceRuleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 规则配置 服务层实现
 *
 * @author compliance
 */
@RequiredArgsConstructor
@Service
public class ComplianceRuleServiceImpl implements IComplianceRuleService {

    private final ComplianceRuleMapper baseMapper;

    /**
     * 分页查询规则配置列表
     */
    @Override
    public TableDataInfo<ComplianceRuleVo> selectPageList(ComplianceRuleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ComplianceRule> lqw = buildQueryWrapper(bo);
        Page<ComplianceRuleVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 查询规则配置信息
     */
    @Override
    public ComplianceRuleVo selectById(Long ruleId) {
        return baseMapper.selectVoById(ruleId);
    }

    /**
     * 查询规则配置列表
     */
    @Override
    public List<ComplianceRuleVo> selectList(ComplianceRuleBo bo) {
        LambdaQueryWrapper<ComplianceRule> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增规则配置
     */
    @Override
    public Long insert(ComplianceRuleBo bo) {
        ComplianceRule rule = MapstructUtils.convert(bo, ComplianceRule.class);
        baseMapper.insert(rule);
        return rule.getRuleId();
    }

    /**
     * 修改规则配置
     */
    @Override
    public Boolean update(ComplianceRuleBo bo) {
        ComplianceRule rule = MapstructUtils.convert(bo, ComplianceRule.class);
        return baseMapper.updateById(rule) > 0;
    }

    /**
     * 批量删除规则信息
     */
    @Override
    public void deleteByIds(List<Long> ruleIds) {
        baseMapper.deleteBatchIds(ruleIds);
    }

    /**
     * 校验规则编码是否唯一
     */
    @Override
    public boolean checkCodeUnique(ComplianceRuleBo bo) {
        LambdaQueryWrapper<ComplianceRule> lqw = Wrappers.lambdaQuery();
        lqw.eq(ComplianceRule::getRuleCode, bo.getRuleCode());
        if (bo.getRuleId() != null) {
            lqw.ne(ComplianceRule::getRuleId, bo.getRuleId());
        }
        return !baseMapper.exists(lqw);
    }

    private LambdaQueryWrapper<ComplianceRule> buildQueryWrapper(ComplianceRuleBo bo) {
        LambdaQueryWrapper<ComplianceRule> lqw = Wrappers.lambdaQuery();
        lqw.like(bo.getRuleName() != null, ComplianceRule::getRuleName, bo.getRuleName());
        lqw.eq(bo.getRuleCode() != null, ComplianceRule::getRuleCode, bo.getRuleCode());
        lqw.eq(bo.getRuleType() != null, ComplianceRule::getRuleType, bo.getRuleType());
        lqw.eq(bo.getCategory() != null, ComplianceRule::getCategory, bo.getCategory());
        lqw.eq(bo.getStatus() != null, ComplianceRule::getStatus, bo.getStatus());
        lqw.orderByAsc(ComplianceRule::getRuleOrder);
        lqw.orderByDesc(ComplianceRule::getCreateTime);
        return lqw;
    }
}
