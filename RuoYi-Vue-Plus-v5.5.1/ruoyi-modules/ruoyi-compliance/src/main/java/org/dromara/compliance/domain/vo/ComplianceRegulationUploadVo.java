package org.dromara.compliance.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 法规文件上传响应视图对象
 *
 * @author compliance
 */
@Data
public class ComplianceRegulationUploadVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 法规ID
     */
    private Long regulationId;

    /**
     * 文件URL地址
     */
    private String url;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 对象存储主键
     */
    private String ossId;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;
}
