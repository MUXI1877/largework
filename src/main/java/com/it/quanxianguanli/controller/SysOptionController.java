package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SysOption;
import com.it.quanxianguanli.service.SysOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/option")
public class SysOptionController {

    @Autowired
    private SysOptionService optionService;

    @GetMapping("/list")
    public Result<List<SysOption>> list(@RequestParam(required = false) String groupName) {
        if (groupName != null && !groupName.isEmpty()) {
            return Result.success(optionService.findByGroupName(groupName));
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
    public Result<SysOption> save(@RequestBody SysOption option) {
        return Result.success(optionService.save(option));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        optionService.deleteById(id);
        return Result.success("删除成功", null);
    }
}

