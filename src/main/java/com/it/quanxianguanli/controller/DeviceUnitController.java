package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.DeviceUnit;
import com.it.quanxianguanli.service.DeviceUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device-unit")
public class DeviceUnitController {

    @Autowired
    private DeviceUnitService deviceUnitService;

    @GetMapping("/list")
    public Result<List<DeviceUnit>> list() {
        return Result.success(deviceUnitService.findAll());
    }

    @GetMapping("/{id}")
    public Result<DeviceUnit> getById(@PathVariable String id) {
        DeviceUnit deviceUnit = deviceUnitService.findById(id);
        if (deviceUnit == null) {
            return Result.error("设备不存在");
        }
        return Result.success(deviceUnit);
    }

    @PostMapping("/save")
    public Result<DeviceUnit> save(@RequestBody DeviceUnit deviceUnit) {
        return Result.success(deviceUnitService.save(deviceUnit));
    }

    @PutMapping("/update")
    public Result<DeviceUnit> update(@RequestBody DeviceUnit deviceUnit) {
        if (deviceUnit.getId() == null) {
            return Result.error("ID不能为空");
        }
        return Result.success(deviceUnitService.save(deviceUnit));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        deviceUnitService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

