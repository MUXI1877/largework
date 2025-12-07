package org.example.permissionsystembackend.service;

import org.example.permissionsystembackend.dto.ModuleDTO;
import org.example.permissionsystembackend.entity.Module;
import org.example.permissionsystembackend.vo.ModuleTreeVO;

import java.util.List;

/**
 * 模块服务接口
 */
public interface ModuleService {

    List<ModuleTreeVO> getModuleTree();

    List<ModuleTreeVO> getModuleTreeByRole(String roleId);

    List<Module> getAllModules();

    Module getModuleById(String id);

    Module createModule(ModuleDTO dto);

    Module updateModule(String id, ModuleDTO dto);

    void deleteModule(String id);
}
