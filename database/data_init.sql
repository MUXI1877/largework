- 权限管理系统 - 数据初始化脚本
-- 注意：此脚本应在JPA自动创建表结构后执行

USE quanxianguanli;

-- 1. 插入角色数据
INSERT INTO sys_role (id, create_time, update_time, role_name, role_desc) VALUES
                                                                              ('r001', NOW(), NOW(), '超级管理员', '系统最高权限管理员'),
                                                                              ('r002', NOW(), NOW(), '系统管理员', '系统日常管理维护人员'),
                                                                              ('r003', NOW(), NOW(), '普通用户', '普通系统用户');

-- 2. 插入选项数据（岗位分组）
INSERT INTO sys_option (id, create_time, update_time, group_name, title, option_value, sort) VALUES
                                                                                                 ('o001', NOW(), NOW(), 'post', '总经理', 'general_manager', 1),
                                                                                                 ('o002', NOW(), NOW(), 'post', '部门经理', 'department_manager', 2),
                                                                                                 ('o003', NOW(), NOW(), 'post', '项目经理', 'project_manager', 3),
                                                                                                 ('o004', NOW(), NOW(), 'post', '开发工程师', 'developer', 4),
                                                                                                 ('o005', NOW(), NOW(), 'post', '测试工程师', 'tester', 5),
                                                                                                 ('o006', NOW(), NOW(), 'post', '产品经理', 'product_manager', 6);

-- 3. 插入选项数据（地区分组）
INSERT INTO sys_option (id, create_time, update_time, group_name, title, option_value, sort) VALUES
                                                                                                 ('o101', NOW(), NOW(), 'area', '北京', 'beijing', 1),
                                                                                                 ('o102', NOW(), NOW(), 'area', '上海', 'shanghai', 2),
                                                                                                 ('o103', NOW(), NOW(), 'area', '广州', 'guangzhou', 3),
                                                                                                 ('o104', NOW(), NOW(), 'area', '深圳', 'shenzhen', 4),
                                                                                                 ('o105', NOW(), NOW(), 'area', '杭州', 'hangzhou', 5),
                                                                                                 ('o106', NOW(), NOW(), 'area', '成都', 'chengdu', 6);

-- 4. 插入模块数据（系统管理模块）
INSERT INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m001', NOW(), NOW(), '系统管理', 'System Management', 1, 1, '/system', 'el-icon-setting', 'system', NULL, true, true),
                                                                                                                                                       ('m002', NOW(), NOW(), '账号管理', 'User Management', 2, 1, '/user-management', 'el-icon-user', 'system', 'm001', false, false),
                                                                                                                                                       ('m003', NOW(), NOW(), '角色管理', 'Role Management', 2, 2, '/role-management', 'el-icon-s-custom', 'system', 'm001', false, false),
                                                                                                                                                       ('m004', NOW(), NOW(), '模块管理', 'Module Management', 2, 3, '/module-management', 'el-icon-menu', 'system', 'm001', false, false),
                                                                                                                                                       ('m005', NOW(), NOW(), '权限配置', 'Permission Config', 2, 4, '/permission-management', 'el-icon-lock', 'system', 'm001', false, false);

-- 5. 插入模块数据（选项管理模块）
INSERT INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m006', NOW(), NOW(), '基础数据', 'Basic Data', 1, 2, '/basic', 'el-icon-data-board', 'basic', NULL, true, true),
                                                                                                                                                       ('m007', NOW(), NOW(), '选项管理', 'Option Management', 2, 1, '/option-management', 'el-icon-collection', 'basic', 'm006', false, false);

-- 6. 插入模块数据（基本信息管理模块）
INSERT INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
                                                                                                                                                       ('m011', NOW(), NOW(), '基本信息管理', 'Basic Information Management', 1, 3, '/basic-info', 'el-icon-folder', 'basic', NULL, true, true),
                                                                                                                                                       ('m008', NOW(), NOW(), '客户管理', 'Customer Management', 2, 1, '/customer-management', 'el-icon-user-solid', 'basic', 'm011', false, false),
                                                                                                                                                       ('m009', NOW(), NOW(), '团队信息管理', 'Team Management', 2, 2, '/team-management', 'el-icon-s-custom', 'basic', 'm011', false, false),
                                                                                                                                                       ('m010', NOW(), NOW(), '产品管理', 'Product Management', 2, 3, '/product-management', 'el-icon-box', 'basic', 'm011', false, false);

