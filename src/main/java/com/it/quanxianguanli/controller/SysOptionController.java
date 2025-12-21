package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SysOption;
import com.it.quanxianguanli.service.SysOptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/option")
public class SysOptionController {

    @Autowired
    private SysOptionService optionService;

    private static final Set<String> ADMIN_ROLES = Set.of("r001", "r002");

    private boolean isAdmin(HttpServletRequest request) {
        Object roleId = request.getAttribute("roleId");
        return roleId != null && ADMIN_ROLES.contains(roleId.toString());
    }

    private <T> Result<T> forbidden() {
        return Result.error(403, "无权限操作");
    }

    @GetMapping("/list")
    public Result<List<SysOption>> list(@RequestParam(required = false) String group) {
        if (group != null && !group.isEmpty()) {
            return Result.success(optionService.findByGroup(group));
        }
        return Result.success(optionService.findAll());
    }

    @GetMapping("/{id}")
    public Result<SysOption> getById(@PathVariable String id) {
        SysOption option = optionService.findById(id);
        if (option == null) {
            return Result.error("选项不存在");
        }
        return Result.success(option);
    }

    @PostMapping("/save")
    public Result<SysOption> save(@RequestBody SysOption option, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return forbidden();
        }
        return Result.success(optionService.save(option));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return forbidden();
        }
        optionService.deleteById(id);
        return Result.success("删除成功", null);
    }
}
