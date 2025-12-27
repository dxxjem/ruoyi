package org.dromara.compliance.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 规则配置表 compliance_rule
 *
 * @author compliance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("compliance_rule")
public class ComplianceRule extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(value = "rule_id", type = IdType.ASSIGN_ID)
    private Long ruleId;

    /**
     * 规则编码
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则类型（HARD-硬规则 RAG-语义规则）
     */
    private String ruleType;

    /**
     * 分类
     */
    private String category;

    /**
     * 关键词数组（硬规则用）
     * PostgreSQL TEXT[]类型，需要TypeHandler
     */
    @TableField(typeHandler = org.dromara.compliance.handler.ComplianceStringArrayTypeHandler.class)
    private List<String> keywords;

    /**
     * 描述
     */
    private String description;

    /**
     * 严重程度（HIGH高 MEDIUM中 LOW低）
     */
    private String severity;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 排序号
     */
    private Integer ruleOrder;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
