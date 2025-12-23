-- 为缺少数据的表添加测试数据
-- 执行此脚本前请确保相关表已存在且有对应的关联数据

USE quanxianguanli;

-- ============================================
-- 1. 销售机会跟踪记录 (opportunity_tracking)
-- ============================================
INSERT INTO `opportunity_tracking` (`id`, `create_time`, `update_time`, `attachment`, `budget`, `customer_id`, `customer_name`, `description`, `industry`, `matter`, `opportunity_date`, `opportunity_id`, `opportunity_name`, `remarks`, `stage`, `visit_time`, `visitor`) VALUES 
('ot001', NOW(), NOW(), NULL, 500000.00, 'c001', '上海机械制造有限公司', '客户对生产线改造项目很感兴趣，已安排技术团队对接', '制造业', '技术对接', '2024-01-20', 'po001', '生产线改造项目', '首次拜访，客户需求明确', '初期接触', '2024-01-15 10:00:00.000000', '张销售'),
('ot002', NOW(), NOW(), NULL, 500000.00, 'c001', '上海机械制造有限公司', '已完成技术方案初稿，等待客户反馈', '制造业', '方案提交', '2024-01-20', 'po001', '生产线改造项目', '方案已提交，预计下周反馈', '方案阶段', '2024-01-22 14:30:00.000000', '张销售'),
('ot003', NOW(), NOW(), NULL, 300000.00, 'c002', '北京科技发展有限公司', '客户对设备升级项目有预算，需要详细报价', '科技', '报价需求', '2024-02-01', 'po002', '设备升级项目', '客户要求提供详细报价单', '报价阶段', '2024-02-05 09:00:00.000000', '赵销售'),
('ot004', NOW(), NOW(), NULL, 200000.00, 'c003', '广州电子科技有限公司', '新产品研发项目已完成，客户满意度高', '电子', '项目完成', '2024-01-10', 'po003', '新产品研发项目', '项目已顺利完成，客户非常满意', '已完成', '2024-01-25 16:00:00.000000', '王销售')
ON DUPLICATE KEY UPDATE 
    `update_time` = VALUES(`update_time`),
    `attachment` = VALUES(`attachment`),
    `budget` = VALUES(`budget`),
    `customer_id` = VALUES(`customer_id`),
    `customer_name` = VALUES(`customer_name`),
    `description` = VALUES(`description`),
    `industry` = VALUES(`industry`),
    `matter` = VALUES(`matter`),
    `opportunity_date` = VALUES(`opportunity_date`),
    `opportunity_id` = VALUES(`opportunity_id`),
    `opportunity_name` = VALUES(`opportunity_name`),
    `remarks` = VALUES(`remarks`),
    `stage` = VALUES(`stage`),
    `visit_time` = VALUES(`visit_time`),
    `visitor` = VALUES(`visitor`);

-- ============================================
-- 2. 销售机会片区关联 (opportunity_region)
-- ============================================
INSERT INTO `opportunity_region` (`id`, `create_time`, `update_time`, `opportunity_id`, `region_id`, `region_name`) VALUES 
('or001', NOW(), NOW(), 'po001', 'sr001', '华东片区'),
('or002', NOW(), NOW(), 'po002', 'sr003', '华北片区'),
('or003', NOW(), NOW(), 'po003', 'sr002', '华南片区'),
('or004', NOW(), NOW(), 'po004', 'sr001', '华东片区')
ON DUPLICATE KEY UPDATE 
    `update_time` = VALUES(`update_time`),
    `opportunity_id` = VALUES(`opportunity_id`),
    `region_id` = VALUES(`region_id`),
    `region_name` = VALUES(`region_name`);

-- ============================================
-- 3. 销售机会员工关联 (opportunity_employee)
-- ============================================
INSERT INTO `opportunity_employee` (`id`, `create_time`, `update_time`, `employee_id`, `employee_name`, `opportunity_id`) VALUES 
('oe001', NOW(), NOW(), 'sp001', '张销售', 'po001'),
('oe002', NOW(), NOW(), 'sp002', '李销售', 'po001'),
('oe003', NOW(), NOW(), 'sp004', '赵销售', 'po002'),
('oe004', NOW(), NOW(), 'sp003', '王销售', 'po003')
ON DUPLICATE KEY UPDATE 
    `update_time` = VALUES(`update_time`),
    `employee_id` = VALUES(`employee_id`),
    `employee_name` = VALUES(`employee_name`),
    `opportunity_id` = VALUES(`opportunity_id`);

