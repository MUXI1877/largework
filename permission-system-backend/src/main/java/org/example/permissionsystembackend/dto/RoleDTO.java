package org.example.permissionsystembackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 角色DTO
 */
@Data
public class RoleDTO {

    private String id;

    @NotBlank(message = "角色名不能为空")
    private String roleName;

    private String roleDesc;
}
