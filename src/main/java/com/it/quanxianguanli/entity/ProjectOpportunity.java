package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "project_opportunity")
public class ProjectOpportunity extends BaseEntity {
    
    @Column(name = "opportunity_name", nullable = false, length = 200)
    private String opportunityName; // 机会名称
    
    @Column(name = "opportunity_code", length = 50)
    private String opportunityCode; // 机会编号
    
    @Column(name = "project_name", nullable = false, length = 200)
    private String projectName; // 项目名称
    
    @Column(name = "stage", length = 50)
    private String stage; // 阶段
    
    @Column(name = "customer_id", length = 36)
    private String customerId; // 客户ID（可为空，支持临时客户）
    
    @Column(name = "customer_name", length = 200)
    private String customerName; // 客户名称（支持临时客户名称）
    
    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget; // 预算
    
    @Column(name = "industry", length = 100)
    private String industry; // 行业
    
    @Column(name = "regions", length = 500)
    private String regions; // 片区（可多选，逗号分隔）
    
    @Column(name = "owner_region_id", length = 36)
    private String ownerRegionId; // 原始所属片区ID（创建机会的片区）
    
    @Column(name = "opportunity_date")
    private LocalDate opportunityDate; // 销售机会日期
    
    @Column(name = "source", length = 100)
    private String source; // 线索来源
    
    @Column(name = "status", length = 50)
    private String status; // 状态：草稿、已提交、已关闭等
    
    @Column(name = "receive_status", length = 50)
    private String receiveStatus; // 接收状态
    
    @Column(name = "inventory", length = 200)
    private String inventory; // 存货
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注
    
    @Column(name = "is_submitted", nullable = false)
    private Boolean isSubmitted = false; // 是否已提交
    
    @Column(name = "creator_id", length = 36)
    private String creatorId; // 创建人ID

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public String getOpportunityCode() {
        return opportunityCode;
    }

    public void setOpportunityCode(String opportunityCode) {
        this.opportunityCode = opportunityCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getRegions() {
        return regions;
    }

    public void setRegions(String regions) {
        this.regions = regions;
    }

    public String getOwnerRegionId() {
        return ownerRegionId;
    }

    public void setOwnerRegionId(String ownerRegionId) {
        this.ownerRegionId = ownerRegionId;
    }

    public LocalDate getOpportunityDate() {
        return opportunityDate;
    }

    public void setOpportunityDate(LocalDate opportunityDate) {
        this.opportunityDate = opportunityDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(Boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}

