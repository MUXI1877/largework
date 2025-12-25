package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.*;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.CustomerService;
import com.it.quanxianguanli.service.SysPermissionService;
import com.it.quanxianguanli.util.ExcelUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SysPermissionService permissionService;

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");
    private static final String MODULE_CUSTOMER = "m008";

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
    public Result<List<Customer>> list(HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(customerService.findAll());
    }

    @GetMapping("/{id}")
    public Result<Customer> getById(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return customerService.findById(id)
                .map(Result::success)
                .orElse(Result.error("客户不存在"));
    }

    @PostMapping("/save")
    public Result<Customer> save(@RequestBody Customer customer, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            Customer saved = customerService.save(customer);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            customerService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            // 获取数据
            List<Customer> customers = customerService.findAll();
            
            // 构建表头（列表展示字段：客户编号、单位、所属区域、行业、地区、客户属性、信用评级）
            List<String> headers = new ArrayList<>();
            headers.add("客户编号");
            headers.add("单位");
            headers.add("所属区域");
            headers.add("行业");
            headers.add("地区");
            headers.add("客户属性");
            headers.add("信用评级");
            headers.add("公司地址");
            headers.add("订货代表");
            headers.add("联系电话");
            headers.add("传真");
            headers.add("邮编");
            headers.add("开票地址");
            headers.add("开票银行");
            headers.add("银行账号");
            headers.add("税号");
            headers.add("欠款金额");
            headers.add("备注");
            
            // 构建数据
            List<List<Object>> data = new ArrayList<>();
            for (Customer customer : customers) {
                List<Object> row = new ArrayList<>();
                row.add(customer.getCustomerCode());
                row.add(customer.getUnitName());
                row.add(customer.getRegion());
                row.add(customer.getIndustry());
                row.add(customer.getArea());
                row.add(customer.getBuyerAttribute());
                row.add(customer.getCreditLevel());
                row.add(customer.getCompanyAddress());
                row.add(customer.getOrderRepresentative());
                row.add(customer.getContactPhone());
                row.add(customer.getFax());
                row.add(customer.getPostalCode());
                row.add(customer.getInvoiceAddress());
                row.add(customer.getInvoiceBank());
                row.add(customer.getBankAccount());
                row.add(customer.getTaxNumber());
                row.add(customer.getArrearsAmount());
                row.add(customer.getRemarks());
                data.add(row);
            }
            
            // 创建Excel
            Workbook workbook = ExcelUtil.createWorkbook(headers, data);
            
            // 导出
            ExcelUtil.exportToResponse(response, workbook, "客户列表.xlsx");
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }

    // 客户关键人物管理
    @GetMapping("/{customerId}/key-persons")
    public Result<List<CustomerKeyPerson>> getKeyPersons(@PathVariable String customerId, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(customerService.getKeyPersons(customerId));
    }

    @PostMapping("/key-person/save")
    public Result<CustomerKeyPerson> saveKeyPerson(@RequestBody CustomerKeyPerson keyPerson, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            CustomerKeyPerson saved = customerService.saveKeyPerson(keyPerson);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/key-person/{id}")
    public Result<Void> deleteKeyPerson(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            customerService.deleteKeyPerson(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 项目机会
    @GetMapping("/{customerId}/project-opportunities")
    public Result<List<ProjectOpportunity>> getProjectOpportunities(@PathVariable String customerId, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(customerService.getProjectOpportunities(customerId));
    }

    // 合同信息
    @GetMapping("/{customerId}/contracts")
    public Result<List<Contract>> getContracts(@PathVariable String customerId, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(customerService.getContracts(customerId));
    }

    // 售后信息
    @GetMapping("/{customerId}/after-sales")
    public Result<List<AfterSales>> getAfterSales(@PathVariable String customerId, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(customerService.getAfterSales(customerId));
    }

    // 客户来访信息
    @GetMapping("/{customerId}/visits")
    public Result<List<CustomerVisit>> getCustomerVisits(@PathVariable String customerId, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_CUSTOMER, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(customerService.getCustomerVisits(customerId));
    }
}

