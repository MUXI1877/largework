import request from './request'

export const getAfterSalePlanList = () => {
  return request.get('/after-sale-plan/list')
}

export const getAfterSalePlanById = (id) => {
  return request.get(`/after-sale-plan/${id}`)
}

export const saveAfterSalePlan = (data) => {
  return request.post('/after-sale-plan/save', data)
}

export const updateAfterSalePlan = (data) => {
  return request.put('/after-sale-plan/update', data)
}

export const deleteAfterSalePlan = (id) => {
  return request.delete(`/after-sale-plan/${id}`)
}

