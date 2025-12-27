package org.dromara.compliance.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 规则配置视图对象 compliance_rule
 *
 * @author compliance
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = org.dromara.compliance.domain.ComplianceRule.class)
public class ComplianceRuleVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @ExcelProperty(value = "规则ID")
    private Long ruleId;

    /**
     * 规则编码
     */
    @ExcelProperty(value = "规则编码")
    private String ruleCode;

    /**
     * 规则名称
     */
    @ExcelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 规则类型（HARD-硬规则 RAG-语义规则）
     */
    @ExcelProperty(value = "规则类型")
    private String ruleType;

    /**
     * 分类
     */
    @ExcelProperty(value = "分类")
    private String category;

    /**
     * 关键词数组（硬规则用）
     */
    @ExcelProperty(value = "关键词")
    private List<String> keywords;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String description;

    /**
     * 严重程度（HIGH高 MEDIUM中 LOW低）
     */
    @ExcelProperty(value = "严重程度")
    private String severity;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 排序号
     */
    @ExcelProperty(value = "排序号")
    private Integer ruleOrder;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
