import request from './request'

export const getDeviceUnitList = () => {
  return request.get('/device-unit/list')
}

export const getDeviceUnitById = (id) => {
  return request.get(`/device-unit/${id}`)
}

export const saveDeviceUnit = (data) => {
  return request.post('/device-unit/save', data)
}

export const updateDeviceUnit = (data) => {
  return request.put('/device-unit/update', data)
}

export const deleteDeviceUnit = (id) => {
  return request.delete(`/device-unit/${id}`)
}

