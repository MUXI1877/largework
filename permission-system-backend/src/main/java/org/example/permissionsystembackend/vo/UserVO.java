package org.example.permissionsystembackend.vo;

import lombok.Data;

import java.time.LocalDate;

/**
 * 用户VO
 */
@Data
public class UserVO {

    private String id;
    private String name;
    private String idCard;
    private String phone;
    private LocalDate birthDate;
    private String gender;
    private String positionId;
    private String positionName;
    private String areaId;
    private String areaName;
    private String username;
    private String roleId;
    private String roleName;
    private String status;
}
