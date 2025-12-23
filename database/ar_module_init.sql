-- 应收账管理模块初始化

-- 1. 创建应收账计划表
CREATE TABLE IF NOT EXISTS `receivable_plan` (
  `id` varchar(36) NOT NULL,
  `create_time` datetime(6) NOT NULL,
  `update_time` datetime(6) NOT NULL,
  `contract_code` varchar(50) NOT NULL,
  `contract_name` varchar(200) NOT NULL,
  `payment_stage` varchar(50) DEFAULT NULL,
  `payment_stage_name` varchar(100) DEFAULT NULL,
  `due_amount` decimal(15,2) DEFAULT NULL,
  `paid_amount` decimal(15,2) DEFAULT 0,
  `due_date` date DEFAULT NULL,
  `paid_date` date DEFAULT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `status` varchar(30) DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应收账计划';

-- 2. 创建回款登记表
CREATE TABLE IF NOT EXISTS `receivable_receipt` (
  `id` varchar(36) NOT NULL,
  `create_time` datetime(6) NOT NULL,
  `update_time` datetime(6) NOT NULL,
  `contract_code` varchar(50) NOT NULL,
  `contract_name` varchar(200) NOT NULL,
  `contract_amount` decimal(15,2) DEFAULT NULL,
  `company_name` varchar(200) DEFAULT NULL,
  `company_code` varchar(100) DEFAULT NULL,
  `remaining_amount` decimal(15,2) DEFAULT NULL,
  `receive_date` date DEFAULT NULL,
  `receive_amount` decimal(15,2) DEFAULT NULL,
  `remarks` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回款登记';

-- 3. 模块与权限
-- 主模块：应收账管理 (m020)，子模块：计划(m021)、回款登记(m022)、查询(m023)
INSERT INTO `sys_module` (`id`,`create_time`,`update_time`,`cn_name`,`en_name`,`menu_level`,`sort`,`path`,`icon`,`group_name`,`parent_id`,`is_parent`,`is_expand`,`permission`,`visible`)
SELECT 'm020', NOW(), NOW(), '应收账管理', 'AR Management', 1, 5, '/ar', 'el-icon-money', 'finance', NULL, 1, 1, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_module WHERE id='m020');

INSERT INTO `sys_module` (`id`,`create_time`,`update_time`,`cn_name`,`en_name`,`menu_level`,`sort`,`path`,`icon`,`group_name`,`parent_id`,`is_parent`,`is_expand`,`permission`,`visible`)
SELECT 'm021', NOW(), NOW(), '应收账计划', 'AR Plan', 2, 1, '/ar-plan', 'el-icon-document', 'finance', 'm020', 0, 0, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_module WHERE id='m021');

INSERT INTO `sys_module` (`id`,`create_time`,`update_time`,`cn_name`,`en_name`,`menu_level`,`sort`,`path`,`icon`,`group_name`,`parent_id`,`is_parent`,`is_expand`,`permission`,`visible`)
SELECT 'm022', NOW(), NOW(), '回款登记', 'AR Receipt', 2, 2, '/ar-receipt', 'el-icon-edit', 'finance', 'm020', 0, 0, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_module WHERE id='m022');

INSERT INTO `sys_module` (`id`,`create_time`,`update_time`,`cn_name`,`en_name`,`menu_level`,`sort`,`path`,`icon`,`group_name`,`parent_id`,`is_parent`,`is_expand`,`permission`,`visible`)
SELECT 'm023', NOW(), NOW(), '应收账查询', 'AR Query', 2, 3, '/ar-query', 'el-icon-search', 'finance', 'm020', 0, 0, NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM sys_module WHERE id='m023');

-- 权限：r001/r002全权限，r003只读+可见
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r001', 'm020', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r001' AND module_id='m020');
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r001', 'm021', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r001' AND module_id='m021');
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r001', 'm022', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r001' AND module_id='m022');
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r001', 'm023', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r001' AND module_id='m023');

INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r002', 'm020', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r002' AND module_id='m020');
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r002', 'm021', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r002' AND module_id='m021');
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r002', 'm022', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r002' AND module_id='m022');
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r002', 'm023', 1,1,1,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r002' AND module_id='m023');

INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r003', 'm020', 1,0,0,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r003' AND module_id='m020');
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r003', 'm021', 1,0,0,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r003' AND module_id='m021');
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r003', 'm022', 1,0,0,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r003' AND module_id='m022');
INSERT INTO `sys_permission` (`id`,`create_time`,`update_time`,`role_id`,`module_id`,`can_read`,`can_add`,`can_update`,`can_see`)
SELECT CONCAT('p', LPAD((SELECT COUNT(*)+1 FROM sys_permission),3,'0')), NOW(), NOW(), 'r003', 'm023', 1,0,0,1
WHERE NOT EXISTS (SELECT 1 FROM sys_permission WHERE role_id='r003' AND module_id='m023');

-- 4. 示例数据（可选）
INSERT INTO `receivable_plan` (`id`,`create_time`,`update_time`,`contract_code`,`contract_name`,`payment_stage`,`payment_stage_name`,`due_amount`,`paid_amount`,`due_date`,`paid_date`,`owner`,`status`,`remarks`)
VALUES ('rp001', NOW(), NOW(), 'HT2024001', '生产线设备采购合同', '30%', '首付款', 150000.00, 50000.00, '2024-02-01', '2024-01-15', '张经理', 'PARTIAL', '首付款已部分收到')
ON DUPLICATE KEY UPDATE update_time=VALUES(update_time);

INSERT INTO `receivable_receipt` (`id`,`create_time`,`update_time`,`contract_code`,`contract_name`,`contract_amount`,`company_name`,`company_code`,`remaining_amount`,`receive_date`,`receive_amount`,`remarks`)
VALUES ('rr001', NOW(), NOW(), 'HT2024001', '生产线设备采购合同', 500000.00, '上海机械制造有限公司', 'CUST001', 450000.00, '2024-01-15', 50000.00, '首付款到账')
ON DUPLICATE KEY UPDATE update_time=VALUES(update_time);

