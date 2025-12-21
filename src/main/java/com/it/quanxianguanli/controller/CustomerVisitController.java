package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.CustomerVisit;
import com.it.quanxianguanli.service.CustomerVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-visit")
public class CustomerVisitController {

  @Autowired
  private CustomerVisitService customerVisitService;

  @GetMapping("/list")
  public Result<List<CustomerVisit>> list() {
    return Result.success(customerVisitService.findAll());
  }

  @GetMapping("/{id}")
  public Result<CustomerVisit> getById(@PathVariable String id) {
    CustomerVisit customerVisit = customerVisitService.findById(id);
    if (customerVisit == null) {
      return Result.error("客户来访记录不存在");
    }
    return Result.success(customerVisit);
  }

  @PostMapping("/save")
  public Result<CustomerVisit> save(@RequestBody CustomerVisit customerVisit) {
    return Result.success(customerVisitService.save(customerVisit));
  }

  @PutMapping("/update")
  public Result<CustomerVisit> update(@RequestBody CustomerVisit customerVisit) {
    if (customerVisit.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(customerVisitService.save(customerVisit));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    customerVisitService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

