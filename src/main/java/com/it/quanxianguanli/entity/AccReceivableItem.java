package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "acc_receivable_item")
public class AccReceivableItem extends BaseEntity {

  @Column(name = "contract_id", length = 36)
  private String contractId;

  @Column(name = "receive_date")
  private LocalDate receiveDate;

  @Column(precision = 18, scale = 2)
  private BigDecimal amount;

  public String getContractId() {
    return contractId;
  }

  public void setContractId(String contractId) {
    this.contractId = contractId;
  }

  public LocalDate getReceiveDate() {
    return receiveDate;
  }

  public void setReceiveDate(LocalDate receiveDate) {
    this.receiveDate = receiveDate;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}

