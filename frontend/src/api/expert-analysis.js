import request from './request'

export const getExpertAnalysisList = () => {
  return request.get('/expert-analysis/list')
}

export const getExpertAnalysisById = (id) => {
  return request.get(`/expert-analysis/${id}`)
}

export const getExpertAnalysisByDeviceId = (deviceId) => {
  return request.get(`/expert-analysis/device/${deviceId}`)
}

export const saveExpertAnalysis = (data) => {
  return request.post('/expert-analysis/save', data)
}

export const updateExpertAnalysis = (data) => {
  return request.put('/expert-analysis/update', data)
}

export const deleteExpertAnalysis = (id) => {
  return request.delete(`/expert-analysis/${id}`)
}

