package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_order")
public class ServiceOrder extends BaseEntity {

    @Column(name = "order_no", nullable = false, unique = true, length = 50)
    private String orderNo;

    @Column(name = "customer_id", nullable = false, length = 36)
    private String customerId;

    @Column(name = "product_id", nullable = false, length = 36)
    private String productId;

    @Column(name = "problem_desc", nullable = false, columnDefinition = "TEXT")
    private String problemDesc;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "assign_to")
    private SysUser assignTo;

    @ManyToOne
    @JoinColumn(name = "create_user_id", nullable = false)
    private SysUser createUser;

    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SysUser getAssignTo() {
        return assignTo;
    }

    public void setAssignTo(SysUser assignTo) {
        this.assignTo = assignTo;
    }

    public SysUser getCreateUser() {
        return createUser;
    }

    public void setCreateUser(SysUser createUser) {
        this.createUser = createUser;
    }

    public LocalDateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(LocalDateTime completeTime) {
        this.completeTime = completeTime;
    }
}