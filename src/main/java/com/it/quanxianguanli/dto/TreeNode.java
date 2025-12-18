package com.it.quanxianguanli.dto;

import com.it.quanxianguanli.entity.SysModule;
import com.it.quanxianguanli.entity.BasicInfo;
import lombok.Data;
import java.util.List;

@Data
public class TreeNode {
    private String id;
    private String cnName;
    private String enName;
    private Integer menuLevel;
    private Integer sort;
    private String path;
    private String icon;
    private String groupName;
    private String permission;
    private Boolean visible;
    private String parentId;
    private Boolean isParent;
    private List<TreeNode> children;

    public TreeNode() {
    }

    public TreeNode(SysModule module) {
        this.id = module.getId();
        this.cnName = module.getCnName();
        this.enName = module.getEnName();
        this.menuLevel = module.getMenuLevel();
        this.sort = module.getSort();
        this.path = module.getPath();
        this.icon = module.getIcon();
        this.groupName = module.getGroupName();
        this.permission = module.getPermission();
        this.visible = module.getVisible();
        this.parentId = module.getParentId();
        this.isParent = module.getIsParent();
    }

    public TreeNode(BasicInfo basicInfo) {
        this.id = basicInfo.getId();
        this.cnName = basicInfo.getName();
        this.enName = basicInfo.getCode();
        this.sort = basicInfo.getSort();
        this.parentId = basicInfo.getParentId();
    }
}