package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_visit")
public class CustomerVisit extends BaseEntity {

  @Column(name = "unit_name", length = 200)
  private String unitName;

  @Column(length = 50)
  private String region;

  @Column(length = 50)
  private String industry;

  @Column(length = 200)
  private String address;

  @Column(length = 50)
  private String city;

  public String getUnitName() {
    return unitName;
  }

  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}

