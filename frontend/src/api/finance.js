import request from './request'

export const financeAPI = {
  monthlyReport: (year) => request.get('/finance/monthly-report', { params: { year } }),
  balanceStructure: (yearMonth) => request.get('/finance/balance-structure', { params: { yearMonth } }),
  costAnalysis: (year) => request.get('/finance/cost-analysis', { params: { year } })
}
