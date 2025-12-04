package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sys_option")
public class SysOption extends BaseEntity {
    
    @Column(name = "group_name", nullable = false)
    private String groupName;
    
    @Column(nullable = false)
    private String title;
    
    @Column(name = "option_value")
    private String optionValue;
    
    private Integer sort;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}

