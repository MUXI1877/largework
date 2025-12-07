package org.example.permissionsystembackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.Result;
import org.example.permissionsystembackend.service.UserService;
import org.example.permissionsystembackend.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取待审核用户列表
     */
    @GetMapping("/pending")
    public Result<List<UserVO>> getPendingUsers() {
        List<UserVO> users = userService.getPendingUsers();
        return Result.success(users);
    }

    /**
     * 获取所有用户列表
     */
    @GetMapping
    public Result<List<UserVO>> getAllUsers() {
        List<UserVO> users = userService.getAllUsers();
        return Result.success(users);
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable String id) {
        UserVO user = userService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 激活账号
     */
    @PostMapping("/{id}/activate")
    public Result<Void> activateAccount(@PathVariable String id, @RequestParam String roleId) {
        userService.activateAccount(id, roleId);
        return Result.success("账号激活成功", null);
    }

    /**
     * 重置密码
     */
    @PostMapping("/{id}/reset-password")
    public Result<Void> resetPassword(@PathVariable String id) {
        userService.resetPassword(id);
        return Result.success("密码重置成功", null);
    }

    /**
     * 设置角色
     */
    @PostMapping("/{id}/set-role")
    public Result<Void> setRole(@PathVariable String id, @RequestParam String roleId) {
        userService.setRole(id, roleId);
        return Result.success("角色设置成功", null);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return Result.success("用户删除成功", null);
    }
}
