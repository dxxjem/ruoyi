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
 * 待检查文档表 compliance_document
 *
 * @author compliance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("compliance_document")
public class ComplianceDocument extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文档ID
     */
    @TableId(value = "document_id", type = IdType.AUTO)
    private Long documentId;

    /**
     * 文档名称
     */
    private String documentName;

    /**
     * 文件路径
     */
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 上传人
     */
    private Long uploader;

    /**
     * 状态（PENDING待处理 PROCESSING处理中 COMPLETED已完成 FAILED失败）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建部门
     */
    private Long createDept;

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

    /**
     * 删除标志（0存在 2删除）
     */
    private String delFlag;
}
