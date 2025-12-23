package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "opportunity_key_person")
public class OpportunityKeyPerson extends BaseEntity {
    
    @Column(name = "opportunity_id", nullable = false, length = 36)
    private String opportunityId; // 销售机会ID
    
    @Column(name = "contact_code", length = 50)
    private String contactCode; // 联系人编码
    
    @Column(name = "contact_name", length = 100)
    private String contactName; // 联系人名称
    
    @Column(name = "title", length = 50)
    private String title; // 称呼
    
    @Column(name = "position", length = 100)
    private String position; // 职务
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getContactCode() {
        return contactCode;
    }

    public void setContactCode(String contactCode) {
        this.contactCode = contactCode;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

