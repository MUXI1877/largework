package org.example.permissionsystembackend.service;

import org.example.permissionsystembackend.dto.PermissionConfigDTO;
import org.example.permissionsystembackend.vo.PermissionVO;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService {

    List<PermissionVO> getPermissionsByRole(String roleId);

    void configurePermission(PermissionConfigDTO dto);

    void batchConfigurePermissions(String roleId, List<PermissionConfigDTO> dtoList);

    void deletePermission(String roleId, String moduleId);
}
