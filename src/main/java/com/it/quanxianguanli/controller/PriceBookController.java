package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.PriceBook;
import com.it.quanxianguanli.service.PriceBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/product/{productCode}")
    public Result<List<PriceBook>> listByProductCode(@PathVariable String productCode) {
        return Result.success(priceBookService.findByProductCode(productCode));
    }

    @GetMapping("/current/{productCode}")
    public Result<PriceBook> getCurrentPrice(@PathVariable String productCode) {
        PriceBook currentPrice = priceBookService.findCurrentPriceByProductCode(productCode);
        if (currentPrice != null) {
            return Result.success(currentPrice);
        } else {
            return Result.error("当前产品没有生效的价格记录");
        }
    }

    @GetMapping("/history/{productCode}")
    public Result<List<PriceBook>> getPriceHistory(@PathVariable String productCode) {
        return Result.success(priceBookService.findPriceHistoryByProductCode(productCode));
    }

    @GetMapping("/status/{status}")
    public Result<List<PriceBook>> listByStatus(@PathVariable String status) {
        return Result.success(priceBookService.findByStatus(status));
    }

    @GetMapping("/effective-range")
    public Result<List<PriceBook>> listByEffectiveDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return Result.success(priceBookService.findByEffectiveDateRange(startDate, endDate));
    }

    @GetMapping("/expire-range")
    public Result<List<PriceBook>> listByExpireDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return Result.success(priceBookService.findByExpireDateRange(startDate, endDate));
    }

    @GetMapping("/{id}")
    public Result<PriceBook> getById(@PathVariable String id) {
        return priceBookService.findById(id)
                .map(Result::success)
                .orElse(Result.error("价格记录不存在"));
    }

    @PostMapping("/save")
    public Result<PriceBook> save(@RequestBody PriceBook priceBook) {
        try {
            return Result.success(priceBookService.save(priceBook));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<PriceBook> update(@RequestBody PriceBook priceBook) {
        try {
            return Result.success(priceBookService.update(priceBook));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            priceBookService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update-status/{id}")
    public Result<PriceBook> updateStatus(@PathVariable String id, @RequestParam String status) {
        try {
            return Result.success(priceBookService.updateStatus(id, status));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/create-new-version")
    public Result<PriceBook> createNewPriceVersion(@RequestBody PriceBook newPrice) {
        try {
            return Result.success(priceBookService.createNewPriceVersion(newPrice));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
