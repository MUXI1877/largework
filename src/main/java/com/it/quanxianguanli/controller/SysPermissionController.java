package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.entity.SysModule;
import com.it.quanxianguanli.service.SysPermissionService;
import com.it.quanxianguanli.service.SysModuleService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/permission")
public class SysPermissionController {

    private static final Logger logger = LoggerFactory.getLogger(SysPermissionController.class);

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private SysModuleService moduleService;

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");

    private boolean isAdmin(HttpServletRequest request) {
        Object roleId = request.getAttribute("roleId");
        return roleId != null && ADMIN_ROLES.contains(roleId.toString());
    }

    private <T> Result<T> forbidden() {
        return Result.error(403, "无权限操作");
    }

    @GetMapping("/my")
    public Result<List<SysPermission>> getMyPermissions(HttpServletRequest request) {
        try {
            Object currentRoleId = request.getAttribute("roleId");
            logger.info("获取我的权限 - 当前用户角色: {}", currentRoleId);

            if (currentRoleId == null) {
                logger.warn("获取权限失败 - 用户未登录");
                return Result.error(401, "未登录");
            }

            List<SysPermission> permissions = permissionService.findByRoleId(currentRoleId.toString());
            logger.info("获取权限成功 - 角色: {}, 权限数量: {}", currentRoleId, permissions.size());
            return Result.success(permissions);

        } catch (Exception e) {
            logger.error("获取权限失败 - 系统错误: " + e.getMessage(), e);
            return Result.error(500, "获取权限失败: " + e.getMessage());
        }
    }

