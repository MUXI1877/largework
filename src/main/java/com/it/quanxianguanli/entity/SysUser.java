package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity {
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "id_card", unique = true, nullable = false, length = 18)
    private String idCard;
    
    @Column(unique = true, nullable = false, length = 11)
    private String phone;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    private String gender;
    
    @Column(name = "post_id", length = 36)
    private String postId;
    
    @Column(name = "area_id", length = 36)
    private String areaId;
    
    @Column(unique = true)
    private String username;
    
    private String password;
    
    @Column(name = "role_id", length = 36)
    private String roleId;
    
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}

