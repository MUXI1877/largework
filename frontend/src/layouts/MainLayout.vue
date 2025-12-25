<template>
  <el-container class="main-container">
    <el-header class="header">
      <div class="header-left">
        <h2>权限管理平台</h2>
      </div>
      <div class="header-right">
        <el-button type="danger" @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px" class="aside">
        <el-menu :default-active="activeMenu" router class="menu" :default-openeds="defaultOpeneds">
          <!-- 首页 -->
          <el-menu-item index="/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          
          <!-- 分级菜单 -->
          <template v-for="item in menuTree" :key="item.id">
            <!-- 父模块（有子模块） -->
            <el-sub-menu v-if="item.children && item.children.length > 0" :index="item.id">
              <template #title>
                <el-icon v-if="getIconComponent(item.icon)">
                  <component :is="getIconComponent(item.icon)" />
                </el-icon>
                <span>{{ item.cnName }}</span>
              </template>
              <!-- 子模块 -->
              <el-menu-item
                v-for="child in item.children"
                :key="child.id"
                :index="getRoutePath(child.id)"
              >
                <el-icon v-if="getIconComponent(child.icon)">
                  <component :is="getIconComponent(child.icon)" />
                </el-icon>
                <span>{{ child.cnName }}</span>
              </el-menu-item>
            </el-sub-menu>
            
            <!-- 独立模块（无子模块且有路由路径） -->
            <el-menu-item v-else-if="getRoutePath(item.id) !== '/home'" :index="getRoutePath(item.id)">
              <el-icon v-if="getIconComponent(item.icon)">
                <component :is="getIconComponent(item.icon)" />
              </el-icon>
              <span>{{ item.cnName }}</span>
            </el-menu-item>
          </template>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { removeToken } from '../utils/auth'
import { getMyPermissions } from '../api/permission'
import { getModuleTree } from '../api/module'
import { 
  User, 
  Avatar, 
  Setting, 
  Menu as MenuIcon, 
  Lock, 
  House,
  Folder,
  UserFilled,
  Box,
  DataBoard,
  Collection,
  Money,
  ShoppingCart,
  Opportunity,
  Document,
  Goods,
  Files,
  Calendar,
  Location
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)
const menuTree = ref([])
const defaultOpeneds = ref([])

// 图标映射表（Element Plus图标名称 -> Vue组件）
const ICON_MAP = {
  'el-icon-user': User,
  'el-icon-s-custom': Avatar,
  'el-icon-menu': MenuIcon,
  'el-icon-lock': Lock,
  'el-icon-setting': Setting,
  'el-icon-folder': Folder,
  'el-icon-user-solid': UserFilled,
  'el-icon-box': Box,
  'el-icon-data-board': DataBoard,
  'el-icon-collection': Collection,
  'el-icon-money': Money,
  'el-icon-shopping-cart': ShoppingCart,
  'el-icon-opportunity': Opportunity,
  'el-icon-document': Document,
  'el-icon-goods': Goods,
  'el-icon-files': Files,
  'el-icon-date': Calendar,
  'el-icon-location': Location
}

// 模块ID到路由路径的映射
const MODULE_PATH_MAP = {
  'm002': '/user-management',
  'm003': '/role-management',
  'm004': '/module-management',
  'm005': '/permission-management',
  'm007': '/option-management',
  'm008': '/customer-management',
  'm009': '/team-management',
  'm010': '/product-management',
  'm012': '/customer-visit-management',
  'm013': '/project-opportunity-management',
  'm014': '/product-inventory',
  'm015': '/sales-quotation-management',
  'm016': '/reduced-stock-management',
  'm018': '/bidding-info-management',
  'm019': '/price-book-management',
  'm021': '/ar-plan',
  'm022': '/ar-receipt',
  'm023': '/ar-query',
  'm024': '/destination-management',
  'm025': '/weekly-report-management'
}

// 获取图标组件
const getIconComponent = (iconName) => {
  if (!iconName) return null
  return ICON_MAP[iconName] || MenuIcon
}

// 根据模块ID获取路由路径
const getRoutePath = (moduleId) => {
  return MODULE_PATH_MAP[moduleId] || '/home'
}

// 过滤模块树，只保留有权限的模块
const filterModuleTree = (tree, allowedModuleIds) => {
  return tree
    .filter(node => {
      // 检查当前节点是否有权限
      const hasPermission = allowedModuleIds.has(node.id)
      
      // 如果有子节点，递归过滤
      if (node.children && node.children.length > 0) {
        node.children = filterModuleTree(node.children, allowedModuleIds)
        // 如果子节点被过滤后还有剩余，或者当前节点有权限，则保留
        return node.children.length > 0 || hasPermission
      }
      
      // 叶子节点，必须有权限才保留
      return hasPermission
    })
    .map(node => {
      // 如果父节点没有权限但子节点有权限，父节点仍然显示（用于展开）
      if (node.children && node.children.length > 0 && !allowedModuleIds.has(node.id)) {
        // 父节点没有权限，但子节点有权限，保留父节点结构
        return node
      }
      return node
    })
}

const loadMenu = async () => {
  try {
    // 获取权限信息
    const permRes = await getMyPermissions()
    const permissions = permRes.data || []
    const allowedModuleIds = new Set(
      permissions.filter((p) => p.canSee).map((p) => p.moduleId)
    )

    if (allowedModuleIds.size === 0) {
      ElMessage.warning('当前角色无可访问模块')
      return
    }

    // 获取模块树
    const moduleRes = await getModuleTree()
    const allModules = moduleRes.data || []
    
    // 过滤模块树，只保留有权限的模块
    const filteredTree = filterModuleTree(allModules, allowedModuleIds)
    
    // 设置默认展开的父模块
    defaultOpeneds.value = filteredTree
      .filter(node => node.isParent && node.children && node.children.length > 0)
      .map(node => node.id)
    
    menuTree.value = filteredTree

    // 检查当前路由是否在允许的模块中
    const currentModuleId = Object.keys(MODULE_PATH_MAP).find(
      id => MODULE_PATH_MAP[id] === route.path
    )
    
    if (currentModuleId && !allowedModuleIds.has(currentModuleId)) {
      // 当前路由对应的模块无权限，跳转到第一个有权限的模块
      const firstAllowedPath = findFirstAllowedPath(filteredTree, allowedModuleIds)
      if (firstAllowedPath) {
        router.replace(firstAllowedPath)
      } else {
        router.replace('/home')
      }
    }
  } catch (error) {
    ElMessage.error(error.message || '加载菜单失败')
    removeToken()
    router.push('/login')
  }
}

// 查找第一个有权限的路径
const findFirstAllowedPath = (tree, allowedModuleIds) => {
  for (const node of tree) {
    if (allowedModuleIds.has(node.id) && MODULE_PATH_MAP[node.id]) {
      return MODULE_PATH_MAP[node.id]
    }
    if (node.children && node.children.length > 0) {
      const path = findFirstAllowedPath(node.children, allowedModuleIds)
      if (path) return path
    }
  }
  return null
}

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    removeToken()
    router.push('/login')
  })
}

onMounted(() => {
  loadMenu()
})
</script>

<style scoped>
.main-container {
  height: 100vh;
}

.header {
  background: #409eff;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header h2 {
  margin: 0;
}

.aside {
  background: #fff;
  border-right: 1px solid #e4e7ed;
}

.menu {
  border-right: none;
}

.main {
  background: #f5f5f5;
  padding: 20px;
}
</style>

