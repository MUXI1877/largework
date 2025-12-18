import request from './request'

export const getServiceOrderList = () => {
  return request.get('/service-order/list')
}

export const getServiceOrderById = (id) => {
  return request.get(`/service-order/get/${id}`)
}

export const getServiceOrderByOrderNo = (orderNo) => {
  return request.get(`/service-order/order-no/${orderNo}`)
}

export const getServiceOrderListByStatus = (status) => {
  return request.get(`/service-order/status/${status}`)
}

export const getServiceOrderListByCustomerId = (customerId) => {
  return request.get(`/service-order/customer/${customerId}`)
}

export const saveServiceOrder = (data) => {
  return request.post('/service-order/save', data)
}

export const updateServiceOrder = (data) => {
  return request.put('/service-order/update', data)
}

export const updateServiceOrderStatus = (id, status) => {
  return request.put(`/service-order/update-status/${id}/${status}`)
}

export const assignServiceOrder = (id, userId) => {
  return request.put(`/service-order/assign/${id}/${userId}`)
}

export const deleteServiceOrder = (id) => {
  return request.delete(`/service-order/delete/${id}`)
}

export const getServiceRecordList = () => {
  return request.get('/service-record/list')
}

export const getServiceRecordById = (id) => {
  return request.get(`/service-record/get/${id}`)
}

export const getServiceRecordListByServiceId = (serviceId) => {
  return request.get(`/service-record/service/${serviceId}`)
}

export const saveServiceRecord = (data) => {
  return request.post('/service-record/save', data)
}

export const updateServiceRecord = (data) => {
  return request.put('/service-record/update', data)
}

export const batchSaveServiceRecords = (data) => {
  return request.post('/service-record/batch-save', data)
}

export const deleteServiceRecord = (id) => {
  return request.delete(`/service-record/delete/${id}`)
}