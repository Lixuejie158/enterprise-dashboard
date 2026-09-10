<template>
  <div class="finance-container">
    <!-- Sidebar Navigation -->
    <el-container class="finance-layout">
      <el-aside width="200px">
        <div class="logo">财务管理</div>
        <el-menu
          :default-active="activeMenu"
          class="finance-menu"
          background-color="#1a2a4a"
          text-color="rgba(255,255,255,0.7)"
          active-text-color="#00d4ff"
          router
        >
          <el-menu-item index="/dashboard">
            <span>经营总览</span>
          </el-menu-item>
          <el-menu-item index="/finance" disabled>
            <span>财务报表</span>
          </el-menu-item>
          <el-menu-item index="/sales">
            <span>销售分析</span>
          </el-menu-item>
          <el-menu-item index="/risk">
            <span>风险预警</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-main>
        <!-- Header -->
        <div class="page-header">
          <h2>财务报表分析</h2>
          <el-date-picker
            v-model="selectedYear"
            type="year"
            placeholder="选择年份"
            format="YYYY年"
            value-format="YYYY"
            @change="handleYearChange"
          />
        </div>

        <!-- KPI Cards -->
        <div class="kpi-row">
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-label">本月营收</div>
              <div class="kpi-value primary">{{ currentMonthRevenue }}</div>
              <div class="kpi-unit">万元</div>
            </div>
          </el-card>
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-label">本月净利润</div>
              <div class="kpi-value success">{{ currentMonthProfit }}</div>
              <div class="kpi-unit">万元</div>
            </div>
          </el-card>
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-label">毛利率</div>
              <div class="kpi-value warning">{{ grossMargin }}%</div>
              <div class="kpi-trend up">
                <span>较上月</span>
                <span>+1.2%</span>
              </div>
            </div>
          </el-card>
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-label">现金流</div>
              <div class="kpi-value info">{{ cashFlow }}</div>
              <div class="kpi-unit">万元</div>
            </div>
          </el-card>
        </div>

        <!-- Charts Row 1 -->
        <div class="charts-row">
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>营收/成本/利润月度趋势</span>
              </div>
            </template>
            <ChartWrapper height="300px" :options="trendChartOptions" />
          </el-card>
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>资产负债结构</span>
              </div>
            </template>
            <ChartWrapper height="300px" :options="balanceChartOptions" />
          </el-card>
        </div>

        <!-- Charts Row 2 -->
        <div class="charts-row">
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>成本构成分析</span>
              </div>
            </template>
            <ChartWrapper height="300px" :options="costChartOptions" />
          </el-card>
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>应收账款 vs 应付账款</span>
              </div>
            </template>
            <ChartWrapper height="300px" :options="receivableChartOptions" />
          </el-card>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import ChartWrapper from '../../components/ChartWrapper.vue'

const activeMenu = ref('/finance')
const selectedYear = ref('2024')

// KPI Data
const currentMonthRevenue = ref('1,468.00')
const currentMonthProfit = ref('268.50')
const grossMargin = ref('18.6')
const cashFlow = ref('856.30')

const handleYearChange = (val) => {
  console.log('Year changed:', val)
}

// Trend Chart - Revenue/Cost/Profit Monthly
const trendChartOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    formatter: (params) => {
      let result = params[0].name + '<br/>'
      params.forEach(p => {
        result += p.marker + p.seriesName + ': ' + p.value.toLocaleString() + '万<br/>'
      })
      return result
    }
  },
  legend: {
    data: ['营收', '成本', '利润'],
    top: '5%'
  },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
  xAxis: {
    type: 'category',
    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' }
  },
  yAxis: {
    type: 'value',
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' },
    splitLine: { lineStyle: { color: '#ebeef5' } }
  },
  series: [
    {
      name: '营收',
      type: 'line',
      smooth: true,
      data: [820, 780, 910, 980, 1050, 1120, 1080, 1200, 1150, 1250, 1320, 1468],
      lineStyle: { color: '#409eff', width: 3 },
      itemStyle: { color: '#409eff' },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(64,158,255,0.3)' },
            { offset: 1, color: 'rgba(64,158,255,0)' }
          ]
        }
      }
    },
    {
      name: '成本',
      type: 'line',
      smooth: true,
      data: [580, 540, 620, 680, 720, 760, 740, 820, 780, 850, 900, 980],
      lineStyle: { color: '#f56c6c', width: 3 },
      itemStyle: { color: '#f56c6c' }
    },
    {
      name: '利润',
      type: 'line',
      smooth: true,
      data: [240, 240, 290, 300, 330, 360, 340, 380, 370, 400, 420, 488],
      lineStyle: { color: '#67c23a', width: 3 },
      itemStyle: { color: '#67c23a' }
    }
  ]
}))

