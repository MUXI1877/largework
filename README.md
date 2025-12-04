# 权限管理平台

基于前后端分离架构的权限管理平台，技术栈：SpringBoot + SpringDataJPA + MySQL + Vue3 + Element Plus

## 项目结构

```
quanxianguanli/
├── src/                    # 后端源码
│   └── main/
│       ├── java/          # Java源码
│       └── resources/     # 配置文件
├── frontend/              # 前端源码
│   └── src/
│       ├── api/          # API接口
│       ├── views/        # 页面组件
│       ├── router/       # 路由配置
│       └── utils/        # 工具函数
├── nginx.conf            # Nginx配置
├── start.sh              # Linux启动脚本
└── start.bat             # Windows启动脚本
```

## 数据库设计

### 表结构

1. **sys_user** - 用户表
2. **sys_role** - 角色表
3. **sys_option** - 选项表
4. **sys_module** - 模块表
5. **sys_permission** - 权限配置表

所有表主键统一使用36位UUID，包含createTime和updateTime自动填充字段。

## 环境要求

- JDK 17+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+
- Nginx (可选，用于生产环境)

## 后端部署

### 1. 配置数据库

修改 `src/main/resources/application.yml` 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/quanxianguanli?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: liyuan12345211
```

### 2. 创建数据库

```sql
CREATE DATABASE quanxianguanli CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 编译打包

```bash
mvn clean package
```

### 4. 启动服务

**Linux/Mac:**
```bash
chmod +x start.sh
./start.sh
```

**Windows:**
```cmd
start.bat
```

或者直接运行：
```bash
nohup java -jar target/quanxianguanli-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

## 前端部署

### 1. 安装依赖

```bash
cd frontend
npm install
```

### 2. 开发环境运行

```bash
npm run dev
```

### 3. 生产环境打包

```bash
npm run build
```

打包后的文件在 `frontend/dist` 目录。

### 4. 部署方式

#### 方式一：Nginx部署

1. 将 `frontend/dist` 目录内容复制到 Nginx 的 html 目录
2. 配置 Nginx（参考 `nginx.conf`）
3. 启动 Nginx

#### 方式二：内置Tomcat部署

1. 将 `frontend/dist` 目录内容复制到 `src/main/resources/static` 目录
2. 重新打包后端项目
3. 启动后端服务，前端会自动通过后端服务访问

## 功能说明

### 用户注册模块
- 身份证号验证
- 两步注册流程
- 自动解析身份证信息（性别、出生日期）

### 账号管理
- 用户列表查询（含未审核用户）
- 审核通过
- 重置密码
- 角色分配

### 角色管理
- 角色的增删改查
- 角色名唯一性校验

### 选项管理
- 选项的增删改查
- 按分组筛选

### 模块管理
- 树形结构展示
- 支持新增根节点和子节点
- 模块的增删改查

### 权限配置
- 角色-模块权限配置
- 支持可读、可新增、可更新、可见四种权限

## API接口

### 认证接口
- `POST /api/auth/login` - 用户登录

### 用户接口
- `POST /api/user/verify` - 验证身份证号
- `POST /api/user/register` - 用户注册
- `POST /api/user/approve/{userId}` - 审核通过
- `POST /api/user/reset-password/{userId}` - 重置密码
- `GET /api/user/list` - 用户列表
- `POST /api/user/assign-role` - 角色分配

### 角色接口
- `GET /api/role/list` - 角色列表
- `GET /api/role/{id}` - 角色详情
- `POST /api/role/save` - 保存角色
- `DELETE /api/role/{id}` - 删除角色

### 选项接口
- `GET /api/option/list` - 选项列表
- `GET /api/option/{id}` - 选项详情
- `POST /api/option/save` - 保存选项
- `DELETE /api/option/{id}` - 删除选项

### 模块接口
- `GET /api/module/list` - 模块列表
- `GET /api/module/tree` - 模块树
- `GET /api/module/{id}` - 模块详情
- `POST /api/module/save` - 保存模块
- `DELETE /api/module/{id}` - 删除模块

### 权限接口
- `GET /api/permission/role/{roleId}` - 获取角色权限
- `POST /api/permission/save` - 保存权限配置

## 默认配置

- 后端端口：8080
- 前端开发端口：3000
- JWT Token过期时间：24小时
- 默认密码：123456（重置后）

## 注意事项

1. 首次启动会自动创建数据库表结构
2. JWT Token存储在localStorage中
3. 所有接口（除登录和注册）都需要携带Token
4. 密码使用BCrypt加密存储
5. 身份证号验证支持18位标准格式

## 开发说明

### 后端开发
- 使用Spring Data JPA进行数据持久化
- JWT拦截器自动验证Token
- 跨域已配置，支持前端域名访问

### 前端开发
- 使用Vue3 Composition API
- Element Plus组件库
- Axios拦截器自动携带Token
- 路由守卫控制访问权限

## 许可证

MIT License

