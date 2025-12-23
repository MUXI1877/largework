package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales_quotation")
public class SalesQuotation extends BaseEntity {
    
    @Column(name = "quotation_code", length = 50)
    private String quotationCode; // 报价编号
    
    @Column(name = "quotation_name", nullable = false, length = 200)
    private String quotationName; // 报价名称
    
    @Column(name = "quotation_type", length = 50)
    private String quotationType; // 报价类型
    
    @Column(name = "opportunity_id", length = 36)
    private String opportunityId; // 销售机会ID
    
    @Column(name = "opportunity_name", length = 200)
    private String opportunityName; // 机会名称
    
    @Column(name = "opportunity_code", length = 50)
    private String opportunityCode; // 机会编号
    
    @Column(name = "stage", length = 50)
    private String stage; // 阶段
    
    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget; // 预算
    
    @Column(name = "industry", length = 100)
    private String industry; // 行业
    
    @Column(name = "customer_id", length = 36)
    private String customerId; // 报价客户ID
    
    @Column(name = "customer_name", length = 200)
    private String customerName; // 报价客户名称
    
    @Column(name = "project_name", length = 200)
    private String projectName; // 项目名称
    
    @Column(name = "total_price", precision = 15, scale = 2)
    private BigDecimal totalPrice; // 总价
    
    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate; // 税率
    
    @Column(name = "competitor_name", length = 200)
    private String competitorName; // 竞争对手名称
    
    @Column(name = "competitor_quotation", precision = 15, scale = 2)
    private BigDecimal competitorQuotation; // 竞争对手报价
    
    @Column(name = "quotation_date")
    private LocalDate quotationDate; // 报价日期
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getQuotationCode() {
        return quotationCode;
    }

    public void setQuotationCode(String quotationCode) {
        this.quotationCode = quotationCode;
    }

    public String getQuotationName() {
        return quotationName;
    }

    public void setQuotationName(String quotationName) {
        this.quotationName = quotationName;
    }

    public String getQuotationType() {
        return quotationType;
    }

    public void setQuotationType(String quotationType) {
        this.quotationType = quotationType;
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

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public String getCompetitorName() {
        return competitorName;
    }

    public void setCompetitorName(String competitorName) {
        this.competitorName = competitorName;
    }

    public BigDecimal getCompetitorQuotation() {
        return competitorQuotation;
    }

    public void setCompetitorQuotation(BigDecimal competitorQuotation) {
        this.competitorQuotation = competitorQuotation;
    }

    public LocalDate getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(LocalDate quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

