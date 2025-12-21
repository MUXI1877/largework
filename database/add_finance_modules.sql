-- 添加价格本管理和应收账管理模块
-- 注意：此脚本用于向现有数据库添加价格本管理和应收账管理模块

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE quanxianguanli;

-- 插入价格本管理模块
INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m028', NOW(), NOW(), '价格本管理', 'Price Book Management', 1, 5, '/price-book', 'el-icon-coin', 'price-book', NULL, true, true),
                                                                                                                                                       ('m029', NOW(), NOW(), '价格本管理', 'Price Book', 2, 1, '/price-book/manage', 'el-icon-document', 'price-book', 'm028', false, false);

-- 插入应收账管理模块
INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m030', NOW(), NOW(), '应收账管理', 'Accounts Receivable Management', 1, 6, '/acc-receivable', 'el-icon-wallet', 'acc-receivable', NULL, true, true),
                                                                                                                                                       ('m031', NOW(), NOW(), '应收账计划', 'Receivable Plan', 2, 1, '/acc-receivable/plan', 'el-icon-calendar', 'acc-receivable', 'm030', false, false),
                                                                                                                                                       ('m032', NOW(), NOW(), '应收账录入', 'Receivable Entry', 2, 2, '/acc-receivable/entry', 'el-icon-edit', 'acc-receivable', 'm030', false, false),
                                                                                                                                                       ('m033', NOW(), NOW(), '应收账查询', 'Receivable Query', 2, 3, '/acc-receivable/query', 'el-icon-search', 'acc-receivable', 'm030', false, false);

-- 插入超级管理员权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p075', NOW(), NOW(), 'r001', 'm028', true, true, true, true),
                                                                                                                          ('p076', NOW(), NOW(), 'r001', 'm029', true, true, true, true),
                                                                                                                          ('p077', NOW(), NOW(), 'r001', 'm030', true, true, true, true),
                                                                                                                          ('p078', NOW(), NOW(), 'r001', 'm031', true, true, true, true),
                                                                                                                          ('p079', NOW(), NOW(), 'r001', 'm032', true, true, true, true),
                                                                                                                          ('p080', NOW(), NOW(), 'r001', 'm033', true, true, true, true);

-- 插入系统管理员权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p081', NOW(), NOW(), 'r002', 'm028', true, true, true, true),
                                                                                                                          ('p082', NOW(), NOW(), 'r002', 'm029', true, true, true, true),
                                                                                                                          ('p083', NOW(), NOW(), 'r002', 'm030', true, true, true, true),
                                                                                                                          ('p084', NOW(), NOW(), 'r002', 'm031', true, true, true, true),
                                                                                                                          ('p085', NOW(), NOW(), 'r002', 'm032', true, true, true, true),
                                                                                                                          ('p086', NOW(), NOW(), 'r002', 'm033', true, true, true, true);

-- 插入普通用户权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p087', NOW(), NOW(), 'r003', 'm028', true, false, false, true),
                                                                                                                          ('p088', NOW(), NOW(), 'r003', 'm029', true, false, false, true),
                                                                                                                          ('p089', NOW(), NOW(), 'r003', 'm030', true, false, false, true),
                                                                                                                          ('p090', NOW(), NOW(), 'r003', 'm031', true, false, false, true),
                                                                                                                          ('p091', NOW(), NOW(), 'r003', 'm032', true, false, false, true),
                                                                                                                          ('p092', NOW(), NOW(), 'r003', 'm033', true, false, false, true);

SELECT '价格本管理和应收账管理模块添加完成！' AS message;

