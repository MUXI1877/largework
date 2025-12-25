-- 日常管理模块数据库表
-- 注意：此脚本用于向现有数据库添加日常管理模块的业务表

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE quanxianguanli;

-- =====================================================
-- 1. 去向管理表
-- =====================================================
CREATE TABLE IF NOT EXISTS daily_destination (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    user_id VARCHAR(36) NOT NULL COMMENT '营销人员ID',
    destination_time DATETIME NOT NULL COMMENT '时间（精确到时分秒）',
    activity_name VARCHAR(200) NOT NULL COMMENT '营销活动名称',
    location VARCHAR(200) NOT NULL COMMENT '地点',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_destination_time (destination_time),
    INDEX idx_activity_name (activity_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='去向管理表';

-- =====================================================
-- 2. 周报管理表
-- =====================================================
CREATE TABLE IF NOT EXISTS weekly_report (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    user_id VARCHAR(36) NOT NULL COMMENT '员工ID',
    report_name VARCHAR(200) NOT NULL COMMENT '周报名称',
    report_time DATETIME NOT NULL COMMENT '周报时间（精确到时分秒）',
    report_content TEXT COMMENT '周报内容',
    report_remark VARCHAR(500) COMMENT '周报备注',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态：draft-草稿,submitted-已提交',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_report_time (report_time),
    INDEX idx_status (status),
    INDEX idx_report_name (report_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='周报管理表';

SELECT '日常管理模块表添加完成！' AS message;

