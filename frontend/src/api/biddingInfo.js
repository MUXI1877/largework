import request from './request'

// 投标管理
export const getBiddingInfoList = (params) => {
  return request.get('/bidding-info/list', { params })
}

export const getBiddingInfoById = (id) => {
  return request.get(`/bidding-info/${id}`)
}

export const saveBiddingInfo = (data) => {
  return request.post('/bidding-info/save', data)
}

export const deleteBiddingInfo = (id) => {
  return request.delete(`/bidding-info/${id}`)
}

export const submitSummary = (id, data) => {
  return request.post(`/bidding-info/${id}/summary`, data)
}

export const exportBiddingInfos = (params) => {
  return request.get('/bidding-info/export', { params, responseType: 'blob' })
}

