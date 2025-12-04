# 快速启动指南

## 前置条件

1. 安装 JDK 17+
2. 安装 Maven 3.6+
3. 安装 Node.js 16+
4. 安装 MySQL 8.0+
5. 创建数据库（用户名：root，密码：liyuan12345211）

## 快速启动步骤

### 1. 创建数据库

```sql
CREATE DATABASE quanxianguanli CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

或者执行 `database/init.sql` 文件。

### 2. 启动后端

```bash
# 编译打包
mvn clean package

# 启动（Linux/Mac）
./start.sh

# 启动（Windows）
start.bat

# 或者直接运行
java -jar target/quanxianguanli-0.0.1-SNAPSHOT.jar
```

后端启动后访问：http://localhost:8080

### 3. 启动前端

```bash
cd frontend

# 安装依赖（首次运行）
npm install

# 开发模式启动
npm run dev
```

前端启动后访问：http://localhost:3000

### 4. 测试登录

1. 首先访问 http://localhost:3000/register 注册账号
2. 注册后等待管理员审核
3. 管理员登录后，在"账号管理"页面审核用户
4. 审核通过后，用户可以使用手机号作为用户名，密码123456登录

## 默认账号

首次使用需要注册，注册后由管理员审核。

## 常见问题

### 1. 数据库连接失败

检查 `application.yml` 中的数据库配置：
- 数据库名称：quanxianguanli
- 用户名：root
- 密码：liyuan12345211

### 2. 端口被占用

- 后端端口：8080（可在 application.yml 中修改）
- 前端端口：3000（可在 vite.config.js 中修改）

### 3. 前端无法连接后端

检查后端是否正常启动，以及 vite.config.js 中的代理配置。

### 4. JWT Token过期

Token有效期为24小时，过期后需要重新登录。

## 生产环境部署

### 方式一：Nginx部署

1. 打包前端：`cd frontend && npm run build`
2. 将 `frontend/dist` 内容复制到 Nginx html 目录
3. 配置 Nginx（参考 `nginx.conf`）
4. 启动后端服务
5. 启动 Nginx

### 方式二：内置Tomcat部署

1. 打包前端：`cd frontend && npm run build`
2. 将 `frontend/dist` 内容复制到 `src/main/resources/static`
3. 重新打包后端：`mvn clean package`
4. 启动后端服务

## 开发说明

- 后端使用 Spring Data JPA，首次启动会自动创建表结构
- 前端使用 Vite 作为构建工具
- 所有API接口需要携带JWT Token（除登录和注册接口）
- 密码使用BCrypt加密存储

