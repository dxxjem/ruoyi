package org.dromara.compliance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 检查结果明细表 compliance_check_result
 *
 * @author compliance
 */
@Data
@TableName("compliance_check_result")
public class ComplianceCheckResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 结果ID
     */
    @TableId(value = "result_id", type = IdType.AUTO)
    private Long resultId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 规则ID
     */
    private Long ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型（HARD RAG）
     */
    private String ruleType;

    /**
     * 是否违规
     */
    private Boolean violated;

    /**
     * 严重程度（HIGH MEDIUM LOW）
     */
    private String severity;

    /**
     * 违规位置（页码、段落等JSON格式）
     */
    private String location;

    /**
     * 匹配内容
     */
    private String matchedContent;

    /**
     * 违规描述
     */
    private String violationDesc;

    /**
     * 修改建议
     */
    private String suggestion;

    /**
     * 创建时间
     */
    private Date createTime;
}
