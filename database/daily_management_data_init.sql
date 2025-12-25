-- 日常管理模块测试数据初始化脚本
-- 注意：此脚本用于向日常管理模块添加测试数据

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE quanxianguanli;

-- =====================================================
-- 1. 去向管理测试数据
-- =====================================================
-- 注意：user_id需要替换为实际存在的用户ID
-- 如果sys_user表中有数据，可以使用第一个用户的ID
-- 否则需要先创建用户数据

-- 获取第一个用户ID（如果存在）
SET @first_user_id = (SELECT id FROM sys_user LIMIT 1);

-- 如果存在用户，插入测试数据
INSERT INTO daily_destination (id, user_id, destination_time, activity_name, location, create_time, update_time)
SELECT 
    'dd001', 
    @first_user_id,
    '2024-01-15 09:00:00',
    '客户拜访-上海机械制造有限公司',
    '上海市浦东新区张江路1000号',
    NOW(),
    NOW()
WHERE @first_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

INSERT INTO daily_destination (id, user_id, destination_time, activity_name, location, create_time, update_time)
SELECT 
    'dd002', 
    @first_user_id,
    '2024-01-15 14:30:00',
    '产品展示会-重庆水泵行业交流会',
    '重庆市渝北区龙溪街道',
    NOW(),
    NOW()
WHERE @first_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

INSERT INTO daily_destination (id, user_id, destination_time, activity_name, location, create_time, update_time)
SELECT 
    'dd003', 
    @first_user_id,
    '2024-01-16 10:00:00',
    '技术交流-北京化工设备公司',
    '北京市朝阳区建国路88号',
    NOW(),
    NOW()
WHERE @first_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

INSERT INTO daily_destination (id, user_id, destination_time, activity_name, location, create_time, update_time)
SELECT 
    'dd004', 
    @first_user_id,
    '2024-01-16 16:00:00',
    '合同谈判-广州工业设备采购',
    '广州市天河区天河路123号',
    NOW(),
    NOW()
WHERE @first_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

INSERT INTO daily_destination (id, user_id, destination_time, activity_name, location, create_time, update_time)
SELECT 
    'dd005', 
    @first_user_id,
    '2024-01-17 09:30:00',
    '售后服务-深圳某客户设备维护',
    '深圳市南山区科技园',
    NOW(),
    NOW()
WHERE @first_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

-- 如果有多个用户，为第二个用户也添加一些数据
SET @second_user_id = (SELECT id FROM sys_user LIMIT 1 OFFSET 1);

INSERT INTO daily_destination (id, user_id, destination_time, activity_name, location, create_time, update_time)
SELECT 
    'dd006', 
    @second_user_id,
    '2024-01-15 11:00:00',
    '市场调研-成都地区市场分析',
    '成都市锦江区春熙路',
    NOW(),
    NOW()
WHERE @second_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

INSERT INTO daily_destination (id, user_id, destination_time, activity_name, location, create_time, update_time)
SELECT 
    'dd007', 
    @second_user_id,
    '2024-01-16 15:00:00',
    '客户回访-武汉某重点客户',
    '武汉市武昌区中南路',
    NOW(),
    NOW()
WHERE @second_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

-- =====================================================
-- 2. 周报管理测试数据
-- =====================================================

-- 周报1：已提交状态
INSERT INTO weekly_report (id, user_id, report_name, report_time, report_content, report_remark, status, create_time, update_time)
SELECT 
    'wr001',
    @first_user_id,
    '2024年第3周工作周报',
    '2024-01-19 18:00:00',
    '本周主要工作内容：
1. 完成上海机械制造有限公司的客户拜访，了解了客户的具体需求
2. 参加了重庆水泵行业交流会，推广了公司的新产品
3. 与北京化工设备公司进行了技术交流，展示了我们的技术优势
4. 完成了广州工业设备采购项目的合同谈判，已达成初步意向

下周工作计划：
1. 跟进广州项目的合同签署事宜
2. 准备深圳客户的设备维护方案
3. 继续拓展新的客户资源',
    '本周工作进展顺利，多个项目都有实质性进展',
    'submitted',
    NOW(),
    NOW()
WHERE @first_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

-- 周报2：草稿状态
INSERT INTO weekly_report (id, user_id, report_name, report_time, report_content, report_remark, status, create_time, update_time)
SELECT 
    'wr002',
    @first_user_id,
    '2024年第4周工作周报',
    '2024-01-26 17:30:00',
    '本周主要工作内容：
1. 完成了深圳某客户设备维护工作
2. 继续跟进广州项目的合同签署
3. 参加了行业技术研讨会

下周工作计划：
1. 重点跟进几个潜在客户
2. 准备下月的工作计划',
    '本周工作正常进行中',
    'draft',
    NOW(),
    NOW()
WHERE @first_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

-- 周报3：第二个用户的周报（已提交）
INSERT INTO weekly_report (id, user_id, report_name, report_time, report_content, report_remark, status, create_time, update_time)
SELECT 
    'wr003',
    @second_user_id,
    '2024年第3周工作周报',
    '2024-01-19 17:00:00',
    '本周主要工作内容：
1. 完成了成都地区市场调研工作，收集了大量有价值的市场信息
2. 对武汉某重点客户进行了回访，客户对我们的服务表示满意
3. 参与了公司内部培训，提升了专业技能

下周工作计划：
1. 根据市场调研结果，制定针对性的营销策略
2. 继续维护重点客户关系
3. 拓展新的市场区域',
    '市场调研工作取得良好效果',
    'submitted',
    NOW(),
    NOW()
WHERE @second_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

-- 周报4：第二个用户的周报（草稿）
INSERT INTO weekly_report (id, user_id, report_name, report_time, report_content, report_remark, status, create_time, update_time)
SELECT 
    'wr004',
    @second_user_id,
    '2024年第4周工作周报',
    '2024-01-26 16:00:00',
    '本周主要工作内容：
1. 继续市场拓展工作
2. 客户关系维护

下周工作计划：
待补充',
    '',
    'draft',
    NOW(),
    NOW()
WHERE @second_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

-- 周报5：第一个用户的历史周报（已提交）
INSERT INTO weekly_report (id, user_id, report_name, report_time, report_content, report_remark, status, create_time, update_time)
SELECT 
    'wr005',
    @first_user_id,
    '2024年第2周工作周报',
    '2024-01-12 18:00:00',
    '本周主要工作内容：
1. 完成了多个客户的初步接触
2. 参加了公司组织的销售培训
3. 整理了客户资料和项目信息

下周工作计划：
1. 重点跟进几个有意向的客户
2. 准备产品展示材料',
    '本周是新年后的第一周，工作逐步恢复正常',
    'submitted',
    NOW(),
    NOW()
WHERE @first_user_id IS NOT NULL
ON DUPLICATE KEY UPDATE update_time = VALUES(update_time);

SELECT '日常管理模块测试数据初始化完成！' AS message;
SELECT CONCAT('已插入去向管理数据：', (SELECT COUNT(*) FROM daily_destination), ' 条') AS destination_summary;
SELECT CONCAT('已插入周报管理数据：', (SELECT COUNT(*) FROM weekly_report), ' 条') AS report_summary;

