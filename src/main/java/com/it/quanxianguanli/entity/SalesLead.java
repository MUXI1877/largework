package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales_lead")
public class SalesLead extends BaseEntity {

  @Column(name = "customer_name", length = 200)
  private String customerName;

  @Column(length = 50)
  private String contact;

  @Column(precision = 18, scale = 2)
  private BigDecimal amount;

  @Column(name = "expect_date")
  private LocalDate expectDate;

  @Column(length = 20)
  private String status;

  @Column(name = "owner_id", length = 36)
  private String ownerId;

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getContact() {
    return contact;
  }

  public void setContact(String contact) {
    this.contact = contact;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public LocalDate getExpectDate() {
    return expectDate;
  }

  public void setExpectDate(LocalDate expectDate) {
    this.expectDate = expectDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }
}

