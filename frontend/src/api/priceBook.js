import request from './request'

/**
 * 价格本管理API
 */

// 分页查询价格本
export function getPriceBooks(params) {
  return request({
    url: '/price-book/page',
    method: 'get',
    params
  })
}

// 根据ID查询价格本
export function getPriceBookById(id) {
  return request({
    url: `/price-book/${id}`,
    method: 'get'
  })
}

// 根据产品ID和部门查询价格（供销售报价管理模块调用）
export function getPriceByProductAndDepartment(productId, department) {
  return request({
    url: '/price-book/price',
    method: 'get',
    params: { productId, department }
  })
}

// 新增价格本
export function createPriceBook(data) {
  return request({
    url: '/price-book',
    method: 'post',
    data
  })
}

// 更新价格本
export function updatePriceBook(id, data) {
  return request({
    url: `/price-book/${id}`,
    method: 'put',
    data
  })
}

// 删除价格本
export function deletePriceBook(id) {
  return request({
    url: `/price-book/${id}`,
    method: 'delete'
  })
}

// 查询价格修改日志
export function getPriceBookLogs(id) {
  return request({
    url: `/price-book/${id}/logs`,
    method: 'get'
  })
}

// 导出价格本数据
export function exportPriceBooks(params) {
  return request({
    url: '/price-book/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 获取产品列表（用于下拉选择）
export function getProductsForPriceBook(productType) {
  return request({
    url: '/price-book/products',
    method: 'get',
    params: { productType }
  })
}

// 获取部门列表
export function getDepartments() {
  return request({
    url: '/price-book/departments',
    method: 'get'
  })
}

// 获取产品类别列表
export function getProductTypes() {
  return request({
    url: '/price-book/product-types',
    method: 'get'
  })
}

