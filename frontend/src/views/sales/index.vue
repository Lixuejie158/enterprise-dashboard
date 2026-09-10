<template>
  <div class="sales-container">
    <el-container class="sales-layout">
      <!-- Sidebar -->
      <el-aside width="200px">
        <div class="logo">销售分析</div>
        <el-menu
          :default-active="activeMenu"
          class="sales-menu"
          background-color="#1a2a4a"
          text-color="rgba(255,255,255,0.7)"
          active-text-color="#00d4ff"
          router
        >
          <el-menu-item index="/dashboard">
            <span>经营总览</span>
          </el-menu-item>
          <el-menu-item index="/finance">
            <span>财务报表</span>
          </el-menu-item>
          <el-menu-item index="/sales" disabled>
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
          <h2>销售数据分析</h2>
          <div class="header-actions">
            <el-select v-model="selectedRegion" placeholder="选择区域" clearable>
              <el-option label="全国" value="" />
              <el-option label="华东" value="east" />
              <el-option label="华南" value="south" />
              <el-option label="华北" value="north" />
              <el-option label="华中" value="central" />
              <el-option label="西南" value="southwest" />
            </el-select>
          </div>
        </div>

        <!-- KPI Cards -->
        <div class="kpi-row">
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-icon primary">
                <el-icon><Money /></el-icon>
              </div>
              <div class="kpi-info">
                <div class="kpi-label">总销售额</div>
                <div class="kpi-value">{{ totalSales }}</div>
                <div class="kpi-unit">万元</div>
              </div>
            </div>
          </el-card>
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-icon success">
                <el-icon><Document /></el-icon>
              </div>
              <div class="kpi-info">
                <div class="kpi-label">订单数</div>
                <div class="kpi-value">{{ orderCount }}</div>
                <div class="kpi-unit">笔</div>
              </div>
            </div>
          </el-card>
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-icon warning">
                <el-icon><User /></el-icon>
              </div>
              <div class="kpi-info">
                <div class="kpi-label">客户数</div>
                <div class="kpi-value">{{ customerCount }}</div>
                <div class="kpi-unit">家</div>
              </div>
            </div>
          </el-card>
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-icon info">
                <el-icon><Goods /></el-icon>
              </div>
              <div class="kpi-info">
                <div class="kpi-label">客单价</div>
                <div class="kpi-value">{{ avgOrderValue }}</div>
                <div class="kpi-unit">万元/单</div>
              </div>
            </div>
          </el-card>
        </div>

        <!-- Charts Row 1 -->
        <div class="charts-row">
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>区域销售分析</span>
              </div>
            </template>
            <ChartWrapper height="280px" :options="regionChartOptions" />
          </el-card>
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>渠道销售占比</span>
              </div>
            </template>
            <ChartWrapper height="280px" :options="channelChartOptions" />
          </el-card>
        </div>

        <!-- Charts Row 2 -->
        <div class="charts-row">
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>产品销量TOP10</span>
              </div>
            </template>
            <ChartWrapper height="280px" :options="productChartOptions" />
          </el-card>
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>月度销售同比对比</span>
              </div>
            </template>
            <ChartWrapper height="280px" :options="monthlyChartOptions" />
          </el-card>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import ChartWrapper from '../../components/ChartWrapper.vue'
import { Money, Document, User, Goods } from '@element-plus/icons-vue'

const activeMenu = ref('/sales')
const selectedRegion = ref('')

// KPI Data
const totalSales = ref('12,568.00')
const orderCount = ref('8,652')
const customerCount = ref('1,284')
const avgOrderValue = ref('1.45')

