package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "after_sale")
public class AfterSale extends BaseEntity {

  @Column(name = "device_sn", length = 100)
  private String deviceSn;

  @Column(name = "service_type", length = 20)
  private String serviceType;

  @Column(name = "contract_id", length = 36)
  private String contractId;

  @Column(name = "problem_desc", length = 500)
  private String problemDesc;

  @Column(name = "handler_id", length = 36)
  private String handlerId;

  @Column(length = 20)
  private String status;

  public String getDeviceSn() {
    return deviceSn;
  }

  public void setDeviceSn(String deviceSn) {
    this.deviceSn = deviceSn;
  }

  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public String getContractId() {
    return contractId;
  }

  public void setContractId(String contractId) {
    this.contractId = contractId;
  }

  public String getProblemDesc() {
    return problemDesc;
  }

  public void setProblemDesc(String problemDesc) {
    this.problemDesc = problemDesc;
  }

  public String getHandlerId() {
    return handlerId;
  }

  public void setHandlerId(String handlerId) {
    this.handlerId = handlerId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}

