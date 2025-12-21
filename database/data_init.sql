-- 权限管理系统 - 数据初始化脚本
-- 注意：此脚本应在JPA自动创建表结构后执行

-- 设置字符集为 utf8mb4
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE quanxianguanli;

-- 1. 插入角色数据
INSERT IGNORE INTO sys_role (id, create_time, update_time, role_name, role_desc) VALUES
                                                                              ('r001', NOW(), NOW(), '超级管理员', '系统最高权限管理员'),
                                                                              ('r002', NOW(), NOW(), '系统管理员', '系统日常管理维护人员'),
                                                                              ('r003', NOW(), NOW(), '普通用户', '普通系统用户');

-- 2. 插入选项数据（岗位分组）
INSERT IGNORE INTO sys_option (id, create_time, update_time, group_name, title, option_value, sort) VALUES
                                                                                                 ('o001', NOW(), NOW(), 'post', '总经理', 'general_manager', 1),
                                                                                                 ('o002', NOW(), NOW(), 'post', '部门经理', 'department_manager', 2),
                                                                                                 ('o003', NOW(), NOW(), 'post', '项目经理', 'project_manager', 3),
                                                                                                 ('o004', NOW(), NOW(), 'post', '开发工程师', 'developer', 4),
                                                                                                 ('o005', NOW(), NOW(), 'post', '测试工程师', 'tester', 5),
                                                                                                 ('o006', NOW(), NOW(), 'post', '产品经理', 'product_manager', 6);

-- 3. 插入选项数据（地区分组）
INSERT IGNORE INTO sys_option (id, create_time, update_time, group_name, title, option_value, sort) VALUES
                                                                                                 ('o101', NOW(), NOW(), 'area', '北京', 'beijing', 1),
                                                                                                 ('o102', NOW(), NOW(), 'area', '上海', 'shanghai', 2),
                                                                                                 ('o103', NOW(), NOW(), 'area', '广州', 'guangzhou', 3),
                                                                                                 ('o104', NOW(), NOW(), 'area', '深圳', 'shenzhen', 4),
                                                                                                 ('o105', NOW(), NOW(), 'area', '杭州', 'hangzhou', 5),
                                                                                                 ('o106', NOW(), NOW(), 'area', '成都', 'chengdu', 6);

-- 4. 插入模块数据（系统管理模块）
INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m001', NOW(), NOW(), '系统管理', 'System Management', 1, 1, '/system', 'el-icon-setting', 'system', NULL, true, true),
                                                                                                                                                       ('m002', NOW(), NOW(), '账号管理', 'User Management', 2, 1, '/system/user', 'el-icon-user', 'system', 'm001', false, false),
                                                                                                                                                       ('m003', NOW(), NOW(), '角色管理', 'Role Management', 2, 2, '/system/role', 'el-icon-s-custom', 'system', 'm001', false, false),
                                                                                                                                                       ('m004', NOW(), NOW(), '模块管理', 'Module Management', 2, 3, '/system/module', 'el-icon-menu', 'system', 'm001', false, false),
                                                                                                                                                       ('m005', NOW(), NOW(), '权限配置', 'Permission Config', 2, 4, '/system/permission', 'el-icon-lock', 'system', 'm001', false, false);

-- 5. 插入模块数据（选项管理模块）
INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m006', NOW(), NOW(), '基础数据', 'Basic Data', 1, 2, '/basic', 'el-icon-data-board', 'basic', NULL, true, true),
                                                                                                                                                       ('m007', NOW(), NOW(), '选项管理', 'Option Management', 2, 1, '/basic/option', 'el-icon-collection', 'basic', 'm006', false, false);

-- 6. 插入模块数据（基本信息管理模块）
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

-- 7. 插入模块数据（销售管理模块）
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

-- 8. 插入模块数据（价格本管理模块）
INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m028', NOW(), NOW(), '价格本管理', 'Price Book Management', 1, 5, '/price-book', 'el-icon-coin', 'price-book', NULL, true, true),
                                                                                                                                                       ('m029', NOW(), NOW(), '价格本管理', 'Price Book', 2, 1, '/price-book/manage', 'el-icon-document', 'price-book', 'm028', false, false);

