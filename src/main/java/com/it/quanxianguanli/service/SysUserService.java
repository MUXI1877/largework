package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SysUser;
import com.it.quanxianguanli.repository.SysUserRepository;
import com.it.quanxianguanli.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SysUserService {

    @Autowired
    private SysUserRepository userRepository;

    public Optional<SysUser> verifyIdCard(String idCard) {
        return userRepository.findByIdCard(idCard);
    }

    @Transactional
    public SysUser register(SysUser user) {
        if (userRepository.findByIdCard(user.getIdCard()).isPresent()) {
            throw new RuntimeException("身份证号已注册");
        }
        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            throw new RuntimeException("手机号已注册");
        }
        user.setIsApproved(false);
        return userRepository.save(user);
    }

    @Transactional
    public SysUser approve(String userId) {
        SysUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setUsername(user.getPhone());
        user.setPassword(PasswordUtil.encode("123456"));
        user.setIsApproved(true);
        // 默认分配普通用户角色，避免未分配角色导致无权限
        if (user.getRoleId() == null || user.getRoleId().isEmpty()) {
            user.setRoleId("r003");
        }
        return userRepository.save(user);
    }

    @Transactional
    public void resetPassword(String userId) {
        SysUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setPassword(PasswordUtil.encode("123456"));
        userRepository.save(user);
    }

    public List<SysUser> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public SysUser assignRole(String userId, String roleId) {
        SysUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setRoleId(roleId);
        return userRepository.save(user);
    }

    public Optional<SysUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

