package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "spare_parts")
public class SpareParts extends BaseEntity {

  @Column(nullable = false, length = 50, unique = true)
  private String code;

  @Column(nullable = false, length = 200)
  private String name;

  @Column(length = 100)
  private String model;

  @Column(length = 50)
  private String category;

  @Column(length = 20)
  private String unit;

  @Column(precision = 18, scale = 2)
  private BigDecimal price;

  @Column(name = "stock_quantity")
  private Integer stockQuantity;

  @Column(name = "min_stock")
  private Integer minStock;

  @Column(length = 200)
  private String supplier;

  @Column(length = 500)
  private String remark;

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

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Integer getStockQuantity() {
    return stockQuantity;
  }

  public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  public Integer getMinStock() {
    return minStock;
  }

  public void setMinStock(Integer minStock) {
    this.minStock = minStock;
  }

  public String getSupplier() {
    return supplier;
  }

  public void setSupplier(String supplier) {
    this.supplier = supplier;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}

