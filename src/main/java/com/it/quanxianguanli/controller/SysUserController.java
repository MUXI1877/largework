package com.it.quanxianguanli.controller;

import com.it.quanxianguanli.dto.Result;
import com.it.quanxianguanli.entity.SysUser;
import com.it.quanxianguanli.service.SysUserService;
import com.it.quanxianguanli.util.IdCardUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @PostMapping("/verify")
    public Result<Map<String, String>> verifyIdCard(@RequestBody Map<String, String> request) {
        String idCard = request.get("idCard");
        if (!IdCardUtil.isValid(idCard)) {
            return Result.error("身份证号格式不正确");
        }
        
        return userService.verifyIdCard(idCard)
                .map(user -> {
                    Map<String, String> data = new HashMap<>();
                    data.put("username", user.getUsername());
                    data.put("name", user.getName());
                    return Result.success(data);
                })
                .orElse(Result.success(new HashMap<>()));
    }

    @PostMapping("/register")
    public Result<SysUser> register(@RequestBody SysUser user) {
        try {
            if (!IdCardUtil.isValid(user.getIdCard())) {
                return Result.error("身份证号格式不正确");
            }
            SysUser saved = userService.register(user);
            return Result.success("注册成功，等待管理员审核", saved);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/approve/{userId}")
    public Result<SysUser> approve(@PathVariable String userId) {
        try {
            SysUser user = userService.approve(userId);
            return Result.success("审核通过", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/reset-password/{userId}")
    public Result<Void> resetPassword(@PathVariable String userId) {
        try {
            userService.resetPassword(userId);
            return Result.success("密码已重置为123456", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<java.util.List<SysUser>> list() {
        return Result.success(userService.findAll());
    }

    @PostMapping("/assign-role")
    public Result<SysUser> assignRole(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String roleId = request.get("roleId");
            SysUser user = userService.assignRole(userId, roleId);
            return Result.success("角色分配成功", user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

