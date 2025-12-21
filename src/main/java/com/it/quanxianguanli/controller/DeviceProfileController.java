package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.DeviceProfile;
import com.it.quanxianguanli.service.DeviceProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device-profile")
public class DeviceProfileController {

  @Autowired
  private DeviceProfileService deviceProfileService;

  @GetMapping("/list")
  public Result<List<DeviceProfile>> list() {
    return Result.success(deviceProfileService.findAll());
  }

  @GetMapping("/sn/{sn}")
  public Result<DeviceProfile> getBySn(@PathVariable String sn) {
    DeviceProfile deviceProfile = deviceProfileService.findBySn(sn);
    if (deviceProfile == null) {
      return Result.error("设备档案不存在");
    }
    return Result.success(deviceProfile);
  }

  @GetMapping("/customer/{customerId}")
  public Result<List<DeviceProfile>> getByCustomerId(@PathVariable String customerId) {
    return Result.success(deviceProfileService.findByCustomerId(customerId));
  }

  @GetMapping("/model/{model}")
  public Result<List<DeviceProfile>> getByModel(@PathVariable String model) {
    return Result.success(deviceProfileService.findByModel(model));
  }

  @GetMapping("/{id}")
  public Result<DeviceProfile> getById(@PathVariable String id) {
    DeviceProfile deviceProfile = deviceProfileService.findById(id);
    if (deviceProfile == null) {
      return Result.error("设备档案不存在");
    }
    return Result.success(deviceProfile);
  }

  @PostMapping("/save")
  public Result<DeviceProfile> save(@RequestBody DeviceProfile deviceProfile) {
    return Result.success(deviceProfileService.save(deviceProfile));
  }

  @PutMapping("/update")
  public Result<DeviceProfile> update(@RequestBody DeviceProfile deviceProfile) {
    if (deviceProfile.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(deviceProfileService.save(deviceProfile));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    deviceProfileService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

