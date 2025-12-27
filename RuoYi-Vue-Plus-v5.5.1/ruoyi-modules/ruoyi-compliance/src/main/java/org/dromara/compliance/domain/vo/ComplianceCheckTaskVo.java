package org.dromara.compliance.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 检查任务视图对象 compliance_check_task
 *
 * @author compliance
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = org.dromara.compliance.domain.ComplianceCheckTask.class)
public class ComplianceCheckTaskVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @ExcelProperty(value = "任务ID")
    private Long taskId;

    /**
     * 任务名称
     */
    @ExcelProperty(value = "任务名称")
    private String taskName;

    /**
     * 文档ID
     */
    @ExcelProperty(value = "文档ID")
    private Long documentId;

    /**
     * 规则ID列表(JSON数组)
     */
    @ExcelProperty(value = "规则ID列表")
    private String ruleIds;

    /**
     * 检查类型（HARD硬规则 RAG语义规则）
     */
    @ExcelProperty(value = "检查类型")
    private String checkType;

    /**
     * 任务状态（PENDING待处理 RUNNING运行中 COMPLETED已完成 FAILED失败）
     */
    @ExcelProperty(value = "任务状态")
    private String status;

    /**
     * 执行进度(0-100)
     */
    @ExcelProperty(value = "执行进度")
    private Integer progress;

    /**
     * 检查结果摘要
     */
    @ExcelProperty(value = "检查结果摘要")
    private String resultSummary;

    /**
     * 错误信息
     */
    @ExcelProperty(value = "错误信息")
    private String errorMessage;

    /**
     * 开始执行时间
     */
    @ExcelProperty(value = "开始执行时间")
    private Date executeTime;

    /**
     * 完成时间
     */
    @ExcelProperty(value = "完成时间")
    private Date completeTime;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
