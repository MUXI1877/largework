package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "opportunity_tracking")
public class OpportunityTracking extends BaseEntity {
    
    @Column(name = "opportunity_id", nullable = false, length = 36)
    private String opportunityId; // 销售机会ID
    
    @Column(name = "opportunity_name", length = 200)
    private String opportunityName; // 机会名称
    
    @Column(name = "stage", length = 50)
    private String stage; // 阶段
    
    @Column(name = "customer_id", length = 36)
    private String customerId; // 客户ID
    
    @Column(name = "customer_name", length = 200)
    private String customerName; // 客户名称
    
    @Column(name = "budget", precision = 15, scale = 2)
    private java.math.BigDecimal budget; // 预算
    
    @Column(name = "industry", length = 100)
    private String industry; // 行业
    
    @Column(name = "source", length = 100)
    private String source; // 线索来源
    
    @Column(name = "opportunity_date")
    private java.time.LocalDate opportunityDate; // 销售机会日期
    
    @Column(name = "matter", length = 500)
    private String matter; // 事宜
    
    @Column(name = "visit_time")
    private LocalDateTime visitTime; // 拜访时间
    
    @Column(name = "visitor", length = 100)
    private String visitor; // 拜访人
    
    @Column(name = "description", length = 2000)
    private String description; // 情况说明
    
    @Column(name = "attachment", length = 500)
    private String attachment; // 附件路径
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
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

    public java.math.BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(java.math.BigDecimal budget) {
        this.budget = budget;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public java.time.LocalDate getOpportunityDate() {
        return opportunityDate;
    }

    public void setOpportunityDate(java.time.LocalDate opportunityDate) {
        this.opportunityDate = opportunityDate;
    }

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    public LocalDateTime getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(LocalDateTime visitTime) {
        this.visitTime = visitTime;
    }

    public String getVisitor() {
        return visitor;
    }

    public void setVisitor(String visitor) {
        this.visitor = visitor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

