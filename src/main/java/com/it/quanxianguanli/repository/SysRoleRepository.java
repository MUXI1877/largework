package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysRoleRepository extends JpaRepository<SysRole, String> {
    Optional<SysRole> findByRoleName(String roleName);
    Optional<SysRole> findByRoleCode(String roleCode); // 添加这个方法
}