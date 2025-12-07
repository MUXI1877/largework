package org.example.permissionsystembackend.service;

import org.example.permissionsystembackend.dto.LoginDTO;
import org.example.permissionsystembackend.dto.UserRegisterDTO;
import org.example.permissionsystembackend.vo.LoginVO;
import org.example.permissionsystembackend.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 验证身份证号是否已注册
     */
    Map<String, Object> checkIdCard(String idCard);

    /**
     * 用户注册
     */
    void register(UserRegisterDTO dto);

    /**
     * 用户登录
     */
    LoginVO login(LoginDTO dto);

    /**
     * 获取待审核用户列表
     */
    List<UserVO> getPendingUsers();

    /**
     * 获取所有用户列表
     */
    List<UserVO> getAllUsers();

    /**
     * 根据ID获取用户
     */
    UserVO getUserById(String id);

    /**
     * 激活账号
     */
    void activateAccount(String userId, String roleId);

    /**
     * 重置密码
     */
    void resetPassword(String userId);

    /**
     * 设置角色
     */
    void setRole(String userId, String roleId);

    /**
     * 删除用户
     */
    void deleteUser(String userId);
}
