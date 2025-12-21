package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "after_sale_exp")
public class AfterSaleExp extends BaseEntity {

  @Column(name = "device_model", length = 100)
  private String deviceModel;

  @Column(length = 500)
  private String problem;

  @Column(length = 1000)
  private String solution;

  public String getDeviceModel() {
    return deviceModel;
  }

  public void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  public String getProblem() {
    return problem;
  }

  public void setProblem(String problem) {
    this.problem = problem;
  }

  public String getSolution() {
    return solution;
  }

  public void setSolution(String solution) {
    this.solution = solution;
  }
}

