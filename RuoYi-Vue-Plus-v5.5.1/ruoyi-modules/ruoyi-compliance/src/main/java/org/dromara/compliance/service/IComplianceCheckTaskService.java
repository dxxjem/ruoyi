package org.dromara.compliance.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.bo.ComplianceCheckTaskBo;
import org.dromara.compliance.domain.vo.ComplianceCheckTaskVo;

import java.util.List;

/**
 * 检查任务服务层
 *
 * @author compliance
 */
public interface IComplianceCheckTaskService {

    /**
     * 分页查询检查任务列表
     *
     * @param bo       查询条件
     * @param pageQuery 分页参数
     * @return 检查任务分页列表
     */
    TableDataInfo<ComplianceCheckTaskVo> selectPageList(ComplianceCheckTaskBo bo, PageQuery pageQuery);

    /**
     * 查询检查任务信息
     *
     * @param taskId 任务ID
     * @return 检查任务信息
     */
    ComplianceCheckTaskVo selectById(Long taskId);

    /**
     * 查询检查任务列表
     *
     * @param bo 检查任务信息
     * @return 检查任务集合
     */
    List<ComplianceCheckTaskVo> selectList(ComplianceCheckTaskBo bo);

    /**
     * 新增检查任务
     *
     * @param bo 检查任务信息
     * @return 任务ID
     */
    Long insert(ComplianceCheckTaskBo bo);

    /**
     * 修改检查任务
     *
     * @param bo 检查任务信息
     * @return 结果
     */
    Boolean update(ComplianceCheckTaskBo bo);

    /**
     * 批量删除检查任务信息
     *
     * @param taskIds 需要删除的任务ID
     */
    void deleteByIds(List<Long> taskIds);

    /**
     * 更新任务状态
     *
     * @param taskId 任务ID
     * @param status 状态
     * @param progress 进度
     * @return 结果
     */
    Boolean updateStatus(Long taskId, String status, Integer progress);
}
