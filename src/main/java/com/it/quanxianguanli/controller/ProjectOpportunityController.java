package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.*;
import com.it.quanxianguanli.service.ProjectOpportunityService;
import com.it.quanxianguanli.util.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project-opportunity")
public class ProjectOpportunityController {

    @Autowired
    private ProjectOpportunityService projectOpportunityService;

    @GetMapping("/list")
    public Result<List<ProjectOpportunity>> list(
            @RequestParam(required = false) String opportunityName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String industry) {
        List<ProjectOpportunity> opportunities = projectOpportunityService.findByConditions(
                opportunityName, startDate, endDate, status, source, industry);
        return Result.success(opportunities);
    }

    @GetMapping("/{id}")
    public Result<ProjectOpportunity> getById(@PathVariable String id) {
        return projectOpportunityService.findById(id)
                .map(Result::success)
                .orElse(Result.error("销售机会不存在"));
    }

    @PostMapping("/save")
    public Result<ProjectOpportunity> save(@RequestBody ProjectOpportunity opportunity) {
        try {
            // 检查是否已提交
            if (opportunity.getId() != null && !opportunity.getId().isEmpty()) {
                var existing = projectOpportunityService.findById(opportunity.getId());
                if (existing.isPresent() && existing.get().getIsSubmitted()) {
                    return Result.error("已提交的销售机会不可修改");
                }
            }
            
            ProjectOpportunity saved = projectOpportunityService.save(opportunity);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            projectOpportunityService.delete(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/submit")
    public Result<Void> submit(@PathVariable String id) {
        try {
            projectOpportunityService.submit(id);
            return Result.success("提交成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/transfer-regions")
    public Result<Void> transferRegions(@PathVariable String id, @RequestBody Map<String, List<String>> request) {
        try {
            List<String> regionIds = request.get("regionIds");
            projectOpportunityService.transferRegions(id, regionIds);
            return Result.success("传递片区成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/assign-employees")
    public Result<Void> assignEmployees(@PathVariable String id, @RequestBody Map<String, List<String>> request) {
        try {
            List<String> employeeIds = request.get("employeeIds");
            projectOpportunityService.assignEmployees(id, employeeIds);
            return Result.success("分配员工成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/close")
    public Result<Void> close(@PathVariable String id, @RequestBody Map<String, String> request) {
        try {
            String closeReason = request.get("closeReason");
            projectOpportunityService.close(id, closeReason);
            return Result.success("关闭成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}/trackings")
    public Result<List<OpportunityTracking>> getTrackings(@PathVariable String id) {
        return Result.success(projectOpportunityService.getTrackings(id));
    }

    @PostMapping("/tracking/save")
    public Result<OpportunityTracking> saveTracking(@RequestBody OpportunityTracking tracking) {
        try {
            OpportunityTracking saved = projectOpportunityService.saveTracking(tracking);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/tracking/{id}")
    public Result<Void> deleteTracking(@PathVariable String id) {
        try {
            projectOpportunityService.deleteTracking(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}/regions")
    public Result<List<OpportunityRegion>> getRegions(@PathVariable String id) {
        return Result.success(projectOpportunityService.getRegions(id));
    }

    @GetMapping("/{id}/employees")
    public Result<List<OpportunityEmployee>> getEmployees(@PathVariable String id) {
        return Result.success(projectOpportunityService.getEmployees(id));
    }

    @GetMapping("/{id}/key-persons")
    public Result<List<OpportunityKeyPerson>> getKeyPersons(@PathVariable String id) {
        return Result.success(projectOpportunityService.getKeyPersons(id));
    }

    @PostMapping("/{id}/key-persons")
    public Result<Void> saveKeyPersons(@PathVariable String id, @RequestBody List<OpportunityKeyPerson> keyPersons) {
        try {
            projectOpportunityService.saveKeyPersons(id, keyPersons);
            return Result.success("保存成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/export")
    public void export(
            @RequestParam(required = false) String opportunityName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String industry,
            HttpServletResponse response) {
        try {
            List<ProjectOpportunity> opportunities = projectOpportunityService.findByConditions(
                    opportunityName, startDate, endDate, status, source, industry);
            
            // 构建表头（列表展示字段：销售机会日期、销售机会主题、存货、预算、线索来源、接收状态）
            List<String> headers = new ArrayList<>();
            headers.add("销售机会日期");
            headers.add("销售机会主题");
            headers.add("存货");
            headers.add("预算");
            headers.add("线索来源");
            headers.add("接收状态");
            
            // 构建数据
            List<List<Object>> data = new ArrayList<>();
            for (ProjectOpportunity opp : opportunities) {
                List<Object> row = new ArrayList<>();
                row.add(opp.getOpportunityDate());
                row.add(opp.getOpportunityName());
                row.add(opp.getInventory());
                row.add(opp.getBudget());
                row.add(opp.getSource());
                row.add(opp.getReceiveStatus());
                data.add(row);
            }
            
            Workbook workbook = ExcelUtil.createWorkbook(headers, data);
            ExcelUtil.exportToResponse(response, workbook, "销售机会列表.xlsx");
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }
}

