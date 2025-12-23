package com.it.quanxianguanli.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应收账回款登记
 */
@Entity
@Table(name = "receivable_receipt")
public class ReceivableReceipt extends BaseEntity {

    @Column(name = "contract_code", nullable = false, length = 50)
    private String contractCode; // 合同号

    @Column(name = "contract_name", nullable = false, length = 200)
    private String contractName; // 合同名称

    @Column(name = "contract_amount", precision = 15, scale = 2)
    private BigDecimal contractAmount; // 合同总额

    @Column(name = "company_name", length = 200)
    private String companyName; // 公司名称

    @Column(name = "company_code", length = 100)
    private String companyCode; // 公司编号

    @Column(name = "remaining_amount", precision = 15, scale = 2)
    private BigDecimal remainingAmount; // 剩余金额（合同总额-已收）

    @Column(name = "receive_date")
    private LocalDate receiveDate; // 到款时间

    @Column(name = "receive_amount", precision = 15, scale = 2)
    private BigDecimal receiveAmount; // 到款金额

    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

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

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public LocalDate getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(LocalDate receiveDate) {
        this.receiveDate = receiveDate;
    }

    public BigDecimal getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(BigDecimal receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

