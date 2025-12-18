import request from './request'

export const getPermissionsByRole = (roleId) => {
  return request.get(`/permission/role/${roleId}`)
}

export const getMyPermissions = () => {
  return request.get('/permission/my')
}

export const savePermission = (data) => {
  return request.post('/permission/save', data)
}

export const bulkSavePermission = (dataList) => {
  return request.post('/permission/bulk-save', dataList)
}

export const configurePermissionsForChildren = (data) => {
  return request.post('/permission/configure-children', data)
}

