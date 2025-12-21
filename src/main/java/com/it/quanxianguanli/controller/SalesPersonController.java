package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SalesPerson;
import com.it.quanxianguanli.service.SalesPersonService;
import com.it.quanxianguanli.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales-person")
public class SalesPersonController {

    @Autowired
    private SalesPersonService salesPersonService;

    @GetMapping("/list")
    public Result<List<SalesPerson>> list() {
        return Result.success(salesPersonService.findAll());
    }

    @GetMapping("/region/{regionId}")
    public Result<List<SalesPerson>> listByRegion(@PathVariable String regionId) {
        return Result.success(salesPersonService.findByRegionId(regionId));
    }

    @GetMapping("/{id}")
    public Result<SalesPerson> getById(@PathVariable String id) {
        return salesPersonService.findById(id)
                .map(Result::success)
                .orElse(Result.error("营销人员不存在"));
    }

    @PostMapping("/save")
    public Result<SalesPerson> save(@RequestBody SalesPerson person) {
        try {
            SalesPerson saved = salesPersonService.save(person);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            salesPersonService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/batch-transfer")
    public Result<Void> batchTransfer(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<String> personIds = (List<String>) request.get("personIds");
            String targetRegionId = (String) request.get("targetRegionId");
            String targetDepartment = (String) request.get("targetDepartment");
            String targetArea = (String) request.get("targetArea");
            
            salesPersonService.batchTransfer(personIds, targetRegionId, targetDepartment, targetArea);
            return Result.success("批量调动成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) {
        try {
            // 获取数据
            List<SalesPerson> persons = salesPersonService.findAll();
            
            // 构建表头（列表展示字段：工号、姓名、职务、所属部门、联系方式、当前负责区域）
            List<String> headers = new ArrayList<>();
            headers.add("工号");
            headers.add("姓名");
            headers.add("性别");
            headers.add("生日");
            headers.add("联系方式");
            headers.add("职务");
            headers.add("所属部门");
            headers.add("当前负责区域");
            headers.add("备注");
            
            // 构建数据
            List<List<Object>> data = new ArrayList<>();
            for (SalesPerson person : persons) {
                List<Object> row = new ArrayList<>();
                row.add(person.getEmployeeCode());
                row.add(person.getName());
                row.add(person.getGender());
                row.add(person.getBirthday());
                row.add(person.getContactInfo());
                row.add(person.getPosition());
                row.add(person.getDepartment());
                row.add(person.getResponsibleArea());
                row.add(person.getRemarks());
                data.add(row);
            }
            
            // 创建Excel
            Workbook workbook = ExcelUtil.createWorkbook(headers, data);
            
            // 导出
            ExcelUtil.exportToResponse(response, workbook, "营销人员列表.xlsx");
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }
}

