package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contract")
public class Contract extends BaseEntity {
    
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId; // 客户ID
    
    @Column(name = "contract_code", unique = true, nullable = false, length = 50)
    private String contractCode; // 合同编号
    
    @Column(name = "contract_name", nullable = false, length = 200)
    private String contractName; // 合同名称
    
    // 其他合同相关字段可根据需要扩展

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
}

