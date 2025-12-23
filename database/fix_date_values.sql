-- 修复数据库中的无效日期值
-- MySQL 8.0 不允许 '0000-00-00' 这样的无效日期
-- 此脚本用于清理和修复所有无效日期值

USE quanxianguanli;

-- ============================================
-- 修复 project_opportunity 表的日期字段
-- ============================================
-- 将无效日期设置为 NULL
UPDATE `project_opportunity` 
SET `opportunity_date` = NULL
WHERE `opportunity_date` = '0000-00-00' OR `opportunity_date` = '';

-- 从 project_time 提取有效日期
UPDATE `project_opportunity` 
SET `opportunity_date` = DATE(`project_time`)
WHERE `opportunity_date` IS NULL 
  AND `project_time` IS NOT NULL
  AND `project_time` != '0000-00-00 00:00:00';

-- 如果仍然为空，设置为当前日期
UPDATE `project_opportunity` 
SET `opportunity_date` = CURDATE()
WHERE `opportunity_date` IS NULL;

-- ============================================
-- 修复 product 表的日期字段
-- ============================================
-- 将无效日期设置为 NULL
UPDATE `product` 
SET `expected_delivery_date` = NULL
WHERE `expected_delivery_date` = '0000-00-00' OR `expected_delivery_date` = '';

-- 为呆滞产品设置预计交货期
UPDATE `product` 
SET `expected_delivery_date` = DATE_ADD(CURDATE(), INTERVAL 7 DAY)
WHERE `expected_delivery_date` IS NULL 
  AND `is_stagnant` = b'1';

-- 为其他产品设置预计交货期
UPDATE `product` 
SET `expected_delivery_date` = DATE_ADD(CURDATE(), INTERVAL 15 DAY)
WHERE `expected_delivery_date` IS NULL;

-- ============================================
-- 修复 customer_visit 表的日期字段
-- ============================================
UPDATE `customer_visit` 
SET `visit_date` = NULL
WHERE `visit_date` = '0000-00-00' OR `visit_date` = '';

UPDATE `customer_visit` 
SET `visit_date` = CURDATE()
WHERE `visit_date` IS NULL;

-- ============================================
-- 修复 sales_quotation 表的日期字段
-- ============================================
UPDATE `sales_quotation` 
SET `quotation_date` = NULL
WHERE `quotation_date` = '0000-00-00' OR `quotation_date` = '';

UPDATE `sales_quotation` 
SET `quotation_date` = CURDATE()
WHERE `quotation_date` IS NULL;

-- ============================================
-- 修复 bidding_info 表的日期字段
-- ============================================
UPDATE `bidding_info` 
SET `opportunity_date` = NULL
WHERE `opportunity_date` = '0000-00-00' OR `opportunity_date` = '';

UPDATE `bidding_info` 
SET `opportunity_date` = CURDATE()
WHERE `opportunity_date` IS NULL;

-- ============================================
-- 验证修复结果
-- ============================================
SELECT '=== 日期字段修复完成 ===' AS info;
SELECT 
    'project_opportunity' AS table_name,
    COUNT(*) AS total,
    SUM(CASE WHEN opportunity_date IS NULL THEN 1 ELSE 0 END) AS null_dates
FROM `project_opportunity`
UNION ALL
SELECT 
    'product' AS table_name,
    COUNT(*) AS total,
    SUM(CASE WHEN expected_delivery_date IS NULL THEN 1 ELSE 0 END) AS null_dates
FROM `product`
UNION ALL
SELECT 
    'customer_visit' AS table_name,
    COUNT(*) AS total,
    SUM(CASE WHEN visit_date IS NULL THEN 1 ELSE 0 END) AS null_dates
FROM `customer_visit`;

SELECT '✅ 日期值修复完成！' AS message;

