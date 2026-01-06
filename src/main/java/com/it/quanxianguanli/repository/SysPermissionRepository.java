package com.it.quanxianguanli.repository;

import com.it.quanxianguanli.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, String> {
    Optional<SysPermission> findByRoleIdAndModuleId(String roleId, String moduleId);
    List<SysPermission> findByRoleId(String roleId);
    List<SysPermission> findByModuleId(String moduleId);
    @Modifying
    @Query("DELETE FROM SysPermission p WHERE p.moduleId = :moduleId")
    void deleteByModuleId(@Param("moduleId") String moduleId);
}

