package org.dromara.compliance.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.bo.ComplianceRuleBo;
import org.dromara.compliance.domain.vo.ComplianceRuleVo;

import java.util.List;

/**
 * 规则配置服务层
 *
 * @author compliance
 */
public interface IComplianceRuleService {

    /**
     * 分页查询规则配置列表
     *
     * @param bo       查询条件
     * @param pageQuery 分页参数
     * @return 规则配置分页列表
     */
    TableDataInfo<ComplianceRuleVo> selectPageList(ComplianceRuleBo bo, PageQuery pageQuery);

    /**
     * 查询规则配置信息
     *
     * @param ruleId 规则ID
     * @return 规则配置信息
     */
    ComplianceRuleVo selectById(Long ruleId);

    /**
     * 查询规则配置列表
     *
     * @param bo 规则配置信息
     * @return 规则配置集合
     */
    List<ComplianceRuleVo> selectList(ComplianceRuleBo bo);

    /**
     * 新增规则配置
     *
     * @param bo 规则配置信息
     * @return 规则ID
     */
    Long insert(ComplianceRuleBo bo);

    /**
     * 修改规则配置
     *
     * @param bo 规则配置信息
     * @return 结果
     */
    Boolean update(ComplianceRuleBo bo);

    /**
     * 批量删除规则信息
     *
     * @param ruleIds 需要删除的规则ID
     */
    void deleteByIds(List<Long> ruleIds);

    /**
     * 校验规则编码是否唯一
     *
     * @param bo 规则信息
     * @return 结果
     */
    boolean checkCodeUnique(ComplianceRuleBo bo);
}
