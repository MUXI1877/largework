package org.example.permissionsystembackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    NOT_FOUND(404, "资源不存在"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),

    // 业务错误码
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    IDCARD_ALREADY_REGISTERED(1003, "身份证号已注册"),
    PHONE_ALREADY_REGISTERED(1004, "手机号已注册"),
    USERNAME_PASSWORD_ERROR(1005, "用户名或密码错误"),
    ACCOUNT_NOT_ACTIVATED(1006, "账号未激活"),
    ROLE_NOT_FOUND(1007, "角色不存在"),
    ROLE_NAME_EXISTS(1008, "角色名已存在"),
    MODULE_NOT_FOUND(1009, "模块不存在"),
    PERMISSION_DENIED(1010, "权限不足");

    private final Integer code;
    private final String message;
}
