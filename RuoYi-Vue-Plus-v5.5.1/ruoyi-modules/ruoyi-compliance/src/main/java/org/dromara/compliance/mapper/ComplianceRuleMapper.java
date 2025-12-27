package org.dromara.compliance.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.compliance.domain.ComplianceRule;
import org.dromara.compliance.domain.vo.ComplianceRuleVo;

/**
 * 规则配置 数据层
 * 使用 @DS("vector") 注解指定使用 PostgreSQL 向量数据源
 *
 * @author compliance
 */
@DS("vector")
public interface ComplianceRuleMapper extends BaseMapperPlus<ComplianceRule, ComplianceRuleVo> {

}
