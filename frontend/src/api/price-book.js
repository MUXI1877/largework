import request from './request'

// 获取所有价格记录
export const getAllPriceBooks = () => request.get('/price-book/list')

// 根据产品代码获取当前价格
export const getCurrentPriceByProductCode = (productCode) => request.get(`/price-book/current/${productCode}`)

// 获取价格历史记录
export const getPriceHistoryByProductCode = (productCode) => request.get(`/price-book/history/${productCode}`)

// 创建新的价格版本
export const createNewPriceVersion = (priceBook) => request.post('/price-book/create-new-version', priceBook)

// 更新价格记录
// 后端为 PUT /api/price-book/update，路径中不带 id，实体本身包含 id
export const updatePriceBook = (id, priceBook) => request.put('/price-book/update', priceBook)

// 删除价格记录
// 后端为 DELETE /api/price-book/delete/{id}
export const deletePriceBook = (id) => request.delete(`/price-book/delete/${id}`)

// 根据状态查询价格记录
export const getPriceBooksByStatus = (status) => request.get(`/price-book/status/${status}`)
