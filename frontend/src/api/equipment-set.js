import request from './request'

export const getEquipmentSetList = () => {
  return request.get('/equipment-set/list')
}

export const getEquipmentSetById = (id) => {
  return request.get(`/equipment-set/${id}`)
}

export const getEquipmentSetItems = (id) => {
  return request.get(`/equipment-set/${id}/items`)
}

export const saveEquipmentSet = (data) => {
  return request.post('/equipment-set/save', data)
}

export const saveEquipmentSetItem = (data) => {
  return request.post('/equipment-set/item/save', data)
}

export const updateEquipmentSet = (data) => {
  return request.put('/equipment-set/update', data)
}

export const deleteEquipmentSet = (id) => {
  return request.delete(`/equipment-set/${id}`)
}

