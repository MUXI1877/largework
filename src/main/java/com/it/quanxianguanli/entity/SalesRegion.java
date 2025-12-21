package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sales_region")
public class SalesRegion extends BaseEntity {

  @Column(nullable = false, length = 100)
  private String name;

  @Column(length = 20)
  private String code;

  @Column(length = 50)
  private String dept;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDept() {
    return dept;
  }

  public void setDept(String dept) {
    this.dept = dept;
  }
}
