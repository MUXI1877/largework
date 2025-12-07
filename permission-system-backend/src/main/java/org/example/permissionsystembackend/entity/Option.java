package org.example.permissionsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 选项实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_option")
public class Option extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String groupName;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String value;

    @Column(nullable = false)
    private Integer sortOrder;
}
