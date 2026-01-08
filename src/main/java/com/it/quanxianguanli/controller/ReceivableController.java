package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.ReceivablePlan;
import com.it.quanxianguanli.entity.ReceivableReceipt;
import com.it.quanxianguanli.service.ReceivablePlanService;
import com.it.quanxianguanli.service.ReceivableQueryService;
import com.it.quanxianguanli.service.ReceivableReceiptService;
import com.it.quanxianguanli.service.ReceivableQueryService.ReceivableSummary;
import com.it.quanxianguanli.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ReceivableController {

    private final ReceivablePlanService planService;
    private final ReceivableReceiptService receiptService;
    private final ReceivableQueryService queryService;

    public ReceivableController(ReceivablePlanService planService,
                                ReceivableReceiptService receiptService,
                                ReceivableQueryService queryService) {
        this.planService = planService;
        this.receiptService = receiptService;
        this.queryService = queryService;
    }

    // 4.5.1 计划
    @GetMapping("/receivable-plan/page")
    public Page<ReceivablePlan> pagePlans(@RequestParam(required = false) String contractCode,
                                          @RequestParam(required = false) String contractName,
                                          @RequestParam(required = false) String startDueDate,
                                          @RequestParam(required = false) String endDueDate,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return planService.page(contractCode, contractName,
                startDueDate == null || startDueDate.isEmpty() ? null : java.time.LocalDate.parse(startDueDate),
                endDueDate == null || endDueDate.isEmpty() ? null : java.time.LocalDate.parse(endDueDate),
                page, size);
    }

    @PostMapping("/receivable-plan")
    public Result<ReceivablePlan> createPlan(@RequestBody ReceivablePlan plan) {
        try {
            ReceivablePlan saved = planService.save(plan);
            return Result.success("保存成功", saved);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("保存失败: " + e.getMessage());
        }
    }

    @PutMapping("/receivable-plan/{id}")
    public ReceivablePlan updatePlan(@PathVariable String id, @RequestBody ReceivablePlan req) {
        return planService.update(id, req);
    }

    @RequestMapping(value = "/receivable-plan/{id}", method = RequestMethod.DELETE)
    public Result<Void> deletePlan(@PathVariable String id) {
        try {
            planService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/receivable-plan/export")
    public void exportPlans(HttpServletResponse response,
                            @RequestParam(required = false) String contractCode,
                            @RequestParam(required = false) String contractName) throws IOException {
        List<ReceivablePlan> list = planService.page(contractCode, contractName, null, null, 0, Integer.MAX_VALUE).getContent();
        List<String> headers = Arrays.asList("合同号", "合同名称", "付款阶段", "应付金额", "已付金额", "应付时间", "付款日期", "责任人", "状态", "备注");
        List<List<Object>> data = list.stream().map(p -> {
            List<Object> row = new java.util.ArrayList<>();
            row.add(p.getContractCode());
            row.add(p.getContractName());
            row.add(p.getPaymentStageName());
            row.add(p.getDueAmount());
            row.add(p.getPaidAmount());
            row.add(p.getDueDate());
            row.add(p.getPaidDate());
            row.add(p.getOwner());
            row.add(p.getStatus());
            row.add(p.getRemarks());
            return row;
        }).collect(Collectors.toList());
        Workbook wb = ExcelUtil.createWorkbook(headers, data);
        ExcelUtil.exportToResponse(response, wb, "receivable_plan.xlsx");
    }

    // 4.5.2 回款登记
    @GetMapping("/receivable-receipt/page")
    public Page<ReceivableReceipt> pageReceipts(@RequestParam(required = false) String contractCode,
                                                @RequestParam(required = false) String startDate,
                                                @RequestParam(required = false) String endDate,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        return receiptService.page(contractCode,
                startDate == null || startDate.isEmpty() ? null : java.time.LocalDate.parse(startDate),
                endDate == null || endDate.isEmpty() ? null : java.time.LocalDate.parse(endDate),
                page, size);
    }

    @PostMapping("/receivable-receipt")
    public ReceivableReceipt createReceipt(@RequestBody ReceivableReceipt receipt) {
        return receiptService.create(receipt);
    }

    @PutMapping("/receivable-receipt/{id}")
    public Result<ReceivableReceipt> updateReceipt(@PathVariable String id, @RequestBody ReceivableReceipt req) {
        try {
            ReceivableReceipt updated = receiptService.update(id, req);
            return Result.success("更新成功", updated);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("更新失败: " + e.getMessage());
        }
    }

    @PutMapping("/receivable-receipt/{id}/remark")
    public ReceivableReceipt updateReceiptRemark(@PathVariable String id, @RequestBody ReceivableReceipt req) {
        return receiptService.updateRemark(id, req.getRemarks());
    }

    @RequestMapping(value = "/receivable-receipt/{id}", method = RequestMethod.DELETE)
    public Result<Void> deleteReceipt(@PathVariable String id) {
        try {
            receiptService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    @GetMapping("/receivable-receipt/export")
    public void exportReceipts(HttpServletResponse response,
                               @RequestParam(required = false) String contractCode) throws IOException {
        List<ReceivableReceipt> list = receiptService.page(contractCode, null, null, 0, Integer.MAX_VALUE).getContent();
        List<String> headers = Arrays.asList("合同号", "合同名称", "公司名称", "公司编号", "合同总额", "剩余金额", "到款时间", "到款金额", "备注");
        List<List<Object>> data = list.stream().map(r -> {
            List<Object> row = new java.util.ArrayList<>();
            row.add(r.getContractCode());
            row.add(r.getContractName());
            row.add(r.getCompanyName());
            row.add(r.getCompanyCode());
            row.add(r.getContractAmount());
            row.add(r.getRemainingAmount());
            row.add(r.getReceiveDate());
            row.add(r.getReceiveAmount());
            row.add(r.getRemarks());
            return row;
        }).collect(Collectors.toList());
        Workbook wb = ExcelUtil.createWorkbook(headers, data);
        ExcelUtil.exportToResponse(response, wb, "receivable_receipt.xlsx");
    }

    // 4.5.3 汇总查询
    @GetMapping("/receivable-summary/page")
    public Page<ReceivableSummary> summary(@RequestParam(required = false) String contractCode,
                                           @RequestParam(required = false) String companyName,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return queryService.query(contractCode, companyName, page, size);
    }

    @GetMapping("/receivable-summary/export")
    public void exportSummary(HttpServletResponse response,
                              @RequestParam(required = false) String contractCode,
                              @RequestParam(required = false) String companyName) throws IOException {
        List<ReceivableSummary> list = queryService.query(contractCode, companyName, 0, Integer.MAX_VALUE).getContent();
        List<String> headers = Arrays.asList("应收账编号", "合同号", "合同名称", "公司名称", "公司编号", "款项", "金额", "计划收款时间", "实际收款时间", "应收状态", "偏差金额");
        List<List<Object>> data = list.stream().map(s -> {
            List<Object> row = new java.util.ArrayList<>();
            row.add(s.getPlanId());
            row.add(s.getContractCode());
            row.add(s.getContractName());
            row.add(s.getCompanyName());
            row.add(s.getCompanyCode());
            row.add(s.getItem());
            row.add(s.getPlanAmount());
            row.add(s.getPlanDate());
            row.add(s.getActualDate());
            row.add(s.getStatus());
            row.add(s.getDeviation());
            return row;
        }).collect(Collectors.toList());
        Workbook wb = ExcelUtil.createWorkbook(headers, data);
        ExcelUtil.exportToResponse(response, wb, "receivable_summary.xlsx");
    }

    // 根据合同号查询合同信息（用于回款登记自动填充）
    @GetMapping("/receivable-receipt/contract-info")
    public Result<ReceivableReceiptService.ContractInfo> getContractInfo(@RequestParam String contractCode) {
        try {
            ReceivableReceiptService.ContractInfo info = receiptService.getContractInfo(contractCode);
            return Result.success("查询成功", info);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败: " + e.getMessage());
        }
    }
}

