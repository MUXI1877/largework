package com.it.quanxianguanli.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应收账计划（合同应收分期计划）
 */
@Entity
@Table(name = "receivable_plan")
public class ReceivablePlan extends BaseEntity {

    @Column(name = "contract_code", nullable = false, length = 50)
    private String contractCode; // 合同号

    @Column(name = "contract_name", nullable = false, length = 200)
    private String contractName; // 合同名称

    @Column(name = "payment_stage", length = 50)
    private String paymentStage; // 付款阶段

    @Column(name = "payment_stage_name", length = 100)
    private String paymentStageName; // 付款阶段名称

    @Column(name = "due_amount", precision = 15, scale = 2)
    private BigDecimal dueAmount; // 应付金额

    @Column(name = "paid_amount", precision = 15, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO; // 已付金额

    @Column(name = "due_date")
    private LocalDate dueDate; // 应付时间

    @Column(name = "paid_date")
    private LocalDate paidDate; // 付款日期（最近一次）

    @Column(name = "owner", length = 100)
    private String owner; // 责任人

    @Column(name = "status", length = 30)
    private String status; // NOT_PAID / PARTIAL / PAID

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

    public String getPaymentStage() {
        return paymentStage;
    }

    public void setPaymentStage(String paymentStage) {
        this.paymentStage = paymentStage;
    }

    public String getPaymentStageName() {
        return paymentStageName;
    }

    public void setPaymentStageName(String paymentStageName) {
        this.paymentStageName = paymentStageName;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal dueAmount) {
        this.dueAmount = dueAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate paidDate) {
        this.paidDate = paidDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

