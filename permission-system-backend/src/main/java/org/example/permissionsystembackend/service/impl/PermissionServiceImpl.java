package org.example.permissionsystembackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.BusinessException;
import org.example.permissionsystembackend.common.ResultCode;
import org.example.permissionsystembackend.dto.PermissionConfigDTO;
import org.example.permissionsystembackend.entity.Module;
import org.example.permissionsystembackend.entity.Permission;
import org.example.permissionsystembackend.entity.Role;
import org.example.permissionsystembackend.repository.ModuleRepository;
import org.example.permissionsystembackend.repository.PermissionRepository;
import org.example.permissionsystembackend.repository.RoleRepository;
import org.example.permissionsystembackend.service.PermissionService;
import org.example.permissionsystembackend.vo.PermissionVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;

    @Override
    public List<PermissionVO> getPermissionsByRole(String roleId) {
        List<Permission> permissions = permissionRepository.findByRoleId(roleId);
        return permissions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void configurePermission(PermissionConfigDTO dto) {
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new BusinessException(ResultCode.ROLE_NOT_FOUND));

        Module module = moduleRepository.findById(dto.getModuleId())
                .orElseThrow(() -> new BusinessException(ResultCode.MODULE_NOT_FOUND));

        Permission permission = permissionRepository
                .findByRoleIdAndModuleId(dto.getRoleId(), dto.getModuleId())
                .orElse(new Permission());

        permission.setRole(role);
        permission.setModule(module);
        permission.setCanRead(dto.getCanRead());
        permission.setCanAdd(dto.getCanAdd());
        permission.setCanUpdate(dto.getCanUpdate());
        permission.setCanDelete(dto.getCanDelete());

        permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void batchConfigurePermissions(String roleId, List<PermissionConfigDTO> dtoList) {
        // 先删除该角色的所有权限配置
        permissionRepository.deleteByRoleId(roleId);

        // 批量保存新的权限配置
        for (PermissionConfigDTO dto : dtoList) {
            dto.setRoleId(roleId);
            configurePermission(dto);
        }
    }

    @Override
    @Transactional
    public void deletePermission(String roleId, String moduleId) {
        Permission permission = permissionRepository
                .findByRoleIdAndModuleId(roleId, moduleId)
                .orElseThrow(() -> new BusinessException(ResultCode.NOT_FOUND));

        permissionRepository.delete(permission);
    }

    private PermissionVO convertToVO(Permission permission) {
        PermissionVO vo = new PermissionVO();
        BeanUtils.copyProperties(permission, vo);
        vo.setRoleId(permission.getRole().getId());
        vo.setRoleName(permission.getRole().getRoleName());
        vo.setModuleId(permission.getModule().getId());
        vo.setModuleName(permission.getModule().getNameCn());
        return vo;
    }
}
