package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipt_record")
public class ReceiptRecord extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "receivable_id", nullable = false)
    private Receivable receivable;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;

    @Column(name = "receipt_method", length = 50)
    private String receiptMethod;

    @ManyToOne
    @JoinColumn(name = "operator_id", nullable = false)
    private SysUser operator;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    public Receivable getReceivable() {
        return receivable;
    }

    public void setReceivable(Receivable receivable) {
        this.receivable = receivable;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getReceiptMethod() {
        return receiptMethod;
    }

    public void setReceiptMethod(String receiptMethod) {
        this.receiptMethod = receiptMethod;
    }

    public SysUser getOperator() {
        return operator;
    }

    public void setOperator(SysUser operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}