-- 9. 插入模块数据（应收账管理模块）
INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m030', NOW(), NOW(), '应收账管理', 'Accounts Receivable Management', 1, 6, '/acc-receivable', 'el-icon-wallet', 'acc-receivable', NULL, true, true),
                                                                                                                                                       ('m031', NOW(), NOW(), '应收账计划', 'Receivable Plan', 2, 1, '/acc-receivable/plan', 'el-icon-calendar', 'acc-receivable', 'm030', false, false),
                                                                                                                                                       ('m032', NOW(), NOW(), '应收账录入', 'Receivable Entry', 2, 2, '/acc-receivable/entry', 'el-icon-edit', 'acc-receivable', 'm030', false, false),
                                                                                                                                                       ('m033', NOW(), NOW(), '应收账查询', 'Receivable Query', 2, 3, '/acc-receivable/query', 'el-icon-search', 'acc-receivable', 'm030', false, false);

-- 9. 插入模块数据（售后服务管理模块）
INSERT IGNORE INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m034', NOW(), NOW(), '售后服务管理', 'After-Sale Service Management', 1, 7, '/after-sale', 'el-icon-service', 'after-sale', NULL, true, true),
                                                                                                                                                       ('m035', NOW(), NOW(), '售后信息录入及分配', 'Service Entry & Assignment', 2, 1, '/after-sale/entry', 'el-icon-edit', 'after-sale', 'm034', false, false),
                                                                                                                                                       ('m036', NOW(), NOW(), '返厂泵信息查询', 'Returned Pump Query', 2, 2, '/after-sale/returned-pump', 'el-icon-search', 'after-sale', 'm034', false, false),
                                                                                                                                                       ('m037', NOW(), NOW(), '维修进度查询', 'Repair Progress Query', 2, 3, '/after-sale/repair-progress', 'el-icon-view', 'after-sale', 'm034', false, false),
                                                                                                                                                       ('m038', NOW(), NOW(), '售后服务执行管理', 'Service Execution Management', 2, 4, '/after-sale/execution', 'el-icon-setting', 'after-sale', 'm034', false, false),
                                                                                                                                                       ('m039', NOW(), NOW(), '维修泵信息查询', 'Repair Pump Query', 2, 5, '/after-sale/repair-pump', 'el-icon-search', 'after-sale', 'm034', false, false),
                                                                                                                                                       ('m040', NOW(), NOW(), '售后零件营销降库', 'Parts Inventory Reduction', 2, 6, '/after-sale/parts-reduction', 'el-icon-minus', 'after-sale', 'm034', false, false),
                                                                                                                                                       ('m041', NOW(), NOW(), '售后4S店管理', '4S Store Management', 2, 7, '/after-sale/4s-store', 'el-icon-shop', 'after-sale', 'm034', true, true),
                                                                                                                                                       ('m042', NOW(), NOW(), '设备信息管理', 'Device Info Management', 3, 1, '/after-sale/4s-store/device', 'el-icon-monitor', 'after-sale', 'm041', false, false),
                                                                                                                                                       ('m043', NOW(), NOW(), '售后计划管理', 'Service Plan Management', 3, 2, '/after-sale/4s-store/plan', 'el-icon-calendar', 'after-sale', 'm041', false, false),
                                                                                                                                                       ('m044', NOW(), NOW(), '售后经验管理', 'Service Experience Management', 3, 3, '/after-sale/4s-store/experience', 'el-icon-document', 'after-sale', 'm041', false, false),
                                                                                                                                                       ('m045', NOW(), NOW(), '远程设备状态采集', 'Remote Device Monitoring', 2, 8, '/after-sale/remote-monitor', 'el-icon-connection', 'after-sale', 'm034', true, true),
                                                                                                                                                       ('m046', NOW(), NOW(), '基础信息管理配置', 'Basic Info Config', 3, 1, '/after-sale/remote-monitor/config', 'el-icon-setting', 'after-sale', 'm045', false, false),
                                                                                                                                                       ('m047', NOW(), NOW(), '设备运行监视', 'Device Runtime Monitor', 3, 2, '/after-sale/remote-monitor/runtime', 'el-icon-view', 'after-sale', 'm045', false, false),
                                                                                                                                                       ('m048', NOW(), NOW(), '专家分析', 'Expert Analysis', 3, 3, '/after-sale/remote-monitor/analysis', 'el-icon-data-analysis', 'after-sale', 'm045', false, false);

