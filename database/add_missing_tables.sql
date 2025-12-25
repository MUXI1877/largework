-- 添加缺失的业务表
-- 注意：此脚本用于向现有数据库添加缺失的业务表

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE quanxianguanli;

-- =====================================================
-- 1. 片区人员调动管理表
-- =====================================================
CREATE TABLE IF NOT EXISTS person_transfer (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    person_id VARCHAR(36) NOT NULL COMMENT '人员ID',
    from_region_id VARCHAR(36) COMMENT '原片区ID',
    to_region_id VARCHAR(36) NOT NULL COMMENT '目标片区ID',
    transfer_date DATE NOT NULL COMMENT '调动日期',
    reason VARCHAR(500) COMMENT '调动原因',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending-待审批,approved-已批准,rejected-已拒绝',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    INDEX idx_person_id (person_id),
    INDEX idx_transfer_date (transfer_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='片区人员调动管理表';

-- =====================================================
-- 2. 备品备件表
-- =====================================================
CREATE TABLE IF NOT EXISTS spare_parts (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    code VARCHAR(50) NOT NULL COMMENT '备件编码',
    name VARCHAR(200) NOT NULL COMMENT '备件名称',
    model VARCHAR(100) COMMENT '型号',
    category VARCHAR(50) COMMENT '类别',
    unit VARCHAR(20) COMMENT '单位',
    price DECIMAL(18, 2) COMMENT '单价',
    stock_quantity INT DEFAULT 0 COMMENT '库存数量',
    min_stock INT DEFAULT 0 COMMENT '最低库存',
    supplier VARCHAR(200) COMMENT '供应商',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_code (code),
    INDEX idx_name (name),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='备品备件表';

-- =====================================================
-- 3. 设备成套表
-- =====================================================
CREATE TABLE IF NOT EXISTS equipment_set (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    code VARCHAR(50) NOT NULL COMMENT '成套编码',
    name VARCHAR(200) NOT NULL COMMENT '成套名称',
    description VARCHAR(500) COMMENT '描述',
    total_price DECIMAL(18, 2) COMMENT '总价',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态：active-启用,inactive-停用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_code (code),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备成套表';

-- =====================================================
-- 4. 设备成套明细表
-- =====================================================
CREATE TABLE IF NOT EXISTS equipment_set_item (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    set_id VARCHAR(36) NOT NULL COMMENT '成套ID',
    device_unit_id VARCHAR(36) COMMENT '单体设备ID',
    spare_parts_id VARCHAR(36) COMMENT '备件ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    unit_price DECIMAL(18, 2) COMMENT '单价',
    total_price DECIMAL(18, 2) COMMENT '小计',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    INDEX idx_set_id (set_id),
    INDEX idx_device_unit_id (device_unit_id),
    INDEX idx_spare_parts_id (spare_parts_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备成套明细表';

-- =====================================================
-- 5. 销售降库表
-- =====================================================
CREATE TABLE IF NOT EXISTS sales_inventory_reduction (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    reduction_no VARCHAR(50) NOT NULL COMMENT '降库单号',
    reduction_date DATE NOT NULL COMMENT '降库日期',
    customer_id VARCHAR(36) COMMENT '客户ID',
    sales_person_id VARCHAR(36) COMMENT '销售人员ID',
    total_amount DECIMAL(18, 2) COMMENT '总金额',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态：draft-草稿,approved-已批准,completed-已完成',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_reduction_no (reduction_no),
    INDEX idx_reduction_date (reduction_date),
    INDEX idx_customer_id (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售降库表';

-- =====================================================
-- 6. 销售降库明细表
-- =====================================================
CREATE TABLE IF NOT EXISTS sales_inventory_reduction_item (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    reduction_id VARCHAR(36) NOT NULL COMMENT '降库单ID',
    product_type VARCHAR(20) NOT NULL COMMENT '产品类型：device_unit-单体设备,spare_parts-备件,equipment_set-成套',
    product_id VARCHAR(36) NOT NULL COMMENT '产品ID',
    quantity INT NOT NULL COMMENT '数量',
    unit_price DECIMAL(18, 2) COMMENT '单价',
    total_price DECIMAL(18, 2) COMMENT '小计',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    INDEX idx_reduction_id (reduction_id),
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售降库明细表';

-- =====================================================
-- 7. 投标管理表
-- =====================================================
CREATE TABLE IF NOT EXISTS bidding (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    bidding_no VARCHAR(50) NOT NULL COMMENT '投标编号',
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    customer_id VARCHAR(36) COMMENT '客户ID',
    bidding_date DATE NOT NULL COMMENT '投标日期',
    deadline DATE COMMENT '截止日期',
    total_amount DECIMAL(18, 2) COMMENT '投标金额',
    status VARCHAR(20) DEFAULT 'draft' COMMENT '状态：draft-草稿,submitted-已提交,won-中标,lost-未中标',
    sales_person_id VARCHAR(36) COMMENT '负责人ID',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_bidding_no (bidding_no),
    INDEX idx_bidding_date (bidding_date),
    INDEX idx_customer_id (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投标管理表';

-- =====================================================
-- 8. 投标明细表
-- =====================================================
CREATE TABLE IF NOT EXISTS bidding_item (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    bidding_id VARCHAR(36) NOT NULL COMMENT '投标ID',
    product_type VARCHAR(20) NOT NULL COMMENT '产品类型',
    product_id VARCHAR(36) NOT NULL COMMENT '产品ID',
    quantity INT NOT NULL COMMENT '数量',
    unit_price DECIMAL(18, 2) COMMENT '单价',
    total_price DECIMAL(18, 2) COMMENT '小计',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    INDEX idx_bidding_id (bidding_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投标明细表';

-- =====================================================
-- 9. 售后4S店表
-- =====================================================
CREATE TABLE IF NOT EXISTS after_sale_store (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    store_code VARCHAR(50) NOT NULL COMMENT '4S店编码',
    store_name VARCHAR(200) NOT NULL COMMENT '4S店名称',
    region VARCHAR(100) COMMENT '区域',
    address VARCHAR(500) COMMENT '地址',
    contact_person VARCHAR(50) COMMENT '联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态：active-启用,inactive-停用',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_store_code (store_code),
    INDEX idx_store_name (store_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='售后4S店表';

-- =====================================================
-- 10. 售后计划表
-- =====================================================
CREATE TABLE IF NOT EXISTS after_sale_plan (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    plan_no VARCHAR(50) NOT NULL COMMENT '计划编号',
    plan_name VARCHAR(200) NOT NULL COMMENT '计划名称',
    device_id VARCHAR(36) COMMENT '设备ID',
    customer_id VARCHAR(36) COMMENT '客户ID',
    store_id VARCHAR(36) COMMENT '4S店ID',
    plan_type VARCHAR(20) COMMENT '计划类型：maintenance-保养,inspection-巡检,repair-维修',
    plan_date DATE NOT NULL COMMENT '计划日期',
    executor_id VARCHAR(36) COMMENT '执行人ID',
    status VARCHAR(20) DEFAULT 'pending' COMMENT '状态：pending-待执行,executing-执行中,completed-已完成',
    content VARCHAR(1000) COMMENT '计划内容',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_plan_no (plan_no),
    INDEX idx_plan_date (plan_date),
    INDEX idx_device_id (device_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='售后计划表';

-- =====================================================
-- 11. 远程设备状态采集表
-- =====================================================
CREATE TABLE IF NOT EXISTS device_telemetry (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    device_id VARCHAR(36) NOT NULL COMMENT '设备ID',
    metric_key VARCHAR(50) NOT NULL COMMENT '指标键',
    metric_value DECIMAL(18, 4) COMMENT '指标值',
    collect_time DATETIME NOT NULL COMMENT '采集时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    INDEX idx_device_id (device_id),
    INDEX idx_collect_time (collect_time),
    INDEX idx_metric_key (metric_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='远程设备状态采集表';

-- =====================================================
-- 12. 设备运行监视配置表
-- =====================================================
CREATE TABLE IF NOT EXISTS device_monitor_config (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    device_id VARCHAR(36) COMMENT '设备ID（NULL表示全局配置）',
    metric_key VARCHAR(50) NOT NULL COMMENT '指标键',
    metric_name VARCHAR(100) COMMENT '指标名称',
    unit VARCHAR(20) COMMENT '单位',
    min_value DECIMAL(18, 4) COMMENT '最小值',
    max_value DECIMAL(18, 4) COMMENT '最大值',
    warning_min DECIMAL(18, 4) COMMENT '警告下限',
    warning_max DECIMAL(18, 4) COMMENT '警告上限',
    alarm_min DECIMAL(18, 4) COMMENT '报警下限',
    alarm_max DECIMAL(18, 4) COMMENT '报警上限',
    is_enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    INDEX idx_device_id (device_id),
    INDEX idx_metric_key (metric_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备运行监视配置表';

-- =====================================================
-- 13. 专家分析结果表
-- =====================================================
CREATE TABLE IF NOT EXISTS expert_analysis (
    id VARCHAR(36) PRIMARY KEY COMMENT '主键ID',
    device_id VARCHAR(36) NOT NULL COMMENT '设备ID',
    analysis_type VARCHAR(50) NOT NULL COMMENT '分析类型：health-健康度,prediction-预测,fault-故障诊断',
    analysis_result TEXT COMMENT '分析结果（JSON格式）',
    score DECIMAL(5, 2) COMMENT '评分',
    conclusion VARCHAR(500) COMMENT '结论',
    suggestions TEXT COMMENT '建议',
    analysis_time DATETIME NOT NULL COMMENT '分析时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    INDEX idx_device_id (device_id),
    INDEX idx_analysis_time (analysis_time),
    INDEX idx_analysis_type (analysis_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='专家分析结果表';

SELECT '缺失的业务表添加完成！' AS message;

