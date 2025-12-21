package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "quotation_item")
public class QuotationItem extends BaseEntity {

  @Column(name = "quotation_id", length = 36, nullable = false)
  private String quotationId;

  @Column(name = "product_id", length = 36)
  private String productId;

  @Column
  private Integer quantity;

  @Column(name = "unit_price", precision = 18, scale = 2)
  private BigDecimal unitPrice;

  @Column(name = "total_price", precision = 18, scale = 2)
  private BigDecimal totalPrice;

  public String getQuotationId() {
    return quotationId;
  }

  public void setQuotationId(String quotationId) {
    this.quotationId = quotationId;
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

