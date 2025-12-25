import request from './request'

export const getDeviceMonitorConfigList = () => {
  return request.get('/device-monitor-config/list')
}

export const getDeviceMonitorConfigById = (id) => {
  return request.get(`/device-monitor-config/${id}`)
}

export const getDeviceMonitorConfigByDeviceId = (deviceId) => {
  return request.get(`/device-monitor-config/device/${deviceId}`)
}

export const saveDeviceMonitorConfig = (data) => {
  return request.post('/device-monitor-config/save', data)
}

export const updateDeviceMonitorConfig = (data) => {
  return request.put('/device-monitor-config/update', data)
}

export const deleteDeviceMonitorConfig = (id) => {
  return request.delete(`/device-monitor-config/${id}`)
}

