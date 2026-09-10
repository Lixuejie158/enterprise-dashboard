<template>
  <div class="dashboard-container">
    <!-- Header -->
    <div class="dashboard-header">
      <h1>企业经营数据分析平台</h1>
      <div class="datetime">{{ currentDateTime }}</div>
    </div>

    <!-- 6-Panel Grid Layout -->
    <div class="dashboard-grid">
      <!-- Left Column: KPI Cards -->
      <div class="grid-item kpi-row">
        <KpiCard label="营收总额" :value="animatedValues.revenue" format="currency" :trend="8.5" color="#00d4ff" />
        <KpiCard label="净利润" :value="animatedValues.profit" format="currency" :trend="12.3" color="#00c853" />
        <KpiCard label="毛利率" :value="animatedValues.margin" format="percent" :trend="-2.1" color="#ff9800" />
        <KpiCard label="订单数" :value="animatedValues.orders" format="number" :trend="5.7" color="#ab47bc" />
      </div>

      <!-- Left Column: Region Sales Chart -->
      <div class="grid-item chart-item region-chart">
        <ChartWrapper title="区域销售分布" :options="regionChartOptions" height="280px" />
      </div>

      <!-- Left Column: Product Sales Pie Chart -->
      <div class="grid-item chart-item product-chart">
        <ChartWrapper title="产品销售占比" :options="productChartOptions" height="280px" />
      </div>

      <!-- Center Column: Revenue Trend Line Chart -->
      <div class="grid-item chart-item trend-chart">
        <ChartWrapper title="营收/成本/利润趋势" :options="trendChartOptions" height="320px" />
      </div>

      <!-- Center Column: Customer TOP10 -->
      <div class="grid-item chart-item customer-chart">
        <ChartWrapper title="客户销售额TOP10" :options="customerChartOptions" height="320px" />
      </div>

      <!-- Right Column: Risk Summary -->
      <div class="grid-item risk-summary">
        <div class="risk-card">
          <div class="risk-title">风险预警汇总</div>
          <div class="risk-kpis">
            <div class="risk-kpi high">
              <span class="risk-count">{{ riskData.high }}</span>
              <span class="risk-label">高危</span>
            </div>
            <div class="risk-kpi medium">
              <span class="risk-count">{{ riskData.medium }}</span>
              <span class="risk-label">中危</span>
            </div>
            <div class="risk-kpi low">
              <span class="risk-count">{{ riskData.low }}</span>
              <span class="risk-label">低危</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Column: Alert List with Scroll -->
      <div class="grid-item alert-list">
        <div class="alert-card">
          <div class="alert-title">最新预警列表</div>
          <ScrollList :list="alertList" maxHeight="240px">
            <template #default="{ item }">
              <div class="alert-item">
                <div class="alert-header">
                  <span class="alert-level" :class="item.level">{{ item.levelText }}</span>
                  <span class="alert-time">{{ item.time }}</span>
                </div>
                <div class="alert-content">{{ item.content }}</div>
              </div>
            </template>
          </ScrollList>
        </div>
      </div>

      <!-- Right Column: Receivable vs Payable -->
      <div class="grid-item chart-item payable-chart">
        <ChartWrapper title="应收/应付对比" :options="payableChartOptions" height="280px" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, computed } from 'vue'
import KpiCard from '../../components/KpiCard.vue'
import ChartWrapper from '../../components/ChartWrapper.vue'
import ScrollList from '../../components/ScrollList.vue'

// Live datetime
const currentDateTime = ref('')
let datetimeTimer = null

const updateDateTime = () => {
  const now = new Date()
  currentDateTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// Animated values
const animatedValues = reactive({
  revenue: 0,
  profit: 0,
  margin: 0,
  orders: 0
})

const targetValues = {
  revenue: 125680000,
  profit: 18560000,
  margin: 18.6,
  orders: 8652
}

let animationTimer = null

const animateValues = () => {
  const duration = 2000
  const startTime = Date.now()
  const startValues = { ...animatedValues }

  const animate = () => {
    const elapsed = Date.now() - startTime
    const progress = Math.min(elapsed / duration, 1)
    const easeOut = 1 - Math.pow(1 - progress, 3)

    animatedValues.revenue = Math.round(startValues.revenue + (targetValues.revenue - startValues.revenue) * easeOut)
    animatedValues.profit = Math.round(startValues.profit + (targetValues.profit - startValues.profit) * easeOut)
    animatedValues.margin = parseFloat((startValues.margin + (targetValues.margin - startValues.margin) * easeOut).toFixed(1))
    animatedValues.orders = Math.round(startValues.orders + (targetValues.orders - startValues.orders) * easeOut)

    if (progress < 1) {
      animationTimer = requestAnimationFrame(animate)
    }
  }
  animate()
}

// Risk data
const riskData = reactive({
  high: 3,
  medium: 12,
  low: 25
})

// Alert list
const alertList = ref([
  { level: 'high', levelText: '高危', time: '10:23', content: '供应商账期超90天未结算' },
  { level: 'high', levelText: '高危', time: '09:45', content: '客户信用额度超标' },
  { level: 'medium', levelText: '中危', time: '09:12', content: '库存周转率低于行业均值' },
  { level: 'medium', levelText: '中危', time: '08:56', content: '应收账款逾期30天' },
  { level: 'low', levelText: '低危', time: '08:30', content: '合同即将到期预警' },
  { level: 'low', levelText: '低危', time: '08:15', content: '客户投诉处理超时' },
  { level: 'low', levelText: '低危', time: '07:52', content: '银行对账差异' }
])

// Chart options - Region Sales Bar Chart
const regionChartOptions = computed(() => ({
  tooltip: { trigger: 'axis' },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: {
    type: 'category',
    data: ['华东', '华南', '华北', '华中', '西南', '西北', '东北'],
    axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
    axisLabel: { color: 'rgba(255,255,255,0.7)' }
  },
  yAxis: {
    type: 'value',
    axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
    axisLabel: { color: 'rgba(255,255,255,0.7)', formatter: (v) => (v / 10000).toFixed(0) + '万' },
    splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } }
  },
  series: [{
    type: 'bar',
    data: [4200, 3800, 2900, 2500, 2100, 1800, 1200],
    itemStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: '#00d4ff' },
          { offset: 1, color: '#0066cc' }
        ]
      },
      borderRadius: [4, 4, 0, 0]
    },
    barWidth: '50%'
  }]
}))

