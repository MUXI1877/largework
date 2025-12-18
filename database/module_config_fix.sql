-- 新增模块配置数据（使用INSERT IGNORE避免重复插入）
 -- 1. 插入基本信息管理模块 
 INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES 
 ('m008', NOW(), NOW(), '基本信息管理', 'Basic Info Management', 1, 3, '/basic-info', 'el-icon-info', 'business', NULL, true, true), 
 ('m009', NOW(), NOW(), '信息分类', 'Info Category', 2, 1, '/basic-info/category', 'el-icon-folder', 'business', 'm008', false, false), 
 ('m010', NOW(), NOW(), '信息列表', 'Info List', 2, 2, '/basic-info/list', 'el-icon-document', 'business', 'm008', false, false); 
 
 -- 2. 插入销售管理模块 
 INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES 
 ('m011', NOW(), NOW(), '销售管理', 'Sales Management', 1, 4, '/sales', 'el-icon-shopping-cart', 'business', NULL, true, true), 
 ('m012', NOW(), NOW(), '销售订单', 'Sales Order', 2, 1, '/sales/order', 'el-icon-document-add', 'business', 'm011', false, false), 
 ('m013', NOW(), NOW(), '销售统计', 'Sales Statistic', 2, 2, '/sales/statistic', 'el-icon-pie-chart', 'business', 'm011', false, false); 
 
 -- 3. 插入价格本管理模块 
 INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES 
 ('m014', NOW(), NOW(), '价格本管理', 'Price Book', 1, 5, '/price-book', 'el-icon-money', 'business', NULL, true, true), 
 ('m015', NOW(), NOW(), '价格维护', 'Price Maintenance', 2, 1, '/price-book/maintain', 'el-icon-edit', 'business', 'm014', false, false), 
 ('m016', NOW(), NOW(), '价格历史', 'Price History', 2, 2, '/price-book/history', 'el-icon-time', 'business', 'm014', false, false); 
 
 -- 4. 插入应收账管理模块 
 INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES 
 ('m017', NOW(), NOW(), '应收账管理', 'Receivable Management', 1, 6, '/receivable', 'el-icon-wallet', 'business', NULL, true, true), 
 ('m018', NOW(), NOW(), '应收列表', 'Receivable List', 2, 1, '/receivable/list', 'el-icon-list', 'business', 'm017', false, false), 
 ('m019', NOW(), NOW(), '收款登记', 'Receipt Record', 2, 2, '/receivable/receipt', 'el-icon-circle-check', 'business', 'm017', false, false), 
 ('m020', NOW(), NOW(), '账龄分析', 'Aging Analysis', 2, 3, '/receivable/aging', 'el-icon-trending-up', 'business', 'm017', false, false); 
 
 -- 5. 插入售后服务管理模块 
 INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES 
 ('m021', NOW(), NOW(), '售后服务管理', 'Service Management', 1, 7, '/service', 'el-icon-headset', 'business', NULL, true, true), 
 ('m022', NOW(), NOW(), '服务工单', 'Service Order', 2, 1, '/service/order', 'el-icon-tickets', 'business', 'm021', false, false), 
 ('m023', NOW(), NOW(), '服务记录', 'Service Record', 2, 2, '/service/record', 'el-icon-notebook-2', 'business', 'm021', false, false); 
 
 -- 分配模块权限（超级管理员） 
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
 ('p024', NOW(), NOW(), 'r001', 'm017', true, true, true, true), 
 ('p025', NOW(), NOW(), 'r001', 'm018', true, true, true, true), 
 ('p026', NOW(), NOW(), 'r001', 'm019', true, true, true, true), 
 ('p027', NOW(), NOW(), 'r001', 'm020', true, true, true, true), 
 ('p028', NOW(), NOW(), 'r001', 'm021', true, true, true, true), 
 ('p029', NOW(), NOW(), 'r001', 'm022', true, true, true, true), 
 ('p030', NOW(), NOW(), 'r001', 'm023', true, true, true, true); 
 
 -- 分配模块权限（系统管理员） 
 INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES 
 ('p031', NOW(), NOW(), 'r002', 'm008', true, true, true, true), 
 ('p032', NOW(), NOW(), 'r002', 'm009', true, true, true, true), 
 ('p033', NOW(), NOW(), 'r002', 'm010', true, true, true, true), 
 ('p034', NOW(), NOW(), 'r002', 'm011', true, true, true, true), 
 ('p035', NOW(), NOW(), 'r002', 'm012', true, true, true, true), 
 ('p036', NOW(), NOW(), 'r002', 'm013', true, true, true, true), 
 ('p037', NOW(), NOW(), 'r002', 'm014', true, true, true, true), 
 ('p038', NOW(), NOW(), 'r002', 'm015', true, true, true, true), 
 ('p039', NOW(), NOW(), 'r002', 'm016', true, true, true, true), 
 ('p040', NOW(), NOW(), 'r002', 'm017', true, true, true, true), 
 ('p041', NOW(), NOW(), 'r002', 'm018', true, true, true, true), 
 ('p042', NOW(), NOW(), 'r002', 'm019', true, true, true, true), 
 ('p043', NOW(), NOW(), 'r002', 'm020', true, true, true, true), 
 ('p044', NOW(), NOW(), 'r002', 'm021', true, true, true, true), 
 ('p045', NOW(), NOW(), 'r002', 'm022', true, true, true, true), 
 ('p046', NOW(), NOW(), 'r002', 'm023', true, true, true, true); 
 
 -- 分配模块权限（普通用户 - 只读权限） 
 INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES 
 ('p047', NOW(), NOW(), 'r003', 'm008', true, false, false, true), 
 ('p048', NOW(), NOW(), 'r003', 'm010', true, false, false, true), 
 ('p049', NOW(), NOW(), 'r003', 'm012', true, true, false, true), 
 ('p050', NOW(), NOW(), 'r003', 'm015', true, false, false, true), 
 ('p051', NOW(), NOW(), 'r003', 'm018', true, false, false, true), 
 ('p052', NOW(), NOW(), 'r003', 'm022', true, true, false, true);