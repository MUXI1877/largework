package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SysPermission;
import com.it.quanxianguanli.entity.SysModule;
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
        // 验证必填字段
        if (permission.getRoleId() == null || permission.getRoleId().isEmpty()) {
            throw new IllegalArgumentException("roleId不能为空");
        }
        if (permission.getModuleId() == null || permission.getModuleId().isEmpty()) {
            throw new IllegalArgumentException("moduleId不能为空");
        }
        
        Optional<SysPermission> existing = permissionRepository.findByRoleIdAndModuleId(
                permission.getRoleId(), permission.getModuleId());
        if (existing.isPresent()) {
            SysPermission existingPerm = existing.get();
            existingPerm.setCanRead(permission.getCanRead() != null ? permission.getCanRead() : false);
            existingPerm.setCanAdd(permission.getCanAdd() != null ? permission.getCanAdd() : false);
            existingPerm.setCanUpdate(permission.getCanUpdate() != null ? permission.getCanUpdate() : false);
            existingPerm.setCanSee(permission.getCanSee() != null ? permission.getCanSee() : false);
            return permissionRepository.save(existingPerm);
        }
        
        // 确保布尔字段不为null
        if (permission.getCanRead() == null) permission.setCanRead(false);
        if (permission.getCanAdd() == null) permission.setCanAdd(false);
        if (permission.getCanUpdate() == null) permission.setCanUpdate(false);
        if (permission.getCanSee() == null) permission.setCanSee(false);
        
        return permissionRepository.save(permission);
    }

    /**
     * 批量保存权限，逐条复用单条保存逻辑，保证幂等
     */
    @Transactional
    public void saveAll(List<SysPermission> permissions) {
        for (int i = 0; i < permissions.size(); i++) {
            SysPermission permission = permissions.get(i);
            try {
                save(permission);
            } catch (Exception e) {
                throw new RuntimeException("保存权限记录失败（索引: " + i + ", roleId: " + permission.getRoleId() + ", moduleId: " + permission.getModuleId() + "）: " + e.getMessage(), e);
            }
        }
    }

    @Transactional
    public void deleteById(String id) {
        permissionRepository.deleteById(id);
    }

    /**
     * 获取模块及其所有子模块的ID列表
     */
    public List<String> getModuleAndChildrenIds(String moduleId, SysModuleService moduleService) {
        List<String> moduleIds = new java.util.ArrayList<>();
        moduleIds.add(moduleId);
        
        // 递归获取所有子模块ID
        getChildrenModuleIds(moduleId, moduleService, moduleIds);
        
        return moduleIds;
    }

    /**
     * 递归获取子模块ID
     */
    private void getChildrenModuleIds(String parentId, SysModuleService moduleService, List<String> moduleIds) {
        List<SysModule> children = moduleService.findByParentId(parentId);
        if (children != null && !children.isEmpty()) {
            for (SysModule child : children) {
                moduleIds.add(child.getId());
                getChildrenModuleIds(child.getId(), moduleService, moduleIds);
            }
        }
    }

    /**
     * 批量配置模块及其子节点的权限
     */
    @Transactional
    public void configurePermissionsForModuleAndChildren(String roleId, String moduleId, 
                                                         Boolean canRead, Boolean canAdd, 
                                                         Boolean canUpdate, Boolean canSee,
                                                         SysModuleService moduleService) {
        List<String> moduleIds = getModuleAndChildrenIds(moduleId, moduleService);
        
        for (String modId : moduleIds) {
            SysPermission permission = new SysPermission();
            permission.setRoleId(roleId);
            permission.setModuleId(modId);
            permission.setCanRead(canRead != null ? canRead : false);
            permission.setCanAdd(canAdd != null ? canAdd : false);
            permission.setCanUpdate(canUpdate != null ? canUpdate : false);
            permission.setCanSee(canSee != null ? canSee : false);
            
            save(permission);
        }
    }
}

