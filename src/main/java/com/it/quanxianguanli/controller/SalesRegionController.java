package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SalesRegion;
import com.it.quanxianguanli.service.SalesRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-region")
public class SalesRegionController {

  @Autowired
  private SalesRegionService salesRegionService;

  @GetMapping("/list")
  public Result<List<SalesRegion>> list() {
    return Result.success(salesRegionService.findAll());
  }

  @GetMapping("/{id}")
  public Result<SalesRegion> getById(@PathVariable String id) {
    SalesRegion salesRegion = salesRegionService.findById(id);
    if (salesRegion == null) {
      return Result.error("销售片区不存在");
    }
    return Result.success(salesRegion);
  }

  @PostMapping("/save")
  public Result<SalesRegion> save(@RequestBody SalesRegion salesRegion) {
    return Result.success(salesRegionService.save(salesRegion));
  }

  @PutMapping("/update")
  public Result<SalesRegion> update(@RequestBody SalesRegion salesRegion) {
    if (salesRegion.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(salesRegionService.save(salesRegion));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    salesRegionService.deleteById(id);
    return Result.success("删除成功", null);
  }
}
