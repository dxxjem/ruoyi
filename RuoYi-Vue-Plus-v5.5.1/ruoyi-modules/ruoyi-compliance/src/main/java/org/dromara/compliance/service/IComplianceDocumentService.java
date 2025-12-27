package org.dromara.compliance.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.bo.ComplianceDocumentBo;
import org.dromara.compliance.domain.vo.ComplianceDocumentVo;

import java.util.List;

/**
 * 待检查文档服务层
 *
 * @author compliance
 */
public interface IComplianceDocumentService {

    /**
     * 分页查询待检查文档列表
     *
     * @param bo       查询条件
     * @param pageQuery 分页参数
     * @return 待检查文档分页列表
     */
    TableDataInfo<ComplianceDocumentVo> selectPageList(ComplianceDocumentBo bo, PageQuery pageQuery);

    /**
     * 查询待检查文档信息
     *
     * @param documentId 文档ID
     * @return 待检查文档信息
     */
    ComplianceDocumentVo selectById(Long documentId);

    /**
     * 查询待检查文档列表
     *
     * @param bo 待检查文档信息
     * @return 待检查文档集合
     */
    List<ComplianceDocumentVo> selectList(ComplianceDocumentBo bo);

    /**
     * 新增待检查文档
     *
     * @param bo 待检查文档信息
     * @return 文档ID
     */
    Long insert(ComplianceDocumentBo bo);

    /**
     * 修改待检查文档
     *
     * @param bo 待检查文档信息
     * @return 结果
     */
    Boolean update(ComplianceDocumentBo bo);

    /**
     * 批量删除待检查文档信息
     *
     * @param documentIds 需要删除的文档ID
     */
    void deleteByIds(List<Long> documentIds);
}
