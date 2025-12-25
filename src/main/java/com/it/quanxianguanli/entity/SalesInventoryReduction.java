package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales_inventory_reduction")
public class SalesInventoryReduction extends BaseEntity {

  @Column(name = "reduction_no", nullable = false, length = 50, unique = true)
  private String reductionNo;

  @Column(name = "reduction_date", nullable = false)
  private LocalDate reductionDate;

  @Column(name = "customer_id", length = 36)
  private String customerId;

  @Column(name = "sales_person_id", length = 36)
  private String salesPersonId;

  @Column(name = "total_amount", precision = 18, scale = 2)
  private BigDecimal totalAmount;

  @Column(length = 20)
  private String status;

  @Column(length = 500)
  private String remark;

  public String getReductionNo() {
    return reductionNo;
  }

  public void setReductionNo(String reductionNo) {
    this.reductionNo = reductionNo;
  }

  public LocalDate getReductionDate() {
    return reductionDate;
  }

  public void setReductionDate(LocalDate reductionDate) {
    this.reductionDate = reductionDate;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getSalesPersonId() {
    return salesPersonId;
  }

  public void setSalesPersonId(String salesPersonId) {
    this.salesPersonId = salesPersonId;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}

