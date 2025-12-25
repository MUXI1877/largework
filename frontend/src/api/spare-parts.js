import request from './request'

export const getSparePartsList = () => {
  return request.get('/spare-parts/list')
}

export const getSparePartsById = (id) => {
  return request.get(`/spare-parts/${id}`)
}

export const saveSpareParts = (data) => {
  return request.post('/spare-parts/save', data)
}

export const updateSpareParts = (data) => {
  return request.put('/spare-parts/update', data)
}

export const deleteSpareParts = (id) => {
  return request.delete(`/spare-parts/${id}`)
}

