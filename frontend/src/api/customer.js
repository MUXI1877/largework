import request from './request'

export const getCustomerList = () => {
  return request.get('/customer/list')
}

export const getCustomerById = (id) => {
  return request.get(`/customer/${id}`)
}

export const saveCustomer = (data) => {
  return request.post('/customer/save', data)
}

export const updateCustomer = (data) => {
  return request.put('/customer/update', data)
}

export const deleteCustomer = (id) => {
  return request.delete(`/customer/${id}`)
}

