package org.dromara.compliance.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.compliance.domain.ComplianceRegulation;

import java.util.Date;

/**
 * 法规库业务对象 compliance_regulation
 *
 * @author compliance
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ComplianceRegulation.class, reverseConvertGenerate = false)
public class ComplianceRegulationBo extends BaseEntity {

    /**
     * 法规ID
     */
    private Long regulationId;

    /**
     * 法规编码
     */
    @NotBlank(message = "法规编码不能为空")
    @Size(min = 0, max = 100, message = "法规编码不能超过{max}个字符")
    private String regulationCode;

    /**
     * 法规名称
     */
    @NotBlank(message = "法规名称不能为空")
    @Size(min = 0, max = 500, message = "法规名称不能超过{max}个字符")
    private String regulationName;

    /**
     * 分类
     */
    @Size(min = 0, max = 100, message = "分类不能超过{max}个字符")
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
    @Size(min = 0, max = 500, message = "文件URL不能超过{max}个字符")
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
}
