import request from './request'

// 销售机会管理
export const getProjectOpportunityList = (params) => {
  return request.get('/project-opportunity/list', { params })
}

export const getProjectOpportunityById = (id) => {
  return request.get(`/project-opportunity/${id}`)
}

export const saveProjectOpportunity = (data) => {
  return request.post('/project-opportunity/save', data)
}

export const deleteProjectOpportunity = (id) => {
  return request.delete(`/project-opportunity/${id}`)
}

export const submitProjectOpportunity = (id) => {
  return request.post(`/project-opportunity/${id}/submit`)
}

export const uploadToSuperAdmin = (id) => {
  return request.post(`/project-opportunity/${id}/upload`)
}

export const transferRegions = (id, data) => {
  return request.post(`/project-opportunity/${id}/transfer-regions`, data)
}

export const assignEmployees = (id, data) => {
  return request.post(`/project-opportunity/${id}/assign-employees`, data)
}

export const closeOpportunity = (id, data) => {
  return request.post(`/project-opportunity/${id}/close`, data)
}

export const getTrackings = (id) => {
  return request.get(`/project-opportunity/${id}/trackings`)
}

export const saveTracking = (data) => {
  return request.post('/project-opportunity/tracking/save', data)
}

export const deleteTracking = (id) => {
  return request.delete(`/project-opportunity/tracking/${id}`)
}

export const getRegions = (id) => {
  return request.get(`/project-opportunity/${id}/regions`)
}

export const getEmployees = (id) => {
  return request.get(`/project-opportunity/${id}/employees`)
}

export const getKeyPersons = (id) => {
  return request.get(`/project-opportunity/${id}/key-persons`)
}

export const saveKeyPersons = (id, data) => {
  return request.post(`/project-opportunity/${id}/key-persons`, data)
}

export const exportProjectOpportunities = (params) => {
  return request.get('/project-opportunity/export', { params, responseType: 'blob' })
}

