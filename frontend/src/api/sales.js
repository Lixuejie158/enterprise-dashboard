import request from './request'

export const salesAPI = {
  regionAnalysis: () => request.get('/sales/region-analysis'),
  channelAnalysis: () => request.get('/sales/channel-analysis'),
  customerRanking: (limit, region) => request.get('/sales/customer-ranking', { params: { limit, region } }),
  productRanking: (limit, category) => request.get('/sales/product-ranking', { params: { limit, category } }),
  monthlyComparison: (year) => request.get('/sales/monthly-comparison', { params: { year } })
}
