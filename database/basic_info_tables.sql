-- 基本信息管理模块 - 数据库表结构
-- 注意：如果使用JPA自动创建表，此脚本仅用于参考
-- 如果需要手动创建，可以执行此脚本

USE quanxianguanli;

-- ============================================
-- 客户管理相关表
-- ============================================

-- 客户表
CREATE TABLE IF NOT EXISTS customer (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    customer_code VARCHAR(50) UNIQUE NOT NULL COMMENT '客户编号',
    unit_name VARCHAR(200) UNIQUE NOT NULL COMMENT '单位名称',
    region VARCHAR(100) COMMENT '所属区域',
    area VARCHAR(100) COMMENT '地区',
    industry VARCHAR(100) COMMENT '所属行业',
    company_address VARCHAR(500) COMMENT '公司地址',
    order_representative VARCHAR(100) COMMENT '订货代表',
    buyer_attribute VARCHAR(50) COMMENT '买方属性',
    invoice_phone VARCHAR(20) COMMENT '开票电话',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    fax VARCHAR(20) COMMENT '传真',
    postal_code VARCHAR(10) COMMENT '邮编',
    invoice_address VARCHAR(500) COMMENT '开票地址',
    invoice_bank VARCHAR(200) COMMENT '开票银行',
    bank_account VARCHAR(50) COMMENT '银行账号',
    tax_number VARCHAR(50) COMMENT '税号',
    arrears_amount DECIMAL(15,2) COMMENT '欠款金额',
    credit_level VARCHAR(50) COMMENT '信用等级',
    remarks VARCHAR(1000) COMMENT '备注',
    INDEX idx_unit_name (unit_name),
    INDEX idx_customer_code (customer_code),
    INDEX idx_region (region)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户表';

-- 客户关键人物表
CREATE TABLE IF NOT EXISTS customer_key_person (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    customer_id VARCHAR(36) NOT NULL COMMENT '客户ID',
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) COMMENT '性别',
    birthday DATE COMMENT '生日',
    position VARCHAR(100) COMMENT '职位',
    contact_info VARCHAR(200) COMMENT '联系方式',
    job_type VARCHAR(50) COMMENT '职务（决策者/部门主管/普通员工）',
    direct_supervisor VARCHAR(100) COMMENT '直接上级',
    is_main_contact BOOLEAN DEFAULT FALSE COMMENT '是否主要联系人',
    remarks VARCHAR(1000) COMMENT '备注',
    INDEX idx_customer_id (customer_id),
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户关键人物表';

-- 项目机会表
CREATE TABLE IF NOT EXISTS project_opportunity (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    customer_id VARCHAR(36) NOT NULL COMMENT '客户ID',
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    project_region VARCHAR(100) COMMENT '项目所属片区',
    status VARCHAR(50) COMMENT '状态',
    project_time DATETIME COMMENT '时间',
    remarks VARCHAR(1000) COMMENT '备注',
    INDEX idx_customer_id (customer_id),
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目机会表';

-- 合同表
CREATE TABLE IF NOT EXISTS contract (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    customer_id VARCHAR(36) NOT NULL COMMENT '客户ID',
    contract_code VARCHAR(50) UNIQUE NOT NULL COMMENT '合同编号',
    contract_name VARCHAR(200) NOT NULL COMMENT '合同名称',
    INDEX idx_customer_id (customer_id),
    INDEX idx_contract_code (contract_code),
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='合同表';

-- 售后信息表
CREATE TABLE IF NOT EXISTS after_sales (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    customer_id VARCHAR(36) NOT NULL COMMENT '客户ID',
    contract_code VARCHAR(50) COMMENT '合同编号',
    contract_name VARCHAR(200) COMMENT '合同名称',
    region VARCHAR(100) COMMENT '区域',
    after_sales_person VARCHAR(100) COMMENT '售后人员',
    remarks VARCHAR(1000) COMMENT '备注',
    INDEX idx_customer_id (customer_id),
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='售后信息表';

-- 客户来访信息表
CREATE TABLE IF NOT EXISTS customer_visit (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    customer_id VARCHAR(36) NOT NULL COMMENT '客户ID',
    visit_date DATE NOT NULL COMMENT '日期',
    customer_sequence VARCHAR(50) COMMENT '客户序号',
    customer_name VARCHAR(200) COMMENT '客户名称',
    status VARCHAR(50) COMMENT '状态',
    remarks VARCHAR(1000) COMMENT '备注',
    INDEX idx_customer_id (customer_id),
    INDEX idx_visit_date (visit_date),
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户来访信息表';

-- ============================================
-- 团队信息管理相关表
-- ============================================

-- 销售片区表
CREATE TABLE IF NOT EXISTS sales_region (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    region_name VARCHAR(200) NOT NULL COMMENT '片区名称',
    region_code VARCHAR(50) UNIQUE NOT NULL COMMENT '片区编号',
    parent_department VARCHAR(100) COMMENT '上级部门（成套处、外贸处、销售处）',
    create_date DATE NOT NULL COMMENT '创建日期',
    remarks VARCHAR(1000) COMMENT '备注',
    INDEX idx_region_code (region_code),
    INDEX idx_parent_department (parent_department)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售片区表';

-- 营销人员表
CREATE TABLE IF NOT EXISTS sales_person (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    employee_code VARCHAR(50) UNIQUE COMMENT '工号',
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    gender VARCHAR(10) COMMENT '性别',
    birthday DATE COMMENT '生日',
    contact_info VARCHAR(200) COMMENT '联系方式',
    region_id VARCHAR(36) COMMENT '所属片区ID',
    position VARCHAR(100) COMMENT '职务',
    department VARCHAR(100) COMMENT '所属部门',
    responsible_area VARCHAR(200) COMMENT '当前负责区域',
    remarks VARCHAR(1000) COMMENT '备注',
    erp_sync_id VARCHAR(50) COMMENT 'ERP系统同步ID，用于与信息化平台系统保持一致',
    INDEX idx_employee_code (employee_code),
    INDEX idx_region_id (region_id),
    INDEX idx_erp_sync_id (erp_sync_id),
    FOREIGN KEY (region_id) REFERENCES sales_region(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='营销人员表';

-- ============================================
-- 产品管理相关表
-- ============================================

-- 产品表
CREATE TABLE IF NOT EXISTS product (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    name VARCHAR(200) NOT NULL COMMENT '名称',
    model VARCHAR(100) COMMENT '型号',
    parameters VARCHAR(1000) COMMENT '参数',
    price DECIMAL(15,2) COMMENT '价格',
    weight VARCHAR(50) COMMENT '重量',
    dimensions VARCHAR(200) COMMENT '尺寸',
    delivery_cycle VARCHAR(100) COMMENT '交货周期',
    product_type VARCHAR(50) NOT NULL COMMENT '产品类型：SINGLE（单体设备）、SPARE（备品备件）、COMPLETE（设备成套）',
    remarks VARCHAR(1000) COMMENT '备注',
    INDEX idx_product_type (product_type),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

-- 产品附件表
CREATE TABLE IF NOT EXISTS product_attachment (
    id VARCHAR(36) PRIMARY KEY,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    product_id VARCHAR(36) NOT NULL COMMENT '产品ID',
    file_name VARCHAR(200) NOT NULL COMMENT '文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_size BIGINT COMMENT '文件大小（字节）',
    file_type VARCHAR(50) COMMENT '文件类型',
    INDEX idx_product_id (product_id),
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品附件表';

-- 表结构创建完成
SELECT '基本信息管理模块表结构创建完成！' AS message;

