package org.dromara.compliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.ComplianceDocument;
import org.dromara.compliance.domain.bo.ComplianceDocumentBo;
import org.dromara.compliance.domain.vo.ComplianceDocumentVo;
import org.dromara.compliance.mapper.ComplianceDocumentMapper;
import org.dromara.compliance.service.IComplianceDocumentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 待检查文档 服务层实现
 *
 * @author compliance
 */
@RequiredArgsConstructor
@Service
public class ComplianceDocumentServiceImpl implements IComplianceDocumentService {

    private final ComplianceDocumentMapper baseMapper;

    /**
     * 分页查询待检查文档列表
     */
    @Override
    public TableDataInfo<ComplianceDocumentVo> selectPageList(ComplianceDocumentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ComplianceDocument> lqw = buildQueryWrapper(bo);
        Page<ComplianceDocumentVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 查询待检查文档信息
     */
    @Override
    public ComplianceDocumentVo selectById(Long documentId) {
        return baseMapper.selectVoById(documentId);
    }

    /**
     * 查询待检查文档列表
     */
    @Override
    public List<ComplianceDocumentVo> selectList(ComplianceDocumentBo bo) {
        LambdaQueryWrapper<ComplianceDocument> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增待检查文档
     */
    @Override
    public Long insert(ComplianceDocumentBo bo) {
        ComplianceDocument document = MapstructUtils.convert(bo, ComplianceDocument.class);
        baseMapper.insert(document);
        return document.getDocumentId();
    }

    /**
     * 修改待检查文档
     */
    @Override
    public Boolean update(ComplianceDocumentBo bo) {
        ComplianceDocument document = MapstructUtils.convert(bo, ComplianceDocument.class);
        return baseMapper.updateById(document) > 0;
    }

    /**
     * 批量删除待检查文档信息
     */
    @Override
    public void deleteByIds(List<Long> documentIds) {
        baseMapper.deleteBatchIds(documentIds);
    }

    private LambdaQueryWrapper<ComplianceDocument> buildQueryWrapper(ComplianceDocumentBo bo) {
        LambdaQueryWrapper<ComplianceDocument> lqw = Wrappers.lambdaQuery();
        lqw.like(bo.getDocumentName() != null, ComplianceDocument::getDocumentName, bo.getDocumentName());
        lqw.eq(bo.getFileType() != null, ComplianceDocument::getFileType, bo.getFileType());
        lqw.eq(bo.getStatus() != null, ComplianceDocument::getStatus, bo.getStatus());
        lqw.eq(bo.getUploader() != null, ComplianceDocument::getUploader, bo.getUploader());
        lqw.orderByDesc(ComplianceDocument::getCreateTime);
        return lqw;
    }
}
