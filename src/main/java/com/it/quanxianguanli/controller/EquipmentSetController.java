package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.EquipmentSet;
import com.it.quanxianguanli.entity.EquipmentSetItem;
import com.it.quanxianguanli.service.EquipmentSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment-set")
public class EquipmentSetController {

  @Autowired
  private EquipmentSetService equipmentSetService;

  @GetMapping("/list")
  public Result<List<EquipmentSet>> list() {
    return Result.success(equipmentSetService.findAll());
  }

  @GetMapping("/{id}")
  public Result<EquipmentSet> getById(@PathVariable String id) {
    EquipmentSet equipmentSet = equipmentSetService.findById(id);
    if (equipmentSet == null) {
      return Result.error("设备成套不存在");
    }
    return Result.success(equipmentSet);
  }

  @GetMapping("/{id}/items")
  public Result<List<EquipmentSetItem>> getItems(@PathVariable String id) {
    return Result.success(equipmentSetService.findItemsBySetId(id));
  }

  @PostMapping("/save")
  public Result<EquipmentSet> save(@RequestBody EquipmentSet equipmentSet) {
    return Result.success(equipmentSetService.save(equipmentSet));
  }

  @PostMapping("/item/save")
  public Result<EquipmentSetItem> saveItem(@RequestBody EquipmentSetItem item) {
    return Result.success(equipmentSetService.saveItem(item));
  }

  @PutMapping("/update")
  public Result<EquipmentSet> update(@RequestBody EquipmentSet equipmentSet) {
    if (equipmentSet.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(equipmentSetService.save(equipmentSet));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    equipmentSetService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