-- ============================================
-- 4. 销售机会关键人物 (opportunity_key_person)
-- ============================================
INSERT INTO `opportunity_key_person` (`id`, `create_time`, `update_time`, `contact_code`, `contact_name`, `title`, `position`, `remarks`, `opportunity_id`) VALUES 
('okp001', NOW(), NOW(), 'KP001', '张总', '先生', '总经理', '主要决策人', 'po001'),
('okp002', NOW(), NOW(), 'KP002', '李经理', '女士', '采购经理', '负责采购业务', 'po001'),
('okp003', NOW(), NOW(), 'KP003', '王总', '先生', '总经理', '公司负责人', 'po002'),
('okp004', NOW(), NOW(), 'KP004', '赵经理', '女士', '技术经理', '技术负责人', 'po003')
ON DUPLICATE KEY UPDATE 
    `update_time` = VALUES(`update_time`),
    `contact_code` = VALUES(`contact_code`),
    `contact_name` = VALUES(`contact_name`),
    `title` = VALUES(`title`),
    `position` = VALUES(`position`),
    `remarks` = VALUES(`remarks`),
    `opportunity_id` = VALUES(`opportunity_id`);

-- ============================================
-- 5. 销售报价单 (sales_quotation)
-- ============================================
INSERT INTO `sales_quotation` (`id`, `create_time`, `update_time`, `budget`, `competitor_name`, `competitor_quotation`, `customer_id`, `customer_name`, `industry`, `opportunity_code`, `opportunity_id`, `opportunity_name`, `project_name`, `quotation_code`, `quotation_date`, `quotation_name`, `quotation_type`, `remarks`, `stage`, `tax_rate`, `total_price`) VALUES 
('sq001', NOW(), NOW(), 500000.00, '竞争对手A', 480000.00, 'c001', '上海机械制造有限公司', '制造业', 'OPP001', 'po001', '生产线改造项目', '生产线改造项目', 'QUO001', '2024-01-25', '生产线改造项目报价', '标准报价', '包含设备及安装服务', '方案阶段', 13.00, 565000.00),
('sq002', NOW(), NOW(), 300000.00, '竞争对手B', 290000.00, 'c002', '北京科技发展有限公司', '科技', 'OPP002', 'po002', '设备升级项目', '设备升级项目', 'QUO002', '2024-02-10', '设备升级项目报价', '优惠报价', '批量采购优惠', '报价阶段', 13.00, 339000.00),
('sq003', NOW(), NOW(), 200000.00, NULL, NULL, 'c003', '广州电子科技有限公司', '电子', 'OPP003', 'po003', '新产品研发项目', '新产品研发项目', 'QUO003', '2024-01-20', '新产品研发项目报价', '标准报价', '研发项目报价', '已完成', 13.00, 226000.00)
ON DUPLICATE KEY UPDATE 
    `update_time` = VALUES(`update_time`),
    `budget` = VALUES(`budget`),
    `competitor_name` = VALUES(`competitor_name`),
    `competitor_quotation` = VALUES(`competitor_quotation`),
    `customer_id` = VALUES(`customer_id`),
    `customer_name` = VALUES(`customer_name`),
    `industry` = VALUES(`industry`),
    `opportunity_code` = VALUES(`opportunity_code`),
    `opportunity_id` = VALUES(`opportunity_id`),
    `opportunity_name` = VALUES(`opportunity_name`),
    `project_name` = VALUES(`project_name`),
    `quotation_code` = VALUES(`quotation_code`),
    `quotation_date` = VALUES(`quotation_date`),
    `quotation_name` = VALUES(`quotation_name`),
    `quotation_type` = VALUES(`quotation_type`),
    `remarks` = VALUES(`remarks`),
    `stage` = VALUES(`stage`),
    `tax_rate` = VALUES(`tax_rate`),
    `total_price` = VALUES(`total_price`);

