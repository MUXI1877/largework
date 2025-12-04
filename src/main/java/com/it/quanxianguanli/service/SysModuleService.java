package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SysModule;
import com.it.quanxianguanli.repository.SysModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.it.quanxianguanli.dto.TreeNode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysModuleService {

    @Autowired
    private SysModuleRepository moduleRepository;

    public List<SysModule> findAll() {
        return moduleRepository.findAll();
    }


    public List<TreeNode> findTree() {
        List<SysModule> all = moduleRepository.findAll();
        return buildTree(all);
    }

    private List<TreeNode> buildTree(List<SysModule> modules) {
        List<TreeNode> rootNodes = modules.stream()
                .filter(m -> m.getParentId() == null)
                .sorted((a, b) -> {
                    int sortA = a.getSort() != null ? a.getSort() : 0;
                    int sortB = b.getSort() != null ? b.getSort() : 0;
                    return sortA - sortB;
                })
                .map(TreeNode::new)
                .collect(Collectors.toList());

        rootNodes.forEach(root -> setChildren(root, modules));
        return rootNodes;
    }

    private void setChildren(TreeNode parent, List<SysModule> modules) {
        List<TreeNode> children = modules.stream()
                .filter(m -> parent.getId().equals(m.getParentId()))
                .sorted((a, b) -> {
                    int sortA = a.getSort() != null ? a.getSort() : 0;
                    int sortB = b.getSort() != null ? b.getSort() : 0;
                    return sortA - sortB;
                })
                .map(TreeNode::new)
                .collect(Collectors.toList());

        parent.setIsParent(!children.isEmpty());
        parent.setChildren(children);

        // 递归设置子节点的子节点
        children.forEach(child -> setChildren(child, modules));
    }

    public List<SysModule> findByParentId(String parentId) {
        if (parentId == null || parentId.isEmpty()) {
            return moduleRepository.findByParentIdIsNullOrderBySortAsc();
        }
        return moduleRepository.findByParentIdOrderBySortAsc(parentId);
    }

    public SysModule findById(String id) {
        return moduleRepository.findById(id).orElse(null);
    }

    @Transactional
    public SysModule save(SysModule module) {
        // 校验同级中文名称唯一性
        if (module.getCnName() != null && !module.getCnName().trim().isEmpty()) {
            List<SysModule> siblings = moduleRepository.findByParentIdOrderBySortAsc(module.getParentId());
            boolean cnNameExists = siblings.stream()
                    .filter(m -> !m.getId().equals(module.getId()))
                    .anyMatch(m -> module.getCnName().equals(m.getCnName()));
            if (cnNameExists) {
                throw new RuntimeException("同级中文名称已存在");
            }
        }

        // 校验英文名称全局唯一性
        if (module.getEnName() != null && !module.getEnName().trim().isEmpty()) {
            SysModule existing = moduleRepository.findByEnName(module.getEnName());
            if (existing != null && !existing.getId().equals(module.getId())) {
                throw new RuntimeException("英文名称已存在");
            }
        }

        // 自动计算菜单级别
        if (module.getParentId() != null && !module.getParentId().isEmpty()) {
            SysModule parent = moduleRepository.findById(module.getParentId()).orElse(null);
            if (parent != null) {
                parent.setIsParent(true);
                moduleRepository.save(parent);
                module.setMenuLevel((parent.getMenuLevel() != null ? parent.getMenuLevel() : 0) + 1);
            }
        } else {
            module.setMenuLevel(1);
        }

        return moduleRepository.save(module);
    }

    @Transactional
    public void deleteById(String id) {
        SysModule module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new RuntimeException("模块不存在");
        }

        // 检查是否有子节点
        List<SysModule> children = moduleRepository.findByParentIdOrderBySortAsc(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("请先删除下级模块");
        }

        // 检查是否被角色引用（这里需要权限表的查询）
        // TODO: 添加角色引用检查逻辑

        // 更新父节点的isParent状态
        if (module.getParentId() != null) {
            List<SysModule> siblings = moduleRepository.findByParentIdOrderBySortAsc(module.getParentId());
            if (siblings.size() == 1) { // 当前节点是唯一的子节点
                SysModule parent = moduleRepository.findById(module.getParentId()).orElse(null);
                if (parent != null) {
                    parent.setIsParent(false);
                    moduleRepository.save(parent);
                }
            }
        }

        moduleRepository.deleteById(id);
    }

    public SysModule findByEnName(String enName) {
        return moduleRepository.findByEnName(enName);
    }
}