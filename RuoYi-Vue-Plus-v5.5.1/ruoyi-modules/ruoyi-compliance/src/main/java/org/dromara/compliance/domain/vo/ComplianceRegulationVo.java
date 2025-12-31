package org.dromara.compliance.domain.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 法规库视图对象 compliance_regulation
 *
 * @author compliance
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = org.dromara.compliance.domain.ComplianceRegulation.class)
public class ComplianceRegulationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 法规ID
     */
    @ExcelProperty(value = "法规ID")
    private Long regulationId;

    /**
     * 法规编码
     */
    @ExcelProperty(value = "法规编码")
    private String regulationCode;

    /**
     * 法规名称
     */
    @ExcelProperty(value = "法规名称")
    private String regulationName;

    /**
     * 分类
     */
    @ExcelProperty(value = "分类")
    private String category;

    /**
     * 生效日期
     */
    @ExcelProperty(value = "生效日期")
    private Date effectiveDate;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 文件URL（存储ossId）
     */
    @ExcelProperty(value = "文件URL")
    private String fileUrl;

    /**
     * 文件名（用于前端显示）
     */
    private String fileName;

    /**
     * 文件大小（字节）
     */
    @ExcelProperty(value = "文件大小")
    private Long fileSize;

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
