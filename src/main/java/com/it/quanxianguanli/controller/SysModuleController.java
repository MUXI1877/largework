package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SysModule;
import com.it.quanxianguanli.service.SysModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/module")
public class SysModuleController {

    @Autowired
    private SysModuleService moduleService;

    @GetMapping("/list")
    public Result<List<SysModule>> list() {
        return Result.success(moduleService.findAll());
    }

    @GetMapping("/tree")
    public Result<List<SysModule>> tree() {
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
    public Result<SysModule> save(@RequestBody SysModule module) {
        return Result.success(moduleService.save(module));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            moduleService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

