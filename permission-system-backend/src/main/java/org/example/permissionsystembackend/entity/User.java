package org.example.permissionsystembackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 用户实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_user")
public class User extends BaseEntity {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 18)
    private String idCard;

    @Column(nullable = false, unique = true, length = 11)
    private String phone;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 10)
    private String gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Option position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Option area;

    @Column(unique = true, length = 50)
    private String username;

    @Column(length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.PENDING;

    public enum UserStatus {
        PENDING, // 待审核
        ACTIVE, // 已激活
        DISABLED // 已禁用
    }
}
