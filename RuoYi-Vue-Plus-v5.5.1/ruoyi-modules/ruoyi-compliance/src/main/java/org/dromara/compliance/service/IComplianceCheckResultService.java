package org.dromara.compliance.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.vo.ComplianceCheckResultVo;

import java.util.List;

/**
 * 检查结果明细服务层
 *
 * @author compliance
 */
public interface IComplianceCheckResultService {

    /**
     * 分页查询检查结果明细列表
     *
     * @param taskId    任务ID
     * @param pageQuery 分页参数
     * @return 检查结果明细分页列表
     */
    TableDataInfo<ComplianceCheckResultVo> selectPageListByTaskId(Long taskId, PageQuery pageQuery);

    /**
     * 查询检查结果明细列表
     *
     * @param taskId 任务ID
     * @return 检查结果明细集合
     */
    List<ComplianceCheckResultVo> selectListByTaskId(Long taskId);

    /**
     * 查询检查结果明细信息
     *
     * @param resultId 结果ID
     * @return 检查结果明细信息
     */
    ComplianceCheckResultVo selectById(Long resultId);

    /**
     * 批量删除检查结果明细信息
     *
     * @param resultIds 需要删除的结果ID
     */
    void deleteByIds(List<Long> resultIds);
}
