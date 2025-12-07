package org.example.permissionsystembackend.vo;

import lombok.Data;

/**
 * 权限VO
 */
@Data
public class PermissionVO {

    private String id;
    private String roleId;
    private String roleName;
    private String moduleId;
    private String moduleName;
    private Boolean canRead;
    private Boolean canAdd;
    private Boolean canUpdate;
    private Boolean canDelete;
}
