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
        <el-menu :default-active="activeMenu" router class="menu">
          <el-menu-item
            v-for="item in [...HOME_MENU, ...menuItems]"
            :key="item.path"
            :index="item.path"
          >
            <el-icon>
              <component :is="item.icon" />
            </el-icon>
            <span>{{ item.label }}</span>
          </el-menu-item>
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
import { User, Avatar, Setting, Menu as MenuIcon, Lock, House } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)

const HOME_MENU = [{ path: '/home', label: '首页', icon: House }]
const ICON_MAP = {
  m002: User,
  m003: Avatar,
  m004: MenuIcon,
  m005: Lock,
  m007: Setting
}

const menuItems = ref([])

const loadMenu = async () => {
  try {
    const res = await getMyPermissions()
    const permissions = res.data || []
    const allowedModuleIds = new Set(
      permissions.filter((p) => p.canSee).map((p) => p.moduleId)
    )

    // 动态从路由构建菜单，支持新增模块
    const dynamicMenus = router
      .getRoutes()
      .filter(
        (r) =>
          r.meta &&
          r.meta.moduleId &&
          allowedModuleIds.has(r.meta.moduleId) &&
          r.path.startsWith('/')
      )
      .map((r) => ({
        path: r.path,
        label: r.meta.title || r.name || r.path,
        icon: ICON_MAP[r.meta.moduleId] || MenuIcon
      }))

    const seen = new Set()
    menuItems.value = dynamicMenus
      .filter((m) => {
        if (seen.has(m.path)) return false
        seen.add(m.path)
        return true
      })
      .sort((a, b) => a.label.localeCompare(b.label))

    if (menuItems.value.length === 0) {
      ElMessage.warning('当前角色无可访问模块')
      return
    }

    const isCurrentAllowed = menuItems.value.some(
      (item) => item.path === route.path
    )
    if (!isCurrentAllowed) {
      router.replace(menuItems.value[0].path)
    }
  } catch (error) {
    ElMessage.error(error.message || '加载权限信息失败')
    removeToken()
    router.push('/login')
  }
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