-- 10. 插入权限配置数据（超级管理员权限）
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p001', NOW(), NOW(), 'r001', 'm001', true, true, true, true),
                                                                                                                          ('p002', NOW(), NOW(), 'r001', 'm002', true, true, true, true),
                                                                                                                          ('p003', NOW(), NOW(), 'r001', 'm003', true, true, true, true),
                                                                                                                          ('p004', NOW(), NOW(), 'r001', 'm004', true, true, true, true),
                                                                                                                          ('p005', NOW(), NOW(), 'r001', 'm005', true, true, true, true),
                                                                                                                          ('p006', NOW(), NOW(), 'r001', 'm006', true, true, true, true),
                                                                                                                          ('p007', NOW(), NOW(), 'r001', 'm007', true, true, true, true),
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
                                                                                                                          ('p045', NOW(), NOW(), 'r001', 'm018', true, true, true, true),
                                                                                                                          ('p046', NOW(), NOW(), 'r001', 'm019', true, true, true, true),
                                                                                                                          ('p047', NOW(), NOW(), 'r001', 'm020', true, true, true, true),
                                                                                                                          ('p048', NOW(), NOW(), 'r001', 'm021', true, true, true, true),
                                                                                                                          ('p049', NOW(), NOW(), 'r001', 'm022', true, true, true, true),
                                                                                                                          ('p050', NOW(), NOW(), 'r001', 'm023', true, true, true, true),
                                                                                                                          ('p051', NOW(), NOW(), 'r001', 'm024', true, true, true, true),
                                                                                                                          ('p052', NOW(), NOW(), 'r001', 'm025', true, true, true, true),
                                                                                                                          ('p053', NOW(), NOW(), 'r001', 'm026', true, true, true, true),
                                                                                                                          ('p054', NOW(), NOW(), 'r001', 'm027', true, true, true, true),
                                                                                                                          ('p075', NOW(), NOW(), 'r001', 'm028', true, true, true, true),
                                                                                                                          ('p076', NOW(), NOW(), 'r001', 'm029', true, true, true, true),
                                                                                                                          ('p077', NOW(), NOW(), 'r001', 'm030', true, true, true, true),
                                                                                                                          ('p078', NOW(), NOW(), 'r001', 'm031', true, true, true, true),
                                                                                                                          ('p079', NOW(), NOW(), 'r001', 'm032', true, true, true, true),
                                                                                                                          ('p080', NOW(), NOW(), 'r001', 'm033', true, true, true, true),
                                                                                                                          ('p093', NOW(), NOW(), 'r001', 'm034', true, true, true, true),
                                                                                                                          ('p094', NOW(), NOW(), 'r001', 'm035', true, true, true, true),
                                                                                                                          ('p095', NOW(), NOW(), 'r001', 'm036', true, true, true, true),
                                                                                                                          ('p096', NOW(), NOW(), 'r001', 'm037', true, true, true, true),
                                                                                                                          ('p097', NOW(), NOW(), 'r001', 'm038', true, true, true, true),
                                                                                                                          ('p098', NOW(), NOW(), 'r001', 'm039', true, true, true, true),
                                                                                                                          ('p099', NOW(), NOW(), 'r001', 'm040', true, true, true, true),
                                                                                                                          ('p100', NOW(), NOW(), 'r001', 'm041', true, true, true, true),
                                                                                                                          ('p101', NOW(), NOW(), 'r001', 'm042', true, true, true, true),
                                                                                                                          ('p102', NOW(), NOW(), 'r001', 'm043', true, true, true, true),
                                                                                                                          ('p103', NOW(), NOW(), 'r001', 'm044', true, true, true, true),
                                                                                                                          ('p104', NOW(), NOW(), 'r001', 'm045', true, true, true, true),
                                                                                                                          ('p105', NOW(), NOW(), 'r001', 'm046', true, true, true, true),
                                                                                                                          ('p106', NOW(), NOW(), 'r001', 'm047', true, true, true, true),
                                                                                                                          ('p107', NOW(), NOW(), 'r001', 'm048', true, true, true, true);

