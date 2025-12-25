package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.PriceBook;
import com.it.quanxianguanli.entity.PriceBookLog;
import com.it.quanxianguanli.entity.Product;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.PriceBookService;
import com.it.quanxianguanli.service.ProductService;
import com.it.quanxianguanli.service.SysPermissionService;
import com.it.quanxianguanli.util.ExcelUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/price-book")
public class PriceBookController {
    
    @Autowired
    private PriceBookService priceBookService;
    
    @Autowired
    private ProductService productService;

    @Autowired
    private SysPermissionService permissionService;

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");
    private static final String MODULE_PRICE_BOOK = "m019";

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
    
    /**
     * 分页查询价格本
     */
    @GetMapping("/page")
    public Result<Page<PriceBook>> getPriceBooks(
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updateTime"));
            Page<PriceBook> priceBooks = priceBookService.findPriceBooks(
                    productType, department, productName, model, pageable);
            return Result.success(priceBooks);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据ID查询价格本
     */
    @GetMapping("/{id}")
    public Result<PriceBook> getPriceBookById(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        try {
            return priceBookService.findById(id)
                    .map(Result::success)
                    .orElse(Result.error("价格本不存在"));
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据产品ID和部门查询价格
     * 供销售报价管理模块调用
     */
    @GetMapping("/price")
    public Result<Map<String, Object>> getPrice(
            @RequestParam String productId,
            @RequestParam String department,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        try {
            java.math.BigDecimal price = priceBookService.getPriceByProductIdAndDepartment(productId, department);
            Map<String, Object> result = new HashMap<>();
            result.put("price", price);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    
    /**
     * 新增价格本
     */
    @PostMapping
    public Result<PriceBook> createPriceBook(@RequestBody PriceBook priceBook,
                                             @RequestHeader(value = "userId", required = false) String userId,
                                             @RequestHeader(value = "username", required = false) String username,
                                             HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            PriceBook saved = priceBookService.createPriceBook(priceBook, userId, username);
            return Result.success(saved);
        } catch (Exception e) {
            return Result.error("新增失败：" + e.getMessage());
        }
    }
    
    /**
     * 更新价格本
     */
    @PutMapping("/{id}")
    public Result<PriceBook> updatePriceBook(@PathVariable String id,
                                            @RequestBody PriceBook priceBook,
                                            @RequestHeader(value = "userId", required = false) String userId,
                                            @RequestHeader(value = "username", required = false) String username,
                                            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            PriceBook updated = priceBookService.updatePriceBook(id, priceBook, userId, username);
            return Result.success(updated);
        } catch (Exception e) {
            return Result.error("更新失败：" + e.getMessage());
        }
    }
    
    /**
     * 删除价格本
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePriceBook(@PathVariable String id,
                                        @RequestHeader(value = "userId", required = false) String userId,
                                        @RequestHeader(value = "username", required = false) String username,
                                        HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            priceBookService.deletePriceBook(id, userId, username);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error("删除失败：" + e.getMessage());
        }
    }
    
    /**
     * 查询价格修改日志
     */
    @GetMapping("/{id}/logs")
    public Result<List<PriceBookLog>> getPriceBookLogs(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        try {
            List<PriceBookLog> logs = priceBookService.findPriceBookLogs(id);
            return Result.success(logs);
        } catch (Exception e) {
            return Result.error("查询日志失败：" + e.getMessage());
        }
    }
    
    /**
     * 导出价格本数据
     */
    @GetMapping("/export")
    public void exportPriceBooks(
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String model,
            HttpServletResponse response,
            HttpServletRequest request) throws IOException {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            List<PriceBook> priceBooks;
            if (productType != null || department != null || productName != null || model != null) {
                // 导出筛选后的数据
                priceBooks = priceBookService.exportFilteredPriceBooks(productType, department, productName, model);
            } else {
                // 导出全部数据
                priceBooks = priceBookService.exportAllPriceBooks();
            }
            
            // 设置响应头
            String fileName = URLEncoder.encode("价格本数据", StandardCharsets.UTF_8) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            
            // 导出Excel
            ExcelUtil.exportPriceBooks(priceBooks, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("导出失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取产品列表（用于下拉选择）
     */
    @GetMapping("/products")
    public Result<List<Product>> getProducts(@RequestParam(required = false) String productType, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        try {
            List<Product> products;
            if (productType != null && !productType.isEmpty()) {
                products = productService.findByType(productType);
            } else {
                products = productService.findAll();
            }
            return Result.success(products);
        } catch (Exception e) {
            return Result.error("查询产品列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取部门列表（用于下拉选择）
     */
    @GetMapping("/departments")
    public Result<List<Map<String, String>>> getDepartments(HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        try {
            List<Map<String, String>> departments = List.of(
                    Map.of("value", "销售处", "label", "销售处"),
                    Map.of("value", "成套处", "label", "成套处"),
                    Map.of("value", "外贸处", "label", "外贸处")
            );
            return Result.success(departments);
        } catch (Exception e) {
            return Result.error("查询部门列表失败：" + e.getMessage());
        }
    }
    
    /**
     * 获取产品类别列表（用于下拉选择）
     */
    @GetMapping("/product-types")
    public Result<List<Map<String, String>>> getProductTypes(HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PRICE_BOOK, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        try {
            List<Map<String, String>> productTypes = List.of(
                    Map.of("value", "SINGLE", "label", "单泵（单体设备）"),
                    Map.of("value", "COMPLETE", "label", "成套设备"),
                    Map.of("value", "SPARE", "label", "备品备件")
            );
            return Result.success(productTypes);
        } catch (Exception e) {
            return Result.error("查询产品类别列表失败：" + e.getMessage());
        }
    }
}

