package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bidding")
public class Bidding extends BaseEntity {

  @Column(name = "bidding_no", nullable = false, length = 50, unique = true)
  private String biddingNo;

  @Column(name = "project_name", nullable = false, length = 200)
  private String projectName;

  @Column(name = "customer_id", length = 36)
  private String customerId;

  @Column(name = "bidding_date", nullable = false)
  private LocalDate biddingDate;

  private LocalDate deadline;

  @Column(name = "total_amount", precision = 18, scale = 2)
  private BigDecimal totalAmount;

  @Column(length = 20)
  private String status;

  @Column(name = "sales_person_id", length = 36)
  private String salesPersonId;

  @Column(length = 500)
  private String remark;

  public String getBiddingNo() {
    return biddingNo;
  }

  public void setBiddingNo(String biddingNo) {
    this.biddingNo = biddingNo;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public LocalDate getBiddingDate() {
    return biddingDate;
  }

  public void setBiddingDate(LocalDate biddingDate) {
    this.biddingDate = biddingDate;
  }

  public LocalDate getDeadline() {
    return deadline;
  }

  public void setDeadline(LocalDate deadline) {
    this.deadline = deadline;
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

  public String getSalesPersonId() {
    return salesPersonId;
  }

  public void setSalesPersonId(String salesPersonId) {
    this.salesPersonId = salesPersonId;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}

