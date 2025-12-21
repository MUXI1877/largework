-- 添加基本信息管理模块
-- 注意：此脚本用于向现有数据库添加基本信息管理模块

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE quanxianguanli;

-- 插入基本信息管理模块及其子模块
INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m008', NOW(), NOW(), '基本信息管理', 'Basic Info Management', 1, 3, '/basic-info', 'el-icon-document', 'basic-info', NULL, true, true),
                                                                                                                                                       ('m009', NOW(), NOW(), '客户管理', 'Customer Management', 2, 1, '/basic-info/customer', 'el-icon-user-solid', 'basic-info', 'm008', false, false),
                                                                                                                                                       ('m010', NOW(), NOW(), '团队信息管理', 'Team Info Management', 2, 2, '/basic-info/team', 'el-icon-s-custom', 'basic-info', 'm008', true, true),
                                                                                                                                                       ('m011', NOW(), NOW(), '销售片区管理', 'Sales Region Management', 3, 1, '/basic-info/team/sales-region', 'el-icon-location', 'basic-info', 'm010', false, false),
                                                                                                                                                       ('m012', NOW(), NOW(), '营销人员管理', 'Sales Person Management', 3, 2, '/basic-info/team/sales-person', 'el-icon-user', 'basic-info', 'm010', false, false),
                                                                                                                                                       ('m013', NOW(), NOW(), '片区人员调动管理', 'Person Transfer Management', 3, 3, '/basic-info/team/person-transfer', 'el-icon-sort', 'basic-info', 'm010', false, false),
                                                                                                                                                       ('m014', NOW(), NOW(), '产品管理', 'Product Management', 2, 3, '/basic-info/product', 'el-icon-box', 'basic-info', 'm008', true, true),
                                                                                                                                                       ('m015', NOW(), NOW(), '单体设备', 'Single Device', 3, 1, '/basic-info/product/device-unit', 'el-icon-cpu', 'basic-info', 'm014', false, false),
                                                                                                                                                       ('m016', NOW(), NOW(), '备品备件', 'Spare Parts', 3, 2, '/basic-info/product/spare-parts', 'el-icon-collection', 'basic-info', 'm014', false, false),
                                                                                                                                                       ('m017', NOW(), NOW(), '设备成套', 'Equipment Set', 3, 3, '/basic-info/product/equipment-set', 'el-icon-suitcase', 'basic-info', 'm014', false, false);

-- 插入超级管理员权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p015', NOW(), NOW(), 'r001', 'm008', true, true, true, true),
                                                                                                                          ('p016', NOW(), NOW(), 'r001', 'm009', true, true, true, true),
                                                                                                                          ('p017', NOW(), NOW(), 'r001', 'm010', true, true, true, true),
                                                                                                                          ('p018', NOW(), NOW(), 'r001', 'm011', true, true, true, true),
                                                                                                                          ('p019', NOW(), NOW(), 'r001', 'm012', true, true, true, true),
                                                                                                                          ('p020', NOW(), NOW(), 'r001', 'm013', true, true, true, true),
                                                                                                                          ('p021', NOW(), NOW(), 'r001', 'm014', true, true, true, true),
                                                                                                                          ('p022', NOW(), NOW(), 'r001', 'm015', true, true, true, true),
                                                                                                                          ('p023', NOW(), NOW(), 'r001', 'm016', true, true, true, true),
                                                                                                                          ('p024', NOW(), NOW(), 'r001', 'm017', true, true, true, true);

-- 插入系统管理员权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p025', NOW(), NOW(), 'r002', 'm008', true, true, true, true),
                                                                                                                          ('p026', NOW(), NOW(), 'r002', 'm009', true, true, true, true),
                                                                                                                          ('p027', NOW(), NOW(), 'r002', 'm010', true, true, true, true),
                                                                                                                          ('p028', NOW(), NOW(), 'r002', 'm011', true, true, true, true),
                                                                                                                          ('p029', NOW(), NOW(), 'r002', 'm012', true, true, true, true),
                                                                                                                          ('p030', NOW(), NOW(), 'r002', 'm013', true, true, true, true),
                                                                                                                          ('p031', NOW(), NOW(), 'r002', 'm014', true, true, true, true),
                                                                                                                          ('p032', NOW(), NOW(), 'r002', 'm015', true, true, true, true),
                                                                                                                          ('p033', NOW(), NOW(), 'r002', 'm016', true, true, true, true),
                                                                                                                          ('p034', NOW(), NOW(), 'r002', 'm017', true, true, true, true);

-- 插入普通用户权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p035', NOW(), NOW(), 'r003', 'm008', true, false, false, true),
                                                                                                                          ('p036', NOW(), NOW(), 'r003', 'm009', true, false, false, true),
                                                                                                                          ('p037', NOW(), NOW(), 'r003', 'm010', true, false, false, true),
                                                                                                                          ('p038', NOW(), NOW(), 'r003', 'm011', true, false, false, true),
                                                                                                                          ('p039', NOW(), NOW(), 'r003', 'm012', true, false, false, true),
                                                                                                                          ('p040', NOW(), NOW(), 'r003', 'm013', true, false, false, true),
                                                                                                                          ('p041', NOW(), NOW(), 'r003', 'm014', true, false, false, true),
                                                                                                                          ('p042', NOW(), NOW(), 'r003', 'm015', true, false, false, true),
                                                                                                                          ('p043', NOW(), NOW(), 'r003', 'm016', true, false, false, true),
                                                                                                                          ('p044', NOW(), NOW(), 'r003', 'm017', true, false, false, true);

SELECT '基本信息管理模块添加完成！' AS message;
