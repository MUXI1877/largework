package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.AfterSaleExp;
import com.it.quanxianguanli.service.AfterSaleExpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/after-sale-exp")
public class AfterSaleExpController {

  @Autowired
  private AfterSaleExpService afterSaleExpService;

  @GetMapping("/list")
  public Result<List<AfterSaleExp>> list() {
    return Result.success(afterSaleExpService.findAll());
  }

  @GetMapping("/model/{deviceModel}")
  public Result<List<AfterSaleExp>> getByDeviceModel(@PathVariable String deviceModel) {
    return Result.success(afterSaleExpService.findByDeviceModel(deviceModel));
  }

  @GetMapping("/search")
  public Result<List<AfterSaleExp>> searchByProblem(@RequestParam String problem) {
    return Result.success(afterSaleExpService.findByProblemContaining(problem));
  }

  @GetMapping("/{id}")
  public Result<AfterSaleExp> getById(@PathVariable String id) {
    AfterSaleExp afterSaleExp = afterSaleExpService.findById(id);
    if (afterSaleExp == null) {
      return Result.error("售后经验记录不存在");
    }
    return Result.success(afterSaleExp);
  }

  @PostMapping("/save")
  public Result<AfterSaleExp> save(@RequestBody AfterSaleExp afterSaleExp) {
    return Result.success(afterSaleExpService.save(afterSaleExp));
  }

  @PutMapping("/update")
  public Result<AfterSaleExp> update(@RequestBody AfterSaleExp afterSaleExp) {
    if (afterSaleExp.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(afterSaleExpService.save(afterSaleExp));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    afterSaleExpService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

