package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "device_profile")
public class DeviceProfile extends BaseEntity {

  @Column(length = 100)
  private String sn;

  @Column(length = 100)
  private String model;

  @Column(name = "produce_date")
  private LocalDate produceDate;

  @Column(name = "customer_id", length = 36)
  private String customerId;

  public String getSn() {
    return sn;
  }

  public void setSn(String sn) {
    this.sn = sn;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public LocalDate getProduceDate() {
    return produceDate;
  }

  public void setProduceDate(LocalDate produceDate) {
    this.produceDate = produceDate;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
}

