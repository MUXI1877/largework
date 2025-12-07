package org.example.permissionsystembackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.Result;
import org.example.permissionsystembackend.dto.OptionDTO;
import org.example.permissionsystembackend.entity.Option;
import org.example.permissionsystembackend.service.OptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 选项控制器
 */
@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class OptionController {

    private final OptionService optionService;

    /**
     * 获取所有选项
     */
    @GetMapping
    public Result<List<Option>> getAllOptions() {
        List<Option> options = optionService.getAllOptions();
        return Result.success(options);
    }

    /**
     * 按分组获取选项
     */
    @GetMapping("/by-group")
    public Result<Map<String, List<Option>>> getOptionsByGroup() {
        Map<String, List<Option>> options = optionService.getOptionsByGroup();
        return Result.success(options);
    }

    /**
     * 根据分组名获取选项
     */
    @GetMapping("/group/{groupName}")
    public Result<List<Option>> getOptionsByGroupName(@PathVariable String groupName) {
        List<Option> options = optionService.getOptionsByGroupName(groupName);
        return Result.success(options);
    }

    /**
     * 根据ID获取选项
     */
    @GetMapping("/{id}")
    public Result<Option> getOptionById(@PathVariable String id) {
        Option option = optionService.getOptionById(id);
        return Result.success(option);
    }

    /**
     * 创建选项
     */
    @PostMapping
    public Result<Option> createOption(@Valid @RequestBody OptionDTO dto) {
        Option option = optionService.createOption(dto);
        return Result.success("选项创建成功", option);
    }

    /**
     * 更新选项
     */
    @PutMapping("/{id}")
    public Result<Option> updateOption(@PathVariable String id, @Valid @RequestBody OptionDTO dto) {
        Option option = optionService.updateOption(id, dto);
        return Result.success("选项更新成功", option);
    }

    /**
     * 删除选项
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteOption(@PathVariable String id) {
        optionService.deleteOption(id);
        return Result.success("选项删除成功", null);
    }
}
