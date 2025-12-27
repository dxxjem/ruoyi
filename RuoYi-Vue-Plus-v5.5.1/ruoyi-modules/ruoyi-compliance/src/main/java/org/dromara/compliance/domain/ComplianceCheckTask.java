package org.dromara.compliance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 检查任务表 compliance_check_task
 *
 * @author compliance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("compliance_check_task")
public class ComplianceCheckTask extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @TableId(value = "task_id", type = IdType.AUTO)
    private Long taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 规则ID列表(JSON数组)
     */
    private String ruleIds;

    /**
     * 检查类型（HARD硬规则 RAG语义规则）
     */
    private String checkType;

    /**
     * 任务状态（PENDING待处理 RUNNING运行中 COMPLETED已完成 FAILED失败）
     */
    private String status;

    /**
     * 执行进度(0-100)
     */
    private Integer progress;

    /**
     * 检查结果摘要
     */
    private String resultSummary;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 开始执行时间
     */
    private Date executeTime;

    /**
     * 完成时间
     */
    private Date completeTime;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}
