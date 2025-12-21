package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sales_region")
public class SalesRegion extends BaseEntity {
    
    @Column(name = "region_name", nullable = false, length = 200)
    private String regionName; // 片区名称
    
    @Column(name = "region_code", unique = true, nullable = false, length = 50)
    private String regionCode; // 片区编号
    
    @Column(name = "parent_department", length = 100)
    private String parentDepartment; // 上级部门（成套处、外贸处、销售处）
    
    @Column(name = "create_date", nullable = false)
    private LocalDate createDate; // 创建日期
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(String parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