// Balance Structure Pie Chart
const balanceChartOptions = computed(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c}万 ({d}%)'
  },
  legend: {
    orient: 'vertical',
    right: '5%',
    top: 'center',
    textStyle: { color: '#606266' }
  },
  series: [{
    type: 'pie',
    radius: ['45%', '70%'],
    center: ['35%', '50%'],
    avoidLabelOverlap: false,
    itemStyle: {
      borderRadius: 8,
      borderColor: '#fff',
      borderWidth: 2
    },
    label: { show: false },
    emphasis: {
      label: { show: true, fontSize: 14, fontWeight: 'bold' }
    },
    data: [
      { value: 4200, name: '流动资产', itemStyle: { color: '#409eff' } },
      { value: 2800, name: '固定资产', itemStyle: { color: '#67c23a' } },
      { value: 1500, name: '无形资产', itemStyle: { color: '#e6a23c' } },
      { value: 800, name: '其他资产', itemStyle: { color: '#909399' } }
    ]
  }]
}))

// Cost Analysis Stacked Bar Chart
const costChartOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    formatter: (params) => {
      let result = params[0].name + '<br/>'
      let total = 0
      params.forEach(p => {
        total += p.value
        result += p.marker + p.seriesName + ': ' + p.value + '万<br/>'
      })
      result += '<strong>总计: ' + total + '万</strong>'
      return result
    }
  },
  legend: {
    data: ['原材料', '人工成本', '制造费用', '管理费用'],
    top: '5%'
  },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
  xAxis: {
    type: 'category',
    data: ['1月', '2月', '3月', '4月', '5月', '6月'],
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' }
  },
  yAxis: {
    type: 'value',
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' },
    splitLine: { lineStyle: { color: '#ebeef5' } }
  },
  series: [
    {
      name: '原材料',
      type: 'bar',
      stack: 'total',
      data: [280, 260, 300, 320, 340, 360],
      itemStyle: { color: '#409eff' }
    },
    {
      name: '人工成本',
      type: 'bar',
      stack: 'total',
      data: [150, 140, 160, 170, 180, 190],
      itemStyle: { color: '#67c23a' }
    },
    {
      name: '制造费用',
      type: 'bar',
      stack: 'total',
      data: [80, 75, 85, 90, 95, 100],
      itemStyle: { color: '#e6a23c' }
    },
    {
      name: '管理费用',
      type: 'bar',
      stack: 'total',
      data: [70, 65, 75, 80, 85, 90],
      itemStyle: { color: '#909399' }
    }
  ]
}))

// Receivable vs Payable Bar Chart
const receivableChartOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    formatter: (params) => {
      let result = params[0].name + '<br/>'
      params.forEach(p => {
        result += p.marker + p.seriesName + ': ' + p.value.toLocaleString() + '万<br/>'
      })
      return result
    }
  },
  legend: {
    data: ['应收账款', '应付账款'],
    top: '5%'
  },
  grid: { left: '3%', right: '4%', bottom: '10%', top: '15%', containLabel: true },
  xAxis: {
    type: 'category',
    data: ['1月', '2月', '3月', '4月', '5月', '6月'],
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' }
  },
  yAxis: {
    type: 'value',
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' },
    splitLine: { lineStyle: { color: '#ebeef5' } }
  },
  series: [
    {
      name: '应收账款',
      type: 'bar',
      data: [450, 480, 520, 490, 530, 580],
      itemStyle: { color: '#409eff', borderRadius: [4, 4, 0, 0] }
    },
    {
      name: '应付账款',
      type: 'bar',
      data: [320, 350, 380, 360, 400, 420],
      itemStyle: { color: '#e6a23c', borderRadius: [4, 4, 0, 0] }
    }
  ]
}))
</script>

<style scoped>
.finance-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.finance-layout {
  min-height: 100vh;
}

.el-aside {
  background-color: #1a2a4a;
  min-height: 100vh;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 18px;
  font-weight: 600;
  color: #00d4ff;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.finance-menu {
  border-right: none;
}

.el-main {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.kpi-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.kpi-card {
  background: #fff;
  border-radius: 8px;
}

.kpi-card :deep(.el-card__body) {
  padding: 20px;
}

.kpi-content {
  text-align: center;
}

.kpi-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.kpi-value {
  font-size: 28px;
  font-weight: 600;
}

.kpi-value.primary { color: #409eff; }
.kpi-value.success { color: #67c23a; }
.kpi-value.warning { color: #e6a23c; }
.kpi-value.info { color: #909399; }

.kpi-unit {
  font-size: 12px;
  color: #c0c4cc;
  margin-top: 4px;
}

.kpi-trend {
  font-size: 12px;
  margin-top: 8px;
  display: flex;
  justify-content: center;
  gap: 8px;
}

.kpi-trend.up { color: #67c23a; }
.kpi-trend.down { color: #f56c6c; }

.charts-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.chart-card {
  background: #fff;
  border-radius: 8px;
}

.chart-card :deep(.el-card__header) {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
}

.card-header {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}
</style>
