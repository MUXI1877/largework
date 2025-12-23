package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "quotation_item")
public class QuotationItem extends BaseEntity {
    
    @Column(name = "quotation_id", nullable = false, length = 36)
    private String quotationId; // 报价单ID
    
    @Column(name = "product_id", length = 36)
    private String productId; // 产品ID
    
    @Column(name = "product_name", length = 200)
    private String productName; // 产品/零件名称
    
    @Column(name = "drawing_number", length = 100)
    private String drawingNumber; // 图号
    
    @Column(name = "inventory_quantity", precision = 15, scale = 2)
    private BigDecimal inventoryQuantity; // 零件库存
    
    @Column(name = "quantity", precision = 15, scale = 2)
    private BigDecimal quantity; // 数量
    
    @Column(name = "is_stagnant")
    private Boolean isStagnant; // 是否呆滞
    
    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice; // 报价（单价）
    
    @Column(name = "total_price", precision = 15, scale = 2)
    private BigDecimal totalPrice; // 报价（总价）

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDrawingNumber() {
        return drawingNumber;
    }

    public void setDrawingNumber(String drawingNumber) {
        this.drawingNumber = drawingNumber;
    }

    public BigDecimal getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(BigDecimal inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsStagnant() {
        return isStagnant;
    }

    public void setIsStagnant(Boolean isStagnant) {
        this.isStagnant = isStagnant;
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

