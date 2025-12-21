-- 为没有数据的表添加初始数据
-- 注意：此脚本使用 INSERT IGNORE，可以安全地重复执行

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE quanxianguanli;

-- 1. 客户来访管理 (customer_visit)
INSERT IGNORE INTO customer_visit (id, create_time, update_time, unit_name, region, industry, address, city) VALUES
('cv001', NOW(), NOW(), '北京科技有限公司', '北京', '信息技术', '北京市海淀区中关村大街1号', '北京'),
('cv002', NOW(), NOW(), '上海贸易有限公司', '上海', '贸易', '上海市浦东新区世纪大道100号', '上海'),
('cv003', NOW(), NOW(), '广州制造企业', '广州', '制造业', '广州市天河区天河路200号', '广州'),
('cv004', NOW(), NOW(), '深圳创新科技', '深圳', '科技创新', '深圳市南山区科技园南路100号', '深圳'),
('cv005', NOW(), NOW(), '杭州电商公司', '杭州', '电子商务', '杭州市西湖区文三路200号', '杭州');

-- 2. 销售机会管理 (sales_lead)
INSERT IGNORE INTO sales_lead (id, create_time, update_time, customer_name, contact, amount, expect_date, status, owner_id) VALUES
('sl001', NOW(), NOW(), '北京科技有限公司', '张经理', 150000.00, '2025-12-31', '跟进中', 'sp001'),
('sl002', NOW(), NOW(), '上海贸易有限公司', '李经理', 280000.00, '2026-01-15', '待分配', 'sp002'),
('sl003', NOW(), NOW(), '广州制造企业', '王经理', 85000.00, '2026-02-20', '已成交', 'sp003'),
('sl004', NOW(), NOW(), '深圳创新科技', '刘经理', 120000.00, '2026-01-30', '跟进中', 'sp004'),
('sl005', NOW(), NOW(), '杭州电商公司', '陈经理', 68000.00, '2026-03-10', '待分配', 'sp005');

-- 3. 销售报价管理 (quotation)
INSERT IGNORE INTO quotation (id, create_time, update_time, customer_name, total_amount, status) VALUES
('q001', NOW(), NOW(), '北京科技有限公司', 150000.00, '待审核'),
('q002', NOW(), NOW(), '上海贸易有限公司', 280000.00, '已通过'),
('q003', NOW(), NOW(), '广州制造企业', 85000.00, '已拒绝'),
('q004', NOW(), NOW(), '深圳创新科技', 120000.00, '待审核'),
('q005', NOW(), NOW(), '杭州电商公司', 68000.00, '已通过');

-- 4. 报价明细 (quotation_item)
INSERT IGNORE INTO quotation_item (id, create_time, update_time, quotation_id, product_id, quantity, unit_price, total_price) VALUES
('qi001', NOW(), NOW(), 'q001', 'du001', 10, 1500.00, 15000.00),
('qi002', NOW(), NOW(), 'q001', 'du002', 5, 2800.00, 14000.00),
('qi003', NOW(), NOW(), 'q002', 'du003', 2, 8500.00, 17000.00),
('qi004', NOW(), NOW(), 'q002', 'du004', 1, 12000.00, 12000.00),
('qi005', NOW(), NOW(), 'q003', 'du005', 20, 680.00, 13600.00),
('qi006', NOW(), NOW(), 'q004', 'du006', 15, 1200.00, 18000.00),
('qi007', NOW(), NOW(), 'q005', 'du007', 3, 3500.00, 10500.00);

-- 5. 价格本管理 (price_book)
INSERT IGNORE INTO price_book (id, create_time, update_time, product_id, dept, price, start_date, end_date) VALUES
('pb001', NOW(), NOW(), 'du001', '销售一部', 1500.00, '2025-01-01', '2025-12-31'),
('pb002', NOW(), NOW(), 'du001', '销售二部', 1450.00, '2025-01-01', '2025-12-31'),
('pb003', NOW(), NOW(), 'du002', '销售一部', 2800.00, '2025-01-01', '2025-12-31'),
('pb004', NOW(), NOW(), 'du002', '销售二部', 2750.00, '2025-01-01', '2025-12-31'),
('pb005', NOW(), NOW(), 'du003', '销售一部', 8500.00, '2025-01-01', '2025-12-31'),
('pb006', NOW(), NOW(), 'du003', '销售三部', 8200.00, '2025-01-01', '2025-12-31'),
('pb007', NOW(), NOW(), 'du004', '销售一部', 12000.00, '2025-01-01', '2025-12-31'),
('pb008', NOW(), NOW(), 'du005', '销售二部', 680.00, '2025-01-01', '2025-12-31');

