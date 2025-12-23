package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 价格本修改日志实体
 * 记录价格修改历史，便于追溯
 */
@Entity
@Table(name = "price_book_log")
public class PriceBookLog extends BaseEntity {
    
    @Column(name = "price_book_id", nullable = false, length = 36)
    private String priceBookId; // 价格本ID
    
    @Column(name = "product_id", length = 36)
    private String productId; // 产品ID
    
    @Column(name = "product_name", length = 200)
    private String productName; // 产品名称
    
    @Column(name = "department", length = 100)
    private String department; // 部门
    
    @Column(name = "old_price", precision = 15, scale = 2)
    private BigDecimal oldPrice; // 原价格
    
    @Column(name = "new_price", precision = 15, scale = 2)
    private BigDecimal newPrice; // 新价格
    
    @Column(name = "operator_id", length = 36)
    private String operatorId; // 操作人ID
    
    @Column(name = "operator_name", length = 100)
    private String operatorName; // 操作人姓名
    
    @Column(name = "operation_type", length = 50)
    private String operationType; // 操作类型：CREATE（新增）、UPDATE（修改）、DELETE（删除）
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注
    
    // Getters and Setters
    public String getPriceBookId() {
        return priceBookId;
    }
    
    public void setPriceBookId(String priceBookId) {
        this.priceBookId = priceBookId;
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
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public BigDecimal getOldPrice() {
        return oldPrice;
    }
    
    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }
    
    public BigDecimal getNewPrice() {
        return newPrice;
    }
    
    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }
    
    public String getOperatorId() {
        return operatorId;
    }
    
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    
    public String getOperatorName() {
        return operatorName;
    }
    
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    
    public String getOperationType() {
        return operationType;
    }
    
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

