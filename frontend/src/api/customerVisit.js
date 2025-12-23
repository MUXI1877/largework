import request from './request'

// 客户来访管理
export const getCustomerVisitList = (params) => {
  return request.get('/customer-visit/list', { params })
}

export const getCustomerVisitById = (id) => {
  return request.get(`/customer-visit/${id}`)
}

export const saveCustomerVisit = (data) => {
  return request.post('/customer-visit/save', data)
}

export const deleteCustomerVisit = (id) => {
  return request.delete(`/customer-visit/${id}`)
}

export const exportCustomerVisits = (params) => {
  return request.get('/customer-visit/export', { params, responseType: 'blob' })
}

