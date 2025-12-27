package org.dromara.compliance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 文档切块表 compliance_document_chunk
 *
 * @author compliance
 */
@Data
@TableName("compliance_document_chunk")
public class ComplianceDocumentChunk implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 切块ID
     */
    @TableId(value = "chunk_id", type = IdType.ASSIGN_ID)
    private Long chunkId;

    /**
     * 法规ID
     */
    private Long regulationId;

    /**
     * 切块序号
     */
    private Integer chunkIndex;

    /**
     * 文本内容
     */
    private String content;

    /**
     * 向量嵌入（1536维）
     * pgvector类型，由JDBC驱动处理
     */
    private Object embedding;

    /**
     * 元数据（JSON格式）
     */
    private String metadata;

    /**
     * 创建时间
     */
    private Date createTime;
}
