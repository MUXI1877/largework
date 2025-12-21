-- 快速修复角色数据脚本
-- 如果角色数据不存在，执行此脚本快速创建

USE quanxianguanli;

-- 检查角色是否存在
SELECT '=== 检查角色数据 ===' AS info;
SELECT id, role_name FROM sys_role WHERE id IN ('r001', 'r002', 'r003');

-- 如果角色不存在，插入角色数据（使用 INSERT IGNORE 避免重复）
INSERT IGNORE INTO sys_role (id, create_time, update_time, role_name, role_desc) VALUES
('r001', NOW(), NOW(), '超级管理员', '系统最高权限管理员'),
('r002', NOW(), NOW(), '系统管理员', '系统日常管理维护人员'),
('r003', NOW(), NOW(), '普通用户', '普通系统用户');

-- 验证插入结果
SELECT '=== 修复后的角色数据 ===' AS info;
SELECT id, role_name, role_desc FROM sys_role WHERE id IN ('r001', 'r002', 'r003');

SELECT '✅ 角色数据修复完成！' AS message;

