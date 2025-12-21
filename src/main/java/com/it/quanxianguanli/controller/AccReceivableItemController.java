package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.AccReceivableItem;
import com.it.quanxianguanli.service.AccReceivableItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acc-receivable-item")
public class AccReceivableItemController {

  @Autowired
  private AccReceivableItemService accReceivableItemService;

  @GetMapping("/list")
  public Result<List<AccReceivableItem>> list() {
    return Result.success(accReceivableItemService.findAll());
  }

  @GetMapping("/contract/{contractId}")
  public Result<List<AccReceivableItem>> getByContractId(@PathVariable String contractId) {
    return Result.success(accReceivableItemService.findByContractId(contractId));
  }

  @GetMapping("/{id}")
  public Result<AccReceivableItem> getById(@PathVariable String id) {
    AccReceivableItem item = accReceivableItemService.findById(id);
    if (item == null) {
      return Result.error("应收账记录不存在");
    }
    return Result.success(item);
  }

  @PostMapping("/save")
  public Result<AccReceivableItem> save(@RequestBody AccReceivableItem accReceivableItem) {
    return Result.success(accReceivableItemService.save(accReceivableItem));
  }

  @PutMapping("/update")
  public Result<AccReceivableItem> update(@RequestBody AccReceivableItem accReceivableItem) {
    if (accReceivableItem.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(accReceivableItemService.save(accReceivableItem));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    accReceivableItemService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

