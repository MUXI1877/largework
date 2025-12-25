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
      },
      {
        path: '/customer-management',
        name: 'CustomerManagement',
        component: () => import('../views/CustomerManagement.vue'),
        meta: { title: '客户管理', moduleId: 'm008' }
      },
      {
        path: '/team-management',
        name: 'TeamManagement',
        component: () => import('../views/TeamManagement.vue'),
        meta: { title: '团队信息管理', moduleId: 'm009' }
      },
      {
        path: '/product-management',
        name: 'ProductManagement',
        component: () => import('../views/ProductManagement.vue'),
        meta: { title: '产品管理', moduleId: 'm010' }
      },
      {
        path: '/customer-visit-management',
        name: 'CustomerVisitManagement',
        component: () => import('../views/CustomerVisitManagement.vue'),
        meta: { title: '客户来访管理', moduleId: 'm012' }
      },
      {
        path: '/project-opportunity-management',
        name: 'ProjectOpportunityManagement',
        component: () => import('../views/ProjectOpportunityManagement.vue'),
        meta: { title: '销售机会管理', moduleId: 'm013' }
      },
      {
        path: '/product-inventory',
        name: 'ProductInventory',
        component: () => import('../views/ProductInventory.vue'),
        meta: { title: '销售库存查询', moduleId: 'm014' }
      },
      {
        path: '/sales-quotation-management',
        name: 'SalesQuotationManagement',
        component: () => import('../views/SalesQuotationManagement.vue'),
        meta: { title: '销售报价管理', moduleId: 'm015' }
      },
      {
        path: '/reduced-stock-management',
        name: 'ReducedStockManagement',
        component: () => import('../views/ReducedStockManagement.vue'),
        meta: { title: '销售降库管理', moduleId: 'm016' }
      },
      {
        path: '/bidding-info-management',
        name: 'BiddingInfoManagement',
        component: () => import('../views/BiddingInfoManagement.vue'),
        meta: { title: '投标管理', moduleId: 'm018' }
      },
      {
        path: '/price-book-management',
        name: 'PriceBookManagement',
        component: () => import('../views/PriceBookManagement.vue'),
        meta: { title: '价格本管理', moduleId: 'm019' }
      },
      {
        path: '/ar-plan',
        name: 'ReceivablePlan',
        component: () => import('../views/ReceivablePlan.vue'),
        meta: { title: '应收账计划', moduleId: 'm021' }
      },
      {
        path: '/ar-receipt',
        name: 'ReceivableReceipt',
        component: () => import('../views/ReceivableReceipt.vue'),
        meta: { title: '回款登记', moduleId: 'm022' }
      },
      {
        path: '/ar-query',
        name: 'ReceivableQuery',
        component: () => import('../views/ReceivableQuery.vue'),
        meta: { title: '应收账查询', moduleId: 'm023' }
      },
      {
        path: '/destination-management',
        name: 'DestinationManagement',
        component: () => import('../views/daily/DestinationManagement.vue'),
        meta: { title: '去向管理', moduleId: 'm024' }
      },
      {
        path: '/weekly-report-management',
        name: 'WeeklyReportManagement',
        component: () => import('../views/daily/WeeklyReportManagement.vue'),
        meta: { title: '周报管理', moduleId: 'm025' }
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

