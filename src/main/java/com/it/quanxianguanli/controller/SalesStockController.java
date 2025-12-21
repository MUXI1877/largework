package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.DeviceUnit;
import com.it.quanxianguanli.service.DeviceUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-stock")
public class SalesStockController {

  @Autowired
  private DeviceUnitService deviceUnitService;

  @GetMapping("/list")
  public Result<List<DeviceUnit>> listStock() {
    return Result.success(deviceUnitService.findAll());
  }
}

