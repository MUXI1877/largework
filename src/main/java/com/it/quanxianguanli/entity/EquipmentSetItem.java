package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "equipment_set_item")
public class EquipmentSetItem extends BaseEntity {

  @Column(name = "set_id", nullable = false, length = 36)
  private String setId;

  @Column(name = "device_unit_id", length = 36)
  private String deviceUnitId;

  @Column(name = "spare_parts_id", length = 36)
  private String sparePartsId;

  @Column(nullable = false)
  private Integer quantity;

  @Column(name = "unit_price", precision = 18, scale = 2)
  private BigDecimal unitPrice;

  @Column(name = "total_price", precision = 18, scale = 2)
  private BigDecimal totalPrice;

  @Column(name = "sort_order")
  private Integer sortOrder;

  public String getSetId() {
    return setId;
  }

  public void setSetId(String setId) {
    this.setId = setId;
  }

  public String getDeviceUnitId() {
    return deviceUnitId;
  }

  public void setDeviceUnitId(String deviceUnitId) {
    this.deviceUnitId = deviceUnitId;
  }

  public String getSparePartsId() {
    return sparePartsId;
  }

  public void setSparePartsId(String sparePartsId) {
    this.sparePartsId = sparePartsId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }
}

