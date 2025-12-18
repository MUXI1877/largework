package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.Receivable;
import com.it.quanxianguanli.entity.SalesOrder;
import com.it.quanxianguanli.service.ReceivableService;
import com.it.quanxianguanli.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/receivable")
public class ReceivableController {

    @Autowired
    private ReceivableService receivableService;

    @Autowired
    private SalesOrderService salesOrderService;

    @GetMapping("/list")
    public Result<List<Receivable>> list() {
        return Result.success(receivableService.findAll());
    }

    @GetMapping("/order/{orderId}")
    public Result<List<Receivable>> listByOrderId(@PathVariable String orderId) {
        return salesOrderService.findById(orderId)
                .map(order -> Result.success(receivableService.findByOrder(order)))
                .orElse(Result.error("销售订单不存在"));
    }

    @GetMapping("/status/{status}")
    public Result<List<Receivable>> listByStatus(@PathVariable String status) {
        return Result.success(receivableService.findByStatus(status));
    }

    @GetMapping("/overdue")
    public Result<List<Receivable>> listOverdue() {
        return Result.success(receivableService.findOverdue(LocalDate.now()));
    }

    @GetMapping("/date-range")
    public Result<List<Receivable>> listByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return Result.success(receivableService.findByDateRange(startDate, endDate));
    }

    @GetMapping("/{id}")
    public Result<Receivable> getById(@PathVariable String id) {
        return receivableService.findById(id)
                .map(Result::success)
                .orElse(Result.error("应收账不存在"));
    }

    @PostMapping("/save")
    public Result<Receivable> save(@RequestBody Receivable receivable) {
        try {
            return Result.success(receivableService.save(receivable));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<Receivable> update(@RequestBody Receivable receivable) {
        try {
            return Result.success(receivableService.update(receivable));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            receivableService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update-status/{id}")
    public Result<Receivable> updateStatus(@PathVariable String id, @RequestParam String status) {
        try {
            return Result.success(receivableService.updateStatus(id, status));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}