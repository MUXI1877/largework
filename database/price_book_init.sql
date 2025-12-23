-- 价格本管理模块初始化脚本

USE quanxianguanli;

-- ============================================
-- 1. 创建价格本表
-- ============================================
DROP TABLE IF EXISTS `price_book_log`;
DROP TABLE IF EXISTS `price_book`;

CREATE TABLE `price_book` (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(6) NOT NULL,
  `update_time` datetime(6) NOT NULL,
  `product_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品ID（关联产品管理模块）',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品名称（从产品表同步）',
  `product_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '产品类别：SINGLE（单泵/单体设备）、COMPLETE（成套设备）、SPARE（备品备件）',
  `model` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '型号（从产品表同步）',
  `parameters` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '参数（从产品表同步）',
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '部门：销售处、成套处、外贸处等',
  `unit_price` decimal(15, 2) NOT NULL COMMENT '单价',
  `remarks` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_product_department` (`product_id`, `department`) USING BTREE COMMENT '同一产品在同一部门只能有一个价格本',
  KEY `idx_product_type` (`product_type`) USING BTREE,
  KEY `idx_department` (`department`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '价格本表' ROW_FORMAT = DYNAMIC;

-- ============================================
-- 2. 创建价格本修改日志表
-- ============================================
CREATE TABLE `price_book_log` (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime(6) NOT NULL,
  `update_time` datetime(6) NOT NULL,
  `price_book_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '价格本ID',
  `product_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '产品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '产品名称',
  `department` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门',
  `old_price` decimal(15, 2) NULL DEFAULT NULL COMMENT '原价格',
  `new_price` decimal(15, 2) NULL DEFAULT NULL COMMENT '新价格',
  `operator_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作人姓名',
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作类型：CREATE（新增）、UPDATE（修改）、DELETE（删除）',
  `remarks` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_price_book_id` (`price_book_id`) USING BTREE,
  KEY `idx_product_id` (`product_id`) USING BTREE,
  KEY `idx_operator_id` (`operator_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '价格本修改日志表' ROW_FORMAT = DYNAMIC;

-- ============================================
-- 3. 添加价格本管理模块到系统模块表
-- ============================================
-- 检查是否已存在，避免重复插入
INSERT INTO `sys_module` (`id`, `create_time`, `update_time`, `cn_name`, `en_name`, `menu_level`, `sort`, `path`, `icon`, `group_name`, `parent_id`, `is_parent`, `is_expand`, `permission`, `visible`)
SELECT 'm019', NOW(), NOW(), '价格本管理', 'Price Book Management', 2, 7, '/price-book-management', 'el-icon-money', 'sales', 'm017', 0, 0, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM `sys_module` WHERE `id` = 'm019');

-- ============================================
-- 4. 为现有角色添加价格本管理权限
-- ============================================
-- 超级管理员（r001）- 全部权限
INSERT INTO `sys_permission` (`id`, `create_time`, `update_time`, `role_id`, `module_id`, `can_read`, `can_add`, `can_update`, `can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*) FROM sys_permission) + 1, 3, '0')), NOW(), NOW(), 'r001', 'm019', 1, 1, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM `sys_permission` WHERE `role_id` = 'r001' AND `module_id` = 'm019');

-- 系统管理员（r002）- 全部权限
INSERT INTO `sys_permission` (`id`, `create_time`, `update_time`, `role_id`, `module_id`, `can_read`, `can_add`, `can_update`, `can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*) FROM sys_permission) + 1, 3, '0')), NOW(), NOW(), 'r002', 'm019', 1, 1, 1, 1
WHERE NOT EXISTS (SELECT 1 FROM `sys_permission` WHERE `role_id` = 'r002' AND `module_id` = 'm019');

