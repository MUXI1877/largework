package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SalesRegion;
import com.it.quanxianguanli.service.SalesRegionService;
import com.it.quanxianguanli.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sales-region")
public class SalesRegionController {

    @Autowired
    private SalesRegionService salesRegionService;

    @GetMapping("/list")
    public Result<List<SalesRegion>> list() {
        return Result.success(salesRegionService.findAll());
    }

    @GetMapping("/{id}")
    public Result<SalesRegion> getById(@PathVariable String id) {
        return salesRegionService.findById(id)
                .map(Result::success)
                .orElse(Result.error("片区不存在"));
    }

    @PostMapping("/save")
    public Result<SalesRegion> save(@RequestBody SalesRegion region) {
        try {
            SalesRegion saved = salesRegionService.save(region);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            salesRegionService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            // 获取数据
            List<SalesRegion> regions = salesRegionService.findAll();
            
            // 构建表头
            List<String> headers = new ArrayList<>();
            headers.add("片区名称");
            headers.add("片区编号");
            headers.add("上级部门");
            headers.add("创建日期");
            headers.add("备注");
            
            // 构建数据
            List<List<Object>> data = new ArrayList<>();
            for (SalesRegion region : regions) {
                List<Object> row = new ArrayList<>();
                row.add(region.getRegionName());
                row.add(region.getRegionCode());
                row.add(region.getParentDepartment());
                row.add(region.getCreateDate());
                row.add(region.getRemarks());
                data.add(row);
            }
            
            // 创建Excel
            Workbook workbook = ExcelUtil.createWorkbook(headers, data);
            
            // 导出
            ExcelUtil.exportToResponse(response, workbook, "销售片区列表.xlsx");
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }
}

