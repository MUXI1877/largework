package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.DeviceTelemetry;
import com.it.quanxianguanli.service.DeviceTelemetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/device-telemetry")
public class DeviceTelemetryController {

  @Autowired
  private DeviceTelemetryService deviceTelemetryService;

  @GetMapping("/list")
  public Result<List<DeviceTelemetry>> list() {
    return Result.success(deviceTelemetryService.findAll());
  }

  @GetMapping("/{id}")
  public Result<DeviceTelemetry> getById(@PathVariable String id) {
    DeviceTelemetry telemetry = deviceTelemetryService.findById(id);
    if (telemetry == null) {
      return Result.error("采集数据不存在");
    }
    return Result.success(telemetry);
  }

  @GetMapping("/device/{deviceId}")
  public Result<List<DeviceTelemetry>> getByDeviceId(@PathVariable String deviceId) {
    return Result.success(deviceTelemetryService.findByDeviceId(deviceId));
  }

  @GetMapping("/device/{deviceId}/range")
  public Result<List<DeviceTelemetry>> getByDeviceIdAndTimeRange(
      @PathVariable String deviceId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
    return Result.success(deviceTelemetryService.findByDeviceIdAndTimeRange(deviceId, start, end));
  }

  @PostMapping("/save")
  public Result<DeviceTelemetry> save(@RequestBody DeviceTelemetry telemetry) {
    return Result.success(deviceTelemetryService.save(telemetry));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    deviceTelemetryService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

