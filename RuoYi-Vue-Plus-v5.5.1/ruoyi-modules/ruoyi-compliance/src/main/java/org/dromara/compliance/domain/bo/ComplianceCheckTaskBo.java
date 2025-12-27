package org.dromara.compliance.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.compliance.domain.ComplianceCheckTask;

/**
 * 检查任务业务对象 compliance_check_task
 *
 * @author compliance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ComplianceCheckTask.class, reverseConvertGenerate = false)
public class ComplianceCheckTaskBo extends BaseEntity {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务名称
     */
    @NotBlank(message = "任务名称不能为空")
    @Size(min = 0, max = 200, message = "任务名称不能超过{max}个字符")
    private String taskName;

    /**
     * 文档ID
     */
    @NotNull(message = "文档ID不能为空")
    private Long documentId;

    /**
     * 规则ID列表(JSON数组)
     */
    @NotBlank(message = "规则ID列表不能为空")
    private String ruleIds;

    /**
     * 检查类型（HARD硬规则 RAG语义规则）
     */
    @Size(min = 0, max = 20, message = "检查类型不能超过{max}个字符")
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
}
