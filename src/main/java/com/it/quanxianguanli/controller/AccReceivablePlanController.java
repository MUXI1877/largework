package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.AccReceivablePlan;
import com.it.quanxianguanli.service.AccReceivablePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acc-receivable-plan")
public class AccReceivablePlanController {

  @Autowired
  private AccReceivablePlanService accReceivablePlanService;

  @GetMapping("/list")
  public Result<List<AccReceivablePlan>> list() {
    return Result.success(accReceivablePlanService.findAll());
  }

  @GetMapping("/contract/{contractId}")
  public Result<List<AccReceivablePlan>> getByContractId(@PathVariable String contractId) {
    return Result.success(accReceivablePlanService.findByContractId(contractId));
  }

  @GetMapping("/{id}")
  public Result<AccReceivablePlan> getById(@PathVariable String id) {
    AccReceivablePlan plan = accReceivablePlanService.findById(id);
    if (plan == null) {
      return Result.error("应收账计划不存在");
    }
    return Result.success(plan);
  }

  @PostMapping("/save")
  public Result<AccReceivablePlan> save(@RequestBody AccReceivablePlan accReceivablePlan) {
    return Result.success(accReceivablePlanService.save(accReceivablePlan));
  }

  @PutMapping("/update")
  public Result<AccReceivablePlan> update(@RequestBody AccReceivablePlan accReceivablePlan) {
    if (accReceivablePlan.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(accReceivablePlanService.save(accReceivablePlan));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    accReceivablePlanService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

