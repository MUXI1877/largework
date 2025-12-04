package com.it.quanxianguanli.service;

import com.it.quanxianguanli.entity.SysRole;
import com.it.quanxianguanli.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SysRoleService {

    @Autowired
    private SysRoleRepository roleRepository;

    public List<SysRole> findAll() {
        return roleRepository.findAll();
    }

    public Optional<SysRole> findById(String id) {
        return roleRepository.findById(id);
    }

    @Transactional
    public SysRole save(SysRole role) {
        if (role.getId() == null) {
            // 新增时检查唯一性
            if (roleRepository.findByRoleName(role.getRoleName()).isPresent()) {
                throw new RuntimeException("角色名已存在");
            }
        } else {
            // 更新时检查唯一性（排除自己）
            Optional<SysRole> existing = roleRepository.findByRoleName(role.getRoleName());
            if (existing.isPresent() && !existing.get().getId().equals(role.getId())) {
                throw new RuntimeException("角色名已存在");
            }
        }
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteById(String id) {
        roleRepository.deleteById(id);
    }
}

