package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SysRole;
import com.it.quanxianguanli.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
public class SysRoleController {

    @Autowired
    private SysRoleService roleService;

    @GetMapping("/list")
    public Result<List<SysRole>> list() {
        return Result.success(roleService.findAll());
    }

    @GetMapping("/{id}")
    public Result<SysRole> getById(@PathVariable String id) {
        return roleService.findById(id)
                .map(Result::success)
                .orElse(Result.error("角色不存在"));
    }

    @PostMapping("/save")
    public Result<SysRole> save(@RequestBody SysRole role) {
        try {
            return Result.success(roleService.save(role));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            roleService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

