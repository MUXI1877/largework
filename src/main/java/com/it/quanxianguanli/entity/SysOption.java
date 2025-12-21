package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sys_option")
public class SysOption extends BaseEntity {

    @Column(name = "group_name", nullable = false, length = 50)
    private String group;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "option_value", length = 100)
    private String value;

    private Integer sort;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
