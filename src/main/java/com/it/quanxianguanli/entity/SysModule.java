package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sys_module")
public class SysModule extends BaseEntity {

    @Column(name = "cn_name", nullable = false, length = 50)
    private String cnName;

    @Column(name = "en_name", unique = true, length = 50)
    private String enName;

    @Column(name = "menu_level")
    private Integer menuLevel;

    private Integer sort;

    @Column(length = 100)
    private String path;

    @Column(length = 50)
    private String icon;

    @Column(name = "group_name", length = 50)
    private String groupName;

    @Column(name = "permission", length = 100)
    private String permission;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "parent_id", length = 36)
    private String parentId;

    @Column(name = "is_parent")
    private Boolean isParent = false;
    
    @Column(name = "is_expand")
    private Boolean isExpand = false;

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Integer getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(Integer menuLevel) {
        this.menuLevel = menuLevel;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(Boolean isParent) {
        this.isParent = isParent;
    }

    public Boolean getIsExpand() {
        return isExpand;
    }

    public void setIsExpand(Boolean isExpand) {
        this.isExpand = isExpand;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}

