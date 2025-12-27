package org.dromara.compliance.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.compliance.domain.ComplianceDocument;

/**
 * 待检查文档业务对象 compliance_document
 *
 * @author compliance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ComplianceDocument.class, reverseConvertGenerate = false)
public class ComplianceDocumentBo extends BaseEntity {

    /**
     * 文档ID
     */
    private Long documentId;

    /**
     * 文档名称
     */
    @NotBlank(message = "文档名称不能为空")
    @Size(min = 0, max = 200, message = "文档名称不能超过{max}个字符")
    private String documentName;

    /**
     * 文件路径
     */
    @NotBlank(message = "文件路径不能为空")
    @Size(min = 0, max = 500, message = "文件路径不能超过{max}个字符")
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    @Size(min = 0, max = 20, message = "文件类型不能超过{max}个字符")
    private String fileType;

    /**
     * 状态（PENDING待处理 PROCESSING处理中 COMPLETED已完成 FAILED失败）
     */
    private String status;

    /**
     * 上传人
     */
    private Long uploader;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建部门
     */
    private Long createDept;
}
