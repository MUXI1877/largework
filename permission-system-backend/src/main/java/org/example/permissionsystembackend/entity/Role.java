package org.example.permissionsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_role")
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String roleName;

    @Column(length = 255)
    private String roleDesc;
}
