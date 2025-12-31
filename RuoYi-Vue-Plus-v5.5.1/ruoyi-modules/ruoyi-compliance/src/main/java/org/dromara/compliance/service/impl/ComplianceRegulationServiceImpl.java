package org.dromara.compliance.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.ComplianceRegulation;
import org.dromara.compliance.domain.bo.ComplianceRegulationBo;
import org.dromara.compliance.domain.vo.ComplianceRegulationUploadVo;
import org.dromara.compliance.domain.vo.ComplianceRegulationVo;
import org.dromara.compliance.mapper.ComplianceRegulationMapper;
import org.dromara.compliance.service.IComplianceRegulationService;
import org.dromara.system.domain.vo.SysOssVo;
import org.dromara.system.service.ISysOssService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 法规库 服务层实现
 *
 * @author compliance
 */
@RequiredArgsConstructor
@Service
public class ComplianceRegulationServiceImpl implements IComplianceRegulationService {

    private final ComplianceRegulationMapper baseMapper;
    private final ISysOssService ossService;

    /**
     * 分页查询法规库列表
     */
    @Override
    public TableDataInfo<ComplianceRegulationVo> selectPageList(ComplianceRegulationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ComplianceRegulation> lqw = buildQueryWrapper(bo);
        Page<ComplianceRegulationVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 查询法规库信息
     */
    @Override
    public ComplianceRegulationVo selectById(Long regulationId) {
        return baseMapper.selectVoById(regulationId);
    }

    /**
     * 查询法规库列表
     */
    @Override
    public List<ComplianceRegulationVo> selectList(ComplianceRegulationBo bo) {
        LambdaQueryWrapper<ComplianceRegulation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增法规库
     */
    @Override
    public Long insert(ComplianceRegulationBo bo) {
        ComplianceRegulation regulation = MapstructUtils.convert(bo, ComplianceRegulation.class);
        baseMapper.insert(regulation);
        return regulation.getRegulationId();
    }

    /**
     * 修改法规库
     */
    @Override
    public Boolean update(ComplianceRegulationBo bo) {
        ComplianceRegulation regulation = MapstructUtils.convert(bo, ComplianceRegulation.class);
        return baseMapper.updateById(regulation) > 0;
    }

    /**
     * 批量删除法规信息
     */
    @Override
    public void deleteByIds(List<Long> regulationIds) {
        baseMapper.deleteBatchIds(regulationIds);
    }

    /**
     * 校验法规编码是否唯一
     */
    @Override
    public boolean checkCodeUnique(ComplianceRegulationBo bo) {
        LambdaQueryWrapper<ComplianceRegulation> lqw = Wrappers.lambdaQuery();
        lqw.eq(ComplianceRegulation::getRegulationCode, bo.getRegulationCode());
        if (bo.getRegulationId() != null) {
            lqw.ne(ComplianceRegulation::getRegulationId, bo.getRegulationId());
        }
        return !baseMapper.exists(lqw);
    }

    /**
     * 上传法规文件并更新法规记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ComplianceRegulationUploadVo uploadRegulationFile(Long regulationId, MultipartFile file) {
        // 校验法规是否存在
        ComplianceRegulation regulation = baseMapper.selectById(regulationId);
        if (ObjectUtil.isNull(regulation)) {
            throw new ServiceException("法规不存在，请先创建法规记录");
        }

        // 校验文件
        if (ObjectUtil.isNull(file) || file.isEmpty()) {
            throw new ServiceException("上传文件不能为空");
        }

        // 调用 OSS 服务上传文件
        SysOssVo oss = ossService.upload(file);

        // 更新法规记录的文件信息
        regulation.setFileUrl(oss.getOssId().toString());
        regulation.setFileSize(file.getSize());
        baseMapper.updateById(regulation);

        // 构造返回结果
        ComplianceRegulationUploadVo uploadVo = new ComplianceRegulationUploadVo();
        uploadVo.setRegulationId(regulationId);
        uploadVo.setUrl(oss.getUrl());
        uploadVo.setFileName(oss.getOriginalName());
        uploadVo.setOssId(oss.getOssId().toString());
        uploadVo.setFileSize(file.getSize());
        return uploadVo;
    }

    /**
     * 附加文件信息到法规VO（用于前端文件回显）
     * 将 ossId 转换为前端 FileUpload 组件需要的格式
     */
    @Override
    public void attachFileInfo(ComplianceRegulationVo vo) {
        if (vo == null || vo.getFileUrl() == null || vo.getFileUrl().isEmpty()) {
            return;
        }

        try {
            // fileUrl 存储的是 ossId，需要查询 OSS 获取完整信息
            Long ossId = Long.parseLong(vo.getFileUrl());
            SysOssVo oss = ossService.getById(ossId);

            if (oss != null) {
                // 附加文件名用于前端显示
                if (vo.getFileName() == null || vo.getFileName().isEmpty()) {
                    vo.setFileName(oss.getOriginalName() != null ? oss.getOriginalName() : oss.getFileName());
                }
            }
        } catch (NumberFormatException e) {
            // 如果 fileUrl 不是有效的 ossId，忽略处理
        }
    }

    private LambdaQueryWrapper<ComplianceRegulation> buildQueryWrapper(ComplianceRegulationBo bo) {
        LambdaQueryWrapper<ComplianceRegulation> lqw = Wrappers.lambdaQuery();
        lqw.like(bo.getRegulationName() != null, ComplianceRegulation::getRegulationName, bo.getRegulationName());
        lqw.eq(bo.getRegulationCode() != null, ComplianceRegulation::getRegulationCode, bo.getRegulationCode());
        lqw.eq(bo.getCategory() != null, ComplianceRegulation::getCategory, bo.getCategory());
        lqw.eq(bo.getStatus() != null, ComplianceRegulation::getStatus, bo.getStatus());
        lqw.orderByDesc(ComplianceRegulation::getCreateTime);
        return lqw;
    }
}
