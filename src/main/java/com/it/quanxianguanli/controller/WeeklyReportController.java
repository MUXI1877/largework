package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.WeeklyReport;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.WeeklyReportService;
import com.it.quanxianguanli.service.SysUserService;
import com.it.quanxianguanli.service.SysPermissionService;
import com.it.quanxianguanli.util.ExcelUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api/weekly-report")
public class WeeklyReportController {

    @Autowired
    private WeeklyReportService weeklyReportService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");
    private static final String MODULE_WEEKLY_REPORT = "m025";

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

    private String getCurrentUserId(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null || username.isEmpty()) {
            return null;
        }
        return userService.findByUsername(username)
                .map(user -> user.getId())
                .orElse(null);
    }

    @GetMapping("/list")
    public Result<List<WeeklyReport>> list(@RequestParam(required = false) String userId, HttpServletRequest request) {
        boolean admin = isAdmin(request);
        String currentUserId = getCurrentUserId(request);
        // 管理员：不传或传all
        if (admin && (userId == null || userId.isEmpty() || "all".equals(userId))) {
            // 管理员可见全部“已提交”，草稿只看自己的
            List<WeeklyReport> all = weeklyReportService.findAll();
            List<WeeklyReport> filtered = new java.util.ArrayList<>();
            for (WeeklyReport r : all) {
                if ("draft".equalsIgnoreCase(r.getStatus())) {
                    if (currentUserId != null && currentUserId.equals(r.getUserId())) {
                        filtered.add(r);
                    }
                } else {
                    filtered.add(r);
                }
            }
            return Result.success(filtered);
        }
        // 非管理员禁止all
        if (!admin && "all".equals(userId)) {
            return forbidden();
        }
        // 默认当前用户
        if (userId == null || userId.isEmpty()) {
            if (currentUserId != null && !currentUserId.isEmpty()) {
                return Result.success(weeklyReportService.findByUserId(currentUserId));
            }
            return Result.success(new java.util.ArrayList<>());
        }
        // 指定 userId
        return Result.success(weeklyReportService.findByUserId(userId));
    }

    @GetMapping("/{id}")
    public Result<WeeklyReport> getById(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_WEEKLY_REPORT, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        WeeklyReport report = weeklyReportService.findById(id);
        if (report == null) {
            return Result.error("周报不存在");
        }
        return Result.success(report);
    }

    @PostMapping("/save")
    public Result<WeeklyReport> save(@RequestBody WeeklyReport weeklyReport, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_WEEKLY_REPORT, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            // 如果userId为空，从request中获取当前用户ID
            if (weeklyReport.getUserId() == null || weeklyReport.getUserId().isEmpty()) {
                String username = (String) request.getAttribute("username");
                if (username != null) {
                    userService.findByUsername(username).ifPresent(user -> {
                        weeklyReport.setUserId(user.getId());
                    });
                }
            }
            // 验证userId是否已设置
            if (weeklyReport.getUserId() == null || weeklyReport.getUserId().isEmpty()) {
                return Result.error("无法获取用户信息，请重新登录");
            }
            // 新增时，确保id为空，让JPA自动生成
            if (weeklyReport.getId() != null && weeklyReport.getId().isEmpty()) {
                weeklyReport.setId(null);
            }
            WeeklyReport saved = weeklyReportService.save(weeklyReport);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<WeeklyReport> update(@RequestBody WeeklyReport weeklyReport, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_WEEKLY_REPORT, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        if (weeklyReport.getId() == null) {
            return Result.error("ID不能为空");
        }
        try {
            WeeklyReport saved = weeklyReportService.save(weeklyReport);
            return Result.success("更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/submit/{id}")
    public Result<WeeklyReport> submit(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_WEEKLY_REPORT, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            WeeklyReport report = weeklyReportService.submit(id);
            if (report == null) {
                return Result.error("周报不存在");
            }
            return Result.success("提交成功", report);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_WEEKLY_REPORT, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            weeklyReportService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response, @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_WEEKLY_REPORT, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            List<WeeklyReport> reports;
            boolean admin = isAdmin(request);
            String currentUserId = getCurrentUserId(request);
            if (userId != null && !userId.isEmpty()) {
                reports = weeklyReportService.findByUserId(userId);
            } else {
                reports = weeklyReportService.findAll();
            }

            // 管理员导出时，同样遵守“草稿只看自己”规则
            if (admin) {
                List<WeeklyReport> filtered = new java.util.ArrayList<>();
                for (WeeklyReport r : reports) {
                    if ("draft".equalsIgnoreCase(r.getStatus())) {
                        if (currentUserId != null && currentUserId.equals(r.getUserId())) {
                            filtered.add(r);
                        }
                    } else {
                        filtered.add(r);
                    }
                }
                reports = filtered;
            }

            List<String> headers = new ArrayList<>();
            headers.add("周报时间");
            headers.add("周报名称");
            headers.add("周报内容");
            headers.add("周报备注");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

            List<List<Object>> data = new ArrayList<>();
            for (WeeklyReport report : reports) {
                List<Object> row = new ArrayList<>();
                row.add(report.getReportTime().format(formatter));
                row.add(report.getReportName());
                row.add(report.getReportContent() != null ? report.getReportContent() : "");
                row.add(report.getReportRemark() != null ? report.getReportRemark() : "");
                data.add(row);
            }

            Workbook workbook = ExcelUtil.createWorkbook(headers, data);
            ExcelUtil.exportToResponse(response, workbook, "周报列表.xlsx");
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }
}
