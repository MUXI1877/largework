package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "customer_visit")
public class CustomerVisit extends BaseEntity {
    
    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId; // 客户ID
    
    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate; // 日期
    
    @Column(name = "customer_sequence", length = 50)
    private String customerSequence; // 客户序号
    
    @Column(name = "customer_name", length = 200)
    private String customerName; // 客户名称
    
    @Column(name = "status", length = 50)
    private String status; // 状态
    
    @Column(name = "contact_person", length = 100)
    private String contactPerson; // 联络员
    
    @Column(name = "contact_department", length = 100)
    private String contactDepartment; // 联络员所属部门
    
    @Column(name = "visit_matter", length = 500)
    private String visitMatter; // 来访事宜
    
    @Column(name = "remarks", length = 1000)
    private String remarks; // 备注

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public String getCustomerSequence() {
        return customerSequence;
    }

    public void setCustomerSequence(String customerSequence) {
        this.customerSequence = customerSequence;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactDepartment() {
        return contactDepartment;
    }

    public void setContactDepartment(String contactDepartment) {
        this.contactDepartment = contactDepartment;
    }

    public String getVisitMatter() {
        return visitMatter;
    }

    public void setVisitMatter(String visitMatter) {
        this.visitMatter = visitMatter;
    }
}