-- 11. 插入权限配置数据（系统管理员权限）
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p008', NOW(), NOW(), 'r002', 'm001', true, true, true, true),
                                                                                                                          ('p009', NOW(), NOW(), 'r002', 'm002', true, true, true, true),
                                                                                                                          ('p010', NOW(), NOW(), 'r002', 'm003', true, true, true, true),
                                                                                                                          ('p011', NOW(), NOW(), 'r002', 'm006', true, true, true, true),
                                                                                                                          ('p012', NOW(), NOW(), 'r002', 'm007', true, true, true, true),
                                                                                                                          ('p025', NOW(), NOW(), 'r002', 'm008', true, true, true, true),
                                                                                                                          ('p026', NOW(), NOW(), 'r002', 'm009', true, true, true, true),
                                                                                                                          ('p027', NOW(), NOW(), 'r002', 'm010', true, true, true, true),
                                                                                                                          ('p028', NOW(), NOW(), 'r002', 'm011', true, true, true, true),
                                                                                                                          ('p029', NOW(), NOW(), 'r002', 'm012', true, true, true, true),
                                                                                                                          ('p030', NOW(), NOW(), 'r002', 'm013', true, true, true, true),
                                                                                                                          ('p031', NOW(), NOW(), 'r002', 'm014', true, true, true, true),
                                                                                                                          ('p032', NOW(), NOW(), 'r002', 'm015', true, true, true, true),
                                                                                                                          ('p033', NOW(), NOW(), 'r002', 'm016', true, true, true, true),
                                                                                                                          ('p034', NOW(), NOW(), 'r002', 'm017', true, true, true, true),
                                                                                                                          ('p055', NOW(), NOW(), 'r002', 'm018', true, true, true, true),
                                                                                                                          ('p056', NOW(), NOW(), 'r002', 'm019', true, true, true, true),
                                                                                                                          ('p057', NOW(), NOW(), 'r002', 'm020', true, true, true, true),
                                                                                                                          ('p058', NOW(), NOW(), 'r002', 'm021', true, true, true, true),
                                                                                                                          ('p059', NOW(), NOW(), 'r002', 'm022', true, true, true, true),
                                                                                                                          ('p060', NOW(), NOW(), 'r002', 'm023', true, true, true, true),
                                                                                                                          ('p061', NOW(), NOW(), 'r002', 'm024', true, true, true, true),
                                                                                                                          ('p062', NOW(), NOW(), 'r002', 'm025', true, true, true, true),
                                                                                                                          ('p063', NOW(), NOW(), 'r002', 'm026', true, true, true, true),
                                                                                                                          ('p064', NOW(), NOW(), 'r002', 'm027', true, true, true, true),
                                                                                                                          ('p081', NOW(), NOW(), 'r002', 'm028', true, true, true, true),
                                                                                                                          ('p082', NOW(), NOW(), 'r002', 'm029', true, true, true, true),
                                                                                                                          ('p083', NOW(), NOW(), 'r002', 'm030', true, true, true, true),
                                                                                                                          ('p084', NOW(), NOW(), 'r002', 'm031', true, true, true, true),
                                                                                                                          ('p085', NOW(), NOW(), 'r002', 'm032', true, true, true, true),
                                                                                                                          ('p086', NOW(), NOW(), 'r002', 'm033', true, true, true, true),
                                                                                                                          ('p108', NOW(), NOW(), 'r002', 'm034', true, true, true, true),
                                                                                                                          ('p109', NOW(), NOW(), 'r002', 'm035', true, true, true, true),
                                                                                                                          ('p110', NOW(), NOW(), 'r002', 'm036', true, true, true, true),
                                                                                                                          ('p111', NOW(), NOW(), 'r002', 'm037', true, true, true, true),
                                                                                                                          ('p112', NOW(), NOW(), 'r002', 'm038', true, true, true, true),
                                                                                                                          ('p113', NOW(), NOW(), 'r002', 'm039', true, true, true, true),
                                                                                                                          ('p114', NOW(), NOW(), 'r002', 'm040', true, true, true, true),
                                                                                                                          ('p115', NOW(), NOW(), 'r002', 'm041', true, true, true, true),
                                                                                                                          ('p116', NOW(), NOW(), 'r002', 'm042', true, true, true, true),
                                                                                                                          ('p117', NOW(), NOW(), 'r002', 'm043', true, true, true, true),
                                                                                                                          ('p118', NOW(), NOW(), 'r002', 'm044', true, true, true, true),
                                                                                                                          ('p119', NOW(), NOW(), 'r002', 'm045', true, true, true, true),
                                                                                                                          ('p120', NOW(), NOW(), 'r002', 'm046', true, true, true, true),
                                                                                                                          ('p121', NOW(), NOW(), 'r002', 'm047', true, true, true, true),
                                                                                                                          ('p122', NOW(), NOW(), 'r002', 'm048', true, true, true, true);

