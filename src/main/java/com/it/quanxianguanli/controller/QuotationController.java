package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.Quotation;
import com.it.quanxianguanli.service.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotation")
public class QuotationController {

  @Autowired
  private QuotationService quotationService;

  @GetMapping("/list")
  public Result<List<Quotation>> list() {
    return Result.success(quotationService.findAll());
  }

  @GetMapping("/{id}")
  public Result<Quotation> getById(@PathVariable String id) {
    Quotation quotation = quotationService.findById(id);
    if (quotation == null) {
      return Result.error("报价单不存在");
    }
    return Result.success(quotation);
  }

  @PostMapping("/save")
  public Result<Quotation> save(@RequestBody Quotation quotation) {
    return Result.success(quotationService.save(quotation));
  }

  @PutMapping("/update")
  public Result<Quotation> update(@RequestBody Quotation quotation) {
    if (quotation.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(quotationService.save(quotation));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    quotationService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

