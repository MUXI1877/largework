package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sales_person")
public class SalesPerson extends BaseEntity {
    
    @Column(name = "employee_code", unique = true, length = 50)
    private String employeeCode; // 工号
    
    @Column(name = "name", nullable = false, length = 100)
    private String name; // 姓名
    
    @Column(name = "gender", length = 10)
    private String gender; // 性别
    
    @Column(name = "birthday")
    private LocalDate birthday; // 生日
    
    @Column(name = "contact_info", length = 200)
    private String contactInfo; // 联系方式
    
    @Column(name = "region_id", length = 36)
    private String regionId; // 所属片区ID
    
    @Column(name = "position", length = 100)
    private String position; // 职务
    
    @Column(name = "department", length = 100)
    private String department; // 所属部门
    
    @Column(name = "responsible_area", length = 200)
    private String responsibleArea; // 当前负责区域
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注
    
    @Column(name = "erp_sync_id", length = 50)
    private String erpSyncId; // ERP系统同步ID，用于与信息化平台系统保持一致

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getResponsibleArea() {
        return responsibleArea;
    }

    public void setResponsibleArea(String responsibleArea) {
        this.responsibleArea = responsibleArea;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getErpSyncId() {
        return erpSyncId;
    }

    public void setErpSyncId(String erpSyncId) {
        this.erpSyncId = erpSyncId;
    }
}

