package org.dromara.compliance.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 待检查文档视图对象 compliance_document
 *
 * @author compliance
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = org.dromara.compliance.domain.ComplianceDocument.class)
public class ComplianceDocumentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文档ID
     */
    @ExcelProperty(value = "文档ID")
    private Long documentId;

    /**
     * 文档名称
     */
    @ExcelProperty(value = "文档名称")
    private String documentName;

    /**
     * 文件路径
     */
    @ExcelProperty(value = "文件路径")
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    @ExcelProperty(value = "文件大小")
    private Long fileSize;

    /**
     * 文件类型
     */
    @ExcelProperty(value = "文件类型")
    private String fileType;

    /**
     * 上传时间
     */
    @ExcelProperty(value = "上传时间")
    private Date uploadTime;

    /**
     * 上传人
     */
    @ExcelProperty(value = "上传人")
    private Long uploader;

    /**
     * 状态（PENDING待处理 PROCESSING处理中 COMPLETED已完成 FAILED失败）
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;
}