-- 12. 插入权限配置数据（普通用户权限）
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p013', NOW(), NOW(), 'r003', 'm002', true, false, false, true),
                                                                                                                          ('p014', NOW(), NOW(), 'r003', 'm006', true, false, false, true),
                                                                                                                          ('p035', NOW(), NOW(), 'r003', 'm008', true, false, false, true),
                                                                                                                          ('p036', NOW(), NOW(), 'r003', 'm009', true, false, false, true),
                                                                                                                          ('p037', NOW(), NOW(), 'r003', 'm010', true, false, false, true),
                                                                                                                          ('p038', NOW(), NOW(), 'r003', 'm011', true, false, false, true),
                                                                                                                          ('p039', NOW(), NOW(), 'r003', 'm012', true, false, false, true),
                                                                                                                          ('p040', NOW(), NOW(), 'r003', 'm013', true, false, false, true),
                                                                                                                          ('p041', NOW(), NOW(), 'r003', 'm014', true, false, false, true),
                                                                                                                          ('p042', NOW(), NOW(), 'r003', 'm015', true, false, false, true),
                                                                                                                          ('p043', NOW(), NOW(), 'r003', 'm016', true, false, false, true),
                                                                                                                          ('p044', NOW(), NOW(), 'r003', 'm017', true, false, false, true),
                                                                                                                          ('p065', NOW(), NOW(), 'r003', 'm018', true, false, false, true),
                                                                                                                          ('p066', NOW(), NOW(), 'r003', 'm019', true, false, false, true),
                                                                                                                          ('p067', NOW(), NOW(), 'r003', 'm020', true, false, false, true),
                                                                                                                          ('p068', NOW(), NOW(), 'r003', 'm021', true, false, false, true),
                                                                                                                          ('p069', NOW(), NOW(), 'r003', 'm022', true, false, false, true),
                                                                                                                          ('p070', NOW(), NOW(), 'r003', 'm023', true, false, false, true),
                                                                                                                          ('p071', NOW(), NOW(), 'r003', 'm024', true, false, false, true),
                                                                                                                          ('p072', NOW(), NOW(), 'r003', 'm025', true, false, false, true),
                                                                                                                          ('p073', NOW(), NOW(), 'r003', 'm026', true, false, false, true),
                                                                                                                          ('p074', NOW(), NOW(), 'r003', 'm027', true, false, false, true),
                                                                                                                          ('p087', NOW(), NOW(), 'r003', 'm028', true, false, false, true),
                                                                                                                          ('p088', NOW(), NOW(), 'r003', 'm029', true, false, false, true),
                                                                                                                          ('p089', NOW(), NOW(), 'r003', 'm030', true, false, false, true),
                                                                                                                          ('p090', NOW(), NOW(), 'r003', 'm031', true, false, false, true),
                                                                                                                          ('p091', NOW(), NOW(), 'r003', 'm032', true, false, false, true),
                                                                                                                          ('p092', NOW(), NOW(), 'r003', 'm033', true, false, false, true),
                                                                                                                          ('p123', NOW(), NOW(), 'r003', 'm034', true, false, false, true),
                                                                                                                          ('p124', NOW(), NOW(), 'r003', 'm035', true, false, false, true),
                                                                                                                          ('p125', NOW(), NOW(), 'r003', 'm036', true, false, false, true),
                                                                                                                          ('p126', NOW(), NOW(), 'r003', 'm037', true, false, false, true),
                                                                                                                          ('p127', NOW(), NOW(), 'r003', 'm038', true, false, false, true),
                                                                                                                          ('p128', NOW(), NOW(), 'r003', 'm039', true, false, false, true),
                                                                                                                          ('p129', NOW(), NOW(), 'r003', 'm040', true, false, false, true),
                                                                                                                          ('p130', NOW(), NOW(), 'r003', 'm041', true, false, false, true),
                                                                                                                          ('p131', NOW(), NOW(), 'r003', 'm042', true, false, false, true),
                                                                                                                          ('p132', NOW(), NOW(), 'r003', 'm043', true, false, false, true),
                                                                                                                          ('p133', NOW(), NOW(), 'r003', 'm044', true, false, false, true),
                                                                                                                          ('p134', NOW(), NOW(), 'r003', 'm045', true, false, false, true),
                                                                                                                          ('p135', NOW(), NOW(), 'r003', 'm046', true, false, false, true),
                                                                                                                          ('p136', NOW(), NOW(), 'r003', 'm047', true, false, false, true),
                                                                                                                          ('p137', NOW(), NOW(), 'r003', 'm048', true, false, false, true);

