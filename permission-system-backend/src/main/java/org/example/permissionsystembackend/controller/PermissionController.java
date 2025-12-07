package org.example.permissionsystembackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.Result;
import org.example.permissionsystembackend.dto.PermissionConfigDTO;
import org.example.permissionsystembackend.service.PermissionService;
import org.example.permissionsystembackend.vo.PermissionVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器
 */
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 获取角色的权限配置
     */
    @GetMapping("/role/{roleId}")
    public Result<List<PermissionVO>> getPermissionsByRole(@PathVariable String roleId) {
        List<PermissionVO> permissions = permissionService.getPermissionsByRole(roleId);
        return Result.success(permissions);
    }

    /**
     * 配置权限
     */
    @PostMapping("/config")
    public Result<Void> configurePermission(@Valid @RequestBody PermissionConfigDTO dto) {
        permissionService.configurePermission(dto);
        return Result.success("权限配置成功", null);
    }

    /**
     * 批量配置权限
     */
    @PostMapping("/batch-config/{roleId}")
    public Result<Void> batchConfigurePermissions(
            @PathVariable String roleId,
            @RequestBody List<PermissionConfigDTO> dtoList) {
        permissionService.batchConfigurePermissions(roleId, dtoList);
        return Result.success("权限批量配置成功", null);
    }

    /**
     * 删除权限配置
     */
    @DeleteMapping("/role/{roleId}/module/{moduleId}")
    public Result<Void> deletePermission(@PathVariable String roleId, @PathVariable String moduleId) {
        permissionService.deletePermission(roleId, moduleId);
        return Result.success("权限删除成功", null);
    }
}
