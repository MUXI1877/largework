package org.example.permissionsystembackend.repository;

import org.example.permissionsystembackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色Repository
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByRoleName(String roleName);

    boolean existsByRoleName(String roleName);
}
