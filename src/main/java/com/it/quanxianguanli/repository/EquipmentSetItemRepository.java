package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.EquipmentSetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentSetItemRepository extends JpaRepository<EquipmentSetItem, String> {
  List<EquipmentSetItem> findBySetId(String setId);
}

