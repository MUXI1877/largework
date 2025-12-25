package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.AfterSaleStore;
import com.it.quanxianguanli.service.AfterSaleStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/after-sale-store")
public class AfterSaleStoreController {

  @Autowired
  private AfterSaleStoreService afterSaleStoreService;

  @GetMapping("/list")
  public Result<List<AfterSaleStore>> list() {
    return Result.success(afterSaleStoreService.findAll());
  }

  @GetMapping("/{id}")
  public Result<AfterSaleStore> getById(@PathVariable String id) {
    AfterSaleStore store = afterSaleStoreService.findById(id);
    if (store == null) {
      return Result.error("4S店不存在");
    }
    return Result.success(store);
  }

  @PostMapping("/save")
  public Result<AfterSaleStore> save(@RequestBody AfterSaleStore store) {
    return Result.success(afterSaleStoreService.save(store));
  }

  @PutMapping("/update")
  public Result<AfterSaleStore> update(@RequestBody AfterSaleStore store) {
    if (store.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(afterSaleStoreService.save(store));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    afterSaleStoreService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

