package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SpareParts;
import com.it.quanxianguanli.service.SparePartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spare-parts")
public class SparePartsController {

  @Autowired
  private SparePartsService sparePartsService;

  @GetMapping("/list")
  public Result<List<SpareParts>> list() {
    return Result.success(sparePartsService.findAll());
  }

  @GetMapping("/{id}")
  public Result<SpareParts> getById(@PathVariable String id) {
    SpareParts spareParts = sparePartsService.findById(id);
    if (spareParts == null) {
      return Result.error("备件不存在");
    }
    return Result.success(spareParts);
  }

  @PostMapping("/save")
  public Result<SpareParts> save(@RequestBody SpareParts spareParts) {
    return Result.success(sparePartsService.save(spareParts));
  }

  @PutMapping("/update")
  public Result<SpareParts> update(@RequestBody SpareParts spareParts) {
    if (spareParts.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(sparePartsService.save(spareParts));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    sparePartsService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

