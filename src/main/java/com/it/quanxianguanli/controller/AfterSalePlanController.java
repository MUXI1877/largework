package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.AfterSalePlan;
import com.it.quanxianguanli.service.AfterSalePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/after-sale-plan")
public class AfterSalePlanController {

  @Autowired
  private AfterSalePlanService afterSalePlanService;

  @GetMapping("/list")
  public Result<List<AfterSalePlan>> list() {
    return Result.success(afterSalePlanService.findAll());
  }

  @GetMapping("/{id}")
  public Result<AfterSalePlan> getById(@PathVariable String id) {
    AfterSalePlan plan = afterSalePlanService.findById(id);
    if (plan == null) {
      return Result.error("售后计划不存在");
    }
    return Result.success(plan);
  }

  @PostMapping("/save")
  public Result<AfterSalePlan> save(@RequestBody AfterSalePlan plan) {
    return Result.success(afterSalePlanService.save(plan));
  }

  @PutMapping("/update")
  public Result<AfterSalePlan> update(@RequestBody AfterSalePlan plan) {
    if (plan.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(afterSalePlanService.save(plan));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    afterSalePlanService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

