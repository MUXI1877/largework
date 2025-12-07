package org.example.permissionsystembackend.repository;

import org.example.permissionsystembackend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限Repository
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

    List<Permission> findByRoleId(String roleId);

    Optional<Permission> findByRoleIdAndModuleId(String roleId, String moduleId);

    @Query("SELECT p FROM Permission p WHERE p.role.id = :roleId AND p.canRead = true")
    List<Permission> findReadablePermissionsByRoleId(String roleId);

    void deleteByRoleId(String roleId);

    void deleteByModuleId(String moduleId);
}
