-- 验证角色数据脚本
-- 用于检查角色数据是否正确创建

USE quanxianguanli;

-- 检查所有角色
SELECT '=== 所有角色列表 ===' AS info;
SELECT id, role_name, role_desc 
FROM sys_role 
ORDER BY id;

-- 检查常见角色是否存在
SELECT '=== 常见角色检查 ===' AS info;
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN '❌ 角色不存在'
        ELSE CONCAT('✅ 角色存在: ', role_name)
    END AS status,
    id,
    role_name
FROM sys_role 
WHERE id IN ('r001', 'r002', 'r003')
GROUP BY id, role_name;

-- 如果角色不存在，显示提示
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN '❌ 警告：角色数据不存在！请执行 data_init.sql 初始化角色数据'
        WHEN COUNT(*) < 3 THEN CONCAT('⚠️ 警告：角色数据不完整！期望3个，实际', COUNT(*), '个。请执行 data_init.sql')
        ELSE '✅ 所有角色数据已正确初始化'
    END AS status
FROM sys_role 
WHERE id IN ('r001', 'r002', 'r003');

-- 检查权限配置中的角色引用
SELECT '=== 权限配置中的角色引用检查 ===' AS info;
SELECT DISTINCT
    p.role_id,
    CASE 
        WHEN r.id IS NULL THEN CONCAT('❌ 角色不存在: ', p.role_id)
        ELSE CONCAT('✅ 角色存在: ', r.role_name)
    END AS status
FROM sys_permission p
LEFT JOIN sys_role r ON r.id = p.role_id
ORDER BY p.role_id;

-- 查找无效的角色引用
SELECT '=== 无效的角色引用（需要修复）===' AS info;
SELECT DISTINCT p.role_id
FROM sys_permission p
LEFT JOIN sys_role r ON r.id = p.role_id
WHERE r.id IS NULL;

