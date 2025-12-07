package org.example.permissionsystembackend.service;

import org.example.permissionsystembackend.dto.RoleDTO;
import org.example.permissionsystembackend.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {

    List<Role> getAllRoles();

    Role getRoleById(String id);

    Role createRole(RoleDTO dto);

    Role updateRole(String id, RoleDTO dto);

    void deleteRole(String id);
}
