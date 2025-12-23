package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bidding_info")
public class BiddingInfo extends BaseEntity {
    
    @Column(name = "bidding_code", length = 50)
    private String biddingCode; // 投标编号
    
    @Column(name = "bidding_name", nullable = false, length = 200)
    private String biddingName; // 投标名称
    
    @Column(name = "bidding_type", length = 50)
    private String biddingType; // 投标类型
    
    @Column(name = "opportunity_id", length = 36)
    private String opportunityId; // 销售机会ID
    
    @Column(name = "opportunity_name", length = 200)
    private String opportunityName; // 机会名称
    
    @Column(name = "opportunity_code", length = 50)
    private String opportunityCode; // 机会编号
    
    @Column(name = "customer_id", length = 36)
    private String customerId; // 投标客户ID
    
    @Column(name = "customer_name", length = 200)
    private String customerName; // 投标客户名称
    
    @Column(name = "project_name", length = 200)
    private String projectName; // 项目名称
    
    @Column(name = "stage", length = 50)
    private String stage; // 阶段
    
    @Column(name = "industry", length = 100)
    private String industry; // 行业
    
    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget; // 预算
    
    @Column(name = "opportunity_date")
    private LocalDate opportunityDate; // 销售机会日期
    
    @Column(name = "source", length = 100)
    private String source; // 线索来源
    
    @Column(name = "region", length = 100)
    private String region; // 片区
    
    @Column(name = "technical_solution", length = 500)
    private String technicalSolution; // 技术方案（附件路径）
    
    @Column(name = "quotation_file", length = 500)
    private String quotationFile; // 报价单（附件路径）
    
    @Column(name = "bidding_status", length = 50)
    private String biddingStatus; // 投标状态：待审批、已审批、已中、未中、流标、废标
    
    @Column(name = "bidding_result", length = 50)
    private String biddingResult; // 投标结果：已中、未中、流标、废标
    
    @Column(name = "bidding_summary", length = 2000)
    private String biddingSummary; // 投标总结
    
    @Column(name = "attachment", length = 500)
    private String attachment; // 附件路径
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getBiddingCode() {
        return biddingCode;
    }

    public void setBiddingCode(String biddingCode) {
        this.biddingCode = biddingCode;
    }

    public String getBiddingName() {
        return biddingName;
    }

    public void setBiddingName(String biddingName) {
        this.biddingName = biddingName;
    }

    public String getBiddingType() {
        return biddingType;
    }

    public void setBiddingType(String biddingType) {
        this.biddingType = biddingType;
    }

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

    public String getOpportunityCode() {
        return opportunityCode;
    }

    public void setOpportunityCode(String opportunityCode) {
        this.opportunityCode = opportunityCode;
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTechnicalSolution() {
        return technicalSolution;
    }

    public void setTechnicalSolution(String technicalSolution) {
        this.technicalSolution = technicalSolution;
    }

    public String getQuotationFile() {
        return quotationFile;
    }

    public void setQuotationFile(String quotationFile) {
        this.quotationFile = quotationFile;
    }

    public String getBiddingStatus() {
        return biddingStatus;
    }

    public void setBiddingStatus(String biddingStatus) {
        this.biddingStatus = biddingStatus;
    }

    public String getBiddingResult() {
        return biddingResult;
    }

    public void setBiddingResult(String biddingResult) {
        this.biddingResult = biddingResult;
    }

    public String getBiddingSummary() {
        return biddingSummary;
    }

    public void setBiddingSummary(String biddingSummary) {
        this.biddingSummary = biddingSummary;
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

