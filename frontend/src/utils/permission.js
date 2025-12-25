import { getMyPermissions } from '../api/permission'

// 权限缓存
let permissionCache = null
let permissionMap = null
let permissionCacheTimestamp = null // 缓存时间戳

// 加载权限并构建权限映射
export const loadPermissions = async (force = false) => {
  try {
    // 如果强制加载或缓存不存在，重新加载
    if (force || !permissionCache) {
      const res = await getMyPermissions()
      const permissions = res.data || []
      permissionCache = permissions
      permissionMap = new Map()
      permissionCacheTimestamp = Date.now() // 记录加载时间
      
      permissions.forEach(p => {
        permissionMap.set(p.moduleId, p)
      })
    }
    
    return permissionCache
  } catch (error) {
    console.error('加载权限失败:', error)
    permissionCache = []
    permissionMap = new Map()
    permissionCacheTimestamp = null
    return []
  }
}

// 清除权限缓存
export const clearPermissionCache = () => {
  permissionCache = null
  permissionMap = null
  permissionCacheTimestamp = null
  // 触发自定义事件，通知所有权限指令重新检查
  if (typeof window !== 'undefined') {
    window.dispatchEvent(new CustomEvent('permission-cache-cleared'))
  }
}

// 获取权限缓存
export const getPermissionCache = () => {
  return permissionCache
}

// 检查是否有模块的查看权限
export const hasModulePermission = (moduleId) => {
  if (!permissionMap) return false
  const permission = permissionMap.get(moduleId)
  return permission && permission.canSee === true
}

// 检查是否有模块的读取权限
export const canRead = (moduleId) => {
  if (!permissionMap) return false
  const permission = permissionMap.get(moduleId)
  return permission && permission.canRead === true
}

// 检查是否有模块的新增权限
export const canAdd = (moduleId) => {
  if (!permissionMap) return false
  const permission = permissionMap.get(moduleId)
  return permission && permission.canAdd === true
}

// 检查是否有模块的更新权限
export const canUpdate = (moduleId) => {
  if (!permissionMap) return false
  const permission = permissionMap.get(moduleId)
  return permission && permission.canUpdate === true
}

// 检查是否有模块的删除权限（通常更新权限包含删除权限）
export const canDelete = (moduleId) => {
  return canUpdate(moduleId)
}

// 获取模块权限对象
export const getModulePermission = (moduleId) => {
  if (!permissionMap) return null
  return permissionMap.get(moduleId) || null
}

