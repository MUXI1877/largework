package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "equipment_set")
public class EquipmentSet extends BaseEntity {

  @Column(nullable = false, length = 50, unique = true)
  private String code;

  @Column(nullable = false, length = 200)
  private String name;

  @Column(length = 500)
  private String description;

  @Column(name = "total_price", precision = 18, scale = 2)
  private BigDecimal totalPrice;

  @Column(length = 20)
  private String status;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}

