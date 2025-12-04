import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '../utils/auth'

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
    redirect: '/user-management',
    meta: { requiresAuth: true },
    children: [
      {
        path: '/user-management',
        name: 'UserManagement',
        component: () => import('../views/UserManagement.vue')
      },
      {
        path: '/role-management',
        name: 'RoleManagement',
        component: () => import('../views/RoleManagement.vue')
      },
      {
        path: '/option-management',
        name: 'OptionManagement',
        component: () => import('../views/OptionManagement.vue')
      },
      {
        path: '/module-management',
        name: 'ModuleManagement',
        component: () => import('../views/ModuleManagement.vue')
      },
      {
        path: '/permission-management',
        name: 'PermissionManagement',
        component: () => import('../views/PermissionManagement.vue')
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
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router

