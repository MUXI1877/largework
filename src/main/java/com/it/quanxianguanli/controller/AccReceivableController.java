package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.AccReceivablePlan;
import com.it.quanxianguanli.entity.AccReceivableItem;
import com.it.quanxianguanli.service.AccReceivablePlanService;
import com.it.quanxianguanli.service.AccReceivableItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/acc-receivable")
public class AccReceivableController {

  @Autowired
  private AccReceivablePlanService accReceivablePlanService;

  @Autowired
  private AccReceivableItemService accReceivableItemService;

  /**
   * 应收账查询（合并计划/实收）
   */
  @GetMapping("/query")
  public Result<List<Map<String, Object>>> query(@RequestParam(required = false) String contractId) {
    List<Map<String, Object>> result = new ArrayList<>();
    
    // 获取计划数据
    List<AccReceivablePlan> plans;
    if (contractId != null && !contractId.isEmpty()) {
      plans = accReceivablePlanService.findByContractId(contractId);
    } else {
      plans = accReceivablePlanService.findAll();
    }
    
    // 获取实收数据
    List<AccReceivableItem> items;
    if (contractId != null && !contractId.isEmpty()) {
      items = accReceivableItemService.findByContractId(contractId);
    } else {
      items = accReceivableItemService.findAll();
    }
    
    // 合并数据：先添加计划数据
    for (AccReceivablePlan plan : plans) {
      Map<String, Object> record = new HashMap<>();
      record.put("contractId", plan.getContractId());
      record.put("stage", plan.getStage());
      record.put("planDate", plan.getPlanDate());
      record.put("planAmount", plan.getPlanAmount());
      record.put("receiveDate", null);
      record.put("amount", null);
      result.add(record);
    }
    
    // 添加实收数据
    for (AccReceivableItem item : items) {
      Map<String, Object> record = new HashMap<>();
      record.put("contractId", item.getContractId());
      record.put("stage", null);
      record.put("planDate", null);
      record.put("planAmount", null);
      record.put("receiveDate", item.getReceiveDate());
      record.put("amount", item.getAmount());
      result.add(record);
    }
    
    return Result.success(result);
  }
}

