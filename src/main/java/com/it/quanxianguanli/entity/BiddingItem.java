package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bidding_item")
public class BiddingItem extends BaseEntity {

  @Column(name = "bidding_id", nullable = false, length = 36)
  private String biddingId;

  @Column(name = "product_type", nullable = false, length = 20)
  private String productType;

  @Column(name = "product_id", nullable = false, length = 36)
  private String productId;

  @Column(nullable = false)
  private Integer quantity;

  @Column(name = "unit_price", precision = 18, scale = 2)
  private BigDecimal unitPrice;

  @Column(name = "total_price", precision = 18, scale = 2)
  private BigDecimal totalPrice;

  public String getBiddingId() {
    return biddingId;
  }

  public void setBiddingId(String biddingId) {
    this.biddingId = biddingId;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
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
}

