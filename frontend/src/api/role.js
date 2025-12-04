import request from './request'

export const getRoleList = () => {
  return request.get('/role/list')
}

export const getRoleById = (id) => {
  return request.get(`/role/${id}`)
}

export const saveRole = (data) => {
  return request.post('/role/save', data)
}

export const deleteRole = (id) => {
  return request.delete(`/role/${id}`)
}

