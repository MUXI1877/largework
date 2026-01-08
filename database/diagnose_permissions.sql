-- 诊断销售管理模块权限和数据可见性问题
-- 检查系统管理员和普通用户的权限配置

USE `111`;

-- ============================================
-- 1. 检查销售管理模块权限配置
-- ============================================
SELECT '=== 销售管理模块权限配置检查 ===' AS info;

SELECT 
    r.id AS role_id,
    r.role_name AS '角色名称',
    m.id AS module_id,
    m.cn_name AS '模块名称',
    p.can_read AS '可读',
    p.can_add AS '可添加',
    p.can_update AS '可更新',
    p.can_see AS '可见',
    p.update_time AS '更新时间'
FROM sys_permission p
JOIN sys_role r ON r.id COLLATE utf8mb4_unicode_ci = p.role_id COLLATE utf8mb4_unicode_ci
JOIN sys_module m ON m.id COLLATE utf8mb4_unicode_ci = p.module_id COLLATE utf8mb4_unicode_ci
WHERE p.module_id IN ('m017', 'm012', 'm013', 'm014', 'm015', 'm016', 'm018')
  AND r.id IN ('r002', 'r003')
ORDER BY r.id, m.id;

-- ============================================
-- 2. 检查销售机会数据的创建者分布
-- ============================================
SELECT '=== 销售机会数据创建者分布 ===' AS info;

SELECT 
    po.id AS opportunity_id,
    po.opportunity_name AS '机会名称',
    po.creator_id AS '创建者ID',
    u.name AS '创建者姓名',
    u.role_id AS '创建者角色',
    r.role_name AS '创建者角色名称',
    po.is_submitted AS '是否已提交',
    po.status AS '状态',
    po.create_time AS '创建时间'
FROM project_opportunity po
LEFT JOIN sys_user u ON u.id COLLATE utf8mb4_unicode_ci = po.creator_id COLLATE utf8mb4_unicode_ci
LEFT JOIN sys_role r ON r.id COLLATE utf8mb4_unicode_ci = u.role_id COLLATE utf8mb4_unicode_ci
ORDER BY po.create_time DESC
LIMIT 20;

-- ============================================
-- 3. 检查系统管理员用户列表
-- ============================================
SELECT '=== 系统管理员用户列表 ===' AS info;

SELECT 
    u.id AS user_id,
    u.username AS '用户名',
    u.name AS '姓名',
    u.role_id AS '角色ID',
    r.role_name AS '角色名称'
FROM sys_user u
JOIN sys_role r ON r.id COLLATE utf8mb4_unicode_ci = u.role_id COLLATE utf8mb4_unicode_ci
WHERE u.role_id = 'r002';

-- ============================================
-- 4. 检查普通用户列表
-- ============================================
SELECT '=== 普通用户列表 ===' AS info;

SELECT 
    u.id AS user_id,
    u.username AS '用户名',
    u.name AS '姓名',
    u.role_id AS '角色ID',
    r.role_name AS '角色名称'
FROM sys_user u
JOIN sys_role r ON r.id COLLATE utf8mb4_unicode_ci = u.role_id COLLATE utf8mb4_unicode_ci
WHERE u.role_id = 'r003'
LIMIT 10;

-- ============================================
-- 5. 检查系统管理员创建但可能看不到的数据
-- ============================================
SELECT '=== 系统管理员创建的数据 ===' AS info;

SELECT 
    po.id AS opportunity_id,
    po.opportunity_name AS '机会名称',
    po.creator_id AS '创建者ID',
    u.name AS '创建者姓名',
    po.is_submitted AS '是否已提交',
    po.status AS '状态',
    po.create_time AS '创建时间'
FROM project_opportunity po
LEFT JOIN sys_user u ON u.id COLLATE utf8mb4_unicode_ci = po.creator_id COLLATE utf8mb4_unicode_ci
WHERE u.role_id = 'r002'
ORDER BY po.create_time DESC
LIMIT 10;

-- ============================================
-- 6. 检查普通用户创建的数据
-- ============================================
SELECT '=== 普通用户创建的数据 ===' AS info;

SELECT 
    po.id AS opportunity_id,
    po.opportunity_name AS '机会名称',
    po.creator_id AS '创建者ID',
    u.name AS '创建者姓名',
    po.is_submitted AS '是否已提交',
    po.status AS '状态',
    po.create_time AS '创建时间'
FROM project_opportunity po
LEFT JOIN sys_user u ON u.id COLLATE utf8mb4_unicode_ci = po.creator_id COLLATE utf8mb4_unicode_ci
WHERE u.role_id = 'r003'
ORDER BY po.create_time DESC
LIMIT 10;

-- ============================================
-- 7. 检查权限配置问题
-- ============================================
SELECT '=== 权限配置问题检查 ===' AS info;

-- 检查是否有权限记录但can_see=0的情况
SELECT 
    r.role_name AS '角色',
    m.cn_name AS '模块',
    p.can_see AS '可见',
    p.can_read AS '可读',
    CASE 
        WHEN p.can_see = 0 THEN '❌ 不可见 - 这是问题！'
        WHEN p.can_read = 0 THEN '⚠️ 不可读'
        ELSE '✓ 正常'
    END AS '状态'
FROM sys_permission p
JOIN sys_role r ON r.id COLLATE utf8mb4_unicode_ci = p.role_id COLLATE utf8mb4_unicode_ci
JOIN sys_module m ON m.id COLLATE utf8mb4_unicode_ci = p.module_id COLLATE utf8mb4_unicode_ci
WHERE p.module_id IN ('m017', 'm012', 'm013', 'm014', 'm015', 'm016', 'm018')
  AND r.id IN ('r002', 'r003')
  AND (p.can_see = 0 OR p.can_read = 0)
ORDER BY r.id, m.id;

SELECT '✅ 诊断完成！' AS message;
SELECT '如果看到"不可见"或"不可读"的状态，说明权限配置有问题' AS note;
SELECT '请检查系统管理员（r002）的can_see和can_read是否都为1' AS note2;

