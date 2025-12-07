package org.example.permissionsystembackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 模块DTO
 */
@Data
public class ModuleDTO {

    private String id;

    @NotBlank(message = "中文名称不能为空")
    private String nameCn;

    @NotBlank(message = "英文名称不能为空")
    private String nameEn;

    @NotNull(message = "菜单级数不能为空")
    private Integer level;

    private String parentId;

    @NotNull(message = "排序序号不能为空")
    private Integer sortOrder;

    private String url;

    private String icon;

    private String groupName;

    private String permission;

    private Boolean isParent;

    private Boolean isExpanded;
}