-- 普通用户（r003）- 仅查看权限
INSERT INTO `sys_permission` (`id`, `create_time`, `update_time`, `role_id`, `module_id`, `can_read`, `can_add`, `can_update`, `can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*) FROM sys_permission) + 1, 3, '0')), NOW(), NOW(), 'r003', 'm019', 1, 0, 0, 1
WHERE NOT EXISTS (SELECT 1 FROM `sys_permission` WHERE `role_id` = 'r003' AND `module_id` = 'm019');

-- ============================================
-- 5. 插入示例数据（可选）
-- ============================================
-- 为部分产品创建示例价格本数据（销售处）
INSERT INTO `price_book` (`id`, `create_time`, `update_time`, `product_id`, `product_name`, `product_type`, `model`, `parameters`, `department`, `unit_price`, `remarks`)
VALUES 
    ('pb001', NOW(), NOW(), 'p001', '高速电机', 'SINGLE', 'HD-1000', '功率：1000W，转速：3000rpm，电压：220V', '销售处', 5000.00, '销售处价格'),
    ('pb002', NOW(), NOW(), 'p002', '减速器', 'SINGLE', 'JS-500', '减速比：1:50，扭矩：500N·m', '销售处', 3000.00, '销售处价格'),
    ('pb003', NOW(), NOW(), 'p003', '控制器', 'SINGLE', 'KZ-200', '输入：220V，输出：24V，功率：200W', '销售处', 2000.00, '销售处价格'),
    ('pb004', NOW(), NOW(), 'p004', '传感器', 'SINGLE', 'CG-100', '精度：0.1%，量程：0-1000', '销售处', 500.00, '销售处价格'),
    ('pb005', NOW(), NOW(), 'p009', '自动化生产线', 'COMPLETE', 'ZDX-1000', '产能：1000件/小时，工位：10个，自动化程度：90%', '销售处', 500000.00, '销售处价格'),
    ('pb006', NOW(), NOW(), 'p010', '包装设备系统', 'COMPLETE', 'BZ-500', '包装速度：500包/分钟，包装规格：可调', '销售处', 200000.00, '销售处价格'),
    ('pb007', NOW(), NOW(), 'p011', '检测设备系统', 'COMPLETE', 'JC-200', '检测精度：0.01mm，检测速度：200件/分钟', '销售处', 150000.00, '销售处价格')
ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- 为部分产品创建示例价格本数据（成套处）
INSERT INTO `price_book` (`id`, `create_time`, `update_time`, `product_id`, `product_name`, `product_type`, `model`, `parameters`, `department`, `unit_price`, `remarks`)
VALUES 
    ('pb008', NOW(), NOW(), 'p001', '高速电机', 'SINGLE', 'HD-1000', '功率：1000W，转速：3000rpm，电压：220V', '成套处', 4750.00, '成套处价格（批量优惠）'),
    ('pb009', NOW(), NOW(), 'p002', '减速器', 'SINGLE', 'JS-500', '减速比：1:50，扭矩：500N·m', '成套处', 2850.00, '成套处价格（批量优惠）'),
    ('pb010', NOW(), NOW(), 'p003', '控制器', 'SINGLE', 'KZ-200', '输入：220V，输出：24V，功率：200W', '成套处', 1900.00, '成套处价格（批量优惠）'),
    ('pb011', NOW(), NOW(), 'p004', '传感器', 'SINGLE', 'CG-100', '精度：0.1%，量程：0-1000', '成套处', 475.00, '成套处价格（批量优惠）'),
    ('pb012', NOW(), NOW(), 'p009', '自动化生产线', 'COMPLETE', 'ZDX-1000', '产能：1000件/小时，工位：10个，自动化程度：90%', '成套处', 475000.00, '成套处价格（批量优惠）'),
    ('pb013', NOW(), NOW(), 'p010', '包装设备系统', 'COMPLETE', 'BZ-500', '包装速度：500包/分钟，包装规格：可调', '成套处', 190000.00, '成套处价格（批量优惠）'),
    ('pb014', NOW(), NOW(), 'p011', '检测设备系统', 'COMPLETE', 'JC-200', '检测精度：0.01mm，检测速度：200件/分钟', '成套处', 142500.00, '成套处价格（批量优惠）')
ON DUPLICATE KEY UPDATE `update_time` = NOW();

-- 为部分产品创建示例价格本数据（外贸处）
INSERT INTO `price_book` (`id`, `create_time`, `update_time`, `product_id`, `product_name`, `product_type`, `model`, `parameters`, `department`, `unit_price`, `remarks`)
VALUES 
    ('pb015', NOW(), NOW(), 'p001', '高速电机', 'SINGLE', 'HD-1000', '功率：1000W，转速：3000rpm，电压：220V', '外贸处', 5500.00, '外贸处价格（含出口费用）'),
    ('pb016', NOW(), NOW(), 'p002', '减速器', 'SINGLE', 'JS-500', '减速比：1:50，扭矩：500N·m', '外贸处', 3300.00, '外贸处价格（含出口费用）'),
    ('pb017', NOW(), NOW(), 'p003', '控制器', 'SINGLE', 'KZ-200', '输入：220V，输出：24V，功率：200W', '外贸处', 2200.00, '外贸处价格（含出口费用）'),
    ('pb018', NOW(), NOW(), 'p004', '传感器', 'SINGLE', 'CG-100', '精度：0.1%，量程：0-1000', '外贸处', 550.00, '外贸处价格（含出口费用）'),
    ('pb019', NOW(), NOW(), 'p009', '自动化生产线', 'COMPLETE', 'ZDX-1000', '产能：1000件/小时，工位：10个，自动化程度：90%', '外贸处', 550000.00, '外贸处价格（含出口费用）'),
    ('pb020', NOW(), NOW(), 'p010', '包装设备系统', 'COMPLETE', 'BZ-500', '包装速度：500包/分钟，包装规格：可调', '外贸处', 220000.00, '外贸处价格（含出口费用）'),
    ('pb021', NOW(), NOW(), 'p011', '检测设备系统', 'COMPLETE', 'JC-200', '检测精度：0.01mm，检测速度：200件/分钟', '外贸处', 165000.00, '外贸处价格（含出口费用）')
ON DUPLICATE KEY UPDATE `update_time` = NOW();

SELECT '✅ 价格本管理模块初始化完成！' AS message;
SELECT '模块ID: m019' AS module_info;
SELECT '路径: /price-book-management' AS path_info;

