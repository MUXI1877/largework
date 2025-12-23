import request from './request'

// 销售报价管理
export const getSalesQuotationList = (params) => {
  return request.get('/sales-quotation/list', { params })
}

export const getSalesQuotationById = (id) => {
  return request.get(`/sales-quotation/${id}`)
}

export const saveSalesQuotation = (data) => {
  return request.post('/sales-quotation/save', data)
}

export const deleteSalesQuotation = (id) => {
  return request.delete(`/sales-quotation/${id}`)
}

export const getQuotationItems = (id) => {
  return request.get(`/sales-quotation/${id}/items`)
}

export const saveQuotationItems = (id, data) => {
  return request.post(`/sales-quotation/${id}/items`, data)
}

export const getPrintData = (id) => {
  return request.get(`/sales-quotation/${id}/print`)
}

export const exportSalesQuotations = (params) => {
  return request.get('/sales-quotation/export', { params, responseType: 'blob' })
}

