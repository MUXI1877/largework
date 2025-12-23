package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 价格本实体
 * 支持不同部门对同一产品维护不同价格
 */
@Entity
@Table(name = "price_book", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "department"}))
public class PriceBook extends BaseEntity {
    
    @Column(name = "product_id", nullable = false, length = 36)
    private String productId; // 产品ID（关联产品管理模块）
    
    @Column(name = "product_name", nullable = false, length = 200)
    private String productName; // 产品名称（从产品表同步）
    
    @Column(name = "product_type", nullable = false, length = 50)
    private String productType; // 产品类别：SINGLE（单泵/单体设备）、COMPLETE（成套设备）、SPARE（备品备件）
    
    @Column(name = "model", length = 100)
    private String model; // 型号（从产品表同步）
    
    @Column(name = "parameters", length = 1000)
    private String parameters; // 参数（从产品表同步）
    
    @Column(name = "department", nullable = false, length = 100)
    private String department; // 部门：销售处、成套处、外贸处等
    
    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice; // 单价
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注
    
    // Getters and Setters
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
    
    public String getProductType() {
        return productType;
    }
    
    public void setProductType(String productType) {
        this.productType = productType;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getParameters() {
        return parameters;
    }
    
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

