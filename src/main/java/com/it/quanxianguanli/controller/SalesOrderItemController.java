package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SalesOrder;
import com.it.quanxianguanli.entity.SalesOrderItem;
import com.it.quanxianguanli.service.SalesOrderItemService;
import com.it.quanxianguanli.service.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-order-item")
public class SalesOrderItemController {

    @Autowired
    private SalesOrderItemService salesOrderItemService;

    @Autowired
    private SalesOrderService salesOrderService;

    @GetMapping("/list")
    public Result<List<SalesOrderItem>> list() {
        return Result.success(salesOrderItemService.findAll());
    }

    @GetMapping("/order/{orderId}")
    public Result<List<SalesOrderItem>> listByOrderId(@PathVariable String orderId) {
        return salesOrderService.findById(orderId)
                .map(order -> Result.success(salesOrderItemService.findByOrder(order)))
                .orElse(Result.error("销售订单不存在"));
    }

    @GetMapping("/product/{productId}")
    public Result<List<SalesOrderItem>> listByProductId(@PathVariable String productId) {
        return Result.success(salesOrderItemService.findByProductId(productId));
    }

    @GetMapping("/{id}")
    public Result<SalesOrderItem> getById(@PathVariable String id) {
        return salesOrderItemService.findById(id)
                .map(Result::success)
                .orElse(Result.error("销售订单商品项不存在"));
    }

    @PostMapping("/save")
    public Result<SalesOrderItem> save(@RequestBody SalesOrderItem salesOrderItem) {
        try {
            return Result.success(salesOrderItemService.save(salesOrderItem));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/save-all")
    public Result<List<SalesOrderItem>> saveAll(@RequestBody List<SalesOrderItem> items) {
        try {
            return Result.success(salesOrderItemService.saveAll(items));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<SalesOrderItem> update(@RequestBody SalesOrderItem salesOrderItem) {
        try {
            return salesOrderItemService.findById(salesOrderItem.getId())
                    .map(item -> Result.success(salesOrderItemService.save(salesOrderItem)))
                    .orElse(Result.error("销售订单商品项不存在"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            salesOrderItemService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete-by-order/{orderId}")
    public Result<Void> deleteByOrderId(@PathVariable String orderId) {
        try {
            salesOrderService.findById(orderId)
                    .ifPresent(order -> salesOrderItemService.deleteByOrder(order));
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}