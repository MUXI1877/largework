package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer extends BaseEntity {

  @Column(nullable = false, length = 100)
  private String name;

  @Column(name = "id_card", length = 18)
  private String idCard;

  @Column(length = 11)
  private String phone;

  @Column(length = 50)
  private String region;

  @Column(length = 50)
  private String industry;

  @Column(name = "buyer_type", length = 50)
  private String buyerType;

  @Column(name = "invoice_phone", length = 20)
  private String invoicePhone;

  @Column(name = "invoice_address", length = 200)
  private String invoiceAddress;

  @Column(length = 200)
  private String address;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIdCard() {
    return idCard;
  }

  public void setIdCard(String idCard) {
    this.idCard = idCard;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
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

  public String getBuyerType() {
    return buyerType;
  }

  public void setBuyerType(String buyerType) {
    this.buyerType = buyerType;
  }

  public String getInvoicePhone() {
    return invoicePhone;
  }

  public void setInvoicePhone(String invoicePhone) {
    this.invoicePhone = invoicePhone;
  }

  public String getInvoiceAddress() {
    return invoiceAddress;
  }

  public void setInvoiceAddress(String invoiceAddress) {
    this.invoiceAddress = invoiceAddress;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
