package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "opportunity_employee")
public class OpportunityEmployee extends BaseEntity {
    
    @Column(name = "opportunity_id", nullable = false, length = 36)
    private String opportunityId; // 销售机会ID
    
    @Column(name = "employee_id", nullable = false, length = 36)
    private String employeeId; // 员工ID
    
    @Column(name = "employee_name", length = 100)
    private String employeeName; // 员工名称

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}

