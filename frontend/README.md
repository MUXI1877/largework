# 权限管理平台 - 前端使用说明

## 项目概述

这是一个基于 Vue 3 + Element Plus 的权限管理平台前端应用，实现了完整的权限管理功能，包括：

- 用户登录/注册
- 角色管理
- 模块管理
- 权限配置
- 业务模块管理（客户管理、设备管理等）

## 技术栈

- **Vue 3** - 前端框架
- **Vue Router 4** - 路由管理
- **Element Plus** - UI组件库
- **Axios** - HTTP请求库
- **Vite** - 构建工具

## 权限管理功能

### 1. 权限指令

使用 `v-permission` 指令控制按钮和元素的显示/隐藏：

```vue
<el-button v-permission="{ moduleId: 'm002', action: 'add' }">
  新增用户
</el-button>
```

**参数说明：**
- `moduleId`: 模块ID（如 'm002' 表示账号管理模块）
- `action`: 操作类型（'read', 'add', 'update', 'delete', 'see'）

### 2. 权限工具函数

在组件中使用权限工具函数：

```javascript
import { canRead, canAdd, canUpdate, canDelete } from '../utils/permission'

// 检查权限
if (canAdd('m002')) {
  // 执行新增操作
}
```

### 3. 路由权限控制

路由会自动检查用户权限，无权限访问的模块会被重定向到第一个有权限的模块。

### 4. 菜单权限控制

菜单会根据用户权限动态显示，只显示用户有权限访问的模块。

## 模块ID说明

- `m002` - 账号管理
- `m003` - 角色管理
- `m004` - 模块管理
- `m005` - 权限配置
- `m006` - 客户管理
- `m007` - 选项管理
- `m008` - 设备管理

## 安装和运行

### 1. 安装依赖

```bash
cd frontend
npm install
```

### 2. 开发模式运行

```bash
npm run dev
```

前端服务将在 `http://localhost:3000` 启动。

### 3. 构建生产版本

```bash
npm run build
```

构建产物将输出到 `dist` 目录。

## 项目结构

```
frontend/
├── src/
│   ├── api/              # API接口定义
│   ├── components/       # 公共组件
│   ├── directives/       # 自定义指令（权限指令）
│   ├── layouts/         # 布局组件
│   ├── router/          # 路由配置
│   ├── utils/           # 工具函数
│   │   ├── auth.js      # 认证工具
│   │   ├── permission.js # 权限工具
│   │   └── idCard.js    # 身份证工具
│   └── views/           # 页面组件
│       ├── basic/       # 业务模块页面
│       ├── Home.vue     # 首页
│       ├── Login.vue    # 登录页
│       ├── Register.vue # 注册页
│       └── ...          # 其他管理页面
├── index.html           # HTML模板
├── package.json         # 项目配置
└── vite.config.js      # Vite配置
```

## 权限配置流程

1. **创建模块**：在"模块管理"中创建系统模块
2. **创建角色**：在"角色管理"中创建角色
3. **配置权限**：在"权限配置"中为角色分配模块权限
4. **分配角色**：在"账号管理"中为用户分配角色

## API配置

前端API请求会自动代理到后端服务（`http://localhost:8080/api`），配置在 `vite.config.js` 中。

## 注意事项

1. 确保后端服务已启动并运行在 `http://localhost:8080`
2. 登录后会自动加载用户权限并缓存
3. 退出登录时会清除权限缓存
4. 权限变更后需要重新登录才能生效

## 开发建议

1. 新增业务模块时，需要在路由中添加对应的路由配置和 `moduleId`
2. 使用权限指令控制按钮显示，而不是在代码中硬编码判断
3. 确保所有需要权限控制的操作都添加了相应的权限检查

