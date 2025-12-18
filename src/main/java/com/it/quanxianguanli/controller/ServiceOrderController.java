package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.ServiceOrder;
import com.it.quanxianguanli.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/service-order")
public class ServiceOrderController {
    @Autowired
    private ServiceOrderService serviceOrderService;

    @GetMapping("/list")
    public Result<List<ServiceOrder>> list() {
        return Result.success(serviceOrderService.findAll());
    }

    @GetMapping("/get/{id}")
    public Result<ServiceOrder> getById(@PathVariable String id) {
        return serviceOrderService.findById(id)
                .map(Result::success)
                .orElse(Result.error("售后服务订单不存在"));
    }

    @GetMapping("/order-no/{orderNo}")
    public Result<ServiceOrder> getByOrderNo(@PathVariable String orderNo) {
        return serviceOrderService.findByOrderNo(orderNo)
                .map(Result::success)
                .orElse(Result.error("售后服务订单不存在"));
    }

    @GetMapping("/status/{status}")
    public Result<List<ServiceOrder>> getByStatus(@PathVariable String status) {
        return Result.success(serviceOrderService.findByStatus(status));
    }

    @GetMapping("/customer/{customerId}")
    public Result<List<ServiceOrder>> getByCustomerId(@PathVariable String customerId) {
        return Result.success(serviceOrderService.findByCustomerId(customerId));
    }

    @PostMapping("/save")
    public Result<ServiceOrder> save(@RequestBody ServiceOrder serviceOrder) {
        return Result.success(serviceOrderService.save(serviceOrder));
    }

    @PutMapping("/update")
    public Result<ServiceOrder> update(@RequestBody ServiceOrder serviceOrder) {
        try {
            return Result.success(serviceOrderService.update(serviceOrder));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update-status/{id}/{status}")
    public Result<ServiceOrder> updateStatus(@PathVariable String id, @PathVariable String status) {
        try {
            return Result.success(serviceOrderService.updateStatus(id, status));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/assign/{id}/{userId}")
    public Result<ServiceOrder> assignTo(@PathVariable String id, @PathVariable String userId) {
        try {
            return Result.success(serviceOrderService.assignTo(id, userId));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable String id) {
        serviceOrderService.deleteById(id);
        return Result.success(null);
    }
}