package org.dromara.compliance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.compliance.domain.ComplianceRegulation;
import org.dromara.compliance.domain.bo.ComplianceRegulationBo;
import org.dromara.compliance.domain.vo.ComplianceRegulationVo;
import org.dromara.compliance.mapper.ComplianceRegulationMapper;
import org.dromara.compliance.service.IComplianceRegulationService;
import org.springframework.stereotype.Service;

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
