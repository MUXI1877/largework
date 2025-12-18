package com.it.quanxianguanli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "basic_info")
public class BasicInfo extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private BasicInfo parent;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", unique = true, length = 50)
    private String code;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "sort", columnDefinition = "INT DEFAULT 0")
    private Integer sort;

    @Column(name = "is_enabled", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isEnabled;

    public BasicInfo getParent() {
        return parent;
    }

    public void setParent(BasicInfo parent) {
        this.parent = parent;
    }
    
    public String getParentId() {
        return parent != null ? parent.getId() : null;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }
}