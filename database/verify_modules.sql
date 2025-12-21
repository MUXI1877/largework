-- 验证模块数据脚本
-- 用于检查基本信息管理模块是否正确创建

USE quanxianguanli;

-- 检查所有模块
SELECT '=== 所有模块列表 ===' AS info;
SELECT id, cn_name, en_name, menu_level, parent_id, is_parent, path 
FROM sys_module 
ORDER BY menu_level, sort;

-- 检查基本信息管理父模块（m011）
SELECT '=== 基本信息管理父模块（m011）===' AS info;
SELECT id, cn_name, en_name, menu_level, parent_id, is_parent, path 
FROM sys_module 
WHERE id = 'm011';

-- 检查基本信息管理子模块（m008, m009, m010）
SELECT '=== 基本信息管理子模块（m008, m009, m010）===' AS info;
SELECT id, cn_name, en_name, menu_level, parent_id, is_parent, path 
FROM sys_module 
WHERE id IN ('m008', 'm009', 'm010');

-- 检查模块层级关系
SELECT '=== 模块层级关系 ===' AS info;
SELECT 
    m1.id AS parent_id,
    m1.cn_name AS parent_name,
    m2.id AS child_id,
    m2.cn_name AS child_name
FROM sys_module m1
LEFT JOIN sys_module m2 ON m2.parent_id = m1.id
WHERE m1.id = 'm011' OR m2.id IN ('m008', 'm009', 'm010')
ORDER BY m1.id, m2.id;

-- 检查权限配置
SELECT '=== 基本信息管理模块权限配置 ===' AS info;
SELECT 
    p.id,
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

-- 如果模块不存在，显示提示
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN '❌ 警告：基本信息管理父模块（m011）不存在！请执行 module_init.sql 或 data_init.sql'
        ELSE '✅ 基本信息管理父模块（m011）存在'
    END AS status
FROM sys_module 
WHERE id = 'm011';

SELECT 
    CASE 
        WHEN COUNT(*) < 3 THEN CONCAT('❌ 警告：基本信息管理子模块不完整！期望3个，实际', COUNT(*), '个。请执行 module_init.sql 或 data_init.sql')
        ELSE '✅ 基本信息管理子模块（m008, m009, m010）全部存在'
    END AS status
FROM sys_module 
WHERE id IN ('m008', 'm009', 'm010');

