package com.it.quanxianguanli.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_record")
public class ServiceRecord extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceOrder service;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "operator_id", nullable = false)
    private SysUser operator;

    @Column(name = "operate_time", nullable = false)
    private LocalDateTime operateTime;

    public ServiceOrder getService() {
        return service;
    }

    public void setService(ServiceOrder service) {
        this.service = service;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SysUser getOperator() {
        return operator;
    }

    public void setOperator(SysUser operator) {
        this.operator = operator;
    }

    public LocalDateTime getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(LocalDateTime operateTime) {
        this.operateTime = operateTime;
    }
}