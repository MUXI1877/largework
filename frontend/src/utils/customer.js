/**
 * 客户数据工具函数
 */

/**
 * 过滤掉无效的客户数据（没有有效ID的记录）
 * 这些记录通常是未保存到数据库的前端临时数据
 * 
 * @param {Array} customers - 客户列表
 * @returns {Array} 过滤后的客户列表
 */
export function filterValidCustomers(customers) {
  if (!Array.isArray(customers)) {
    return []
  }
  
  return customers.filter(c => {
    // 确保记录有有效的ID（不是null、undefined或空字符串）
    if (!c.id) return false
    if (typeof c.id === 'string' && c.id.trim() === '') return false
    return true
  })
}

/**
 * 检查客户是否有有效的ID
 * 
 * @param {Object} customer - 客户对象
 * @returns {boolean} 是否有有效ID
 */
export function hasValidCustomerId(customer) {
  if (!customer || !customer.id) return false
  if (typeof customer.id === 'string' && customer.id.trim() === '') return false
  return true
}