-- 13. 插入测试用户数据（密码为123456的BCrypt加密）
-- BCrypt哈希值：$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT IGNORE INTO sys_user (id, create_time, update_time, name, id_card, phone, birth_date, gender, post_id, area_id, username, password, role_id, is_approved) VALUES
                                                                                                                                                              ('u001', NOW(), NOW(), '超级管理员', '110101199001010001', '13800138001', '1990-01-01', '男', 'o001', 'o101', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'r001', true),
                                                                                                                                                              ('u002', NOW(), NOW(), '系统管理员', '110101199002020002', '13800138002', '1990-02-02', '女', 'o002', 'o102', 'sysadmin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'r002', true),
                                                                                                                                                              ('u003', NOW(), NOW(), '张三', '110101199003030003', '13800138003', '1990-03-03', '男', 'o004', 'o103', 'zhangsan', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'r003', true),
                                                                                                                                                              ('u004', NOW(), NOW(), '李四', '110101199004040004', '13800138004', '1990-04-04', '女', 'o005', 'o104', 'lisi', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'r003', true),
                                                                                                                                                              ('u005', NOW(), NOW(), '王五', '110101199005050005', '13800138005', '1990-05-05', '男', 'o006', 'o105', 'wangwu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'r003', true);

-- 14. 插入待审核用户数据
INSERT IGNORE INTO sys_user (id, create_time, update_time, name, id_card, phone, birth_date, gender, post_id, area_id, username, password, role_id, is_approved) VALUES
                                                                                                                                                              ('u006', NOW(), NOW(), '赵六', '110101199006060006', '13800138006', '1990-06-06', '女', 'o004', 'o106', 'zhaoliu', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'r003', false),
                                                                                                                                                              ('u007', NOW(), NOW(), '钱七', '110101199007070007', '13800138007', '1990-07-07', '男', 'o005', 'o101', 'qianqi', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'r003', false);

-- 15. 插入销售区域数据
INSERT IGNORE INTO sales_region (id, create_time, update_time, name, code, dept) VALUES
                                                                                      ('sr001', NOW(6), NOW(6), '华北销售区', 'HB', '销售一部'),
                                                                                      ('sr002', NOW(6), NOW(6), '华东销售区', 'HD', '销售二部'),
                                                                                      ('sr003', NOW(6), NOW(6), '华南销售区', 'HN', '销售三部'),
                                                                                      ('sr004', NOW(6), NOW(6), '西南销售区', 'XN', '销售四部'),
                                                                                      ('sr005', NOW(6), NOW(6), '华中销售区', 'HZ', '销售五部');

-- 16. 插入客户数据
INSERT IGNORE INTO customer (id, create_time, update_time, name, id_card, phone, region, industry, buyer_type, invoice_phone, invoice_address, address) VALUES
                                                                                                                                                            ('c001', NOW(6), NOW(6), '北京科技有限公司', '91110000123456789X', '010-12345678', '北京', '信息技术', '企业客户', '010-12345678', '北京市海淀区中关村大街1号', '北京市海淀区中关村大街1号'),
                                                                                                                                                            ('c002', NOW(6), NOW(6), '上海贸易有限公司', '91310000987654321Y', '021-87654321', '上海', '贸易', '企业客户', '021-87654321', '上海市浦东新区世纪大道100号', '上海市浦东新区世纪大道100号'),
                                                                                                                                                            ('c003', NOW(6), NOW(6), '广州制造企业', '91440000111222333Z', '020-11122233', '广州', '制造业', '企业客户', '020-11122233', '广州市天河区天河路200号', '广州市天河区天河路200号'),
                                                                                                                                                            ('c004', NOW(6), NOW(6), '深圳创新科技', '91440300444555666A', '0755-44455566', '深圳', '科技创新', '企业客户', '0755-44455566', '深圳市南山区科技园南路100号', '深圳市南山区科技园南路100号'),
                                                                                                                                                            ('c005', NOW(6), NOW(6), '杭州电商公司', '91330100777888999B', '0571-77788899', '杭州', '电子商务', '企业客户', '0571-77788899', '杭州市西湖区文三路200号', '杭州市西湖区文三路200号'),
                                                                                                                                                            ('c006', NOW(6), NOW(6), '张三', '110101198001010001', '13800138001', '北京', '个人', '个人客户', '13800138001', '北京市朝阳区建国路88号', '北京市朝阳区建国路88号'),
                                                                                                                                                            ('c007', NOW(6), NOW(6), '李四', '310101198002020002', '13900139002', '上海', '个人', '个人客户', '13900139002', '上海市黄浦区南京东路100号', '上海市黄浦区南京东路100号');

-- 17. 插入设备单元数据
INSERT IGNORE INTO device_unit (id, create_time, update_time, name, model, params, price, weight, lead_day) VALUES
                                                                                                                 ('du001', NOW(6), NOW(6), '智能传感器A型', 'SENSOR-A-001', '精度:±0.1%, 量程:0-1000, 输出:4-20mA', 1500.00, 0.5, 7),
                                                                                                                 ('du002', NOW(6), NOW(6), '智能传感器B型', 'SENSOR-B-002', '精度:±0.05%, 量程:0-2000, 输出:4-20mA', 2800.00, 0.8, 10),
                                                                                                                 ('du003', NOW(6), NOW(6), '数据采集器', 'DAQ-3000', '通道数:32, 采样率:100kHz, 接口:USB3.0', 8500.00, 2.5, 14),
                                                                                                                 ('du004', NOW(6), NOW(6), '工业控制器', 'PLC-X200', '输入:16路, 输出:16路, 通信:以太网', 12000.00, 3.2, 21),
                                                                                                                 ('du005', NOW(6), NOW(6), '温度变送器', 'TEMP-TX100', '量程:-50~150℃, 精度:±0.2℃, 防护:IP65', 680.00, 0.3, 5),
                                                                                                                 ('du006', NOW(6), NOW(6), '压力变送器', 'PRESS-PX200', '量程:0-10MPa, 精度:±0.1%, 输出:4-20mA', 1200.00, 0.6, 7),
                                                                                                                 ('du007', NOW(6), NOW(6), '流量计', 'FLOW-FM500', '量程:0-1000L/min, 精度:±1%, 材质:不锈钢', 3500.00, 1.8, 14),
                                                                                                                 ('du008', NOW(6), NOW(6), '液位计', 'LEVEL-LX300', '量程:0-5m, 精度:±2mm, 防护:IP68', 2200.00, 1.2, 10);

-- 18. 插入销售人员数据
INSERT IGNORE INTO sales_person (id, create_time, update_time, name, gender, birth, phone, region_id, position_id) VALUES
                                                                                                                         ('sp001', NOW(6), NOW(6), '王强', 1, '1985-03-15', '13900139001', 'sr001', 'o002'),
                                                                                                                         ('sp002', NOW(6), NOW(6), '李娜', 0, '1988-06-20', '13900139002', 'sr001', 'o003'),
                                                                                                                         ('sp003', NOW(6), NOW(6), '张伟', 1, '1990-09-10', '13900139003', 'sr002', 'o003'),
                                                                                                                         ('sp004', NOW(6), NOW(6), '刘芳', 0, '1987-12-05', '13900139004', 'sr002', 'o004'),
                                                                                                                         ('sp005', NOW(6), NOW(6), '陈明', 1, '1992-02-28', '13900139005', 'sr003', 'o004'),
                                                                                                                         ('sp006', NOW(6), NOW(6), '赵敏', 0, '1989-07-18', '13900139006', 'sr003', 'o005'),
                                                                                                                         ('sp007', NOW(6), NOW(6), '孙磊', 1, '1991-11-25', '13900139007', 'sr004', 'o003'),
                                                                                                                         ('sp008', NOW(6), NOW(6), '周静', 0, '1986-04-12', '13900139008', 'sr005', 'o002');

-- 数据初始化完成
SELECT '数据库数据初始化完成！' AS message;