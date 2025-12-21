package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    
    @Column(name = "name", nullable = false, length = 200)
    private String name; // 名称
    
    @Column(name = "model", length = 100)
    private String model; // 型号
    
    @Column(name = "parameters", length = 1000)
    private String parameters; // 参数
    
    @Column(name = "price", precision = 15, scale = 2)
    private BigDecimal price; // 价格
    
    @Column(name = "weight", length = 50)
    private String weight; // 重量
    
    @Column(name = "dimensions", length = 200)
    private String dimensions; // 尺寸
    
    @Column(name = "delivery_cycle", length = 100)
    private String deliveryCycle; // 交货周期
    
    @Column(name = "product_type", nullable = false, length = 50)
    private String productType; // 产品类型：SINGLE（单体设备）、SPARE（备品备件）、COMPLETE（设备成套）
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

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

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getDeliveryCycle() {
        return deliveryCycle;
    }

    public void setDeliveryCycle(String deliveryCycle) {
        this.deliveryCycle = deliveryCycle;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

