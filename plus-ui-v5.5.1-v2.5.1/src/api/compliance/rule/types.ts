/**
 * 规则查询对象类型
 */
export interface RuleQuery extends PageQuery {
  ruleCode?: string;
  ruleName?: string;
  ruleType?: string;
  category?: string;
  status?: string;
}

/**
 * 规则返回对象
 */
export interface RuleVO extends BaseEntity {
  ruleId: string | number;
  ruleCode: string;
  ruleName: string;
  ruleType: string;
  category?: string;
  keywords?: string[];
  description?: string;
  severity: string;
  status: string;
  ruleOrder: number;
}

/**
 * 规则表单类型
 */
export interface RuleForm {
  ruleId?: string | number;
  ruleCode: string;
  ruleName: string;
  ruleType: string;
  category?: string;
  keywords?: string[];
  description?: string;
  severity?: string;
  status?: string;
  ruleOrder?: number;
}
