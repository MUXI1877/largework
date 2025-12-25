package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.Bidding;
import com.it.quanxianguanli.entity.BiddingItem;
import com.it.quanxianguanli.service.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bidding")
public class BiddingController {

  @Autowired
  private BiddingService biddingService;

  @GetMapping("/list")
  public Result<List<Bidding>> list() {
    return Result.success(biddingService.findAll());
  }

  @GetMapping("/{id}")
  public Result<Bidding> getById(@PathVariable String id) {
    Bidding bidding = biddingService.findById(id);
    if (bidding == null) {
      return Result.error("投标记录不存在");
    }
    return Result.success(bidding);
  }

  @GetMapping("/{id}/items")
  public Result<List<BiddingItem>> getItems(@PathVariable String id) {
    return Result.success(biddingService.findItemsByBiddingId(id));
  }

  @PostMapping("/save")
  public Result<Bidding> save(@RequestBody Bidding bidding) {
    return Result.success(biddingService.save(bidding));
  }

  @PostMapping("/item/save")
  public Result<BiddingItem> saveItem(@RequestBody BiddingItem item) {
    return Result.success(biddingService.saveItem(item));
  }

  @PutMapping("/update")
  public Result<Bidding> update(@RequestBody Bidding bidding) {
    if (bidding.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(biddingService.save(bidding));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    biddingService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

