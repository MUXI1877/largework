-- 重新初始化权限配置数据脚本
-- 注意：此脚本会先删除现有权限数据，然后重新插入
-- 请谨慎使用，建议先备份数据

USE quanxianguanli;

-- ============================================
-- 第一步：删除现有权限数据（可选）
-- ============================================
-- 如果需要完全重新初始化，取消下面的注释
-- DELETE FROM sys_permission;

-- ============================================
-- 第二步：插入权限配置数据
-- ============================================

-- 超级管理员（r001）权限 - 所有模块完整权限
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
('p001', NOW(), NOW(), 'r001', 'm001', true, true, true, true),
('p002', NOW(), NOW(), 'r001', 'm002', true, true, true, true),
('p003', NOW(), NOW(), 'r001', 'm003', true, true, true, true),
('p004', NOW(), NOW(), 'r001', 'm004', true, true, true, true),
('p005', NOW(), NOW(), 'r001', 'm005', true, true, true, true),
('p006', NOW(), NOW(), 'r001', 'm006', true, true, true, true),
('p007', NOW(), NOW(), 'r001', 'm007', true, true, true, true),
('p015', NOW(), NOW(), 'r001', 'm011', true, true, true, true),
('p016', NOW(), NOW(), 'r001', 'm008', true, true, true, true),
('p017', NOW(), NOW(), 'r001', 'm009', true, true, true, true),
('p018', NOW(), NOW(), 'r001', 'm010', true, true, true, true)
ON DUPLICATE KEY UPDATE 
    can_read = VALUES(can_read),
    can_add = VALUES(can_add),
    can_update = VALUES(can_update),
    can_see = VALUES(can_see),
    update_time = NOW();

-- 系统管理员（r002）权限
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
('p008', NOW(), NOW(), 'r002', 'm001', true, true, true, true),
('p009', NOW(), NOW(), 'r002', 'm002', true, true, true, true),
('p010', NOW(), NOW(), 'r002', 'm003', true, true, true, true),
('p011', NOW(), NOW(), 'r002', 'm006', true, true, true, true),
('p012', NOW(), NOW(), 'r002', 'm007', true, true, true, true),
('p019', NOW(), NOW(), 'r002', 'm011', true, true, true, true),
('p020', NOW(), NOW(), 'r002', 'm008', true, true, true, true),
('p021', NOW(), NOW(), 'r002', 'm009', true, true, true, true),
('p022', NOW(), NOW(), 'r002', 'm010', true, true, true, true)
ON DUPLICATE KEY UPDATE 
    can_read = VALUES(can_read),
    can_add = VALUES(can_add),
    can_update = VALUES(can_update),
    can_see = VALUES(can_see),
    update_time = NOW();

-- 普通用户（r003）权限 - 只读权限
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
('p013', NOW(), NOW(), 'r003', 'm002', true, false, false, true),
('p014', NOW(), NOW(), 'r003', 'm006', true, false, false, true),
('p023', NOW(), NOW(), 'r003', 'm011', true, false, false, true),
('p024', NOW(), NOW(), 'r003', 'm008', true, false, false, true),
('p025', NOW(), NOW(), 'r003', 'm009', true, false, false, true),
('p026', NOW(), NOW(), 'r003', 'm010', true, false, false, true)
ON DUPLICATE KEY UPDATE 
    can_read = VALUES(can_read),
    can_add = VALUES(can_add),
    can_update = VALUES(can_update),
    can_see = VALUES(can_see),
    update_time = NOW();

-- ============================================
-- 第三步：验证插入结果
-- ============================================
SELECT '=== 权限配置统计 ===' AS info;
SELECT 
    r.role_name,
    COUNT(*) AS permission_count
FROM sys_permission p
JOIN sys_role r ON r.id = p.role_id
GROUP BY r.id, r.role_name
ORDER BY r.id;

SELECT '=== 基本信息管理模块权限配置 ===' AS info;
SELECT 
    r.role_name,
    m.cn_name AS module_name,
    p.can_read,
    p.can_add,
    p.can_update,
    p.can_see
FROM sys_permission p
JOIN sys_role r ON r.id = p.role_id
JOIN sys_module m ON m.id = p.module_id
WHERE p.module_id IN ('m011', 'm008', 'm009', 'm010')
ORDER BY r.role_name, m.id;

SELECT '✅ 权限配置数据重新插入完成！' AS message;

