import request from './request'

export const getPermissionsByRole = (roleId) => {
  return request.get(`/permission/role/${roleId}`)
}

export const savePermission = (data) => {
  return request.post('/permission/save', data)
}

