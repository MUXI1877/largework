package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.PriceBook;
import com.it.quanxianguanli.service.PriceBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price-book")
public class PriceBookController {

  @Autowired
  private PriceBookService priceBookService;

  @GetMapping("/list")
  public Result<List<PriceBook>> list() {
    return Result.success(priceBookService.findAll());
  }

  @GetMapping("/{id}")
  public Result<PriceBook> getById(@PathVariable String id) {
    PriceBook priceBook = priceBookService.findById(id);
    if (priceBook == null) {
      return Result.error("价格本记录不存在");
    }
    return Result.success(priceBook);
  }

  @PostMapping("/save")
  public Result<PriceBook> save(@RequestBody PriceBook priceBook) {
    return Result.success(priceBookService.save(priceBook));
  }

  @PutMapping("/update")
  public Result<PriceBook> update(@RequestBody PriceBook priceBook) {
    if (priceBook.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(priceBookService.save(priceBook));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    priceBookService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

