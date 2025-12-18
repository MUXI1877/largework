import request from './request'

export const getBasicInfoList = () => {
  return request.get('/basic-info/list')
}

export const getBasicInfoListByCategory = (category) => {
  return request.get(`/basic-info/category/${category}`)
}

export const getEnabledBasicInfoListByCategory = (category) => {
  return request.get(`/basic-info/category/${category}/enabled`)
}

export const getBasicInfoListByParentId = (parentId) => {
  return request.get(`/basic-info/parent/${parentId}`)
}

export const getBasicInfoByCode = (code) => {
  return request.get(`/basic-info/code/${code}`)
}

export const getBasicInfoById = (id) => {
  return request.get(`/basic-info/${id}`)
}

export const saveBasicInfo = (data) => {
  return request.post('/basic-info/save', data)
}

export const updateBasicInfo = (data) => {
  return request.put('/basic-info/update', data)
}

export const deleteBasicInfo = (id) => {
  return request.delete(`/basic-info/delete/${id}`)
}

export const toggleBasicInfoEnabled = (id, enabled) => {
  return request.put(`/basic-info/toggle-enabled/${id}?enabled=${enabled}`)
}

export const getCategoryTree = (category) => {
  return request.get(`/basic-info/tree/${category}`)
}

export const getEnabledCategoryTree = (category) => {
  return request.get(`/basic-info/tree/${category}/enabled`)
}