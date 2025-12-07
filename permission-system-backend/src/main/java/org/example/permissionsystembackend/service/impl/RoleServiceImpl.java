package org.example.permissionsystembackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.BusinessException;
import org.example.permissionsystembackend.common.ResultCode;
import org.example.permissionsystembackend.dto.RoleDTO;
import org.example.permissionsystembackend.entity.Role;
import org.example.permissionsystembackend.repository.RoleRepository;
import org.example.permissionsystembackend.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务实现类
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(String id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.ROLE_NOT_FOUND));
    }

    @Override
    @Transactional
    public Role createRole(RoleDTO dto) {
        if (roleRepository.existsByRoleName(dto.getRoleName())) {
            throw new BusinessException(ResultCode.ROLE_NAME_EXISTS);
        }

        Role role = new Role();
        BeanUtils.copyProperties(dto, role);
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role updateRole(String id, RoleDTO dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.ROLE_NOT_FOUND));

        // 检查角色名是否与其他角色重复
        if (!role.getRoleName().equals(dto.getRoleName()) &&
                roleRepository.existsByRoleName(dto.getRoleName())) {
            throw new BusinessException(ResultCode.ROLE_NAME_EXISTS);
        }

        role.setRoleName(dto.getRoleName());
        role.setRoleDesc(dto.getRoleDesc());
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new BusinessException(ResultCode.ROLE_NOT_FOUND);
        }
        roleRepository.deleteById(id);
    }
}
