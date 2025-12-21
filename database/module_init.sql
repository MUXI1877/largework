-- 模块信息初始化脚本
-- 包含系统管理模块、基础数据模块、基本信息管理模块
-- 注意：此脚本应在表结构创建后执行

USE quanxianguanli;

-- ============================================
-- 系统管理模块（m001-m005）
-- ============================================
-- 注意：如果模块已存在，请先删除再插入，或使用 INSERT IGNORE / ON DUPLICATE KEY UPDATE
INSERT INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
('m001', NOW(), NOW(), '系统管理', 'System Management', 1, 1, '/system', 'el-icon-setting', 'system', NULL, true, true),
('m002', NOW(), NOW(), '账号管理', 'User Management', 2, 1, '/user-management', 'el-icon-user', 'system', 'm001', false, false),
('m003', NOW(), NOW(), '角色管理', 'Role Management', 2, 2, '/role-management', 'el-icon-s-custom', 'system', 'm001', false, false),
('m004', NOW(), NOW(), '模块管理', 'Module Management', 2, 3, '/module-management', 'el-icon-menu', 'system', 'm001', false, false),
('m005', NOW(), NOW(), '权限配置', 'Permission Config', 2, 4, '/permission-management', 'el-icon-lock', 'system', 'm001', false, false)
ON DUPLICATE KEY UPDATE 
    cn_name = VALUES(cn_name),
    en_name = VALUES(en_name),
    path = VALUES(path),
    icon = VALUES(icon),
    update_time = NOW();

-- ============================================
-- 基础数据模块（m006-m007）
-- ============================================
INSERT INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
('m006', NOW(), NOW(), '基础数据', 'Basic Data', 1, 2, '/basic', 'el-icon-data-board', 'basic', NULL, true, true),
('m007', NOW(), NOW(), '选项管理', 'Option Management', 2, 1, '/option-management', 'el-icon-collection', 'basic', 'm006', false, false)
ON DUPLICATE KEY UPDATE 
    cn_name = VALUES(cn_name),
    en_name = VALUES(en_name),
    path = VALUES(path),
    icon = VALUES(icon),
    update_time = NOW();

-- ============================================
-- 基本信息管理模块（m011为父模块，m008-m010为子模块）
-- ============================================
INSERT INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
('m011', NOW(), NOW(), '基本信息管理', 'Basic Information Management', 1, 3, '/basic-info', 'el-icon-folder', 'basic', NULL, true, true),
('m008', NOW(), NOW(), '客户管理', 'Customer Management', 2, 1, '/customer-management', 'el-icon-user-solid', 'basic', 'm011', false, false),
('m009', NOW(), NOW(), '团队信息管理', 'Team Management', 2, 2, '/team-management', 'el-icon-s-custom', 'basic', 'm011', false, false),
('m010', NOW(), NOW(), '产品管理', 'Product Management', 2, 3, '/product-management', 'el-icon-box', 'basic', 'm011', false, false)
ON DUPLICATE KEY UPDATE 
    cn_name = VALUES(cn_name),
    en_name = VALUES(en_name),
    path = VALUES(path),
    icon = VALUES(icon),
    parent_id = VALUES(parent_id),
    menu_level = VALUES(menu_level),
    is_parent = VALUES(is_parent),
    update_time = NOW();

-- ============================================
-- 模块说明
-- ============================================
-- m001: 系统管理（父模块）
--   - m002: 账号管理
--   - m003: 角色管理
--   - m004: 模块管理
--   - m005: 权限配置
--
-- m006: 基础数据（父模块）
--   - m007: 选项管理
--
-- m011: 基本信息管理（父模块）
--   - m008: 客户管理
--     功能：客户信息登记、查询、新增、修改、删除、导出
--     关联：客户关键人物、项目机会、合同、售后、来访信息
--   - m009: 团队信息管理
--     功能：销售片区管理、营销人员管理、片区人员调动管理
--   - m010: 产品管理
--     功能：单体设备管理、备品备件管理、设备成套管理
--     支持：Excel导入导出、附件上传

SELECT '模块信息初始化完成！' AS message;
SELECT '已插入11个模块：系统管理模块5个，基础数据模块2个，基本信息管理模块4个（1个父模块+3个子模块）' AS summary;

