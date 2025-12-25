package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.ExpertAnalysis;
import com.it.quanxianguanli.service.ExpertAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expert-analysis")
public class ExpertAnalysisController {

  @Autowired
  private ExpertAnalysisService expertAnalysisService;

  @GetMapping("/list")
  public Result<List<ExpertAnalysis>> list() {
    return Result.success(expertAnalysisService.findAll());
  }

  @GetMapping("/{id}")
  public Result<ExpertAnalysis> getById(@PathVariable String id) {
    ExpertAnalysis analysis = expertAnalysisService.findById(id);
    if (analysis == null) {
      return Result.error("分析结果不存在");
    }
    return Result.success(analysis);
  }

  @GetMapping("/device/{deviceId}")
  public Result<List<ExpertAnalysis>> getByDeviceId(@PathVariable String deviceId) {
    return Result.success(expertAnalysisService.findByDeviceId(deviceId));
  }

  @PostMapping("/save")
  public Result<ExpertAnalysis> save(@RequestBody ExpertAnalysis analysis) {
    return Result.success(expertAnalysisService.save(analysis));
  }

  @PutMapping("/update")
  public Result<ExpertAnalysis> update(@RequestBody ExpertAnalysis analysis) {
    if (analysis.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(expertAnalysisService.save(analysis));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id) {
    expertAnalysisService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

