package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.ServiceOrder;
import com.it.quanxianguanli.entity.ServiceRecord;
import com.it.quanxianguanli.service.ServiceOrderService;
import com.it.quanxianguanli.service.ServiceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-record")
public class ServiceRecordController {
    @Autowired
    private ServiceRecordService serviceRecordService;
    @Autowired
    private ServiceOrderService serviceOrderService;

    @GetMapping("/list")
    public Result<List<ServiceRecord>> list() {
        return Result.success(serviceRecordService.findAll());
    }

    @GetMapping("/get/{id}")
    public Result<ServiceRecord> getById(@PathVariable String id) {
        return serviceRecordService.findById(id)
                .map(Result::success)
                .orElse(Result.error("服务记录不存在"));
    }

    @GetMapping("/service/{serviceId}")
    public Result<List<ServiceRecord>> listByServiceId(@PathVariable String serviceId) {
        return serviceOrderService.findById(serviceId)
                .map(service -> Result.success(serviceRecordService.findByService(service)))
                .orElse(Result.error("售后服务订单不存在"));
    }

    @PostMapping("/save")
    public Result<ServiceRecord> save(@RequestBody ServiceRecord serviceRecord) {
        return Result.success(serviceRecordService.save(serviceRecord));
    }

    @PutMapping("/update")
    public Result<ServiceRecord> update(@RequestBody ServiceRecord serviceRecord) {
        try {
            return Result.success(serviceRecordService.update(serviceRecord));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/batch-save")
    public Result<List<ServiceRecord>> batchSave(@RequestBody List<ServiceRecord> records) {
        return Result.success(serviceRecordService.saveAll(records));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable String id) {
        serviceRecordService.deleteById(id);
        return Result.success(null);
    }
}