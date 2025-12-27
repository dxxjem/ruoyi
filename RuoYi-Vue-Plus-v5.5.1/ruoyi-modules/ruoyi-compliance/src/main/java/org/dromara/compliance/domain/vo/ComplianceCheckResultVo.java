package org.dromara.compliance.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 检查结果明细视图对象 compliance_check_result
 *
 * @author compliance
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = org.dromara.compliance.domain.ComplianceCheckResult.class)
public class ComplianceCheckResultVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 结果ID
     */
    @ExcelProperty(value = "结果ID")
    private Long resultId;

    /**
     * 任务ID
     */
    @ExcelProperty(value = "任务ID")
    private Long taskId;

    /**
     * 文档ID
     */
    @ExcelProperty(value = "文档ID")
    private Long documentId;

    /**
     * 规则ID
     */
    @ExcelProperty(value = "规则ID")
    private Long ruleId;

    /**
     * 规则名称
     */
    @ExcelProperty(value = "规则名称")
    private String ruleName;

    /**
     * 规则类型（HARD RAG）
     */
    @ExcelProperty(value = "规则类型")
    private String ruleType;

    /**
     * 是否违规
     */
    @ExcelProperty(value = "是否违规")
    private Boolean violated;

    /**
     * 严重程度（HIGH MEDIUM LOW）
     */
    @ExcelProperty(value = "严重程度")
    private String severity;

    /**
     * 违规位置（页码、段落等JSON格式）
     */
    @ExcelProperty(value = "违规位置")
    private String location;

    /**
     * 匹配内容
     */
    @ExcelProperty(value = "匹配内容")
    private String matchedContent;

    /**
     * 违规描述
     */
    @ExcelProperty(value = "违规描述")
    private String violationDesc;

    /**
     * 修改建议
     */
    @ExcelProperty(value = "修改建议")
    private String suggestion;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
