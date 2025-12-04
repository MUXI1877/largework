import request from './request'

export const getModuleList = () => {
  return request.get('/module/list')
}

export const getModuleTree = () => {
  return request.get('/module/tree')
}

export const getModuleChildren = (parentId) => {
  return request.get(`/module/children/${parentId || 'null'}`)
}

export const getModuleById = (id) => {
  return request.get(`/module/${id}`)
}

export const saveModule = (data) => {
  return request.post('/module/save', data)
}

export const deleteModule = (id) => {
  return request.delete(`/module/${id}`)
}

