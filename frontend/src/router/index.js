import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  {
    path: '/',
    component: () => import('../layout/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: '首页概览', component: () => import('../views/Dashboard.vue') },
      { path: 'jobs', name: '兼职信息管理', component: () => import('../views/JobList.vue') },
      { path: 'orders', name: '接单订单管理', component: () => import('../views/OrderList.vue') },
      { path: 'users', name: '用户管理', component: () => import('../views/UserList.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && to.path !== '/register' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
