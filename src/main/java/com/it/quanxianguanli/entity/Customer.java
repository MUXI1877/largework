package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {
    
    @Column(name = "customer_code", unique = true, nullable = false, length = 50)
    private String customerCode; // 客户编号
    
    @Column(name = "unit_name", unique = true, nullable = false, length = 200)
    private String unitName; // 单位名称
    
    @Column(name = "region", length = 100)
    private String region; // 所属区域
    
    @Column(name = "area", length = 100)
    private String area; // 地区
    
    @Column(name = "industry", length = 100)
    private String industry; // 所属行业
    
    @Column(name = "company_address", length = 500)
    private String companyAddress; // 公司地址
    
    @Column(name = "order_representative", length = 100)
    private String orderRepresentative; // 订货代表
    
    @Column(name = "buyer_attribute", length = 50)
    private String buyerAttribute; // 买方属性
    
    @Column(name = "invoice_phone", length = 20)
    private String invoicePhone; // 开票电话
    
    @Column(name = "contact_phone", length = 20)
    private String contactPhone; // 联系电话
    
    @Column(name = "fax", length = 20)
    private String fax; // 传真
    
    @Column(name = "postal_code", length = 10)
    private String postalCode; // 邮编
    
    @Column(name = "invoice_address", length = 500)
    private String invoiceAddress; // 开票地址
    
    @Column(name = "invoice_bank", length = 200)
    private String invoiceBank; // 开票银行
    
    @Column(name = "bank_account", length = 50)
    private String bankAccount; // 银行账号
    
    @Column(name = "tax_number", length = 50)
    private String taxNumber; // 税号
    
    @Column(name = "arrears_amount", precision = 15, scale = 2)
    private BigDecimal arrearsAmount; // 欠款金额
    
    @Column(name = "credit_level", length = 50)
    private String creditLevel; // 信用等级
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getOrderRepresentative() {
        return orderRepresentative;
    }

    public void setOrderRepresentative(String orderRepresentative) {
        this.orderRepresentative = orderRepresentative;
    }

    public String getBuyerAttribute() {
        return buyerAttribute;
    }

    public void setBuyerAttribute(String buyerAttribute) {
        this.buyerAttribute = buyerAttribute;
    }

    public String getInvoicePhone() {
        return invoicePhone;
    }

    public void setInvoicePhone(String invoicePhone) {
        this.invoicePhone = invoicePhone;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public String getInvoiceBank() {
        return invoiceBank;
    }

    public void setInvoiceBank(String invoiceBank) {
        this.invoiceBank = invoiceBank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public BigDecimal getArrearsAmount() {
        return arrearsAmount;
    }

    public void setArrearsAmount(BigDecimal arrearsAmount) {
        this.arrearsAmount = arrearsAmount;
    }

    public String getCreditLevel() {
        return creditLevel;
    }

    public void setCreditLevel(String creditLevel) {
        this.creditLevel = creditLevel;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

