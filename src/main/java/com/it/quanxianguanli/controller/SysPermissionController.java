package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
public class SysPermissionController {

    @Autowired
    private SysPermissionService permissionService;

    @GetMapping("/role/{roleId}")
    public Result<List<SysPermission>> getByRoleId(@PathVariable String roleId) {
        return Result.success(permissionService.findByRoleId(roleId));
    }

    @PostMapping("/save")
    public Result<SysPermission> save(@RequestBody SysPermission permission) {
        return Result.success(permissionService.save(permission));
    }
}

