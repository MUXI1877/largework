package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "customer_key_person")
public class CustomerKeyPerson extends BaseEntity {
    
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId; // 客户ID
    
    @Column(name = "name", nullable = false, length = 100)
    private String name; // 姓名
    
    @Column(name = "gender", length = 10)
    private String gender; // 性别
    
    @Column(name = "birthday")
    private LocalDate birthday; // 生日
    
    @Column(name = "position", length = 100)
    private String position; // 职位
    
    @Column(name = "contact_info", length = 200)
    private String contactInfo; // 联系方式
    
    @Column(name = "job_type", length = 50)
    private String jobType; // 职务（决策者/部门主管/普通员工）
    
    @Column(name = "direct_supervisor", length = 100)
    private String directSupervisor; // 直接上级
    
    @Column(name = "is_main_contact")
    private Boolean isMainContact; // 是否主要联系人
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getDirectSupervisor() {
        return directSupervisor;
    }

    public void setDirectSupervisor(String directSupervisor) {
        this.directSupervisor = directSupervisor;
    }

    public Boolean getIsMainContact() {
        return isMainContact;
    }

    public void setIsMainContact(Boolean isMainContact) {
        this.isMainContact = isMainContact;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

