import request from './request'

export const getDeviceTelemetryList = () => {
  return request.get('/device-telemetry/list')
}

export const getDeviceTelemetryById = (id) => {
  return request.get(`/device-telemetry/${id}`)
}

export const getDeviceTelemetryByDeviceId = (deviceId) => {
  return request.get(`/device-telemetry/device/${deviceId}`)
}

export const getDeviceTelemetryByTimeRange = (deviceId, start, end) => {
  return request.get(`/device-telemetry/device/${deviceId}/range`, {
    params: { start, end }
  })
}

export const saveDeviceTelemetry = (data) => {
  return request.post('/device-telemetry/save', data)
}

export const deleteDeviceTelemetry = (id) => {
  return request.delete(`/device-telemetry/${id}`)
}

