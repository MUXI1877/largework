-- 添加售后服务管理模块
-- 注意：此脚本用于向现有数据库添加售后服务管理模块

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE quanxianguanli;

-- 插入售后服务管理模块
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

-- 插入超级管理员权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
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

-- 插入系统管理员权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
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

-- 插入普通用户权限
INSERT IGNORE INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
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

SELECT '售后服务管理模块添加完成！' AS message;

