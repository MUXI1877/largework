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
        <el-menu
          :default-active="activeMenu"
          router
          class="menu"
        >
          <el-menu-item index="/user-management">
            <el-icon><User /></el-icon>
            <span>账号管理</span>
          </el-menu-item>
          <el-menu-item index="/role-management">
            <el-icon><Avatar /></el-icon>
            <span>角色管理</span>
          </el-menu-item>
          <el-menu-item index="/option-management">
            <el-icon><Setting /></el-icon>
            <span>选项管理</span>
          </el-menu-item>
          <el-menu-item index="/module-management">
            <el-icon><Menu /></el-icon>
            <span>模块管理</span>
          </el-menu-item>
          <el-menu-item index="/permission-management">
            <el-icon><Lock /></el-icon>
            <span>权限配置</span>
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
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { removeToken } from '../utils/auth'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)

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

