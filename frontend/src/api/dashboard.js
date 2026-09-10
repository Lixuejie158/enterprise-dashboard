import request from './request'

export const dashboardAPI = {
  overview: () => request.get('/dashboard/overview'),
  revenueTrend: (year) => request.get('/dashboard/revenue-trend', { params: { year } }),
  salesByRegion: () => request.get('/dashboard/sales-by-region'),
  topCustomers: (limit) => request.get('/dashboard/top-customers', { params: { limit } }),
  productSales: (limit) => request.get('/dashboard/product-sales', { params: { limit } }),
  riskSummary: () => request.get('/dashboard/risk-summary')
}
