package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "acc_receivable_plan")
public class AccReceivablePlan extends BaseEntity {

  @Column(name = "contract_id", length = 36)
  private String contractId;

  @Column
  private Integer stage;

  @Column(name = "plan_date")
  private LocalDate planDate;

  @Column(name = "plan_amount", precision = 18, scale = 2)
  private BigDecimal planAmount;

  public String getContractId() {
    return contractId;
  }

  public void setContractId(String contractId) {
    this.contractId = contractId;
  }

  public Integer getStage() {
    return stage;
  }

  public void setStage(Integer stage) {
    this.stage = stage;
  }

  public LocalDate getPlanDate() {
    return planDate;
  }

  public void setPlanDate(LocalDate planDate) {
    this.planDate = planDate;
  }

  public BigDecimal getPlanAmount() {
    return planAmount;
  }

  public void setPlanAmount(BigDecimal planAmount) {
    this.planAmount = planAmount;
  }
}