// Region Sales Bar Chart
const regionChartOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    formatter: (params) => params[0].name + '<br/>' +
      params[0].marker + '销售额: ' + params[0].value + '万'
  },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
  xAxis: {
    type: 'category',
    data: ['华东', '华南', '华北', '华中', '西南', '西北', '东北'],
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' }
  },
  yAxis: {
    type: 'value',
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266', formatter: (v) => v + '万' },
    splitLine: { lineStyle: { color: '#ebeef5' } }
  },
  series: [{
    type: 'bar',
    data: [
      { value: 4200, itemStyle: { color: '#409eff' } },
      { value: 3800, itemStyle: { color: '#67c23a' } },
      { value: 2900, itemStyle: { color: '#e6a23c' } },
      { value: 2500, itemStyle: { color: '#f56c6c' } },
      { value: 2100, itemStyle: { color: '#909399' } },
      { value: 1800, itemStyle: { color: '#c0c4cc' } },
      { value: 1200, itemStyle: { color: '#dcdfe6' } }
    ],
    barWidth: '50%',
    itemStyle: { borderRadius: [4, 4, 0, 0] }
  }]
}))

// Channel Sales Doughnut Chart
const channelChartOptions = computed(() => ({
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
      { value: 4500, name: '直销', itemStyle: { color: '#409eff' } },
      { value: 3200, name: '代理', itemStyle: { color: '#67c23a' } },
      { value: 2100, name: '电商', itemStyle: { color: '#e6a23c' } },
      { value: 1800, name: '渠道', itemStyle: { color: '#f56c6c' } },
      { value: 968, name: '其他', itemStyle: { color: '#909399' } }
    ]
  }]
}))

// Product Ranking Horizontal Bar Chart
const productChartOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    formatter: (params) => params[0].name + '<br/>销量: ' + params[0].value + '件'
  },
  grid: { left: '3%', right: '12%', bottom: '3%', top: '3%', containLabel: true },
  xAxis: {
    type: 'value',
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' },
    splitLine: { lineStyle: { color: '#ebeef5' } }
  },
  yAxis: {
    type: 'category',
    data: ['产品J', '产品I', '产品H', '产品G', '产品F', '产品E', '产品D', '产品C', '产品B', '产品A'],
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' }
  },
  series: [{
    type: 'bar',
    data: [680, 850, 980, 1150, 1280, 1420, 1580, 1750, 2100, 2850],
    itemStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 1, y2: 0,
        colorStops: [
          { offset: 0, color: '#409eff' },
          { offset: 1, color: '#67c23a' }
        ]
      },
      borderRadius: [0, 4, 4, 0]
    },
    barWidth: '60%'
  }]
}))

// Monthly Comparison Line Chart
const monthlyChartOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    formatter: (params) => {
      let result = params[0].name + '<br/>'
      params.forEach(p => {
        result += p.marker + p.seriesName + ': ' + p.value + '万<br/>'
      })
      return result
    }
  },
  legend: {
    data: ['2024年', '2023年'],
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
    axisLabel: { color: '#606266', formatter: (v) => v + '万' },
    splitLine: { lineStyle: { color: '#ebeef5' } }
  },
  series: [
    {
      name: '2024年',
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
      name: '2023年',
      type: 'line',
      smooth: true,
      data: [680, 650, 780, 850, 920, 980, 1050, 1100, 1080, 1150, 1220, 1350],
      lineStyle: { color: '#909399', width: 3, type: 'dashed' },
      itemStyle: { color: '#909399' }
    }
  ]
}))
</script>

<style scoped>
.sales-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.sales-layout {
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

.sales-menu {
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
  display: flex;
  align-items: center;
  gap: 15px;
}

.kpi-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.kpi-icon.primary { background: rgba(64, 158, 255, 0.1); color: #409eff; }
.kpi-icon.success { background: rgba(103, 194, 58, 0.1); color: #67c23a; }
.kpi-icon.warning { background: rgba(230, 162, 60, 0.1); color: #e6a23c; }
.kpi-icon.info { background: rgba(144, 147, 153, 0.1); color: #909399; }

.kpi-info {
  flex: 1;
}

.kpi-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 4px;
}

.kpi-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.kpi-unit {
  font-size: 12px;
  color: #c0c4cc;
  margin-top: 2px;
}

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
