export const getToken = () => {
  return localStorage.getItem('token')
}

export const setToken = (token) => {
  localStorage.setItem('token', token)
}

export const removeToken = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('roleId')
  localStorage.removeItem('userId')
}

export const getRoleId = () => {
  return localStorage.getItem('roleId')
}

export const getUserId = () => {
  return localStorage.getItem('userId')
}

export const isCommonUser = () => {
  return getRoleId() === 'r003'
}

export const isSystemAdmin = () => {
  return getRoleId() === 'r002'
}

export const isSuperAdmin = () => {
  return getRoleId() === 'r001'
}

