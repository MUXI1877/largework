package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SalesInventoryReduction;
import com.it.quanxianguanli.entity.SalesInventoryReductionItem;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.SalesInventoryReductionService;
import com.it.quanxianguanli.service.SysPermissionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/sales-inventory-reduction")
public class SalesInventoryReductionController {

  @Autowired
  private SalesInventoryReductionService salesInventoryReductionService;

  @Autowired
  private SysPermissionService permissionService;

  private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");
  private static final String MODULE_SALES_INVENTORY_REDUCTION = "m016";

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
  public Result<List<SalesInventoryReduction>> list(HttpServletRequest request) {
    if (!hasPermission(request, MODULE_SALES_INVENTORY_REDUCTION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
      return forbidden();
    }
    return Result.success(salesInventoryReductionService.findAll());
  }

  @GetMapping("/{id}")
  public Result<SalesInventoryReduction> getById(@PathVariable String id, HttpServletRequest request) {
    if (!hasPermission(request, MODULE_SALES_INVENTORY_REDUCTION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
      return forbidden();
    }
    SalesInventoryReduction reduction = salesInventoryReductionService.findById(id);
    if (reduction == null) {
      return Result.error("降库单不存在");
    }
    return Result.success(reduction);
  }

  @GetMapping("/{id}/items")
  public Result<List<SalesInventoryReductionItem>> getItems(@PathVariable String id, HttpServletRequest request) {
    if (!hasPermission(request, MODULE_SALES_INVENTORY_REDUCTION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
      return forbidden();
    }
    return Result.success(salesInventoryReductionService.findItemsByReductionId(id));
  }

  @PostMapping("/save")
  public Result<SalesInventoryReduction> save(@RequestBody SalesInventoryReduction reduction, HttpServletRequest request) {
    if (!hasPermission(request, MODULE_SALES_INVENTORY_REDUCTION, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
      return forbidden();
    }
    return Result.success(salesInventoryReductionService.save(reduction));
  }

  @PostMapping("/item/save")
  public Result<SalesInventoryReductionItem> saveItem(@RequestBody SalesInventoryReductionItem item, HttpServletRequest request) {
    if (!hasPermission(request, MODULE_SALES_INVENTORY_REDUCTION, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
      return forbidden();
    }
    return Result.success(salesInventoryReductionService.saveItem(item));
  }

  @PutMapping("/update")
  public Result<SalesInventoryReduction> update(@RequestBody SalesInventoryReduction reduction, HttpServletRequest request) {
    if (!hasPermission(request, MODULE_SALES_INVENTORY_REDUCTION, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
      return forbidden();
    }
    if (reduction.getId() == null) {
      return Result.error("ID不能为空");
    }
    return Result.success(salesInventoryReductionService.save(reduction));
  }

  @DeleteMapping("/{id}")
  public Result<Void> delete(@PathVariable String id, HttpServletRequest request) {
    if (!hasPermission(request, MODULE_SALES_INVENTORY_REDUCTION, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
      return forbidden();
    }
    salesInventoryReductionService.deleteById(id);
    return Result.success("删除成功", null);
  }
}

