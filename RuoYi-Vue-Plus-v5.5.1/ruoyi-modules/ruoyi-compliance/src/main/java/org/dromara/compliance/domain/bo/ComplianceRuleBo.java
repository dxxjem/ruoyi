package org.dromara.compliance.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.compliance.domain.ComplianceRule;

import java.util.List;

/**
 * 规则配置业务对象 compliance_rule
 *
 * @author compliance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ComplianceRule.class, reverseConvertGenerate = false)
public class ComplianceRuleBo extends BaseEntity {

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 规则编码
     */
    @NotBlank(message = "规则编码不能为空")
    @Size(min = 0, max = 100, message = "规则编码不能超过{max}个字符")
    private String ruleCode;

    /**
     * 规则名称
     */
    @NotBlank(message = "规则名称不能为空")
    @Size(min = 0, max = 200, message = "规则名称不能超过{max}个字符")
    private String ruleName;

    /**
     * 规则类型（HARD-硬规则 RAG-语义规则）
     */
    @NotBlank(message = "规则类型不能为空")
    @Size(min = 0, max = 20, message = "规则类型不能超过{max}个字符")
    private String ruleType;

    /**
     * 分类
     */
    @Size(min = 0, max = 100, message = "分类不能超过{max}个字符")
    private String category;

    /**
     * 关键词数组（硬规则用）
     */
    private List<String> keywords;

    /**
     * 描述
     */
    private String description;

    /**
     * 严重程度（HIGH高 MEDIUM中 LOW低）
     */
    @Size(min = 0, max = 20, message = "严重程度不能超过{max}个字符")
    private String severity;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 排序号
     */
    private Integer ruleOrder;
}
