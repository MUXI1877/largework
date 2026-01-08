-- 修复销售管理模块权限配置脚本
-- 确保系统管理员（r002）有完整的销售模块权限
-- 确保普通用户（r003）只有只读权限

USE `111`;

-- ============================================
-- 修复系统管理员（r002）权限 - 确保有完整的销售管理模块权限
-- ============================================
UPDATE sys_permission 
SET 
    can_read = 1,
    can_add = 1,
    can_update = 1,
    can_see = 1,
    update_time = NOW()
WHERE role_id = 'r002' 
  AND module_id IN ('m017', 'm012', 'm013', 'm014', 'm015', 'm016', 'm018');

-- 如果权限记录不存在，则插入
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
('p034', NOW(), NOW(), 'r002', 'm017', 1, 1, 1, 1),
('p035', NOW(), NOW(), 'r002', 'm012', 1, 1, 1, 1),
('p036', NOW(), NOW(), 'r002', 'm013', 1, 1, 1, 1),
('p037', NOW(), NOW(), 'r002', 'm014', 1, 1, 1, 1),
('p038', NOW(), NOW(), 'r002', 'm015', 1, 1, 1, 1),
('p039', NOW(), NOW(), 'r002', 'm016', 1, 1, 1, 1),
('p040', NOW(), NOW(), 'r002', 'm018', 1, 1, 1, 1)
ON DUPLICATE KEY UPDATE 
    can_read = 1,
    can_add = 1,
    can_update = 1,
    can_see = 1,
    update_time = NOW();

-- ============================================
-- 修复普通用户（r003）权限 - 确保只有只读权限（除了m013可能被特殊处理）
-- ============================================
-- 注意：根据111.sql，p043（r003对m013的权限）可能被设置为完整权限
-- 如果需要保持普通用户对m013的完整权限，则跳过m013的更新
UPDATE sys_permission 
SET 
    can_read = 1,
    can_add = 0,
    can_update = 0,
    can_see = 1,
    update_time = NOW()
WHERE role_id = 'r003' 
  AND module_id IN ('m017', 'm012', 'm014', 'm015', 'm016', 'm018');

-- 如果权限记录不存在，则插入（m013除外，因为可能已有特殊配置）
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
('p041', NOW(), NOW(), 'r003', 'm017', 1, 0, 0, 1),
('p042', NOW(), NOW(), 'r003', 'm012', 1, 0, 0, 1),
('p044', NOW(), NOW(), 'r003', 'm014', 1, 0, 0, 1),
('p045', NOW(), NOW(), 'r003', 'm015', 1, 0, 0, 1),
('p046', NOW(), NOW(), 'r003', 'm016', 1, 0, 0, 1),
('p047', NOW(), NOW(), 'r003', 'm018', 1, 0, 0, 1)
ON DUPLICATE KEY UPDATE 
    can_read = 1,
    can_add = 0,
    can_update = 0,
    can_see = 1,
    update_time = NOW();

-- 确保r003对m013的权限（如果不存在则插入，如果存在则保持原样）
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
('p043', NOW(), NOW(), 'r003', 'm013', 1, 1, 1, 1)
ON DUPLICATE KEY UPDATE 
    update_time = NOW();

-- ============================================
-- 验证修复结果
-- ============================================
SELECT '=== 修复后的销售管理模块权限配置 ===' AS info;

SELECT 
    r.role_name AS '角色',
    m.cn_name AS '模块名称',
    CASE WHEN p.can_read = 1 THEN '✓' ELSE '✗' END AS '可读',
    CASE WHEN p.can_add = 1 THEN '✓' ELSE '✗' END AS '可添加',
    CASE WHEN p.can_update = 1 THEN '✓' ELSE '✗' END AS '可更新',
    CASE WHEN p.can_see = 1 THEN '✓' ELSE '✗' END AS '可见'
FROM sys_permission p
JOIN sys_role r ON r.id = p.role_id
JOIN sys_module m ON m.id = p.module_id
WHERE p.module_id IN ('m017', 'm012', 'm013', 'm014', 'm015', 'm016', 'm018')
  AND r.id IN ('r002', 'r003')
ORDER BY r.role_name, m.id;

SELECT '✅ 销售管理模块权限修复完成！' AS message;
SELECT '系统管理员（r002）现在应该有完整的销售模块权限' AS note;
SELECT '普通用户（r003）对m013有完整权限，对其他模块只有只读权限' AS note2;

