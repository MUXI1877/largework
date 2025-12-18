package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SalesOrder;
import com.it.quanxianguanli.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales-order")
public class SalesOrderController {

    @Autowired
    private SalesOrderService salesOrderService;

    @GetMapping("/list")
    public Result<List<SalesOrder>> list() {
        return Result.success(salesOrderService.findAll());
    }

    @GetMapping("/order-no/{orderNo}")
    public Result<SalesOrder> getByOrderNo(@PathVariable String orderNo) {
        return salesOrderService.findByOrderNo(orderNo)
                .map(Result::success)
                .orElse(Result.error("销售订单不存在"));
    }

    @GetMapping("/status/{status}")
    public Result<List<SalesOrder>> listByStatus(@PathVariable String status) {
        return Result.success(salesOrderService.findByStatus(status));
    }

    @GetMapping("/customer/{customerId}")
    public Result<List<SalesOrder>> listByCustomerId(@PathVariable String customerId) {
        return Result.success(salesOrderService.findByCustomerId(customerId));
    }

    @GetMapping("/date-range")
    public Result<List<SalesOrder>> listByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return Result.success(salesOrderService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/{id}")
    public Result<SalesOrder> getById(@PathVariable String id) {
        return salesOrderService.findById(id)
                .map(Result::success)
                .orElse(Result.error("销售订单不存在"));
    }

    @PostMapping("/save")
    public Result<SalesOrder> save(@RequestBody SalesOrder salesOrder) {
        try {
            return Result.success(salesOrderService.save(salesOrder));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<SalesOrder> update(@RequestBody SalesOrder salesOrder) {
        try {
            return Result.success(salesOrderService.update(salesOrder));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            salesOrderService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update-status/{id}")
    public Result<SalesOrder> updateStatus(@PathVariable String id, @RequestParam String status) {
        try {
            return Result.success(salesOrderService.updateStatus(id, status));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/statistics/total-amount")
    public Result<BigDecimal> getTotalSalesAmount(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            BigDecimal totalAmount = salesOrderService.getTotalSalesAmount(startDate, endDate);
            return Result.success(totalAmount);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/statistics/order-count/status")
    public Result<Long> getOrderCountByStatus(@RequestParam String status) {
        try {
            long count = salesOrderService.getOrderCountByStatus(status);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/statistics/order-count")
    public Result<Map<String, Long>> getOrderCountByStatus() {
        try {
            Map<String, Long> result = salesOrderService.getOrderCountByStatus();
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/statistics/daily-report")
    public Result<Map<LocalDate, BigDecimal>> getDailySalesReport(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            Map<LocalDate, BigDecimal> report = salesOrderService.getDailySalesReport(startDate, endDate);
            return Result.success(report);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/statistics/total-count")
    public Result<Long> getTotalOrderCount() {
        try {
            long count = salesOrderService.getTotalOrderCount();
            return Result.success(count);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}