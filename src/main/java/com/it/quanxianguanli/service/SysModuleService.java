package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SysModule;
import com.it.quanxianguanli.repository.SysModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysModuleService {

    @Autowired
    private SysModuleRepository moduleRepository;

    public List<SysModule> findAll() {
        return moduleRepository.findAll();
    }

    public List<SysModule> findTree() {
        List<SysModule> all = moduleRepository.findAll();
        return all.stream()
                .filter(m -> m.getParentId() == null)
                .sorted((a, b) -> {
                    int sortA = a.getSort() != null ? a.getSort() : 0;
                    int sortB = b.getSort() != null ? b.getSort() : 0;
                    return sortA - sortB;
                })
                .collect(Collectors.toList());
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
        if (module.getParentId() != null && !module.getParentId().isEmpty()) {
            SysModule parent = moduleRepository.findById(module.getParentId()).orElse(null);
            if (parent != null) {
                parent.setIsParent(true);
                moduleRepository.save(parent);
            }
        }
        return moduleRepository.save(module);
    }

    @Transactional
    public void deleteById(String id) {
        SysModule module = moduleRepository.findById(id).orElse(null);
        if (module != null) {
            List<SysModule> children = moduleRepository.findByParentIdOrderBySortAsc(id);
            if (!children.isEmpty()) {
                throw new RuntimeException("该模块存在子模块，无法删除");
            }
            if (module.getParentId() != null) {
                List<SysModule> siblings = moduleRepository.findByParentIdOrderBySortAsc(module.getParentId());
                if (siblings.size() == 1) {
                    SysModule parent = moduleRepository.findById(module.getParentId()).orElse(null);
                    if (parent != null) {
                        parent.setIsParent(false);
                        moduleRepository.save(parent);
                    }
                }
            }
        }
        moduleRepository.deleteById(id);
    }
}

