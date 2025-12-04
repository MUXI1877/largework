package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sys_role")
public class SysRole extends BaseEntity {
    
    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;
    
    @Column(name = "role_desc")
    private String roleDesc;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
}

