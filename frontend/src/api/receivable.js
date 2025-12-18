import request from './request'

export const getReceivableList = () => {
  return request.get('/receivable/list')
}

export const getReceivableListByOrderId = (orderId) => {
  return request.get(`/receivable/order/${orderId}`)
}

export const getReceivableListByStatus = (status) => {
  return request.get(`/receivable/status/${status}`)
}

export const getOverdueReceivableList = () => {
  return request.get('/receivable/overdue')
}

export const getReceivableListByDateRange = (startDate, endDate) => {
  return request.get(`/receivable/date-range?startDate=${startDate}&endDate=${endDate}`)
}

export const getReceivableById = (id) => {
  return request.get(`/receivable/${id}`)
}

export const saveReceivable = (data) => {
  return request.post('/receivable/save', data)
}

export const updateReceivable = (data) => {
  return request.put('/receivable/update', data)
}

export const deleteReceivable = (id) => {
  return request.delete(`/receivable/delete/${id}`)
}

export const updateReceivableStatus = (id, status) => {
  return request.put(`/receivable/update-status/${id}?status=${status}`)
}

export const getReceiptRecordList = () => {
  return request.get('/receipt-record/list')
}

export const getReceiptRecordListByReceivableId = (receivableId) => {
  return request.get(`/receipt-record/receivable/${receivableId}`)
}

export const getReceiptRecordListByDateRange = (startDate, endDate) => {
  return request.get(`/receipt-record/date-range?startDate=${startDate}&endDate=${endDate}`)
}

export const getReceiptRecordListByReceiptMethod = (receiptMethod) => {
  return request.get(`/receipt-record/method/${receiptMethod}`)
}

export const getReceiptRecordById = (id) => {
  return request.get(`/receipt-record/${id}`)
}

export const saveReceiptRecord = (data) => {
  return request.post('/receipt-record/save', data)
}

export const saveAllReceiptRecords = (data) => {
  return request.post('/receipt-record/save-all', data)
}

export const updateReceiptRecord = (data) => {
  return request.put('/receipt-record/update', data)
}

export const deleteReceiptRecord = (id) => {
  return request.delete(`/receipt-record/delete/${id}`)
}