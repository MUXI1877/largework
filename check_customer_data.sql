-- 检查客户数据的SQL查询脚本

-- 1. 查看所有客户数据
SELECT 
    id,
    customer_code AS '客户编号',
    unit_name AS '单位名称',
    region AS '所属区域',
    industry AS '行业',
    create_time AS '创建时间',
    update_time AS '更新时间'
FROM customer
ORDER BY create_time DESC;

-- 2. 查找CUST006客户
SELECT 
    id,
    customer_code AS '客户编号',
    unit_name AS '单位名称',
    region AS '所属区域',
    industry AS '行业',
    create_time AS '创建时间',
    update_time AS '更新时间'
FROM customer
WHERE customer_code = 'CUST006';

-- 3. 查找包含"长安"的客户
SELECT 
    id,
    customer_code AS '客户编号',
    unit_name AS '单位名称',
    region AS '所属区域',
    industry AS '行业',
    create_time AS '创建时间',
    update_time AS '更新时间'
FROM customer
WHERE unit_name LIKE '%长安%';

-- 4. 统计客户总数
SELECT COUNT(*) AS '客户总数' FROM customer;

-- 5. 查看最近创建的10个客户
SELECT 
    id,
    customer_code AS '客户编号',
    unit_name AS '单位名称',
    create_time AS '创建时间'
FROM customer
ORDER BY create_time DESC
LIMIT 10;

-- 6. 检查是否有ID为空的记录（不应该存在）
SELECT 
    id,
    customer_code AS '客户编号',
    unit_name AS '单位名称'
FROM customer
WHERE id IS NULL OR id = '';

