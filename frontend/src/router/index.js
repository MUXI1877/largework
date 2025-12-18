import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from '../utils/auth'
import { getMyPermissions } from '../api/permission'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('../layouts/MainLayout.vue'),
    redirect: '/home',
    meta: { requiresAuth: true },
    children: [
      {
        path: '/home',
        name: 'Home',
        component: () => import('../views/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: '/user-management',
        name: 'UserManagement',
        component: () => import('../views/UserManagement.vue'),
        meta: { title: '账号管理', moduleId: 'm002' }
      },
      {
        path: '/role-management',
        name: 'RoleManagement',
        component: () => import('../views/RoleManagement.vue'),
        meta: { title: '角色管理', moduleId: 'm003' }
      },
      {
        path: '/option-management',
        name: 'OptionManagement',
        component: () => import('../views/OptionManagement.vue'),
        meta: { title: '选项管理', moduleId: 'm007' }
      },
      {
        path: '/module-management',
        name: 'ModuleManagement',
        component: () => import('../views/ModuleManagement.vue'),
        meta: { title: '模块管理', moduleId: 'm004' }
      },
      {
        path: '/permission-management',
        name: 'PermissionManagement',
        component: () => import('../views/PermissionManagement.vue'),
        meta: { title: '权限配置', moduleId: 'm005' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = getToken()
  if (to.meta.requiresAuth && !token) {
    next('/login')
    return
  }

  if (to.path === '/login' && token) {
    next('/')
    return
  }

  if (!to.meta.requiresAuth) {
    next()
    return
  }

  // 权限校验：无权限访问目标模块则跳转到第一个有权限的模块
  getMyPermissions()
    .then((res) => {
      const perms = res.data || []
      const canSeeIds = new Set(
        perms.filter((p) => p.canSee).map((p) => p.moduleId)
      )

      // 如果当前路由对应模块不可见，跳转第一个可见模块
      const targetModuleId = to.meta?.moduleId
      if (targetModuleId && !canSeeIds.has(targetModuleId)) {
        const firstAllowedPath =
          router
            .getRoutes()
            .filter(
              (r) =>
                r.meta &&
                r.meta.moduleId &&
                canSeeIds.has(r.meta.moduleId) &&
                r.path.startsWith('/')
            )
            .map((r) => r.path)[0] || '/login'

        if (firstAllowedPath === '/login') {
          ElMessage.warning('当前角色暂无可访问模块')
          removeToken()
        }
        next(firstAllowedPath)
        return
      }

      next()
    })
    .catch(() => {
      // 拉取权限失败，回登录
      removeToken()
      next('/login')
    })
})

export default router

