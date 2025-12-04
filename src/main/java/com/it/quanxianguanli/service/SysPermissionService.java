package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.repository.SysPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SysPermissionService {

    @Autowired
    private SysPermissionRepository permissionRepository;

    public List<SysPermission> findByRoleId(String roleId) {
        return permissionRepository.findByRoleId(roleId);
    }

    public Optional<SysPermission> findByRoleIdAndModuleId(String roleId, String moduleId) {
        return permissionRepository.findByRoleIdAndModuleId(roleId, moduleId);
    }

    @Transactional
    public SysPermission save(SysPermission permission) {
        Optional<SysPermission> existing = permissionRepository.findByRoleIdAndModuleId(
                permission.getRoleId(), permission.getModuleId());
        if (existing.isPresent()) {
            SysPermission existingPerm = existing.get();
            existingPerm.setCanRead(permission.getCanRead());
            existingPerm.setCanAdd(permission.getCanAdd());
            existingPerm.setCanUpdate(permission.getCanUpdate());
            existingPerm.setCanSee(permission.getCanSee());
            return permissionRepository.save(existingPerm);
        }
        return permissionRepository.save(permission);
    }

    @Transactional
    public void deleteById(String id) {
        permissionRepository.deleteById(id);
    }
}

