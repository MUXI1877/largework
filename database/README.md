# 数据库初始化说明

## 文件说明

1. **init.sql** - 数据库创建脚本（包含系统管理相关表的参考结构）
2. **data_init.sql** - 系统管理模块数据初始化脚本（角色、用户、权限等）
3. **basic_info_tables.sql** - 基本信息管理模块表结构脚本
4. **basic_info_data_init.sql** - 基本信息管理模块测试数据初始化脚本

## 初始化步骤

### 方式一：使用JPA自动创建表（推荐）

如果使用Spring Boot的JPA自动创建表功能（`spring.jpa.hibernate.ddl-auto=update`），只需执行数据初始化脚本：

```sql
-- 1. 执行系统管理模块数据初始化
SOURCE database/data_init.sql;

-- 2. 执行基本信息管理模块测试数据初始化
SOURCE database/basic_info_data_init.sql;
```

### 方式二：手动创建表结构

如果需要手动创建表结构，按以下顺序执行：

```sql
-- 1. 创建数据库
SOURCE database/init.sql;

-- 2. 创建基本信息管理模块表结构
SOURCE database/basic_info_tables.sql;

-- 3. 执行系统管理模块数据初始化
SOURCE database/data_init.sql;

-- 4. 执行基本信息管理模块测试数据初始化
SOURCE database/basic_info_data_init.sql;
```

## 数据库表结构

### 基本信息管理模块表

#### 客户管理相关表
- `customer` - 客户表
- `customer_key_person` - 客户关键人物表
- `project_opportunity` - 项目机会表
- `contract` - 合同表
- `after_sales` - 售后信息表
- `customer_visit` - 客户来访信息表

#### 团队信息管理相关表
- `sales_region` - 销售片区表
- `sales_person` - 营销人员表

#### 产品管理相关表
- `product` - 产品表（支持SINGLE/SPARE/COMPLETE三种类型）
- `product_attachment` - 产品附件表

## 测试数据说明

### 销售片区数据
- 6个测试片区（华东、华南、华北、西南、外贸、成套设备）

### 营销人员数据
- 5个测试人员，分配在不同片区

### 客户数据
- 4个测试客户，包含完整的客户信息

### 客户关联数据
- 每个客户包含关键人物、项目机会、合同、售后、来访信息

### 产品数据
- 4个单体设备
- 4个备品备件
- 3个设备成套

## 注意事项

1. 所有表都包含 `id`、`create_time`、`update_time` 字段（继承自BaseEntity）
2. 外键约束使用 `ON DELETE CASCADE` 或 `ON DELETE SET NULL`，确保数据一致性
3. 所有表使用 `utf8mb4` 字符集，支持中文和特殊字符
4. 索引已优化，提高查询性能
5. 测试数据中的ID使用固定值，便于测试和演示

## 权限配置

数据初始化脚本已自动配置以下权限：

- **超级管理员（r001）**：所有模块的完整权限
- **系统管理员（r002）**：系统管理和基本信息管理模块的完整权限
- **普通用户（r003）**：基本信息管理模块的只读权限

## 模块ID说明

- m008 - 客户管理
- m009 - 团队信息管理
- m010 - 产品管理

这些模块已自动添加到权限配置中，用户登录后即可看到相应菜单。

