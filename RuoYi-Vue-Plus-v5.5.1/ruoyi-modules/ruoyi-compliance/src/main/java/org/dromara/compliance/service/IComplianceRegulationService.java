package org.dromara.compliance.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.bo.ComplianceRegulationBo;
import org.dromara.compliance.domain.vo.ComplianceRegulationVo;

import java.util.List;

/**
 * 法规库服务层
 *
 * @author compliance
 */
public interface IComplianceRegulationService {

    /**
     * 分页查询法规库列表
     *
     * @param bo       查询条件
     * @param pageQuery 分页参数
     * @return 法规库分页列表
     */
    TableDataInfo<ComplianceRegulationVo> selectPageList(ComplianceRegulationBo bo, PageQuery pageQuery);

    /**
     * 查询法规库信息
     *
     * @param regulationId 法规ID
     * @return 法规库信息
     */
    ComplianceRegulationVo selectById(Long regulationId);

    /**
     * 查询法规库列表
     *
     * @param bo 法规库信息
     * @return 法规库集合
     */
    List<ComplianceRegulationVo> selectList(ComplianceRegulationBo bo);

    /**
     * 新增法规库
     *
     * @param bo 法规库信息
     * @return 法规ID
     */
    Long insert(ComplianceRegulationBo bo);

    /**
     * 修改法规库
     *
     * @param bo 法规库信息
     * @return 结果
     */
    Boolean update(ComplianceRegulationBo bo);

    /**
     * 批量删除法规信息
     *
     * @param regulationIds 需要删除的法规ID
     */
    void deleteByIds(List<Long> regulationIds);

    /**
     * 校验法规编码是否唯一
     *
     * @param bo 法规信息
     * @return 结果
     */
    boolean checkCodeUnique(ComplianceRegulationBo bo);
}
