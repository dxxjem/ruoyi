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
 * 法规库表 compliance_regulation
 *
 * @author compliance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("compliance_regulation")
public class ComplianceRegulation extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 法规ID
     */
    @TableId(value = "regulation_id", type = IdType.ASSIGN_ID)
    private Long regulationId;

    /**
     * 法规编码
     */
    private String regulationCode;

    /**
     * 法规名称
     */
    private String regulationName;

    /**
     * 分类
     */
    private String category;

    /**
     * 生效日期
     */
    private Date effectiveDate;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

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
}
