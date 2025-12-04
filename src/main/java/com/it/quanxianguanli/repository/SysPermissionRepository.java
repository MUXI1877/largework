package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, String> {
    Optional<SysPermission> findByRoleIdAndModuleId(String roleId, String moduleId);
    List<SysPermission> findByRoleId(String roleId);
    List<SysPermission> findByModuleId(String moduleId);
}

