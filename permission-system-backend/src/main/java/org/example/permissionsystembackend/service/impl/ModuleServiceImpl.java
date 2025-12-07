package org.example.permissionsystembackend.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.permissionsystembackend.common.BusinessException;
import org.example.permissionsystembackend.common.ResultCode;
import org.example.permissionsystembackend.dto.ModuleDTO;
import org.example.permissionsystembackend.entity.Module;
import org.example.permissionsystembackend.entity.Permission;
import org.example.permissionsystembackend.repository.ModuleRepository;
import org.example.permissionsystembackend.repository.PermissionRepository;
import org.example.permissionsystembackend.service.ModuleService;
import org.example.permissionsystembackend.vo.ModuleTreeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模块服务实现类
 */
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<ModuleTreeVO> getModuleTree() {
        List<Module> allModules = moduleRepository.findAllByOrderByLevelAscSortOrderAsc();
        return buildTree(allModules, null);
    }

    @Override
    public List<ModuleTreeVO> getModuleTreeByRole(String roleId) {
        // 获取该角色有读权限的模块
        List<Permission> permissions = permissionRepository.findReadablePermissionsByRoleId(roleId);
        List<String> moduleIds = permissions.stream()
                .map(p -> p.getModule().getId())
                .collect(Collectors.toList());

        List<Module> allModules = moduleRepository.findAllByOrderByLevelAscSortOrderAsc();
        List<Module> accessibleModules = allModules.stream()
                .filter(m -> moduleIds.contains(m.getId()))
                .collect(Collectors.toList());

        return buildTree(accessibleModules, null);
    }

    @Override
    public List<Module> getAllModules() {
        return moduleRepository.findAllByOrderByLevelAscSortOrderAsc();
    }

    @Override
    public Module getModuleById(String id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.MODULE_NOT_FOUND));
    }

    @Override
    @Transactional
    public Module createModule(ModuleDTO dto) {
        Module module = new Module();
        BeanUtils.copyProperties(dto, module);

        // 如果有父模块,更新父模块的isParent标志
        if (dto.getParentId() != null) {
            Module parent = moduleRepository.findById(dto.getParentId()).orElse(null);
            if (parent != null) {
                parent.setIsParent(true);
                moduleRepository.save(parent);
            }
        }

        return moduleRepository.save(module);
    }

    @Override
    @Transactional
    public Module updateModule(String id, ModuleDTO dto) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ResultCode.MODULE_NOT_FOUND));

        module.setNameCn(dto.getNameCn());
        module.setNameEn(dto.getNameEn());
        module.setLevel(dto.getLevel());
        module.setParentId(dto.getParentId());
        module.setSortOrder(dto.getSortOrder());
        module.setUrl(dto.getUrl());
        module.setIcon(dto.getIcon());
        module.setGroupName(dto.getGroupName());
        module.setPermission(dto.getPermission());
        if (dto.getIsParent() != null) {
            module.setIsParent(dto.getIsParent());
        }
        if (dto.getIsExpanded() != null) {
            module.setIsExpanded(dto.getIsExpanded());
        }

        return moduleRepository.save(module);
    }

    @Override
    @Transactional
    public void deleteModule(String id) {
        if (!moduleRepository.existsById(id)) {
            throw new BusinessException(ResultCode.MODULE_NOT_FOUND);
        }

        // 删除相关权限配置
        permissionRepository.deleteByModuleId(id);

        moduleRepository.deleteById(id);
    }

    /**
     * 构建树形结构
     */
    private List<ModuleTreeVO> buildTree(List<Module> modules, String parentId) {
        List<ModuleTreeVO> tree = new ArrayList<>();

        for (Module module : modules) {
            String moduleParentId = module.getParentId();

            // 判断是否为当前父节点的子节点
            if ((parentId == null && moduleParentId == null) ||
                    (parentId != null && parentId.equals(moduleParentId))) {

                ModuleTreeVO vo = new ModuleTreeVO();
                BeanUtils.copyProperties(module, vo);

                // 递归构建子节点
                List<ModuleTreeVO> children = buildTree(modules, module.getId());
                if (!children.isEmpty()) {
                    vo.setChildren(children);
                }

                tree.add(vo);
            }
        }

        return tree;
    }
}
