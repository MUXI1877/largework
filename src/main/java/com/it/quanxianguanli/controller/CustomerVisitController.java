package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.CustomerVisit;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.CustomerVisitService;
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
import java.util.Set;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/customer-visit")
public class CustomerVisitController {

    @Autowired
    private CustomerVisitService customerVisitService;

    @Autowired
    private SysPermissionService permissionService;

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");
    private static final String MODULE_CUSTOMER_VISIT = "m012";

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
    public Result<List<CustomerVisit>> list(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER_VISIT, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        List<CustomerVisit> visits = customerVisitService.findByConditions(customerName, startDate, endDate, status);
        return Result.success(visits);
    }

    @GetMapping("/{id}")
    public Result<CustomerVisit> getById(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER_VISIT, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return customerVisitService.findById(id)
                .map(Result::success)
                .orElse(Result.error("来访记录不存在"));
    }

    @PostMapping("/save")
    public Result<CustomerVisit> save(@RequestBody CustomerVisit visit, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER_VISIT, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            CustomerVisit saved = customerVisitService.save(visit);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER_VISIT, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            customerVisitService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            HttpServletResponse response,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER_VISIT, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            List<CustomerVisit> visits = customerVisitService.findByConditions(customerName, startDate, endDate, status);
            
            // 构建表头（列表展示字段：客户序号、客户名称、所属区域、所属行业、公司地址、地区、日期、状态、联络员、联络员所属部门、来访事宜、备注）
            List<String> headers = new ArrayList<>();
            headers.add("客户序号");
            headers.add("客户名称");
            headers.add("所属区域");
            headers.add("所属行业");
            headers.add("公司地址");
            headers.add("地区");
            headers.add("日期");
            headers.add("状态");
            headers.add("联络员");
            headers.add("联络员所属部门");
            headers.add("来访事宜");
            headers.add("备注");
            
            // 构建数据
            List<List<Object>> data = new ArrayList<>();
            for (CustomerVisit visit : visits) {
                List<Object> row = new ArrayList<>();
                row.add(visit.getCustomerSequence());
                row.add(visit.getCustomerName());
                // 需要从客户信息中获取区域、行业等信息
                row.add(""); // 所属区域
                row.add(""); // 所属行业
                row.add(""); // 公司地址
                row.add(""); // 地区
                row.add(visit.getVisitDate());
                row.add(visit.getStatus());
                row.add(visit.getContactPerson());
                row.add(visit.getContactDepartment());
                row.add(visit.getVisitMatter());
                row.add(visit.getRemarks());
                data.add(row);
            }
            
            Workbook workbook = ExcelUtil.createWorkbook(headers, data);
            ExcelUtil.exportToResponse(response, workbook, "客户来访列表.xlsx");
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }
}

