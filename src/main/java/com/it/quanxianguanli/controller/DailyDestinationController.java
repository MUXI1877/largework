package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.DailyDestination;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.DailyDestinationService;
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
@RequestMapping("/api/daily-destination")
public class DailyDestinationController {

    @Autowired
    private DailyDestinationService dailyDestinationService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");
    private static final String MODULE_DAILY_DESTINATION = "m024";

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
    public Result<List<DailyDestination>> list(@RequestParam(required = false) String userId,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_DAILY_DESTINATION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        List<DailyDestination> list;
        if (userId != null && !userId.isEmpty()) {
            list = dailyDestinationService.findByUserId(userId);
        } else {
            list = dailyDestinationService.findAll();
        }
        fillUserNames(list);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<DailyDestination> getById(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_DAILY_DESTINATION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        DailyDestination destination = dailyDestinationService.findById(id);
        if (destination == null) {
            return Result.error("去向记录不存在");
        }
        fillUserName(destination);
        return Result.success(destination);
    }

    @PostMapping("/save")
    public Result<DailyDestination> save(@RequestBody DailyDestination dailyDestination, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_DAILY_DESTINATION, p -> Boolean.TRUE.equals(p.getCanAdd()))) {
            return forbidden();
        }
        try {
            // 验证必填字段
            if (dailyDestination.getDestinationTime() == null) {
                return Result.error("时间不能为空");
            }
            if (dailyDestination.getActivityName() == null || dailyDestination.getActivityName().trim().isEmpty()) {
                return Result.error("营销活动名称不能为空");
            }
            if (dailyDestination.getLocation() == null || dailyDestination.getLocation().trim().isEmpty()) {
                return Result.error("地点不能为空");
            }

            // 如果userId为空，从request中获取当前用户ID
            if (dailyDestination.getUserId() == null || dailyDestination.getUserId().isEmpty()) {
                String username = (String) request.getAttribute("username");
                if (username != null) {
                    userService.findByUsername(username).ifPresent(user -> {
                        dailyDestination.setUserId(user.getId());
                    });
                }
            }
            // 验证userId是否已设置
            if (dailyDestination.getUserId() == null || dailyDestination.getUserId().isEmpty()) {
                return Result.error("无法获取用户信息，请重新登录");
            }
            // 新增时，确保id为空，让JPA自动生成
            if (dailyDestination.getId() != null && dailyDestination.getId().isEmpty()) {
                dailyDestination.setId(null);
            }
            DailyDestination saved = dailyDestinationService.save(dailyDestination);
            return Result.success("保存成功", saved);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<DailyDestination> update(@RequestBody DailyDestination dailyDestination, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_DAILY_DESTINATION, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        if (dailyDestination.getId() == null) {
            return Result.error("ID不能为空");
        }
        try {
            // 非管理员只能修改自己的去向记录
            if (!isAdmin(request)) {
                DailyDestination existing = dailyDestinationService.findById(dailyDestination.getId());
                if (existing == null) {
                    return Result.error("去向记录不存在");
                }
                String username = (String) request.getAttribute("username");
                String currentUserId = null;
                if (username != null) {
                    currentUserId = userService.findByUsername(username)
                            .map(u -> u.getId())
                            .orElse(null);
                }
                if (currentUserId == null || !currentUserId.equals(existing.getUserId())) {
                    return forbidden();
                }
                // 确保不会通过前端伪造 userId 篡改归属
                dailyDestination.setUserId(existing.getUserId());
            }
            DailyDestination saved = dailyDestinationService.save(dailyDestination);
            return Result.success("更新成功", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_DAILY_DESTINATION, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            // 非管理员只能删除自己的去向记录
            if (!isAdmin(request)) {
                DailyDestination existing = dailyDestinationService.findById(id);
                if (existing == null) {
                    return Result.error("去向记录不存在");
                }
                String username = (String) request.getAttribute("username");
                String currentUserId = null;
                if (username != null) {
                    currentUserId = userService.findByUsername(username)
                            .map(u -> u.getId())
                            .orElse(null);
                }
                if (currentUserId == null || !currentUserId.equals(existing.getUserId())) {
                    return forbidden();
                }
            }
            dailyDestinationService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response, @RequestParam(required = false) String userId,
            HttpServletRequest request) {
        if (!hasPermission(request, MODULE_DAILY_DESTINATION, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            List<DailyDestination> destinations;
            if (userId != null && !userId.isEmpty()) {
                destinations = dailyDestinationService.findByUserId(userId);
            } else {
                destinations = dailyDestinationService.findAll();
            }

            List<String> headers = new ArrayList<>();
            headers.add("时间");
            headers.add("营销活动名称");
            headers.add("地点");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

            List<List<Object>> data = new ArrayList<>();
            for (DailyDestination destination : destinations) {
                List<Object> row = new ArrayList<>();
                row.add(destination.getDestinationTime().format(formatter));
                row.add(destination.getActivityName());
                row.add(destination.getLocation());
                data.add(row);
            }

            Workbook workbook = ExcelUtil.createWorkbook(headers, data);
            ExcelUtil.exportToResponse(response, workbook, "去向管理列表.xlsx");
        } catch (Exception e) {
            throw new RuntimeException("导出失败：" + e.getMessage(), e);
        }
    }

    /** 为去向记录填充填报人姓名，便于前端展示 */
    private void fillUserNames(List<DailyDestination> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        // 收集本次涉及到的用户ID，一次性查询，避免 N+1 也避免查无关用户
        java.util.Set<String> userIds = new java.util.HashSet<>();
        for (DailyDestination d : list) {
            if (d.getUserId() != null && !d.getUserId().isEmpty()) {
                userIds.add(d.getUserId());
            }
        }
        List<com.it.quanxianguanli.entity.SysUser> users = userService.findByIds(userIds);
        java.util.Map<String, String> idNameMap = new java.util.HashMap<>();
        for (com.it.quanxianguanli.entity.SysUser u : users) {
            idNameMap.put(u.getId(), u.getName());
        }
        for (DailyDestination d : list) {
            d.setUserName(idNameMap.get(d.getUserId()));
        }
    }

    private void fillUserName(DailyDestination destination) {
        if (destination == null) {
            return;
        }
        userService.findById(destination.getUserId())
                .ifPresent(u -> destination.setUserName(u.getName()));
    }
}
