package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.dto.TreeNode;
import com.it.quanxianguanli.entity.BasicInfo;
import com.it.quanxianguanli.service.BasicInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/basic-info")
public class BasicInfoController {

    @Autowired
    private BasicInfoService basicInfoService;

    @GetMapping("/list")
    public Result<List<BasicInfo>> list() {
        return Result.success(basicInfoService.findAll());
    }

    @GetMapping("/category/{category}")
    public Result<List<BasicInfo>> listByCategory(@PathVariable String category) {
        return Result.success(basicInfoService.findByCategory(category));
    }

    @GetMapping("/category/{category}/enabled")
    public Result<List<BasicInfo>> listEnabledByCategory(@PathVariable String category) {
        return Result.success(basicInfoService.findByCategoryAndEnabled(category));
    }

    @GetMapping("/parent/{parentId}")
    public Result<List<BasicInfo>> listByParentId(@PathVariable String parentId) {
        return Result.success(basicInfoService.findByParentId(parentId));
    }

    @GetMapping("/code/{code}")
    public Result<BasicInfo> getByCode(@PathVariable String code) {
        return basicInfoService.findByCode(code)
                .map(Result::success)
                .orElse(Result.error("基本信息不存在"));
    }

    @GetMapping("/{id}")
    public Result<BasicInfo> getById(@PathVariable String id) {
        return basicInfoService.findById(id)
                .map(Result::success)
                .orElse(Result.error("基本信息不存在"));
    }

    @PostMapping("/save")
    public Result<BasicInfo> save(@RequestBody BasicInfo basicInfo) {
        try {
            return Result.success(basicInfoService.save(basicInfo));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<BasicInfo> update(@RequestBody BasicInfo basicInfo) {
        try {
            return Result.success(basicInfoService.update(basicInfo));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable String id) {
        try {
            basicInfoService.deleteById(id);
            return Result.success("删除成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/toggle-enabled/{id}")
    public Result<Void> toggleEnabled(@PathVariable String id, @RequestParam boolean enabled) {
        try {
            basicInfoService.toggleEnabled(id, enabled);
            return Result.success(enabled ? "已启用" : "已禁用", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/tree/{category}")
    public Result<List<TreeNode>> getCategoryTree(@PathVariable String category) {
        return Result.success(basicInfoService.getCategoryTree(category));
    }

    @GetMapping("/tree/{category}/enabled")
    public Result<List<TreeNode>> getEnabledCategoryTree(@PathVariable String category) {
        return Result.success(basicInfoService.getCategoryTreeByEnabled(category));
    }
}