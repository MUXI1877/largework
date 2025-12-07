package org.example.permissionsystembackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.Result;
import org.example.permissionsystembackend.dto.LoginDTO;
import org.example.permissionsystembackend.dto.UserRegisterDTO;
import org.example.permissionsystembackend.service.UserService;
import org.example.permissionsystembackend.vo.LoginVO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 验证身份证号
     */
    @GetMapping("/check-idcard")
    public Result<Map<String, Object>> checkIdCard(@RequestParam String idCard) {
        Map<String, Object> result = userService.checkIdCard(idCard);
        return Result.success(result);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO dto) {
        userService.register(dto);
        return Result.success("注册成功,请等待管理员审核", null);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        LoginVO loginVO = userService.login(dto);
        return Result.success(loginVO);
    }
}
