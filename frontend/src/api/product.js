import request from './request'

// 产品管理
export const getProductList = (productType) => {
  const params = productType ? { productType } : {}
  return request.get('/product/list', { params })
}

export const getProductById = (id) => {
  return request.get(`/product/${id}`)
}

export const saveProduct = (data) => {
  return request.post('/product/save', data)
}

export const deleteProduct = (id) => {
  return request.delete(`/product/${id}`)
}

export const importProducts = (file, productType) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('productType', productType)
  return request.post('/product/import', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export const exportProducts = (productType) => {
  const params = productType ? { productType } : {}
  return request.get('/product/export', { params, responseType: 'blob' })
}

// 产品附件管理
export const getProductAttachments = (productId) => {
  return request.get(`/product/${productId}/attachments`)
}

export const saveProductAttachment = (data) => {
  return request.post('/product/attachment/save', data)
}

export const deleteProductAttachment = (id) => {
  return request.delete(`/product/attachment/${id}`)
}

export const uploadProductAttachment = (file, productId) => {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('productId', productId)
  return request.post('/product/attachment/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

