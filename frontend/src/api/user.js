import request from './request'

export const register = (data) => {
  return request.post('/user/register', data)
}

export const verifyIdCard = (idCard) => {
  return request.post('/user/verify', { idCard })
}

export const approveUser = (userId) => {
  return request.post(`/user/approve/${userId}`)
}

export const resetPassword = (userId) => {
  return request.post(`/user/reset-password/${userId}`)
}

export const getUserList = () => {
  return request.get('/user/list')
}

export const assignRole = (userId, roleId) => {
  return request.post('/user/assign-role', { userId, roleId })
}

