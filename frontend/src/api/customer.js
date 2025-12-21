import request from './request'

// 客户管理
export const getCustomerList = () => {
  return request.get('/customer/list')
}

export const getCustomerById = (id) => {
  return request.get(`/customer/${id}`)
}

export const saveCustomer = (data) => {
  return request.post('/customer/save', data)
}

export const deleteCustomer = (id) => {
  return request.delete(`/customer/${id}`)
}

export const exportCustomers = () => {
  return request.get('/customer/export', { responseType: 'blob' })
}

// 客户关键人物
export const getKeyPersons = (customerId) => {
  return request.get(`/customer/${customerId}/key-persons`)
}

export const saveKeyPerson = (data) => {
  return request.post('/customer/key-person/save', data)
}

export const deleteKeyPerson = (id) => {
  return request.delete(`/customer/key-person/${id}`)
}

// 项目机会
export const getProjectOpportunities = (customerId) => {
  return request.get(`/customer/${customerId}/project-opportunities`)
}

// 合同信息
export const getContracts = (customerId) => {
  return request.get(`/customer/${customerId}/contracts`)
}

// 售后信息
export const getAfterSales = (customerId) => {
  return request.get(`/customer/${customerId}/after-sales`)
}

// 客户来访信息
export const getCustomerVisits = (customerId) => {
  return request.get(`/customer/${customerId}/visits`)
}

