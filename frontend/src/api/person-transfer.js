import request from './request'

export const getPersonTransferList = () => {
  return request.get('/person-transfer/list')
}

export const getPersonTransferById = (id) => {
  return request.get(`/person-transfer/${id}`)
}

export const savePersonTransfer = (data) => {
  return request.post('/person-transfer/save', data)
}

export const updatePersonTransfer = (data) => {
  return request.put('/person-transfer/update', data)
}

export const deletePersonTransfer = (id) => {
  return request.delete(`/person-transfer/${id}`)
}