// Chart options - Product Sales Pie Chart
const productChartOptions = computed(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c}万 ({d}%)'
  },
  legend: {
    orient: 'vertical',
    right: '5%',
    top: 'center',
    textStyle: { color: 'rgba(255,255,255,0.7)' }
  },
  series: [{
    type: 'pie',
    radius: ['40%', '70%'],
    center: ['35%', '50%'],
    avoidLabelOverlap: false,
    itemStyle: {
      borderRadius: 8,
      borderColor: '#0a1628',
      borderWidth: 2
    },
    label: { show: false },
    emphasis: {
      label: { show: true, fontSize: 14, fontWeight: 'bold', color: '#fff' }
    },
    data: [
      { value: 4500, name: '电子产品', itemStyle: { color: '#00d4ff' } },
      { value: 3200, name: '机械设备', itemStyle: { color: '#ff9800' } },
      { value: 2800, name: '原材料', itemStyle: { color: '#ab47bc' } },
      { value: 1800, name: '化工产品', itemStyle: { color: '#26a69a' } },
      { value: 1200, name: '其他', itemStyle: { color: '#78909c' } }
    ]
  }]
}))

// Chart options - Revenue/Cost/Profit Trend Line Chart
const trendChartOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    formatter: (params) => {
      let result = params[0].name + '<br/>'
      params.forEach(p => {
        result += p.marker + p.seriesName + ': ' + (p.value / 10000).toFixed(0) + '万<br/>'
      })
      return result
    }
  },
  legend: {
    data: ['营收', '成本', '利润'],
    textStyle: { color: 'rgba(255,255,255,0.7)' },
    top: '5%'
  },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
  xAxis: {
    type: 'category',
    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
    axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
    axisLabel: { color: 'rgba(255,255,255,0.7)' }
  },
  yAxis: {
    type: 'value',
    axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
    axisLabel: { color: 'rgba(255,255,255,0.7)', formatter: (v) => (v / 10000).toFixed(0) + '万' },
    splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } }
  },
  series: [
    {
      name: '营收',
      type: 'line',
      smooth: true,
      data: [8200, 7800, 9100, 9800, 10500, 11200, 10800, 12000, 11500, 12500, 13200, 14680],
      lineStyle: { color: '#00d4ff', width: 3 },
      itemStyle: { color: '#00d4ff' },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(0,212,255,0.4)' },
            { offset: 1, color: 'rgba(0,212,255,0)' }
          ]
        }
      }
    },
    {
      name: '成本',
      type: 'line',
      smooth: true,
      data: [5800, 5400, 6200, 6800, 7200, 7600, 7400, 8200, 7800, 8500, 9000, 9800],
      lineStyle: { color: '#ff4444', width: 3 },
      itemStyle: { color: '#ff4444' }
    },
    {
      name: '利润',
      type: 'line',
      smooth: true,
      data: [2400, 2400, 2900, 3000, 3300, 3600, 3400, 3800, 3700, 4000, 4200, 4880],
      lineStyle: { color: '#00c853', width: 3 },
      itemStyle: { color: '#00c853' }
    }
  ]
}))

