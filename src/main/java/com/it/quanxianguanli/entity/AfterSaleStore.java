package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "after_sale_store")
public class AfterSaleStore extends BaseEntity {

  @Column(name = "store_code", nullable = false, length = 50, unique = true)
  private String storeCode;

  @Column(name = "store_name", nullable = false, length = 200)
  private String storeName;

  @Column(length = 100)
  private String region;

  @Column(length = 500)
  private String address;

  @Column(name = "contact_person", length = 50)
  private String contactPerson;

  @Column(name = "contact_phone", length = 20)
  private String contactPhone;

  @Column(length = 20)
  private String status;

  @Column(length = 500)
  private String remark;

  public String getStoreCode() {
    return storeCode;
  }

  public void setStoreCode(String storeCode) {
    this.storeCode = storeCode;
  }

  public String getStoreName() {
    return storeName;
  }

  public void setStoreName(String storeName) {
    this.storeName = storeName;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public String getContactPhone() {
    return contactPhone;
  }

  public void setContactPhone(String contactPhone) {
    this.contactPhone = contactPhone;
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

