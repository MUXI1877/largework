package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.Customer;
import com.it.quanxianguanli.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @GetMapping("/list")
  public Result<List<Customer>> list() {
    return Result.success(customerService.findAll());
  }

  @GetMapping("/{id}")
  public Result<Customer> getById(@PathVariable String id) {
    Customer customer = customerService.findById(id);
    if (customer == null) {
      return Result.error("客户不存在");
    }
    return Result.success(customer);
  }

  @PostMapping("/save")
  public Result<Customer> save(@RequestBody Customer customer) {
    return Result.success(customerService.save(customer));
  }

  @PutMapping("/update")
  public Result<Customer> update(@RequestBody Customer customer) {
    if (customer.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(customerService.save(customer));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    customerService.deleteById(id);
    return Result.success("删除成功", null);
  }
}
