package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser, String> {
    Optional<SysUser> findByIdCard(String idCard);
    Optional<SysUser> findByPhone(String phone);
    Optional<SysUser> findByUsername(String username);
    List<SysUser> findByRoleId(String roleId);
}

