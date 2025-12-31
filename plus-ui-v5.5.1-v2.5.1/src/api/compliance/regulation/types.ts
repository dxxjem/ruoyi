/**
 * 法规查询对象类型
 */
export interface RegulationQuery extends PageQuery {
  regulationCode?: string;
  regulationName?: string;
  category?: string;
  status?: string;
}

/**
 * 法规返回对象
 */
export interface RegulationVO extends BaseEntity {
  regulationId: string | number;
  regulationCode: string;
  regulationName: string;
  category?: string;
  effectiveDate?: string;
  status: string;
  fileUrl?: string;
  fileName?: string;
  fileSize?: number;
  remark?: string;
}

/**
 * 法规表单类型
 */
export interface RegulationForm {
  regulationId?: string | number;
  regulationCode: string;
  regulationName: string;
  category?: string;
  effectiveDate?: string;
  status?: string;
  fileUrl?: string;
  fileName?: string;
  fileSize?: number;
  remark?: string;
}

/**
 * 法规文件上传响应类型
 */
export interface RegulationUploadVO {
  regulationId: string | number;
  url: string;
  fileName: string;
  ossId: string;
  fileSize: number;
}
