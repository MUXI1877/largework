import request from './request'

export const getBiddingList = () => {
  return request.get('/bidding/list')
}

export const getBiddingById = (id) => {
  return request.get(`/bidding/${id}`)
}

export const getBiddingItems = (id) => {
  return request.get(`/bidding/${id}/items`)
}

export const saveBidding = (data) => {
  return request.post('/bidding/save', data)
}

export const saveBiddingItem = (data) => {
  return request.post('/bidding/item/save', data)
}

export const updateBidding = (data) => {
  return request.put('/bidding/update', data)
}

export const deleteBidding = (id) => {
  return request.delete(`/bidding/${id}`)
}

