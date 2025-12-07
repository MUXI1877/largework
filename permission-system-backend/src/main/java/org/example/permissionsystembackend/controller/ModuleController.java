package org.example.permissionsystembackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.Result;
import org.example.permissionsystembackend.dto.ModuleDTO;
import org.example.permissionsystembackend.entity.Module;
import org.example.permissionsystembackend.service.ModuleService;
import org.example.permissionsystembackend.vo.ModuleTreeVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模块控制器
 */
@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    /**
     * 获取模块树
     */
    @GetMapping("/tree")
    public Result<List<ModuleTreeVO>> getModuleTree() {
        List<ModuleTreeVO> tree = moduleService.getModuleTree();
        return Result.success(tree);
    }

    /**
     * 根据角色获取模块树
     */
    @GetMapping("/tree/role/{roleId}")
    public Result<List<ModuleTreeVO>> getModuleTreeByRole(@PathVariable String roleId) {
        List<ModuleTreeVO> tree = moduleService.getModuleTreeByRole(roleId);
        return Result.success(tree);
    }

    /**
     * 获取所有模块
     */
    @GetMapping
    public Result<List<Module>> getAllModules() {
        List<Module> modules = moduleService.getAllModules();
        return Result.success(modules);
    }

    /**
     * 根据ID获取模块
     */
    @GetMapping("/{id}")
    public Result<Module> getModuleById(@PathVariable String id) {
        Module module = moduleService.getModuleById(id);
        return Result.success(module);
    }

    /**
     * 创建模块
     */
    @PostMapping
    public Result<Module> createModule(@Valid @RequestBody ModuleDTO dto) {
        Module module = moduleService.createModule(dto);
        return Result.success("模块创建成功", module);
    }

    /**
     * 更新模块
     */
    @PutMapping("/{id}")
    public Result<Module> updateModule(@PathVariable String id, @Valid @RequestBody ModuleDTO dto) {
        Module module = moduleService.updateModule(id, dto);
        return Result.success("模块更新成功", module);
    }

    /**
     * 删除模块
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteModule(@PathVariable String id) {
        moduleService.deleteModule(id);
        return Result.success("模块删除成功", null);
    }
}
