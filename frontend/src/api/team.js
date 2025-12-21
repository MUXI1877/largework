import request from './request'

// 销售片区管理
export const getSalesRegionList = () => {
  return request.get('/sales-region/list')
}

export const getSalesRegionById = (id) => {
  return request.get(`/sales-region/${id}`)
}

export const saveSalesRegion = (data) => {
  return request.post('/sales-region/save', data)
}

export const deleteSalesRegion = (id) => {
  return request.delete(`/sales-region/${id}`)
}

export const exportSalesRegions = () => {
  return request.get('/sales-region/export', { responseType: 'blob' })
}

// 营销人员管理
export const getSalesPersonList = () => {
  return request.get('/sales-person/list')
}

export const getSalesPersonByRegion = (regionId) => {
  return request.get(`/sales-person/region/${regionId}`)
}

export const getSalesPersonById = (id) => {
  return request.get(`/sales-person/${id}`)
}

export const saveSalesPerson = (data) => {
  return request.post('/sales-person/save', data)
}

export const deleteSalesPerson = (id) => {
  return request.delete(`/sales-person/${id}`)
}

export const batchTransfer = (data) => {
  return request.post('/sales-person/batch-transfer', data)
}

export const exportSalesPersons = () => {
  return request.get('/sales-person/export', { responseType: 'blob' })
}

