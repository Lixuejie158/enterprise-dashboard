import request from './request'

export const riskAPI = {
  list: (params) => request.get('/risk/list', { params }),
  statistics: () => request.get('/risk/statistics')
}
