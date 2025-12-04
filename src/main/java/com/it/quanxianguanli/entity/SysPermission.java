package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sys_permission", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"role_id", "module_id"})
})
public class SysPermission extends BaseEntity {
    
    @Column(name = "role_id", nullable = false, length = 36)
    private String roleId;
    
    @Column(name = "module_id", nullable = false, length = 36)
    private String moduleId;
    
    @Column(name = "can_read")
    private Boolean canRead = false;
    
    @Column(name = "can_add")
    private Boolean canAdd = false;
    
    @Column(name = "can_update")
    private Boolean canUpdate = false;
    
    @Column(name = "can_see")
    private Boolean canSee = false;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public Boolean getCanRead() {
        return canRead;
    }

    public void setCanRead(Boolean canRead) {
        this.canRead = canRead;
    }

    public Boolean getCanAdd() {
        return canAdd;
    }

    public void setCanAdd(Boolean canAdd) {
        this.canAdd = canAdd;
    }

    public Boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Boolean getCanSee() {
        return canSee;
    }

    public void setCanSee(Boolean canSee) {
        this.canSee = canSee;
    }
}

