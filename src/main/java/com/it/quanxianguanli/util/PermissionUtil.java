package com.it.quanxianguanli.util;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.SysPermissionService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;
import java.util.function.Predicate;

/**
 * 权限检查工具类
 * 提供通用的权限检查方法，减少控制器中的重复代码
 */
public class PermissionUtil {

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");

    /**
     * 检查是否为管理员
     */
    public static boolean isAdmin(HttpServletRequest request) {
        Object roleId = request.getAttribute("roleId");
        return roleId != null && ADMIN_ROLES.contains(roleId.toString());
    }

    /**
     * 检查是否有指定模块的权限
     */
    public static boolean hasPermission(HttpServletRequest request, String moduleId,
                                       Predicate<SysPermission> predicate,
                                       SysPermissionService permissionService) {
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

    /**
     * 返回无权限错误
     */
    public static <T> Result<T> forbidden() {
        return Result.error(403, "无权限操作");
    }
}

