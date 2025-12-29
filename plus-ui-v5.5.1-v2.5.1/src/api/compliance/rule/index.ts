import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { RuleForm, RuleQuery, RuleVO } from './types';

/**
 * 查询规则列表
 * @param query
 */
export const listRule = (query: RuleQuery): AxiosPromise<RuleVO[]> => {
  return request({
    url: '/compliance/rule/list',
    method: 'get',
    params: query
  });
};

/**
 * 获取规则详情
 * @param ruleId
 */
export const getRule = (ruleId: string | number): AxiosPromise<RuleVO> => {
  return request({
    url: '/compliance/rule/' + ruleId,
    method: 'get'
  });
};

/**
 * 新增规则
 */
export const addRule = (data: RuleForm) => {
  return request({
    url: '/compliance/rule',
    method: 'post',
    data: data
  });
};

/**
 * 修改规则
 */
export const updateRule = (data: RuleForm) => {
  return request({
    url: '/compliance/rule',
    method: 'put',
    data: data
  });
};

/**
 * 删除规则
 * @param ruleId 规则ID
 */
export const delRule = (ruleId: Array<string | number> | string | number) => {
  return request({
    url: '/compliance/rule/' + ruleId,
    method: 'delete'
  });
};

export default {
  listRule,
  getRule,
  addRule,
  updateRule,
  delRule
};
