package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.EquipmentSet;
import com.it.quanxianguanli.entity.EquipmentSetItem;
import com.it.quanxianguanli.repository.EquipmentSetRepository;
import com.it.quanxianguanli.repository.EquipmentSetItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipmentSetService {

  @Autowired
  private EquipmentSetRepository equipmentSetRepository;

  @Autowired
  private EquipmentSetItemRepository equipmentSetItemRepository;

  public List<EquipmentSet> findAll() {
    return equipmentSetRepository.findAll();
  }

  public EquipmentSet findById(String id) {
    return equipmentSetRepository.findById(id).orElse(null);
  }

  public List<EquipmentSetItem> findItemsBySetId(String setId) {
    return equipmentSetItemRepository.findBySetId(setId);
  }

  @Transactional
  public EquipmentSet save(EquipmentSet equipmentSet) {
    return equipmentSetRepository.save(equipmentSet);
  }

  @Transactional
  public EquipmentSetItem saveItem(EquipmentSetItem item) {
    return equipmentSetItemRepository.save(item);
  }

  @Transactional
  public void deleteById(String id) {
    equipmentSetItemRepository.findBySetId(id).forEach(item -> equipmentSetItemRepository.deleteById(item.getId()));
    equipmentSetRepository.deleteById(id);
  }
}

