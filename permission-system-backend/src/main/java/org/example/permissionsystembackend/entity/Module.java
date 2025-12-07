package org.example.permissionsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模块实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_module")
public class Module extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String nameCn;

    @Column(nullable = false, length = 50)
    private String nameEn;

    @Column(nullable = false)
    private Integer level;

    @Column(length = 36)
    private String parentId;

    @Column(nullable = false)
    private Integer sortOrder;

    @Column(length = 255)
    private String url;

    @Column(length = 50)
    private String icon;

    @Column(length = 50)
    private String groupName;

    @Column(length = 255)
    private String permission;

    @Column(nullable = false)
    private Boolean isParent = false;

    @Column(nullable = false)
    private Boolean isExpanded = false;
}
