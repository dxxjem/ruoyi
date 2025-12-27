package org.dromara.compliance.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.compliance.domain.ComplianceRegulation;
import org.dromara.compliance.domain.vo.ComplianceRegulationVo;

/**
 * 法规库 数据层
 * 使用 @DS("vector") 注解指定使用 PostgreSQL 向量数据源
 *
 * @author compliance
 */
@DS("vector")
public interface ComplianceRegulationMapper extends BaseMapperPlus<ComplianceRegulation, ComplianceRegulationVo> {

}
