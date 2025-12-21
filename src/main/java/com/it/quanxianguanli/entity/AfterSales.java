package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "after_sales")
public class AfterSales extends BaseEntity {
    
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId; // 客户ID
    
    @Column(name = "contract_code", length = 50)
    private String contractCode; // 合同编号
    
    @Column(name = "contract_name", length = 200)
    private String contractName; // 合同名称
    
    @Column(name = "region", length = 100)
    private String region; // 区域
    
    @Column(name = "after_sales_person", length = 100)
    private String afterSalesPerson; // 售后人员
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAfterSalesPerson() {
        return afterSalesPerson;
    }

    public void setAfterSalesPerson(String afterSalesPerson) {
        this.afterSalesPerson = afterSalesPerson;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