-- 6. 应收账计划 (acc_receivable_plan)
INSERT IGNORE INTO acc_receivable_plan (id, create_time, update_time, contract_id, stage, plan_date, plan_amount) VALUES
('arp001', NOW(), NOW(), 'CONTRACT001', 1, '2025-12-25', 50000.00),
('arp002', NOW(), NOW(), 'CONTRACT001', 2, '2026-01-25', 50000.00),
('arp003', NOW(), NOW(), 'CONTRACT001', 3, '2026-02-25', 50000.00),
('arp004', NOW(), NOW(), 'CONTRACT002', 1, '2025-12-30', 100000.00),
('arp005', NOW(), NOW(), 'CONTRACT002', 2, '2026-01-30', 100000.00),
('arp006', NOW(), NOW(), 'CONTRACT002', 3, '2026-02-28', 80000.00),
('arp007', NOW(), NOW(), 'CONTRACT003', 1, '2026-01-10', 85000.00);

-- 7. 应收账录入（实收）(acc_receivable_item)
INSERT IGNORE INTO acc_receivable_item (id, create_time, update_time, contract_id, receive_date, amount) VALUES
('ari001', NOW(), NOW(), 'CONTRACT001', '2025-12-20', 50000.00),
('ari002', NOW(), NOW(), 'CONTRACT001', '2026-01-20', 50000.00),
('ari003', NOW(), NOW(), 'CONTRACT002', '2025-12-25', 100000.00),
('ari004', NOW(), NOW(), 'CONTRACT002', '2026-01-25', 100000.00),
('ari005', NOW(), NOW(), 'CONTRACT003', '2026-01-05', 85000.00);

-- 8. 售后请求 (after_sale)
INSERT IGNORE INTO after_sale (id, create_time, update_time, device_sn, service_type, contract_id, problem_desc, handler_id, status) VALUES
('as001', NOW(), NOW(), 'SN20250101001', '维修', 'CONTRACT001', '设备运行异常，需要检修', 'sp001', '处理中'),
('as002', NOW(), NOW(), 'SN20250102002', '保养', 'CONTRACT002', '定期保养服务', 'sp002', '已完成'),
('as003', NOW(), NOW(), 'SN20250103003', '维修', 'CONTRACT003', '传感器故障，需要更换', 'sp003', '待分配'),
('as004', NOW(), NOW(), 'SN20250104004', '安装', 'CONTRACT004', '新设备安装调试', 'sp004', '处理中'),
('as005', NOW(), NOW(), 'SN20250105005', '维修', 'CONTRACT005', '设备漏水问题', 'sp005', '已完成');

-- 9. 设备档案 (device_profile)
INSERT IGNORE INTO device_profile (id, create_time, update_time, sn, model, produce_date, customer_id) VALUES
('dp001', NOW(), NOW(), 'SN20250101001', 'SENSOR-A-001', '2025-01-01', 'c001'),
('dp002', NOW(), NOW(), 'SN20250102002', 'SENSOR-B-002', '2025-01-02', 'c002'),
('dp003', NOW(), NOW(), 'SN20250103003', 'DAQ-3000', '2025-01-03', 'c003'),
('dp004', NOW(), NOW(), 'SN20250104004', 'PLC-X200', '2025-01-04', 'c004'),
('dp005', NOW(), NOW(), 'SN20250105005', 'TEMP-TX100', '2025-01-05', 'c005'),
('dp006', NOW(), NOW(), 'SN20250106006', 'PRESS-PX200', '2025-01-06', 'c001'),
('dp007', NOW(), NOW(), 'SN20250107007', 'FLOW-FM500', '2025-01-07', 'c002'),
('dp008', NOW(), NOW(), 'SN20250108008', 'LEVEL-LX300', '2025-01-08', 'c003');

-- 10. 售后经验库 (after_sale_exp)
INSERT IGNORE INTO after_sale_exp (id, create_time, update_time, device_model, problem, solution) VALUES
('ase001', NOW(), NOW(), 'SENSOR-A-001', '传感器读数异常，显示值不稳定', '检查接线是否松动，重新校准传感器，必要时更换传感器模块'),
('ase002', NOW(), NOW(), 'DAQ-3000', '数据采集器无法连接电脑', '检查USB连接线，更新驱动程序，重启设备'),
('ase003', NOW(), NOW(), 'PLC-X200', '控制器频繁重启', '检查电源电压是否稳定，检查程序是否有死循环，更新固件版本'),
('ase004', NOW(), NOW(), 'TEMP-TX100', '温度变送器显示温度偏差大', '检查传感器探头是否损坏，重新校准温度变送器，检查环境温度影响'),
('ase005', NOW(), NOW(), 'PRESS-PX200', '压力变送器输出信号异常', '检查压力管路是否堵塞，检查传感器膜片是否损坏，重新校准零点'),
('ase006', NOW(), NOW(), 'FLOW-FM500', '流量计读数不准确', '检查管道内是否有气泡，检查传感器安装位置是否正确，清洗传感器探头'),
('ase007', NOW(), NOW(), 'LEVEL-LX300', '液位计无法检测到液位', '检查传感器是否被遮挡，检查传感器是否损坏，检查供电是否正常');

SELECT '所有空表数据插入完成！' AS message;

