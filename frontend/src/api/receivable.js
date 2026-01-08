import request from './request'

// 4.5.1 应收账计划
export function getReceivablePlans(params) {
  return request({
    url: '/receivable-plan/page',
    method: 'get',
    params
  })
}

export function createReceivablePlan(data) {
  return request({
    url: '/receivable-plan',
    method: 'post',
    data
  })
}

export function updateReceivablePlan(id, data) {
  return request({
    url: `/receivable-plan/${id}`,
    method: 'put',
    data
  })
}

export function deleteReceivablePlan(id) {
  return request({
    url: `/receivable-plan/${id}`,
    method: 'delete'
  })
}

export function exportReceivablePlans(params) {
  return request({
    url: '/receivable-plan/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 4.5.2 回款登记
export function getReceivableReceipts(params) {
  return request({
    url: '/receivable-receipt/page',
    method: 'get',
    params
  })
}

export function createReceivableReceipt(data) {
  return request({
    url: '/receivable-receipt',
    method: 'post',
    data
  })
}

// 更新回款记录（包含金额、日期等字段）
export function updateReceivableReceipt(id, data) {
  return request({
    url: `/receivable-receipt/${id}`,
    method: 'put',
    data
  })
}

export function updateReceiptRemark(id, data) {
  return request({
    url: `/receivable-receipt/${id}/remark`,
    method: 'put',
    data
  })
}

export function deleteReceivableReceipt(id) {
  return request({
    url: `/receivable-receipt/${id}`,
    method: 'delete'
  })
}

export function exportReceivableReceipts(params) {
  return request({
    url: '/receivable-receipt/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 4.5.3 汇总查询
export function getReceivableSummary(params) {
  return request({
    url: '/receivable-summary/page',
    method: 'get',
    params
  })
}

export function exportReceivableSummary(params) {
  return request({
    url: '/receivable-summary/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 根据合同号查询合同信息（用于回款登记自动填充）
export function getContractInfo(contractCode) {
  return request({
    url: '/receivable-receipt/contract-info',
    method: 'get',
    params: { contractCode }
  })
}

