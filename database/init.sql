-- =====================================================
-- 营销及服务一体化管理系统 - 权限管理平台数据库表结构脚本
-- 数据库版本: MySQL 8.0+
-- 字符集: utf8mb4
-- 排序规则: utf8mb4_unicode_ci
-- =====================================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS quanxianguanli CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE quanxianguanli;

-- =====================================================
-- 表1: sys_module（模块表）
-- 说明: 模块管理表，存储系统所有功能模块的层级和属性
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_module (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    cn_name VARCHAR(100) NOT NULL COMMENT '中文名称',
    en_name VARCHAR(100) NOT NULL COMMENT '英文名称',
    menu_level INT NOT NULL COMMENT '菜单级数',
    menu_sort INT NOT NULL DEFAULT 0 COMMENT '菜单序号',
    path VARCHAR(255) COMMENT '链接路径',
    icon VARCHAR(50) COMMENT '图标',
    group_name VARCHAR(50) COMMENT '分组',
    permission_tag VARCHAR(100) NOT NULL COMMENT '权限标识',
    parent_id BIGINT DEFAULT 0 COMMENT '父模块ID',
    is_parent TINYINT(1) DEFAULT 0 COMMENT '是否父节点(1=是,0=否)',
    is_expand TINYINT(1) DEFAULT 0 COMMENT '是否展开(1=是,0=否)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id) COMMENT '父模块ID索引',
    INDEX idx_permission_tag (permission_tag) COMMENT '权限标识索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模块管理表，存储系统所有功能模块的层级和属性';

-- =====================================================
-- 表2: sys_permission_item（权限项表）
-- 说明: 模块对应的权限项表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_permission_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    module_id BIGINT NOT NULL COMMENT '关联模块ID',
    permission_code VARCHAR(50) NOT NULL COMMENT '权限编码(canRead/canAdd/canUpdate/canDelete)',
    permission_name VARCHAR(20) NOT NULL COMMENT '权限名称(查看/新增/修改/删除)',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_module_id (module_id) COMMENT '模块ID索引',
    INDEX idx_permission_code (permission_code) COMMENT '权限编码索引',
    FOREIGN KEY (module_id) REFERENCES sys_module(id) ON DELETE CASCADE COMMENT '外键关联模块表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模块对应的权限项表';

-- =====================================================
-- 表3: sys_role_permission（角色-权限关联表）
-- 说明: 角色与权限项的关联表
-- =====================================================
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限项ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_role_id (role_id) COMMENT '角色ID索引',
    INDEX idx_permission_id (permission_id) COMMENT '权限项ID索引',
    FOREIGN KEY (permission_id) REFERENCES sys_permission_item(id) ON DELETE CASCADE COMMENT '外键关联权限项表',
    UNIQUE KEY uk_role_permission (role_id, permission_id) COMMENT '角色和权限项唯一约束'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色与权限项的关联表';

-- =====================================================
-- 脚本执行完成
-- =====================================================
