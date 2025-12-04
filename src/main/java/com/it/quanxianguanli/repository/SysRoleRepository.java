package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, String> {
    Optional<SysRole> findByRoleName(String roleName);
}

