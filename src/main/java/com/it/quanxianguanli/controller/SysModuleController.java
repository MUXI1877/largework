package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.dto.TreeNode;
import com.it.quanxianguanli.entity.SysModule;
import com.it.quanxianguanli.service.SysModuleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/module")
public class SysModuleController {

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

    @GetMapping("/list")
    public Result<List<SysModule>> list() {
        return Result.success(moduleService.findAll());
    }

    @GetMapping("/tree")
    public Result<List<TreeNode>> tree() {
        return Result.success(moduleService.findTree());
    }
    @GetMapping("/children/{parentId}")
    public Result<List<SysModule>> getChildren(@PathVariable String parentId) {
        if ("null".equals(parentId) || "".equals(parentId)) {
            return Result.success(moduleService.findByParentId(null));
        }
        return Result.success(moduleService.findByParentId(parentId));
    }

    @GetMapping("/{id}")
    public Result<SysModule> getById(@PathVariable String id) {
        SysModule module = moduleService.findById(id);
        if (module == null) {
            return Result.error("模块不存在");
        }
        return Result.success(module);
    }

    @PostMapping("/save")
    public Result<SysModule> save(@RequestBody SysModule module, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return forbidden();
        }
        try {
            return Result.success(moduleService.save(module));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return forbidden();
        }
        try {
            moduleService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}