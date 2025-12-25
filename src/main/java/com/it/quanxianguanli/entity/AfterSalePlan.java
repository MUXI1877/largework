package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "after_sale_plan")
public class AfterSalePlan extends BaseEntity {

  @Column(name = "plan_no", nullable = false, length = 50, unique = true)
  private String planNo;

  @Column(name = "plan_name", nullable = false, length = 200)
  private String planName;

  @Column(name = "device_id", length = 36)
  private String deviceId;

  @Column(name = "customer_id", length = 36)
  private String customerId;

  @Column(name = "store_id", length = 36)
  private String storeId;

  @Column(name = "plan_type", length = 20)
  private String planType;

  @Column(name = "plan_date", nullable = false)
  private LocalDate planDate;

  @Column(name = "executor_id", length = 36)
  private String executorId;

  @Column(length = 20)
  private String status;

  @Column(length = 1000)
  private String content;

  @Column(length = 500)
  private String remark;

  public String getPlanNo() {
    return planNo;
  }

  public void setPlanNo(String planNo) {
    this.planNo = planNo;
  }

  public String getPlanName() {
    return planName;
  }

  public void setPlanName(String planName) {
    this.planName = planName;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getStoreId() {
    return storeId;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public String getPlanType() {
    return planType;
  }

  public void setPlanType(String planType) {
    this.planType = planType;
  }

  public LocalDate getPlanDate() {
    return planDate;
  }

  public void setPlanDate(LocalDate planDate) {
    this.planDate = planDate;
  }

  public String getExecutorId() {
    return executorId;
  }

  public void setExecutorId(String executorId) {
    this.executorId = executorId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}

