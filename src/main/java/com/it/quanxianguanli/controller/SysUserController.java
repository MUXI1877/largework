package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SysUser;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.SysUserService;
import com.it.quanxianguanli.service.SysPermissionService;
import com.it.quanxianguanli.util.IdCardUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    private static final Set<String> ADMIN_ROLES = new HashSet<String>() {{
        add("r001");
        add("r002");
    }};

    private static final String MODULE_USER = "m002";

    private boolean isAdmin(HttpServletRequest request) {
        Object roleId = request.getAttribute("roleId");
        return roleId != null && ADMIN_ROLES.contains(roleId.toString());
    }

    private boolean hasPermission(HttpServletRequest request, String moduleId,
                                  java.util.function.Predicate<SysPermission> predicate) {
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

    @PostMapping("/verify")
    public Result<Map<String, String>> verifyIdCard(@RequestBody Map<String, String> request) {
        String idCard = request.get("idCard");
        if (!IdCardUtil.isValid(idCard)) {
            return Result.error("身份证号格式不正确");
        }
        
        return userService.verifyIdCard(idCard)
                .map(user -> {
                    Map<String, String> data = new HashMap<>();
                    data.put("username", user.getUsername());
                    data.put("name", user.getName());
                    return Result.success(data);
                })
                .orElse(Result.success(new HashMap<>()));
    }

    @PostMapping("/register")
    public Result<SysUser> register(@RequestBody SysUser user) {
        try {
            if (!IdCardUtil.isValid(user.getIdCard())) {
                return Result.error("身份证号格式不正确");
            }
            SysUser saved = userService.register(user);
            return Result.success("注册成功，等待管理员审核", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/approve/{userId}")
    public Result<SysUser> approve(@PathVariable String userId, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_USER, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            SysUser user = userService.approve(userId);
            return Result.success("审核通过", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reset-password/{userId}")
    public Result<Void> resetPassword(@PathVariable String userId, HttpServletRequest request) {
        if (!hasPermission(request, MODULE_USER, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            userService.resetPassword(userId);
            return Result.success("密码已重置为123456", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<java.util.List<SysUser>> list(HttpServletRequest request) {
        if (!hasPermission(request, MODULE_USER, p -> Boolean.TRUE.equals(p.getCanRead()))) {
            return forbidden();
        }
        return Result.success(userService.findAll());
    }

    @PostMapping("/assign-role")
    public Result<SysUser> assignRole(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        if (!hasPermission(httpRequest, MODULE_USER, p -> Boolean.TRUE.equals(p.getCanUpdate()))) {
            return forbidden();
        }
        try {
            String userId = request.get("userId");
            String roleId = request.get("roleId");
            SysUser user = userService.assignRole(userId, roleId);
            return Result.success("角色分配成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

