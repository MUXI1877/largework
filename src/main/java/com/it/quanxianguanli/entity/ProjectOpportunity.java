package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_opportunity")
public class ProjectOpportunity extends BaseEntity {
    
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId; // 客户ID
    
    @Column(name = "project_name", nullable = false, length = 200)
    private String projectName; // 项目名称
    
    @Column(name = "project_region", length = 100)
    private String projectRegion; // 项目所属片区
    
    @Column(name = "status", length = 50)
    private String status; // 状态
    
    @Column(name = "project_time")
    private LocalDateTime projectTime; // 时间
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectRegion() {
        return projectRegion;
    }

    public void setProjectRegion(String projectRegion) {
        this.projectRegion = projectRegion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getProjectTime() {
        return projectTime;
    }

    public void setProjectTime(LocalDateTime projectTime) {
        this.projectTime = projectTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

