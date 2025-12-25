-- 日常管理模块初始化
-- 包含：去向管理(m024)、周报管理(m025)

USE quanxianguanli;

-- ============================================
-- 日常管理模块（m026为父模块，m024-m025为子模块）
-- ============================================

-- 主模块：日常管理 (m026)
INSERT INTO `sys_module` (`id`,`create_time`,`update_time`,`cn_name`,`en_name`,`menu_level`,`sort`,`path`,`icon`,`group_name`,`parent_id`,`is_parent`,`is_expand`,`permission`,`visible`)
SELECT 'm026', NOW(), NOW(), '日常管理', 'Daily Management', 1, 6, '/daily', 'el-icon-date', 'daily', NULL, 1, 1, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_module WHERE id='m026');

-- 子模块：去向管理 (m024)
INSERT INTO `sys_module` (`id`,`create_time`,`update_time`,`cn_name`,`en_name`,`menu_level`,`sort`,`path`,`icon`,`group_name`,`parent_id`,`is_parent`,`is_expand`,`permission`,`visible`)
SELECT 'm024', NOW(), NOW(), '去向管理', 'Destination Management', 2, 1, '/destination-management', 'el-icon-location', 'daily', 'm026', 0, 0, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_module WHERE id='m024');

-- 子模块：周报管理 (m025)
INSERT INTO `sys_module` (`id`,`create_time`,`update_time`,`cn_name`,`en_name`,`menu_level`,`sort`,`path`,`icon`,`group_name`,`parent_id`,`is_parent`,`is_expand`,`permission`,`visible`)
SELECT 'm025', NOW(), NOW(), '周报管理', 'Weekly Report Management', 2, 2, '/weekly-report-management', 'el-icon-document', 'daily', 'm026', 0, 0, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_module WHERE id='m025');

-- ============================================
-- 权限配置
-- ============================================
-- r001/r002（管理员）：全权限
-- r003（普通用户）：可读、可添加、可修改、可见

-- r001权限
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r001', 'm026', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r001' AND module_id='m026');

INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r001', 'm024', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r001' AND module_id='m024');

INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r001', 'm025', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r001' AND module_id='m025');

-- r002权限
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r002', 'm026', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r002' AND module_id='m026');

INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r002', 'm024', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r002' AND module_id='m024');

INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r002', 'm025', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r002' AND module_id='m025');

-- r003权限（普通用户：可读、可添加、可修改、可见）
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r003', 'm026', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r003' AND module_id='m026');

INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r003', 'm024', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r003' AND module_id='m024');

INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r003', 'm025', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r003' AND module_id='m025');

-- ============================================
-- 模块说明
-- ============================================
-- m026: 日常管理（父模块）
--   - m024: 去向管理
--     功能：记录营销人员去向计划及实际去向信息，支持人员间去向查询，提供去向看板可视化展示
--   - m025: 周报管理
--     功能：支持员工填报工作总结与计划，方便领导查看审批，实现工作情况同步与管理

SELECT '日常管理模块初始化完成！' AS message;
SELECT '已插入3个模块：日常管理父模块1个（m026），子模块2个（m024、m025）' AS summary;
SELECT '已配置权限：r001、r002、r003全权限（可读、可添加、可修改、可见）' AS permission_summary;

