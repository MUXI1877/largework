package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.DeviceMonitorConfig;
import com.it.quanxianguanli.service.DeviceMonitorConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device-monitor-config")
public class DeviceMonitorConfigController {

  @Autowired
  private DeviceMonitorConfigService deviceMonitorConfigService;

  @GetMapping("/list")
  public Result<List<DeviceMonitorConfig>> list() {
    return Result.success(deviceMonitorConfigService.findAll());
  }

  @GetMapping("/{id}")
  public Result<DeviceMonitorConfig> getById(@PathVariable String id) {
    DeviceMonitorConfig config = deviceMonitorConfigService.findById(id);
    if (config == null) {
      return Result.error("配置不存在");
    }
    return Result.success(config);
  }

  @GetMapping("/device/{deviceId}")
  public Result<List<DeviceMonitorConfig>> getByDeviceId(@PathVariable String deviceId) {
    return Result.success(deviceMonitorConfigService.findByDeviceId(deviceId));
  }

  @PostMapping("/save")
  public Result<DeviceMonitorConfig> save(@RequestBody DeviceMonitorConfig config) {
    return Result.success(deviceMonitorConfigService.save(config));
  }

  @PutMapping("/update")
  public Result<DeviceMonitorConfig> update(@RequestBody DeviceMonitorConfig config) {
    if (config.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(deviceMonitorConfigService.save(config));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    deviceMonitorConfigService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

