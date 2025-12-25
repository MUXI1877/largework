package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.QuotationItem;
import com.it.quanxianguanli.entity.SalesQuotation;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.SalesQuotationService;
import com.it.quanxianguanli.service.SysPermissionService;
import com.it.quanxianguanli.util.ExcelUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/sales-quotation")
public class SalesQuotationController {

    @Autowired
    private SalesQuotationService salesQuotationService;

    @Autowired
    private SysPermissionService permissionService;

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");
    private static final String MODULE_SALES_QUOTATION = "m015";

    private boolean isAdmin(HttpServletRequest request) {
        Object roleId = request.getAttribute("roleId");
        return roleId != null && ADMIN_ROLES.contains(roleId.toString());
    }

    private boolean hasPermission(HttpServletRequest request, String moduleId,
                                  Predicate<SysPermission> predicate) {
        if (isAdmin(request)) {
            return true;
        }
        Object roleId = request.getAttribute("roleId");
        if (roleId == null) {
            return false;
        }
        return permissionService.findByRoleIdAndModuleId(roleId.toString(), moduleId)
                .map(predicate::test)
                .orElse(false);
    }

    private <T> Result<T> forbidden() {
        return Result.error(403, "无权限操作");
    }

    @GetMapping("/list")
    public Result<List<SalesQuotation>> list(
            @RequestParam(required = false) String quotationName,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_SALES_QUOTATION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        List<SalesQuotation> quotations = salesQuotationService.findByConditions(
                quotationName, customerName, startDate, endDate);
        return Result.success(quotations);
    }

    @GetMapping("/{id}")
    public Result<SalesQuotation> getById(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_SALES_QUOTATION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return salesQuotationService.findById(id)
                .map(Result::success)
                .orElse(Result.error("报价单不存在"));
    }

    @PostMapping("/save")
    public Result<SalesQuotation> save(@RequestBody SalesQuotation quotation, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_SALES_QUOTATION, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            SalesQuotation saved = salesQuotationService.save(quotation);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_SALES_QUOTATION, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            salesQuotationService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}/items")
    public Result<List<QuotationItem>> getItems(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_SALES_QUOTATION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(salesQuotationService.getItems(id));
    }

    @PostMapping("/{id}/items")
    public Result<Void> saveItems(@PathVariable String id, @RequestBody List<QuotationItem> items, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_SALES_QUOTATION, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            salesQuotationService.saveItems(id, items);
            return Result.success("保存成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String quotationName,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            HttpServletResponse response,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_SALES_QUOTATION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            List<SalesQuotation> quotations = salesQuotationService.findByConditions(
                    quotationName, customerName, startDate, endDate);
            
            // 构建表头（列表展示字段：报价编号、日期、项目名称、报价客户、报价总额、备注）
            List<String> headers = new ArrayList<>();
            headers.add("报价编号");
            headers.add("日期");
            headers.add("项目名称");
            headers.add("报价客户");
            headers.add("报价总额");
            headers.add("备注");
            
            // 构建数据
            List<List<Object>> data = new ArrayList<>();
            for (SalesQuotation quotation : quotations) {
                List<Object> row = new ArrayList<>();
                row.add(quotation.getQuotationCode());
                row.add(quotation.getQuotationDate());
                row.add(quotation.getProjectName());
                row.add(quotation.getCustomerName());
                row.add(quotation.getTotalPrice());
                row.add(quotation.getRemarks());
                data.add(row);
            }
            
            Workbook workbook = ExcelUtil.createWorkbook(headers, data);
            ExcelUtil.exportToResponse(response, workbook, "报价单列表.xlsx");
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }

    // 打印和导出PDF功能需要前端实现，这里提供数据接口
    @GetMapping("/{id}/print")
    public Result<Map<String, Object>> getPrintData(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_SALES_QUOTATION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return salesQuotationService.findById(id)
                .map(quotation -> {
                    List<QuotationItem> items = salesQuotationService.getItems(id);
                    Map<String, Object> data = new java.util.HashMap<>();
                    data.put("quotation", quotation);
                    data.put("items", items);
                    return Result.success(data);
                })
                .orElse(Result.error("报价单不存在"));
    }
}

