package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "device_unit")
public class DeviceUnit extends BaseEntity {
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 100)
    private String model;
    
    @Column(length = 500)
    private String params;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    private Double weight;
    
    @Column(name = "lead_day")
    private Integer leadDay;

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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getLeadDay() {
        return leadDay;
    }

    public void setLeadDay(Integer leadDay) {
        this.leadDay = leadDay;
    }
}