    @GetMapping("/role/{roleId}")
    public Result<List<SysPermission>> getRolePermissions(@PathVariable String roleId, HttpServletRequest request) {
        try {
            Object currentRoleId = request.getAttribute("roleId");
            logger.info("获取角色权限 - 当前用户角色: {}, 目标角色: {}", currentRoleId, roleId);

            if (currentRoleId == null) {
                logger.warn("获取权限失败 - 用户未登录");
                return Result.error(401, "未登录");
            }

            // 管理员可以查看任何角色的权限，普通用户只能查看自己的权限
            if (!isAdmin(request) && !currentRoleId.toString().equals(roleId)) {
                logger.warn("获取权限失败 - 权限不足: 当前角色={}, 目标角色={}", currentRoleId, roleId);
                return forbidden();
            }

            List<SysPermission> permissions = permissionService.findByRoleId(roleId);
            logger.info("获取权限成功 - 角色: {}, 权限数量: {}", roleId, permissions.size());
            return Result.success(permissions);

        } catch (Exception e) {
            logger.error("获取权限失败 - 系统错误: " + e.getMessage(), e);
            return Result.error(500, "获取权限失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    public Result<SysPermission> save(@RequestBody SysPermission permission, HttpServletRequest request) {
        try {
            Object currentRoleId = request.getAttribute("roleId");
            logger.info("保存权限 - 当前用户角色: {}, 请求权限数据: {}", currentRoleId, permission);

            if (currentRoleId == null) {
                logger.warn("保存权限失败 - 用户未登录");
                return Result.error(401, "未登录");
            }

            // 管理员可以配置任何角色的权限，普通用户只能配置自己的权限
            if (!isAdmin(request) && !currentRoleId.toString().equals(permission.getRoleId())) {
                logger.warn("保存权限失败 - 权限不足: 当前角色={}, 目标角色={}", currentRoleId, permission.getRoleId());
                return forbidden();
            }

            // ✅ 验证必填字段
            if (permission.getRoleId() == null || permission.getModuleId() == null) {
                logger.error("保存权限失败 - 缺少必填字段: roleId={}, moduleId={}",
                        permission.getRoleId(), permission.getModuleId());
                return Result.error(400, "缺少必填字段：roleId和moduleId");
            }

            // ✅ 验证布尔字段不为null
            if (permission.getCanRead() == null) permission.setCanRead(false);
            if (permission.getCanAdd() == null) permission.setCanAdd(false);
            if (permission.getCanUpdate() == null) permission.setCanUpdate(false);
            if (permission.getCanSee() == null) permission.setCanSee(false);

            SysPermission result = permissionService.save(permission);
            logger.info("保存权限成功 - ID: {}", result.getId());
            return Result.success(result);

        } catch (Exception e) {
            logger.error("保存权限失败 - 系统错误: " + e.getMessage(), e);
            return Result.error(500, "保存权限失败: " + e.getMessage());
        }
    }

    @PostMapping("/bulk-save")
    public Result<Void> bulkSave(@RequestBody List<SysPermission> permissions, HttpServletRequest request) {
        try {
            Object currentRoleId = request.getAttribute("roleId");
            logger.info("批量保存权限 - 当前用户角色: {}, 请求数量: {}", currentRoleId, permissions.size());

            if (currentRoleId == null) {
                logger.warn("批量保存权限失败 - 用户未登录");
                return Result.error(401, "未登录");
            }

            if (!isAdmin(request)) {
                boolean invalid = permissions.stream().anyMatch(p ->
                        p.getRoleId() != null && !p.getRoleId().equals(currentRoleId.toString()));
                if (invalid) {
                    logger.warn("批量保存权限失败 - 权限不足: 当前角色={}", currentRoleId);
                    return forbidden();
                }
            }

            permissions.forEach(p -> {
                // 若未传roleId，默认使用当前用户角色，避免外键约束报错
                if (p.getRoleId() == null || p.getRoleId().isEmpty()) {
                    p.setRoleId(currentRoleId.toString());
                }
                // moduleId 为空直接报错
                if (p.getModuleId() == null || p.getModuleId().isEmpty()) {
                    throw new IllegalArgumentException("缺少必填字段：moduleId");
                }
                if (p.getCanRead() == null) p.setCanRead(false);
                if (p.getCanAdd() == null) p.setCanAdd(false);
                if (p.getCanUpdate() == null) p.setCanUpdate(false);
                if (p.getCanSee() == null) p.setCanSee(false);
            });

            permissionService.saveAll(permissions);
            logger.info("批量保存权限成功");
            return Result.success("保存成功", null);
        } catch (Exception e) {
            logger.error("批量保存权限失败 - 系统错误: " + e.getMessage(), e);
            return Result.error(500, "批量保存权限失败: " + e.getMessage());
        }
    }

    /**
     * 配置模块及其所有子节点的权限
     */
    @PostMapping("/configure-children")
    public Result<Void> configurePermissionsForChildren(@RequestBody PermissionConfigRequest request, HttpServletRequest httpRequest) {
        try {
            Object currentRoleId = httpRequest.getAttribute("roleId");
            logger.info("配置子节点权限 - 当前用户角色: {}, 请求数据: {}", currentRoleId, request);

            if (currentRoleId == null) {
                logger.warn("配置子节点权限失败 - 用户未登录");
                return Result.error(401, "未登录");
            }

            // 管理员可以配置任何角色的权限，普通用户只能配置自己的权限
            if (!isAdmin(httpRequest) && !currentRoleId.toString().equals(request.getRoleId())) {
                logger.warn("配置子节点权限失败 - 权限不足: 当前角色={}, 目标角色={}", currentRoleId, request.getRoleId());
                return forbidden();
            }

            // 验证必填字段
            if (request.getRoleId() == null || request.getModuleId() == null) {
                logger.error("配置子节点权限失败 - 缺少必填字段: roleId={}, moduleId={}", 
                        request.getRoleId(), request.getModuleId());
                return Result.error(400, "缺少必填字段：roleId和moduleId");
            }

            // 验证模块是否存在
            SysModule module = moduleService.findById(request.getModuleId());
            if (module == null) {
                logger.error("配置子节点权限失败 - 模块不存在: moduleId={}", request.getModuleId());
                return Result.error(400, "模块不存在");
            }

            permissionService.configurePermissionsForModuleAndChildren(
                    request.getRoleId(), 
                    request.getModuleId(), 
                    request.getCanRead(), 
                    request.getCanAdd(), 
                    request.getCanUpdate(), 
                    request.getCanSee(),
                    moduleService
            );

            logger.info("配置子节点权限成功 - 角色: {}, 模块: {}, 影响模块数量: {}", 
                    request.getRoleId(), request.getModuleId(), 
                    permissionService.getModuleAndChildrenIds(request.getModuleId(), moduleService).size());
            
            return Result.success("权限配置成功", null);

        } catch (Exception e) {
            logger.error("配置子节点权限失败 - 系统错误: " + e.getMessage(), e);
            return Result.error(500, "配置子节点权限失败: " + e.getMessage());
        }
    }

    /**
     * 权限配置请求DTO
     */
    public static class PermissionConfigRequest {
        private String roleId;
        private String moduleId;
        private Boolean canRead;
        private Boolean canAdd;
        private Boolean canUpdate;
        private Boolean canSee;

        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }
        public String getModuleId() { return moduleId; }
        public void setModuleId(String moduleId) { this.moduleId = moduleId; }
        public Boolean getCanRead() { return canRead; }
        public void setCanRead(Boolean canRead) { this.canRead = canRead; }
        public Boolean getCanAdd() { return canAdd; }
        public void setCanAdd(Boolean canAdd) { this.canAdd = canAdd; }
        public Boolean getCanUpdate() { return canUpdate; }
        public void setCanUpdate(Boolean canUpdate) { this.canUpdate = canUpdate; }
        public Boolean getCanSee() { return canSee; }
        public void setCanSee(Boolean canSee) { this.canSee = canSee; }
    }
}