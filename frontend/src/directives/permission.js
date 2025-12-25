import { getPermissionCache, loadPermissions } from '../utils/permission'

// 权限指令
export const permissionDirective = {
  async mounted(el, binding) {
    await checkAndUpdate(el, binding)
    // 监听权限缓存清除事件，重新检查权限
    if (typeof window !== 'undefined') {
      const handler = async () => {
        // 当权限缓存被清除时，重新加载权限并检查
        await loadPermissions(true) // 强制重新加载
        await checkAndUpdate(el, binding)
      }
      window.addEventListener('permission-cache-cleared', handler)
      // 保存事件处理器，以便在组件卸载时移除
      el._permissionHandler = handler
    }
  },
  
  async updated(el, binding) {
    await checkAndUpdate(el, binding)
  },
  
  unmounted(el) {
    // 移除事件监听器
    if (typeof window !== 'undefined' && el._permissionHandler) {
      window.removeEventListener('permission-cache-cleared', el._permissionHandler)
      delete el._permissionHandler
    }
  }
}

// 检查并更新元素显示状态
async function checkAndUpdate(el, binding) {
  const { value } = binding
  
  // 如果没有权限配置，先加载权限
  if (!getPermissionCache()) {
    await loadPermissions()
  }
  
  // 检查权限
  const hasPermission = checkPermission(value)
  
  if (!hasPermission) {
    // 如果没有权限，隐藏元素
    el.style.display = 'none'
    // 保存原始display样式，以便恢复
    if (!el.dataset.originalDisplay) {
      el.dataset.originalDisplay = el.style.display || ''
    }
  } else {
    // 恢复原始display样式
    if (el.dataset.originalDisplay !== undefined) {
      el.style.display = el.dataset.originalDisplay || ''
      delete el.dataset.originalDisplay
    } else {
      // 如果没有保存过原始样式，设置为空（使用默认样式）
      el.style.display = ''
    }
  }
}

// 检查权限
function checkPermission(value) {
  if (!value) return true
  
  const { moduleId, action } = value
  
  if (!moduleId) return true
  
  const permissionCache = getPermissionCache()
  if (!permissionCache || permissionCache.length === 0) return false
  
  const permission = permissionCache.find(p => p.moduleId === moduleId)
  if (!permission) return false
  
  // 如果没有指定action，默认检查canSee
  if (!action) {
    return permission.canSee === true
  }
  
  // 根据action检查对应权限
  switch (action) {
    case 'read':
      return permission.canRead === true
    case 'add':
      return permission.canAdd === true
    case 'update':
      return permission.canUpdate === true
    case 'delete':
      return permission.canUpdate === true // 删除权限通常等同于更新权限
    case 'see':
      return permission.canSee === true
    default:
      return permission.canSee === true
  }
}

