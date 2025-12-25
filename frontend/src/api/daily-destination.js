import request from './request'

// 去向管理
export const getDailyDestinationList = (userId) => {
  const params = userId ? { userId } : {}
  return request.get('/daily-destination/list', { params })
}

export const getDailyDestinationById = (id) => {
  return request.get(`/daily-destination/${id}`)
}

export const saveDailyDestination = (data) => {
  return request.post('/daily-destination/save', data)
}

export const updateDailyDestination = (data) => {
  return request.put('/daily-destination/update', data)
}

export const deleteDailyDestination = (id) => {
  return request.delete(`/daily-destination/${id}`)
}

export const exportDailyDestinations = (userId) => {
  const params = userId ? { userId } : {}
  return request.get('/daily-destination/export', { params, responseType: 'blob' })
}