-- ============================================
-- 6. 报价单明细 (quotation_item)
-- ============================================
INSERT INTO `quotation_item` (`id`, `create_time`, `update_time`, `drawing_number`, `inventory_quantity`, `is_stagnant`, `product_id`, `product_name`, `quantity`, `quotation_id`, `total_price`, `unit_price`) VALUES 
('qi001', NOW(), NOW(), 'HD-1000', 10.00, b'0', 'p001', '高速电机', 5.00, 'sq001', 25000.00, 5000.00),
('qi002', NOW(), NOW(), 'JS-500', 8.00, b'0', 'p002', '减速器', 3.00, 'sq001', 9000.00, 3000.00),
('qi003', NOW(), NOW(), 'KZ-200', 15.00, b'0', 'p003', '控制器', 10.00, 'sq001', 20000.00, 2000.00),
('qi004', NOW(), NOW(), 'CG-100', 20.00, b'0', 'p004', '传感器', 8.00, 'sq001', 4000.00, 500.00),
('qi005', NOW(), NOW(), 'ZDX-1000', 1.00, b'0', 'p009', '自动化生产线', 1.00, 'sq001', 500000.00, 500000.00),
('qi006', NOW(), NOW(), 'HD-1000', 10.00, b'0', 'p001', '高速电机', 2.00, 'sq002', 10000.00, 5000.00),
('qi007', NOW(), NOW(), 'JS-500', 8.00, b'0', 'p002', '减速器', 2.00, 'sq002', 6000.00, 3000.00),
('qi008', NOW(), NOW(), 'BZ-500', 1.00, b'0', 'p010', '包装设备系统', 1.00, 'sq002', 200000.00, 200000.00),
('qi009', NOW(), NOW(), 'JC-200', 1.00, b'0', 'p011', '检测设备系统', 1.00, 'sq002', 150000.00, 150000.00),
('qi010', NOW(), NOW(), 'KZ-200', 15.00, b'0', 'p003', '控制器', 5.00, 'sq003', 10000.00, 2000.00),
('qi011', NOW(), NOW(), 'CG-100', 20.00, b'0', 'p004', '传感器', 10.00, 'sq003', 5000.00, 500.00),
('qi012', NOW(), NOW(), 'JC-200', 1.00, b'0', 'p011', '检测设备系统', 1.00, 'sq003', 150000.00, 150000.00)
ON DUPLICATE KEY UPDATE 
    `update_time` = VALUES(`update_time`),
    `drawing_number` = VALUES(`drawing_number`),
    `inventory_quantity` = VALUES(`inventory_quantity`),
    `is_stagnant` = VALUES(`is_stagnant`),
    `product_id` = VALUES(`product_id`),
    `product_name` = VALUES(`product_name`),
    `quantity` = VALUES(`quantity`),
    `quotation_id` = VALUES(`quotation_id`),
    `total_price` = VALUES(`total_price`),
    `unit_price` = VALUES(`unit_price`);

-- ============================================
-- 7. 投标信息 (bidding_info)
-- ============================================
INSERT INTO `bidding_info` (`id`, `create_time`, `update_time`, `attachment`, `bidding_code`, `bidding_name`, `bidding_result`, `bidding_status`, `bidding_summary`, `bidding_type`, `budget`, `customer_id`, `customer_name`, `industry`, `opportunity_code`, `opportunity_date`, `opportunity_id`, `opportunity_name`, `project_name`, `quotation_file`, `region`, `remarks`, `source`, `stage`, `technical_solution`) VALUES 
('bi001', NOW(), NOW(), NULL, 'BID001', '生产线改造项目投标', NULL, '待审批', NULL, '公开招标', 500000.00, 'c001', '上海机械制造有限公司', '制造业', 'OPP001', '2024-01-20', 'po001', '生产线改造项目', '生产线改造项目', '/uploads/quotation_001.pdf', '华东', '技术方案已准备', '客户推荐', '方案阶段', '/uploads/tech_solution_001.pdf'),
('bi002', NOW(), NOW(), NULL, 'BID002', '设备升级项目投标', NULL, '已审批', NULL, '邀请招标', 300000.00, 'c002', '北京科技发展有限公司', '科技', 'OPP002', '2024-02-01', 'po002', '设备升级项目', '设备升级项目', '/uploads/quotation_002.pdf', '华北', '投标文件已提交', '网络搜索', '报价阶段', '/uploads/tech_solution_002.pdf'),
('bi003', NOW(), NOW(), '/uploads/bidding_summary_003.pdf', 'BID003', '新产品研发项目投标', '已中', '已归档', '投标成功，项目已启动，客户满意度高', '竞争性谈判', 200000.00, 'c003', '广州电子科技有限公司', '电子', 'OPP003', '2024-01-10', 'po003', '新产品研发项目', '新产品研发项目', '/uploads/quotation_003.pdf', '华南', '投标成功总结', '展会', '已完成', '/uploads/tech_solution_003.pdf')
ON DUPLICATE KEY UPDATE 
    `update_time` = VALUES(`update_time`),
    `attachment` = VALUES(`attachment`),
    `bidding_code` = VALUES(`bidding_code`),
    `bidding_name` = VALUES(`bidding_name`),
    `bidding_result` = VALUES(`bidding_result`),
    `bidding_status` = VALUES(`bidding_status`),
    `bidding_summary` = VALUES(`bidding_summary`),
    `bidding_type` = VALUES(`bidding_type`),
    `budget` = VALUES(`budget`),
    `customer_id` = VALUES(`customer_id`),
    `customer_name` = VALUES(`customer_name`),
    `industry` = VALUES(`industry`),
    `opportunity_code` = VALUES(`opportunity_code`),
    `opportunity_date` = VALUES(`opportunity_date`),
    `opportunity_id` = VALUES(`opportunity_id`),
    `opportunity_name` = VALUES(`opportunity_name`),
    `project_name` = VALUES(`project_name`),
    `quotation_file` = VALUES(`quotation_file`),
    `region` = VALUES(`region`),
    `remarks` = VALUES(`remarks`),
    `source` = VALUES(`source`),
    `stage` = VALUES(`stage`),
    `technical_solution` = VALUES(`technical_solution`);

