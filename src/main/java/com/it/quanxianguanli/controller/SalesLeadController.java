package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SalesLead;
import com.it.quanxianguanli.service.SalesLeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-lead")
public class SalesLeadController {

  @Autowired
  private SalesLeadService salesLeadService;

  @GetMapping("/list")
  public Result<List<SalesLead>> list() {
    return Result.success(salesLeadService.findAll());
  }

  @GetMapping("/{id}")
  public Result<SalesLead> getById(@PathVariable String id) {
    SalesLead salesLead = salesLeadService.findById(id);
    if (salesLead == null) {
      return Result.error("销售机会不存在");
    }
    return Result.success(salesLead);
  }

  @PostMapping("/save")
  public Result<SalesLead> save(@RequestBody SalesLead salesLead) {
    return Result.success(salesLeadService.save(salesLead));
  }

  @PutMapping("/update")
  public Result<SalesLead> update(@RequestBody SalesLead salesLead) {
    if (salesLead.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(salesLeadService.save(salesLead));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    salesLeadService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

