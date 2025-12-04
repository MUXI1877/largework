-- 创建数据库
CREATE DATABASE IF NOT EXISTS quanxianguanli CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE quanxianguanli;

-- 注意：表结构会由JPA自动创建，此脚本仅用于参考
-- 如果需要手动创建，可以取消下面的注释

/*
-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    name VARCHAR(100) NOT NULL,
    id_card VARCHAR(18) UNIQUE NOT NULL,
    phone VARCHAR(11) UNIQUE NOT NULL,
    birth_date DATE,
    gender VARCHAR(10),
    post_id VARCHAR(36),
    area_id VARCHAR(36),
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255),
    role_id VARCHAR(36),
    is_approved BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (post_id) REFERENCES sys_option(id),
    FOREIGN KEY (area_id) REFERENCES sys_option(id),
    FOREIGN KEY (role_id) REFERENCES sys_role(id)
);

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    role_name VARCHAR(100) UNIQUE NOT NULL,
    role_desc VARCHAR(500)
);

-- 选项表
CREATE TABLE IF NOT EXISTS sys_option (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    group_name VARCHAR(50) NOT NULL,
    title VARCHAR(100) NOT NULL,
    option_value VARCHAR(255),
    sort INT DEFAULT 0
);

-- 模块表
CREATE TABLE IF NOT EXISTS sys_module (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    cn_name VARCHAR(100) NOT NULL,
    en_name VARCHAR(100),
    menu_level INT,
    sort INT DEFAULT 0,
    path VARCHAR(255),
    icon VARCHAR(100),
    group_name VARCHAR(50),
    parent_id VARCHAR(36),
    is_parent BOOLEAN DEFAULT FALSE,
    is_expand BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (parent_id) REFERENCES sys_module(id)
);

-- 权限配置表
CREATE TABLE IF NOT EXISTS sys_permission (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    role_id VARCHAR(36) NOT NULL,
    module_id VARCHAR(36) NOT NULL,
    can_read BOOLEAN DEFAULT FALSE,
    can_add BOOLEAN DEFAULT FALSE,
    can_update BOOLEAN DEFAULT FALSE,
    can_see BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (role_id) REFERENCES sys_role(id),
    FOREIGN KEY (module_id) REFERENCES sys_module(id),
    UNIQUE KEY uk_role_module (role_id, module_id)
);
*/

