package org.example.permissionsystembackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.Result;
import org.example.permissionsystembackend.dto.RoleDTO;
import org.example.permissionsystembackend.entity.Role;
import org.example.permissionsystembackend.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 获取所有角色
     */
    @GetMapping
    public Result<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    /**
     * 根据ID获取角色
     */
    @GetMapping("/{id}")
    public Result<Role> getRoleById(@PathVariable String id) {
        Role role = roleService.getRoleById(id);
        return Result.success(role);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public Result<Role> createRole(@Valid @RequestBody RoleDTO dto) {
        Role role = roleService.createRole(dto);
        return Result.success("角色创建成功", role);
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public Result<Role> updateRole(@PathVariable String id, @Valid @RequestBody RoleDTO dto) {
        Role role = roleService.updateRole(id, dto);
        return Result.success("角色更新成功", role);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return Result.success("角色删除成功", null);
    }
}
