import request from './request'

// 周报管理
export const getWeeklyReportList = (userId) => {
  const params = userId ? { userId } : {}
  return request.get('/weekly-report/list', { params })
}

export const getWeeklyReportById = (id) => {
  return request.get(`/weekly-report/${id}`)
}

export const saveWeeklyReport = (data) => {
  return request.post('/weekly-report/save', data)
}

export const updateWeeklyReport = (data) => {
  return request.put('/weekly-report/update', data)
}

export const submitWeeklyReport = (id) => {
  return request.post(`/weekly-report/submit/${id}`)
}

export const deleteWeeklyReport = (id) => {
  return request.delete(`/weekly-report/${id}`)
}

export const exportWeeklyReports = (userId) => {
  const params = userId ? { userId } : {}
  return request.get('/weekly-report/export', { params, responseType: 'blob' })
}

