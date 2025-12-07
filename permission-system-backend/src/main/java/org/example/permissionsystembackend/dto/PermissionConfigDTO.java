package org.example.permissionsystembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 权限配置DTO
 */
@Data
public class PermissionConfigDTO {

    @NotBlank(message = "角色ID不能为空")
    private String roleId;

    @NotBlank(message = "模块ID不能为空")
    private String moduleId;

    @NotNull(message = "可查看权限不能为空")
    private Boolean canRead;

    @NotNull(message = "可新增权限不能为空")
    private Boolean canAdd;

    @NotNull(message = "可修改权限不能为空")
    private Boolean canUpdate;

    @NotNull(message = "可删除权限不能为空")
    private Boolean canDelete;
}
