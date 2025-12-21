package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.AfterSale;
import com.it.quanxianguanli.service.AfterSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/after-sale")
public class AfterSaleController {

  @Autowired
  private AfterSaleService afterSaleService;

  @GetMapping("/list")
  public Result<List<AfterSale>> list() {
    return Result.success(afterSaleService.findAll());
  }

  @GetMapping("/device/{deviceSn}")
  public Result<List<AfterSale>> getByDeviceSn(@PathVariable String deviceSn) {
    return Result.success(afterSaleService.findByDeviceSn(deviceSn));
  }

  @GetMapping("/status/{status}")
  public Result<List<AfterSale>> getByStatus(@PathVariable String status) {
    return Result.success(afterSaleService.findByStatus(status));
  }

  @GetMapping("/handler/{handlerId}")
  public Result<List<AfterSale>> getByHandlerId(@PathVariable String handlerId) {
    return Result.success(afterSaleService.findByHandlerId(handlerId));
  }

  @GetMapping("/{id}")
  public Result<AfterSale> getById(@PathVariable String id) {
    AfterSale afterSale = afterSaleService.findById(id);
    if (afterSale == null) {
      return Result.error("售后记录不存在");
    }
    return Result.success(afterSale);
  }

  @PostMapping("/save")
  public Result<AfterSale> save(@RequestBody AfterSale afterSale) {
    return Result.success(afterSaleService.save(afterSale));
  }

  @PutMapping("/update")
  public Result<AfterSale> update(@RequestBody AfterSale afterSale) {
    if (afterSale.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(afterSaleService.save(afterSale));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    afterSaleService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

