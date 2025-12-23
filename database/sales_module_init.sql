-- 销售管理模块初始化脚本
-- 包含客户来访管理、销售机会管理、销售库存查询、销售报价管理、销售降库管理、投标管理
-- 注意：此脚本应在表结构创建后执行

USE quanxianguanli;

-- ============================================
-- 销售管理模块（m017为父模块，m012-m016为子模块）
-- 注意：m011已被基本信息管理使用，所以从m012开始
-- ============================================
INSERT INTO sys_module (id, create_time, update_time, cn_name, en_name, menu_level, sort, path, icon, group_name, parent_id, is_parent, is_expand) VALUES
('m017', NOW(), NOW(), '销售管理', 'Sales Management', 1, 4, '/sales', 'el-icon-shopping-cart', 'sales', NULL, true, true),
('m012', NOW(), NOW(), '客户来访管理', 'Customer Visit Management', 2, 1, '/customer-visit-management', 'el-icon-user', 'sales', 'm017', false, false),
('m013', NOW(), NOW(), '销售机会管理', 'Project Opportunity Management', 2, 2, '/project-opportunity-management', 'el-icon-opportunity', 'sales', 'm017', false, false),
('m014', NOW(), NOW(), '销售库存查询', 'Product Inventory', 2, 3, '/product-inventory', 'el-icon-box', 'sales', 'm017', false, false),
('m015', NOW(), NOW(), '销售报价管理', 'Sales Quotation Management', 2, 4, '/sales-quotation-management', 'el-icon-document', 'sales', 'm017', false, false),
('m016', NOW(), NOW(), '销售降库管理', 'Reduced Stock Management', 2, 5, '/reduced-stock-management', 'el-icon-goods', 'sales', 'm017', false, false),
('m018', NOW(), NOW(), '投标管理', 'Bidding Info Management', 2, 6, '/bidding-info-management', 'el-icon-files', 'sales', 'm017', false, false)
ON DUPLICATE KEY UPDATE 
    cn_name = VALUES(cn_name),
    en_name = VALUES(en_name),
    path = VALUES(path),
    icon = VALUES(icon),
    parent_id = VALUES(parent_id),
    menu_level = VALUES(menu_level),
    is_parent = VALUES(is_parent),
    update_time = NOW();

-- ============================================
-- 模块说明
-- ============================================
-- m017: 销售管理（父模块）
--   - m012: 客户来访管理
--     功能：客户来访信息登记、查询、新增、修改、删除、导出
--   - m013: 销售机会管理
--     功能：销售机会登记、提交、传递片区、分配员工、关闭机会、跟踪记录
--   - m014: 销售库存查询
--     功能：查询产品及零件库存信息，支持条件筛选和导出
--   - m015: 销售报价管理
--     功能：快速生成产品及零件报价单，支持打印、导出PDF
--   - m016: 销售降库管理
--     功能：查询呆滞库存产品、标记降库产品，关联销售合同
--   - m018: 投标管理
--     功能：新建投标信息、投标总结，关联销售机会状态更新

SELECT '销售管理模块信息初始化完成！' AS message;
SELECT '已插入7个模块：销售管理父模块1个（m017），子模块6个（m012-m016, m018）' AS summary;

