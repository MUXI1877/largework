import request from './request'

// ---------------- 销售订单主表接口（对齐后端 SalesOrderController） ----------------

// 列表
export const getSalesOrderList = () => {
  return request.get('/sales-order/list')
}

// 根据 ID 查询
export const getSalesOrderById = (id) => {
  return request.get(`/sales-order/${id}`)
}

// 根据订单号查询
export const getSalesOrderByNo = (orderNo) => {
  return request.get(`/sales-order/order-no/${orderNo}`)
}

// 新增
export const saveSalesOrder = (data) => {
  return request.post('/sales-order/save', data)
}

// 修改
export const updateSalesOrder = (data) => {
  return request.put('/sales-order/update', data)
}

// 删除
export const deleteSalesOrder = (id) => {
  return request.delete(`/sales-order/delete/${id}`)
}

// 更新状态
export const updateSalesOrderStatus = (id, status) => {
  return request.put(`/sales-order/update-status/${id}?status=${status}`)
}

// ---------------- 订单明细接口（对齐后端 SalesOrderItemController） ----------------

// 根据订单 ID 获取明细
export const getSalesOrderItems = (orderId) => {
  return request.get(`/sales-order-item/order/${orderId}`)
}

// 新增明细
export const saveSalesOrderItem = (data) => {
  return request.post('/sales-order-item/save', data)
}

// 修改明细
export const updateSalesOrderItem = (data) => {
  return request.put('/sales-order-item/update', data)
}

// 删除明细
export const deleteSalesOrderItem = (id) => {
  return request.delete(`/sales-order-item/delete/${id}`)
}

// ---------------- 销售统计接口（对齐后端 SalesOrderController 统计接口） ----------------

// 总销售额
export const getTotalSalesAmount = (startDate, endDate) => {
  return request.get(
    `/sales-order/statistics/total-amount?startDate=${startDate}&endDate=${endDate}`
  )
}

// 各状态订单数
export const getOrderStatusCount = () => {
  return request.get('/sales-order/statistics/order-count')
}

// 每日销售额报表
export const getDailySalesReport = (startDate, endDate) => {
  return request.get(
    `/sales-order/statistics/daily-report?startDate=${startDate}&endDate=${endDate}`
  )
}