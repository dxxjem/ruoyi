import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { RegulationForm, RegulationQuery, RegulationVO } from './types';

/**
 * 查询法规列表
 * @param query
 */
export const listRegulation = (query: RegulationQuery): AxiosPromise<RegulationVO[]> => {
  return request({
    url: '/compliance/regulation/list',
    method: 'get',
    params: query
  });
};

/**
 * 获取法规详情
 * @param regulationId
 */
export const getRegulation = (regulationId: string | number): AxiosPromise<RegulationVO> => {
  return request({
    url: '/compliance/regulation/' + regulationId,
    method: 'get'
  });
};

/**
 * 新增法规
 */
export const addRegulation = (data: RegulationForm) => {
  return request({
    url: '/compliance/regulation',
    method: 'post',
    data: data
  });
};

/**
 * 修改法规
 */
export const updateRegulation = (data: RegulationForm) => {
  return request({
    url: '/compliance/regulation',
    method: 'put',
    data: data
  });
};

/**
 * 删除法规
 * @param regulationId 法规ID
 */
export const delRegulation = (regulationId: Array<string | number> | string | number) => {
  return request({
    url: '/compliance/regulation/' + regulationId,
    method: 'delete'
  });
};

export default {
  listRegulation,
  getRegulation,
  addRegulation,
  updateRegulation,
  delRegulation
};