// Chart options - Customer TOP10 Horizontal Bar
const customerChartOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    formatter: (params) => params[0].name + ': ' + (params[0].value / 10000).toFixed(2) + '万'
  },
  grid: { left: '3%', right: '8%', bottom: '3%', top: '3%', containLabel: true },
  xAxis: {
    type: 'value',
    axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
    axisLabel: { color: 'rgba(255,255,255,0.7)', formatter: (v) => (v / 10000).toFixed(0) + '万' },
    splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } }
  },
  yAxis: {
    type: 'category',
    data: ['客户A', '客户B', '客户C', '客户D', '客户E', '客户F', '客户G', '客户H', '客户I', '客户J'],
    axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
    axisLabel: { color: 'rgba(255,255,255,0.7)' }
  },
  series: [{
    type: 'bar',
    data: [1850, 1620, 1480, 1350, 1200, 1080, 950, 820, 750, 680],
    itemStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 1, y2: 0,
        colorStops: [
          { offset: 0, color: '#0066cc' },
          { offset: 1, color: '#00d4ff' }
        ]
      },
      borderRadius: [0, 4, 4, 0]
    },
    barWidth: '60%'
  }]
}))

// Chart options - Receivable vs Payable
const payableChartOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    formatter: (params) => params[0].name + '<br/>' +
      params.map(p => p.marker + p.seriesName + ': ' + (p.value / 10000).toFixed(2) + '万').join('<br/>')
  },
  legend: {
    data: ['应收账款', '应付账款'],
    textStyle: { color: 'rgba(255,255,255,0.7)' },
    top: '5%'
  },
  grid: { left: '3%', right: '4%', bottom: '10%', top: '20%', containLabel: true },
  xAxis: {
    type: 'category',
    data: ['1月', '2月', '3月', '4月', '5月', '6月'],
    axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
    axisLabel: { color: 'rgba(255,255,255,0.7)' }
  },
  yAxis: {
    type: 'value',
    axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
    axisLabel: { color: 'rgba(255,255,255,0.7)', formatter: (v) => (v / 10000).toFixed(0) + '万' },
    splitLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } }
  },
  series: [
    {
      name: '应收账款',
      type: 'bar',
      data: [4500, 4800, 5200, 4900, 5300, 5800],
      itemStyle: { color: '#00d4ff', borderRadius: [4, 4, 0, 0] }
    },
    {
      name: '应付账款',
      type: 'bar',
      data: [3200, 3500, 3800, 3600, 4000, 4200],
      itemStyle: { color: '#ff9800', borderRadius: [4, 4, 0, 0] }
    }
  ]
}))

// Auto-refresh
let refreshTimer = null

const refreshData = () => {
  // Simulate data refresh - in real app would call API
  console.log('Refreshing dashboard data...')
}

onMounted(() => {
  updateDateTime()
  datetimeTimer = setInterval(updateDateTime, 1000)
  animateValues()
  refreshTimer = setInterval(refreshData, 30000)
})

onUnmounted(() => {
  if (datetimeTimer) clearInterval(datetimeTimer)
  if (animationTimer) cancelAnimationFrame(animationTimer)
  if (refreshTimer) clearInterval(refreshTimer)
})
</script>

<style scoped>
@import '../../styles/dashboard.scss';

.dashboard-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #0a1628 0%, #1a2a4a 100%);
  color: rgba(255, 255, 255, 0.9);
  padding: 20px;
}

.dashboard-header {
  text-align: center;
  padding: 20px 0;
}

.dashboard-header h1 {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 10px;
  background: linear-gradient(90deg, #00d4ff, #00c853);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.datetime {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.6);
  font-family: 'Monaco', 'Courier New', monospace;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(12, 1fr);
  grid-template-rows: auto auto auto auto;
  gap: 20px;
  margin-top: 20px;
}

.grid-item {
  min-width: 0;
}

.kpi-row {
  grid-column: 1 / -1;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.region-chart {
  grid-column: 1 / 5;
  grid-row: 2;
}

.product-chart {
  grid-column: 5 / 9;
  grid-row: 2;
}

.risk-summary {
  grid-column: 9 / 13;
  grid-row: 2;
}

.risk-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px;
  height: 100%;
}

.risk-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.risk-kpis {
  display: flex;
  justify-content: space-around;
  text-align: center;
}

.risk-kpi {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.risk-count {
  font-size: 32px;
  font-weight: 600;
}

.risk-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
}

.risk-kpi.high .risk-count { color: #ff4444; }
.risk-kpi.medium .risk-count { color: #ff9800; }
.risk-kpi.low .risk-count { color: #00c853; }

.trend-chart {
  grid-column: 1 / 7;
  grid-row: 3;
}

.customer-chart {
  grid-column: 7 / 13;
  grid-row: 3;
}

.alert-list {
  grid-column: 1 / 5;
  grid-row: 4;
}

.alert-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px;
  height: 100%;
}

.alert-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.alert-item {
  padding: 12px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.alert-item:last-child {
  border-bottom: none;
}

.alert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.alert-level {
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 4px;
  font-weight: 500;
}

.alert-level.high {
  background: rgba(255, 68, 68, 0.2);
  color: #ff4444;
}

.alert-level.medium {
  background: rgba(255, 152, 0, 0.2);
  color: #ff9800;
}

.alert-level.low {
  background: rgba(0, 200, 83, 0.2);
  color: #00c853;
}

.alert-time {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
}

.alert-content {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.payable-chart {
  grid-column: 5 / 9;
  grid-row: 4;
}
</style>
