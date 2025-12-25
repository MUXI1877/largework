import request from './request'

export const getSalesInventoryReductionList = () => {
  return request.get('/sales-inventory-reduction/list')
}

export const getSalesInventoryReductionById = (id) => {
  return request.get(`/sales-inventory-reduction/${id}`)
}

export const getSalesInventoryReductionItems = (id) => {
  return request.get(`/sales-inventory-reduction/${id}/items`)
}

export const saveSalesInventoryReduction = (data) => {
  return request.post('/sales-inventory-reduction/save', data)
}

export const saveSalesInventoryReductionItem = (data) => {
  return request.post('/sales-inventory-reduction/item/save', data)
}

export const updateSalesInventoryReduction = (data) => {
  return request.put('/sales-inventory-reduction/update', data)
}

export const deleteSalesInventoryReduction = (id) => {
  return request.delete(`/sales-inventory-reduction/${id}`)
}

