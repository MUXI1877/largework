package com.it.quanxianguanli.dto;

import java.time.LocalDate;

public class SalesPersonDTO {
    private String id;
    private String employeeCode;
    private String name;
    private String gender;
    private LocalDate birthday;
    private String contactInfo;
    private String regionId;
    private String regionName; // 新增片区名称字段
    private String position;
    private String department;
    private String responsibleArea;
    private String remarks;
    private String erpSyncId;

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public String getRegionId() { return regionId; }
    public void setRegionId(String regionId) { this.regionId = regionId; }
    public String getRegionName() { return regionName; }
    public void setRegionName(String regionName) { this.regionName = regionName; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getResponsibleArea() { return responsibleArea; }
    public void setResponsibleArea(String responsibleArea) { this.responsibleArea = responsibleArea; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public String getErpSyncId() { return erpSyncId; }
    public void setErpSyncId(String erpSyncId) { this.erpSyncId = erpSyncId; }
}