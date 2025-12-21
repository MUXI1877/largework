package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.QuotationItem;
import com.it.quanxianguanli.service.QuotationItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotation-item")
public class QuotationItemController {

  @Autowired
  private QuotationItemService quotationItemService;

  @GetMapping("/list")
  public Result<List<QuotationItem>> list() {
    return Result.success(quotationItemService.findAll());
  }

  @GetMapping("/quotation/{quotationId}")
  public Result<List<QuotationItem>> getByQuotationId(@PathVariable String quotationId) {
    return Result.success(quotationItemService.findByQuotationId(quotationId));
  }

  @GetMapping("/{id}")
  public Result<QuotationItem> getById(@PathVariable String id) {
    QuotationItem quotationItem = quotationItemService.findById(id);
    if (quotationItem == null) {
      return Result.error("报价明细不存在");
    }
    return Result.success(quotationItem);
  }

  @PostMapping("/save")
  public Result<QuotationItem> save(@RequestBody QuotationItem quotationItem) {
    return Result.success(quotationItemService.save(quotationItem));
  }

  @PutMapping("/update")
  public Result<QuotationItem> update(@RequestBody QuotationItem quotationItem) {
    if (quotationItem.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(quotationItemService.save(quotationItem));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    quotationItemService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

