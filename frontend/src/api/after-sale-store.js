import request from './request'

export const getAfterSaleStoreList = () => {
  return request.get('/after-sale-store/list')
}

export const getAfterSaleStoreById = (id) => {
  return request.get(`/after-sale-store/${id}`)
}

export const saveAfterSaleStore = (data) => {
  return request.post('/after-sale-store/save', data)
}

export const updateAfterSaleStore = (data) => {
  return request.put('/after-sale-store/update', data)
}

export const deleteAfterSaleStore = (id) => {
  return request.delete(`/after-sale-store/${id}`)
}