-- 7. 插入权限配置数据（超级管理员权限）
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p001', NOW(), NOW(), 'r001', 'm001', true, true, true, true),
                                                                                                                          ('p002', NOW(), NOW(), 'r001', 'm002', true, true, true, true),
                                                                                                                          ('p003', NOW(), NOW(), 'r001', 'm003', true, true, true, true),
                                                                                                                          ('p004', NOW(), NOW(), 'r001', 'm004', true, true, true, true),
                                                                                                                          ('p005', NOW(), NOW(), 'r001', 'm005', true, true, true, true),
                                                                                                                          ('p006', NOW(), NOW(), 'r001', 'm006', true, true, true, true),
                                                                                                                          ('p007', NOW(), NOW(), 'r001', 'm007', true, true, true, true),
                                                                                                                          ('p015', NOW(), NOW(), 'r001', 'm011', true, true, true, true),
                                                                                                                          ('p016', NOW(), NOW(), 'r001', 'm008', true, true, true, true),
                                                                                                                          ('p017', NOW(), NOW(), 'r001', 'm009', true, true, true, true),
                                                                                                                          ('p018', NOW(), NOW(), 'r001', 'm010', true, true, true, true);

-- 8. 插入权限配置数据（系统管理员权限）
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p008', NOW(), NOW(), 'r002', 'm001', true, true, true, true),
                                                                                                                          ('p009', NOW(), NOW(), 'r002', 'm002', true, true, true, true),
                                                                                                                          ('p010', NOW(), NOW(), 'r002', 'm003', true, true, true, true),
                                                                                                                          ('p011', NOW(), NOW(), 'r002', 'm006', true, true, true, true),
                                                                                                                          ('p012', NOW(), NOW(), 'r002', 'm007', true, true, true, true),
                                                                                                                          ('p019', NOW(), NOW(), 'r002', 'm011', true, true, true, true),
                                                                                                                          ('p020', NOW(), NOW(), 'r002', 'm008', true, true, true, true),
                                                                                                                          ('p021', NOW(), NOW(), 'r002', 'm009', true, true, true, true),
                                                                                                                          ('p022', NOW(), NOW(), 'r002', 'm010', true, true, true, true);

-- 9. 插入权限配置数据（普通用户权限）
INSERT INTO sys_permission (id, create_time, update_time, role_id, module_id, can_read, can_add, can_update, can_see) VALUES
                                                                                                                          ('p013', NOW(), NOW(), 'r003', 'm002', true, false, false, true),
                                                                                                                          ('p014', NOW(), NOW(), 'r003', 'm006', true, false, false, true),
                                                                                                                          ('p023', NOW(), NOW(), 'r003', 'm011', true, false, false, true),
                                                                                                                          ('p024', NOW(), NOW(), 'r003', 'm008', true, false, false, true),
                                                                                                                          ('p025', NOW(), NOW(), 'r003', 'm009', true, false, false, true),
                                                                                                                          ('p026', NOW(), NOW(), 'r003', 'm010', true, false, false, true);

-- 10. 插入测试用户数据（密码为123456的MD5加密）
INSERT INTO sys_user (id, create_time, update_time, name, id_card, phone, birth_date, gender, post_id, area_id, username, password, role_id, is_approved) VALUES
                                                                                                                                                              ('u001', NOW(), NOW(), '超级管理员', '110101199001010001', '13800138001', '1990-01-01', '男', 'o001', 'o101', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'r001', true),
                                                                                                                                                              ('u002', NOW(), NOW(), '系统管理员', '110101199002020002', '13800138002', '1990-02-02', '女', 'o002', 'o102', 'sysadmin', 'e10adc3949ba59abbe56e057f20f883e', 'r002', true),
                                                                                                                                                              ('u003', NOW(), NOW(), '张三', '110101199003030003', '13800138003', '1990-03-03', '男', 'o004', 'o103', 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', 'r003', true),
                                                                                                                                                              ('u004', NOW(), NOW(), '李四', '110101199004040004', '13800138004', '1990-04-04', '女', 'o005', 'o104', 'lisi', 'e10adc3949ba59abbe56e057f20f883e', 'r003', true),
                                                                                                                                                              ('u005', NOW(), NOW(), '王五', '110101199005050005', '13800138005', '1990-05-05', '男', 'o006', 'o105', 'wangwu', 'e10adc3949ba59abbe56e057f20f883e', 'r003', true);

-- 11. 插入待审核用户数据
INSERT INTO sys_user (id, create_time, update_time, name, id_card, phone, birth_date, gender, post_id, area_id, username, password, role_id, is_approved) VALUES
                                                                                                                                                              ('u006', NOW(), NOW(), '赵六', '110101199006060006', '13800138006', '1990-06-06', '女', 'o004', 'o106', 'zhaoliu', 'e10adc3949ba59abbe56e057f20f883e', 'r003', false),
                                                                                                                                                              ('u007', NOW(), NOW(), '钱七', '110101199007070007', '13800138007', '1990-07-07', '男', 'o005', 'o101', 'qianqi', 'e10adc3949ba59abbe56e057f20f883e', 'r003', false);

-- 数据初始化完成
SELECT '数据库数据初始化完成！' AS message;