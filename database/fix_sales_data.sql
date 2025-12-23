-- 修复销售机会管理和销售降库管理的数据问题
-- 此脚本用于更新现有数据，使其符合前端查询要求

USE quanxianguanli;

-- ============================================
-- 1. 修复销售机会数据 (project_opportunity)
-- ============================================
-- 更新销售机会的opportunity_name字段（如果为空，使用project_name）
UPDATE `project_opportunity` 
SET `opportunity_name` = `project_name`
WHERE `opportunity_name` IS NULL OR `opportunity_name` = '';

-- 更新销售机会的opportunity_code（如果为空）
UPDATE `project_opportunity` 
SET `opportunity_code` = CONCAT('OPP', LPAD(SUBSTRING(id, 3), 3, '0'))
WHERE `opportunity_code` IS NULL OR `opportunity_code` = '';

-- 更新销售机会的opportunity_date（如果为空，使用project_time的日期部分）
UPDATE `project_opportunity` 
SET `opportunity_date` = DATE(`project_time`)
WHERE (`opportunity_date` IS NULL OR `opportunity_date` = '0000-00-00') 
  AND `project_time` IS NOT NULL;

-- 更新销售机会的其他必要字段
UPDATE `project_opportunity` 
SET 
    `stage` = CASE 
        WHEN `status` = '进行中' THEN '方案阶段'
        WHEN `status` = '待启动' THEN '初期接触'
        WHEN `status` = '已完成' THEN '已完成'
        WHEN `status` = '规划中' THEN '初期接触'
        ELSE '初期接触'
    END,
    `source` = '客户推荐',
    `receive_status` = '已接收',
    `inventory` = '充足',
    `budget` = CASE 
        WHEN id = 'po001' THEN 500000.00
        WHEN id = 'po002' THEN 300000.00
        WHEN id = 'po003' THEN 200000.00
        WHEN id = 'po004' THEN 400000.00
        ELSE 0.00
    END,
    `industry` = (
        SELECT `industry` 
        FROM `customer` 
        WHERE `customer`.`id` = `project_opportunity`.`customer_id` 
        LIMIT 1
    ),
    `customer_name` = (
        SELECT `unit_name` 
        FROM `customer` 
        WHERE `customer`.`id` = `project_opportunity`.`customer_id` 
        LIMIT 1
    ),
    `regions` = `project_region`
WHERE `stage` IS NULL OR `stage` = '';

-- ============================================
-- 2. 修复产品数据，添加呆滞产品用于降库管理测试
-- ============================================
-- 将部分产品标记为呆滞产品，并添加降库查询所需的参数
UPDATE `product` 
SET 
    `is_stagnant` = b'1',
    `drawing_number` = CASE 
        WHEN id = 'p001' THEN 'HD-1000'
        WHEN id = 'p002' THEN 'JS-500'
        WHEN id = 'p003' THEN 'KZ-200'
        WHEN id = 'p004' THEN 'CG-100'
        WHEN id = 'p005' THEN 'ZC-6205'
        WHEN id = 'p006' THEN 'MF-100'
        WHEN id = 'p007' THEN 'CL-50'
        WHEN id = 'p008' THEN 'LZ-80'
        ELSE `drawing_number`
    END,
    `material` = CASE 
        WHEN id = 'p001' THEN '不锈钢'
        WHEN id = 'p002' THEN '铸铁'
        WHEN id = 'p003' THEN '铝合金'
        WHEN id = 'p004' THEN '塑料'
        WHEN id = 'p005' THEN '轴承钢'
        WHEN id = 'p006' THEN '橡胶'
        WHEN id = 'p007' THEN '45钢'
        WHEN id = 'p008' THEN '碳钢'
        ELSE `material`
    END,
    `quantity` = CASE 
        WHEN id = 'p001' THEN 10.00
        WHEN id = 'p002' THEN 8.00
        WHEN id = 'p003' THEN 15.00
        WHEN id = 'p004' THEN 20.00
        WHEN id = 'p005' THEN 50.00
        WHEN id = 'p006' THEN 100.00
        WHEN id = 'p007' THEN 30.00
        WHEN id = 'p008' THEN 25.00
        ELSE `quantity`
    END,
    `storage_age` = CASE 
        WHEN id IN ('p001', 'p002', 'p003', 'p004') THEN '6个月'
        WHEN id IN ('p005', 'p006', 'p007', 'p008') THEN '12个月'
        ELSE '3个月'
    END,
    `expected_delivery_date` = DATE_ADD(CURDATE(), INTERVAL 7 DAY),
    -- 添加降库查询参数（示例数据）
    `caliber` = CASE 
        WHEN id IN ('p001', 'p002') THEN '100mm'
        WHEN id IN ('p003', 'p004') THEN '80mm'
        ELSE NULL
    END,
    `motor_power` = CASE 
        WHEN id = 'p001' THEN '1000W'
        WHEN id = 'p002' THEN '500W'
        ELSE NULL
    END,
    `flow` = CASE 
        WHEN id IN ('p001', 'p002') THEN '100L/min'
        ELSE NULL
    END,
    `head` = CASE 
        WHEN id IN ('p001', 'p002') THEN '50m'
        ELSE NULL
    END,
    `filter_material` = CASE 
        WHEN id = 'p004' THEN 'PP材质'
        WHEN id = 'p006' THEN '橡胶'
        ELSE NULL
    END,
    `inlet_pressure` = CASE 
        WHEN id IN ('p001', 'p002') THEN '0.6MPa'
        ELSE NULL
    END,
    `outlet_pressure` = CASE 
        WHEN id IN ('p001', 'p002') THEN '0.4MPa'
        ELSE NULL
    END,
    `is_reduced_stock` = b'0'
WHERE id IN ('p001', 'p002', 'p003', 'p004', 'p005', 'p006', 'p007', 'p008');

-- 为其他产品也添加基础字段
UPDATE `product` 
SET 
    `drawing_number` = CONCAT('DR-', LPAD(SUBSTRING(id, 2), 3, '0')),
    `material` = '标准材料',
    `quantity` = 10.00,
    `storage_age` = '3个月',
    `expected_delivery_date` = DATE_ADD(CURDATE(), INTERVAL 15 DAY),
    `is_stagnant` = b'0',
    `is_reduced_stock` = b'0'
WHERE `drawing_number` IS NULL OR `drawing_number` = '';

-- ============================================
-- 验证数据修复结果
-- ============================================
SELECT '=== 销售机会数据验证 ===' AS info;
SELECT 
    id,
    opportunity_name,
    opportunity_code,
    opportunity_date,
    stage,
    source,
    budget,
    industry,
    customer_name
FROM `project_opportunity`
LIMIT 5;

SELECT '=== 呆滞产品数据验证 ===' AS info;
SELECT 
    id,
    name,
    drawing_number,
    material,
    quantity,
    storage_age,
    is_stagnant,
    is_reduced_stock,
    caliber,
    motor_power
FROM `product`
WHERE `is_stagnant` = b'1'
LIMIT 10;

SELECT '=== 数据统计 ===' AS info;
SELECT 
    '销售机会总数' AS type,
    COUNT(*) AS count
FROM `project_opportunity`
UNION ALL
SELECT 
    '呆滞产品总数' AS type,
    COUNT(*) AS count
FROM `product`
WHERE `is_stagnant` = b'1'
UNION ALL
SELECT 
    '已标记降库产品数' AS type,
    COUNT(*) AS count
FROM `product`
WHERE `is_reduced_stock` = b'1';

SELECT '✅ 销售数据修复完成！' AS message;

