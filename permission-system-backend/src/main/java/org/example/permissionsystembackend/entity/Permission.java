package org.example.permissionsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 权限配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_permission", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "role_id", "module_id" })
})
public class Permission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Column(nullable = false)
    private Boolean canRead = false;

    @Column(nullable = false)
    private Boolean canAdd = false;

    @Column(nullable = false)
    private Boolean canUpdate = false;

    @Column(nullable = false)
    private Boolean canDelete = false;
}
