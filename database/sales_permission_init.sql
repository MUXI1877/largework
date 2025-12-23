-- 销售管理模块权限初始化脚本
-- 为销售管理模块（m017及其子模块m012-m016, m018）添加权限配置
-- 注意：此脚本应在销售模块初始化后执行

USE quanxianguanli;

-- ============================================
-- 超级管理员（r001）权限 - 所有销售管理模块完整权限
-- ============================================
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
('p027', NOW(), NOW(), 'r001', 'm017', true, true, true, true),
('p028', NOW(), NOW(), 'r001', 'm012', true, true, true, true),
('p029', NOW(), NOW(), 'r001', 'm013', true, true, true, true),
('p030', NOW(), NOW(), 'r001', 'm014', true, true, true, true),
('p031', NOW(), NOW(), 'r001', 'm015', true, true, true, true),
('p032', NOW(), NOW(), 'r001', 'm016', true, true, true, true),
('p033', NOW(), NOW(), 'r001', 'm018', true, true, true, true)
ON DUPLICATE KEY UPDATE 
    can_read = VALUES(can_read),
    can_add = VALUES(can_add),
    can_update = VALUES(can_update),
    can_see = VALUES(can_see),
    update_time = NOW();

-- ============================================
-- 系统管理员（r002）权限 - 所有销售管理模块完整权限
-- ============================================
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
('p034', NOW(), NOW(), 'r002', 'm017', true, true, true, true),
('p035', NOW(), NOW(), 'r002', 'm012', true, true, true, true),
('p036', NOW(), NOW(), 'r002', 'm013', true, true, true, true),
('p037', NOW(), NOW(), 'r002', 'm014', true, true, true, true),
('p038', NOW(), NOW(), 'r002', 'm015', true, true, true, true),
('p039', NOW(), NOW(), 'r002', 'm016', true, true, true, true),
('p040', NOW(), NOW(), 'r002', 'm018', true, true, true, true)
ON DUPLICATE KEY UPDATE 
    can_read = VALUES(can_read),
    can_add = VALUES(can_add),
    can_update = VALUES(can_update),
    can_see = VALUES(can_see),
    update_time = NOW();

-- ============================================
-- 普通用户（r003）权限 - 销售管理模块只读权限
-- ============================================
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
('p041', NOW(), NOW(), 'r003', 'm017', true, false, false, true),
('p042', NOW(), NOW(), 'r003', 'm012', true, false, false, true),
('p043', NOW(), NOW(), 'r003', 'm013', true, false, false, true),
('p044', NOW(), NOW(), 'r003', 'm014', true, false, false, true),
('p045', NOW(), NOW(), 'r003', 'm015', true, false, false, true),
('p046', NOW(), NOW(), 'r003', 'm016', true, false, false, true),
('p047', NOW(), NOW(), 'r003', 'm018', true, false, false, true)
ON DUPLICATE KEY UPDATE 
    can_read = VALUES(can_read),
    can_add = VALUES(can_add),
    can_update = VALUES(can_update),
    can_see = VALUES(can_see),
    update_time = NOW();

-- ============================================
-- 验证插入结果
-- ============================================
SELECT '=== 销售管理模块权限配置统计 ===' AS info;
SELECT 
    r.role_name,
    COUNT(*) AS permission_count
FROM sys_permission p
JOIN sys_role r ON r.id = p.role_id
WHERE p.module_id IN ('m017', 'm012', 'm013', 'm014', 'm015', 'm016', 'm018')
GROUP BY r.id, r.role_name
ORDER BY r.id;

SELECT '=== 销售管理模块权限详情 ===' AS info;
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
WHERE p.module_id IN ('m017', 'm012', 'm013', 'm014', 'm015', 'm016', 'm018')
ORDER BY r.role_name, m.id;

SELECT '✅ 销售管理模块权限配置完成！' AS message;