-- ============================================
-- 8. 产品附件 (product_attachment)
-- ============================================
INSERT INTO `product_attachment` (`id`, `create_time`, `update_time`, `file_name`, `file_path`, `file_size`, `file_type`, `product_id`) VALUES 
('pa001', NOW(), NOW(), '高速电机技术规格书.pdf', '/uploads/products/p001/spec_001.pdf', 1024000, 'pdf', 'p001'),
('pa002', NOW(), NOW(), '高速电机安装手册.pdf', '/uploads/products/p001/install_001.pdf', 512000, 'pdf', 'p001'),
('pa003', NOW(), NOW(), '减速器产品图册.pdf', '/uploads/products/p002/catalog_002.pdf', 2048000, 'pdf', 'p002'),
('pa004', NOW(), NOW(), '控制器使用说明.pdf', '/uploads/products/p003/manual_003.pdf', 768000, 'pdf', 'p003'),
('pa005', NOW(), NOW(), '自动化生产线视频介绍.mp4', '/uploads/products/p009/video_009.mp4', 52428800, 'mp4', 'p009'),
('pa006', NOW(), NOW(), '包装设备系统3D模型.stp', '/uploads/products/p010/model_010.stp', 10485760, 'stp', 'p010')
ON DUPLICATE KEY UPDATE 
    `update_time` = VALUES(`update_time`),
    `file_name` = VALUES(`file_name`),
    `file_path` = VALUES(`file_path`),
    `file_size` = VALUES(`file_size`),
    `file_type` = VALUES(`file_type`),
    `product_id` = VALUES(`product_id`);

-- ============================================
-- 验证数据插入
-- ============================================
SELECT '=== 销售机会跟踪记录 ===' AS info;
SELECT COUNT(*) AS count FROM opportunity_tracking;

SELECT '=== 销售机会片区关联 ===' AS info;
SELECT COUNT(*) AS count FROM opportunity_region;

SELECT '=== 销售机会员工关联 ===' AS info;
SELECT COUNT(*) AS count FROM opportunity_employee;

SELECT '=== 销售机会关键人物 ===' AS info;
SELECT COUNT(*) AS count FROM opportunity_key_person;

SELECT '=== 销售报价单 ===' AS info;
SELECT COUNT(*) AS count FROM sales_quotation;

SELECT '=== 报价单明细 ===' AS info;
SELECT COUNT(*) AS count FROM quotation_item;

SELECT '=== 投标信息 ===' AS info;
SELECT COUNT(*) AS count FROM bidding_info;

SELECT '=== 产品附件 ===' AS info;
SELECT COUNT(*) AS count FROM product_attachment;

SELECT '✅ 所有测试数据插入完成！' AS message;

