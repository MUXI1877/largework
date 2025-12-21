-- 添加销售管理模块
-- 注意：此脚本用于向现有数据库添加销售管理模块

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE quanxianguanli;

-- 插入销售管理模块及其子模块
INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m018', NOW(), NOW(), '销售管理', 'Sales Management', 1, 4, '/sales', 'el-icon-shopping-cart-full', 'sales', NULL, true, true),
                                                                                                                                                       ('m019', NOW(), NOW(), '客户来访管理', 'Customer Visit Management', 2, 1, '/sales/customer-visit', 'el-icon-user', 'sales', 'm018', false, false),
                                                                                                                                                       ('m020', NOW(), NOW(), '销售机会管理', 'Sales Lead Management', 2, 2, '/sales/lead', 'el-icon-opportunity', 'sales', 'm018', true, true),
                                                                                                                                                       ('m021', NOW(), NOW(), '销售机会登记', 'Sales Lead Registration', 3, 1, '/sales/lead/registration', 'el-icon-edit', 'sales', 'm020', false, false),
                                                                                                                                                       ('m022', NOW(), NOW(), '机会分配及传递', 'Lead Assignment & Transfer', 3, 2, '/sales/lead/assignment', 'el-icon-share', 'sales', 'm020', false, false),
                                                                                                                                                       ('m023', NOW(), NOW(), '机会跟踪', 'Lead Tracking', 3, 3, '/sales/lead/tracking', 'el-icon-view', 'sales', 'm020', false, false),
                                                                                                                                                       ('m024', NOW(), NOW(), '销售库存查询', 'Sales Stock Query', 2, 3, '/sales/stock', 'el-icon-search', 'sales', 'm018', false, false),
                                                                                                                                                       ('m025', NOW(), NOW(), '销售报价管理', 'Sales Quotation Management', 2, 4, '/sales/quotation', 'el-icon-document', 'sales', 'm018', false, false),
                                                                                                                                                       ('m026', NOW(), NOW(), '销售降库管理', 'Sales Inventory Reduction', 2, 5, '/sales/inventory-reduction', 'el-icon-minus', 'sales', 'm018', false, false),
                                                                                                                                                       ('m027', NOW(), NOW(), '投标管理', 'Bidding Management', 2, 6, '/sales/bidding', 'el-icon-files', 'sales', 'm018', false, false);

-- 插入超级管理员权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p045', NOW(), NOW(), 'r001', 'm018', true, true, true, true),
                                                                                                                          ('p046', NOW(), NOW(), 'r001', 'm019', true, true, true, true),
                                                                                                                          ('p047', NOW(), NOW(), 'r001', 'm020', true, true, true, true),
                                                                                                                          ('p048', NOW(), NOW(), 'r001', 'm021', true, true, true, true),
                                                                                                                          ('p049', NOW(), NOW(), 'r001', 'm022', true, true, true, true),
                                                                                                                          ('p050', NOW(), NOW(), 'r001', 'm023', true, true, true, true),
                                                                                                                          ('p051', NOW(), NOW(), 'r001', 'm024', true, true, true, true),
                                                                                                                          ('p052', NOW(), NOW(), 'r001', 'm025', true, true, true, true),
                                                                                                                          ('p053', NOW(), NOW(), 'r001', 'm026', true, true, true, true),
                                                                                                                          ('p054', NOW(), NOW(), 'r001', 'm027', true, true, true, true);

-- 插入系统管理员权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p055', NOW(), NOW(), 'r002', 'm018', true, true, true, true),
                                                                                                                          ('p056', NOW(), NOW(), 'r002', 'm019', true, true, true, true),
                                                                                                                          ('p057', NOW(), NOW(), 'r002', 'm020', true, true, true, true),
                                                                                                                          ('p058', NOW(), NOW(), 'r002', 'm021', true, true, true, true),
                                                                                                                          ('p059', NOW(), NOW(), 'r002', 'm022', true, true, true, true),
                                                                                                                          ('p060', NOW(), NOW(), 'r002', 'm023', true, true, true, true),
                                                                                                                          ('p061', NOW(), NOW(), 'r002', 'm024', true, true, true, true),
                                                                                                                          ('p062', NOW(), NOW(), 'r002', 'm025', true, true, true, true),
                                                                                                                          ('p063', NOW(), NOW(), 'r002', 'm026', true, true, true, true),
                                                                                                                          ('p064', NOW(), NOW(), 'r002', 'm027', true, true, true, true);

-- 插入普通用户权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p065', NOW(), NOW(), 'r003', 'm018', true, false, false, true),
                                                                                                                          ('p066', NOW(), NOW(), 'r003', 'm019', true, false, false, true),
                                                                                                                          ('p067', NOW(), NOW(), 'r003', 'm020', true, false, false, true),
                                                                                                                          ('p068', NOW(), NOW(), 'r003', 'm021', true, false, false, true),
                                                                                                                          ('p069', NOW(), NOW(), 'r003', 'm022', true, false, false, true),
                                                                                                                          ('p070', NOW(), NOW(), 'r003', 'm023', true, false, false, true),
                                                                                                                          ('p071', NOW(), NOW(), 'r003', 'm024', true, false, false, true),
                                                                                                                          ('p072', NOW(), NOW(), 'r003', 'm025', true, false, false, true),
                                                                                                                          ('p073', NOW(), NOW(), 'r003', 'm026', true, false, false, true),
                                                                                                                          ('p074', NOW(), NOW(), 'r003', 'm027', true, false, false, true);

SELECT '销售管理模块添加完成！' AS message;

