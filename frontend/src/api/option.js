import request from './request'

export const getOptionList = (groupName) => {
  return request.get('/option/list', { params: { groupName } })
}

export const getOptionById = (id) => {
  return request.get(`/option/${id}`)
}

export const saveOption = (data) => {
  return request.post('/option/save', data)
}

export const deleteOption = (id) => {
  return request.delete(`/option/${id}`)
}

