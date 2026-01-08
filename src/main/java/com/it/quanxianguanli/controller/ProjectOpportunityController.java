package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.*;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.ProjectOpportunityService;
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
@RequestMapping("/api/project-opportunity")
public class ProjectOpportunityController {

    @Autowired
    private ProjectOpportunityService projectOpportunityService;

    @Autowired
    private SysPermissionService permissionService;

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");
    private static final String MODULE_PROJECT_OPPORTUNITY = "m013";

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
    public Result<List<ProjectOpportunity>> list(
            @RequestParam(required = false) String opportunityName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String industry,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        
        // 获取当前用户信息
        Object userIdObj = request.getAttribute("userId");
        Object roleIdObj = request.getAttribute("roleId");
        String userId = userIdObj != null ? userIdObj.toString() : null;
        String roleId = roleIdObj != null ? roleIdObj.toString() : null;
        
        List<ProjectOpportunity> opportunities;
        
        // 根据角色查询数据
        if (userId != null && roleId != null) {
            opportunities = projectOpportunityService.findByUserRole(
                    userId, roleId, opportunityName, startDate, endDate, status, source, industry);
        } else {
            // 如果没有用户信息，返回空列表
            opportunities = new ArrayList<>();
        }
        
        return Result.success(opportunities);
    }

    @GetMapping("/{id}")
    public Result<ProjectOpportunity> getById(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return projectOpportunityService.findById(id)
                .map(Result::success)
                .orElse(Result.error("销售机会不存在"));
    }

    @PostMapping("/save")
    public Result<ProjectOpportunity> save(@RequestBody ProjectOpportunity opportunity, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            // 获取当前用户信息
            Object userIdObj = request.getAttribute("userId");
            Object roleIdObj = request.getAttribute("roleId");
            String userId = userIdObj != null ? userIdObj.toString() : null;
            String roleId = roleIdObj != null ? roleIdObj.toString() : null;
            
            // 检查是否已提交
            if (opportunity.getId() != null && !opportunity.getId().isEmpty()) {
                var existing = projectOpportunityService.findById(opportunity.getId());
                if (existing.isPresent()) {
                    ProjectOpportunity existingOpp = existing.get();
                    if (existingOpp.getIsSubmitted()) {
                        return Result.error("已提交的销售机会不可修改");
                    }
                    
                    // 普通用户只能修改自己创建的机会
                    if ("r003".equals(roleId) && userId != null && !userId.equals(existingOpp.getCreatorId())) {
                        return Result.error("只能修改自己创建的销售机会");
                    }
                }
            }
            
            // 如果是新建机会，设置原始所属片区（从当前用户的areaId获取）
            if (opportunity.getId() == null || opportunity.getId().isEmpty()) {
                Object userAreaId = request.getAttribute("areaId");
                if (userAreaId != null && !userAreaId.toString().isEmpty()) {
                    opportunity.setOwnerRegionId(userAreaId.toString());
                }
            }
            
            ProjectOpportunity saved = projectOpportunityService.save(opportunity, userId);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            Object userIdObj = request.getAttribute("userId");
            Object roleIdObj = request.getAttribute("roleId");
            String userId = userIdObj != null ? userIdObj.toString() : null;
            String roleId = roleIdObj != null ? roleIdObj.toString() : null;
            
            projectOpportunityService.delete(id, userId, roleId);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/submit")
    public Result<Void> submit(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            Object userIdObj = request.getAttribute("userId");
            Object roleIdObj = request.getAttribute("roleId");
            String userId = userIdObj != null ? userIdObj.toString() : null;
            String roleId = roleIdObj != null ? roleIdObj.toString() : null;
            
            projectOpportunityService.submit(id, userId, roleId);
            return Result.success("提交成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/upload")
    public Result<Void> uploadToSuperAdmin(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            Object roleIdObj = request.getAttribute("roleId");
            String roleId = roleIdObj != null ? roleIdObj.toString() : null;
            
            projectOpportunityService.uploadToSuperAdmin(id, roleId);
            return Result.success("上传成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/transfer-regions")
    public Result<Void> transferRegions(@PathVariable String id, @RequestBody Map<String, List<String>> requestBody, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            List<String> regionIds = requestBody.get("regionIds");
            projectOpportunityService.transferRegions(id, regionIds);
            return Result.success("传递片区成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/assign-employees")
    public Result<Void> assignEmployees(@PathVariable String id, @RequestBody Map<String, List<String>> requestBody, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            List<String> employeeIds = requestBody.get("employeeIds");
            projectOpportunityService.assignEmployees(id, employeeIds);
            return Result.success("分配员工成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{id}/close")
    public Result<Void> close(@PathVariable String id, @RequestBody Map<String, String> requestBody, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            Object roleIdObj = request.getAttribute("roleId");
            String roleId = roleIdObj != null ? roleIdObj.toString() : null;
            String closeReason = requestBody.get("closeReason");
            
            projectOpportunityService.close(id, closeReason, roleId);
            return Result.success("关闭成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}/trackings")
    public Result<List<OpportunityTracking>> getTrackings(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(projectOpportunityService.getTrackings(id));
    }

    @PostMapping("/tracking/save")
    public Result<OpportunityTracking> saveTracking(@RequestBody OpportunityTracking tracking, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            OpportunityTracking saved = projectOpportunityService.saveTracking(tracking);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/tracking/{id}")
    public Result<Void> deleteTracking(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            projectOpportunityService.deleteTracking(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}/regions")
    public Result<List<OpportunityRegion>> getRegions(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(projectOpportunityService.getRegions(id));
    }

    @GetMapping("/{id}/employees")
    public Result<List<OpportunityEmployee>> getEmployees(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(projectOpportunityService.getEmployees(id));
    }

    @GetMapping("/{id}/key-persons")
    public Result<List<OpportunityKeyPerson>> getKeyPersons(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(projectOpportunityService.getKeyPersons(id));
    }

    @PostMapping("/{id}/key-persons")
    public Result<Void> saveKeyPersons(@PathVariable String id, @RequestBody List<OpportunityKeyPerson> keyPersons, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
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
            HttpServletResponse response,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_PROJECT_OPPORTUNITY, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
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

