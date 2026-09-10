import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/dashboard/index.vue'
import Finance from '../views/finance/index.vue'
import Sales from '../views/sales/index.vue'
import Risk from '../views/risk/index.vue'

const routes = [
  { path: '/', redirect: '/dashboard' },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard },
  { path: '/finance', name: 'Finance', component: Finance },
  { path: '/sales', name: 'Sales', component: Sales },
  { path: '/risk', name: 'Risk', component: Risk }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
