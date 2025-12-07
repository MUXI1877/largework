package org.example.permissionsystembackend.vo;

import lombok.Data;

import java.util.List;

/**
 * 模块树VO
 */
@Data
public class ModuleTreeVO {

    private String id;
    private String nameCn;
    private String nameEn;
    private Integer level;
    private String parentId;
    private Integer sortOrder;
    private String url;
    private String icon;
    private String groupName;
    private String permission;
    private Boolean isParent;
    private Boolean isExpanded;
    private List<ModuleTreeVO> children;
}
