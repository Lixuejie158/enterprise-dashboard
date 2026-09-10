<template>
  <div class="risk-container">
    <el-container class="risk-layout">
      <!-- Sidebar -->
      <el-aside width="200px">
        <div class="logo">风险预警</div>
        <el-menu
          :default-active="activeMenu"
          class="risk-menu"
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
          <el-menu-item index="/sales">
            <span>销售分析</span>
          </el-menu-item>
          <el-menu-item index="/risk" disabled>
            <span>风险预警</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-main>
        <!-- Header -->
        <div class="page-header">
          <h2>风险预警中心</h2>
          <div class="header-actions">
            <el-button type="primary" @click="refreshData">
              <el-icon><Refresh /></el-icon>
              刷新数据
            </el-button>
          </div>
        </div>

        <!-- KPI Cards -->
        <div class="kpi-row">
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-icon total">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="kpi-info">
                <div class="kpi-label">预警总数</div>
                <div class="kpi-value">{{ riskStats.total }}</div>
              </div>
            </div>
          </el-card>
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-icon high">
                <el-icon><CircleClose /></el-icon>
              </div>
              <div class="kpi-info">
                <div class="kpi-label">高危预警</div>
                <div class="kpi-value danger">{{ riskStats.high }}</div>
              </div>
            </div>
          </el-card>
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-icon medium">
                <el-icon><InfoFilled /></el-icon>
              </div>
              <div class="kpi-info">
                <div class="kpi-label">中危预警</div>
                <div class="kpi-value warning">{{ riskStats.medium }}</div>
              </div>
            </div>
          </el-card>
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-content">
              <div class="kpi-icon low">
                <el-icon><SuccessFilled /></el-icon>
              </div>
              <div class="kpi-info">
                <div class="kpi-label">低危预警</div>
                <div class="kpi-value success">{{ riskStats.low }}</div>
              </div>
            </div>
          </el-card>
        </div>

        <!-- Charts Row -->
        <div class="charts-row">
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>预警类型分布</span>
              </div>
            </template>
            <ChartWrapper height="280px" :options="typeChartOptions" />
          </el-card>
          <el-card class="chart-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span>预警等级分布</span>
              </div>
            </template>
            <ChartWrapper height="280px" :options="levelChartOptions" />
          </el-card>
        </div>

        <!-- Alert List Table -->
        <el-card class="table-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span>预警列表</span>
              <div class="table-filters">
                <el-select v-model="filterLevel" placeholder="预警等级" clearable style="width: 120px">
                  <el-option label="全部" value="" />
                  <el-option label="高危" value="high" />
                  <el-option label="中危" value="medium" />
                  <el-option label="低危" value="low" />
                </el-select>
                <el-select v-model="filterStatus" placeholder="处理状态" clearable style="width: 120px; margin-left: 10px">
                  <el-option label="全部" value="" />
                  <el-option label="待处理" value="pending" />
                  <el-option label="处理中" value="processing" />
                  <el-option label="已处理" value="resolved" />
                </el-select>
              </div>
            </div>
          </template>
          <el-table :data="filteredAlertList" stripe style="width: 100%">
            <el-table-column prop="id" label="序号" width="80" />
            <el-table-column prop="type" label="预警类型" width="150" />
            <el-table-column prop="content" label="预警内容" min-width="200" />
            <el-table-column prop="level" label="等级" width="100">
              <template #default="{ row }">
                <el-tag :type="getLevelTagType(row.level)" effect="dark">
                  {{ getLevelText(row.level) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="source" label="来源" width="150" />
            <el-table-column prop="createTime" label="发生时间" width="180" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.status)" effect="plain">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" link size="small" @click="handleView(row)">查看</el-button>
                <el-button
                  v-if="row.status === 'pending'"
                  type="success"
                  link
                  size="small"
                  @click="handleProcess(row)"
                >处理</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-wrapper">
            <el-pagination
              v-model:current-page="currentPage"
              v-model:page-size="pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="totalCount"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import ChartWrapper from '../../components/ChartWrapper.vue'
import { Refresh, Warning, CircleClose, InfoFilled, SuccessFilled } from '@element-plus/icons-vue'

const activeMenu = ref('/risk')
const filterLevel = ref('')
const filterStatus = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalCount = ref(50)

// Risk Stats
const riskStats = reactive({
  total: 40,
  high: 3,
  medium: 12,
  low: 25
})

// Alert List Data
const alertList = ref([
  { id: 1, type: '财务风险', content: '供应商账期超90天未结算', level: 'high', source: '应付账款模块', createTime: '2024-01-15 10:23:45', status: 'pending' },
  { id: 2, type: '信用风险', content: '客户信用额度超标', level: 'high', source: '客户信用管理', createTime: '2024-01-15 09:45:12', status: 'pending' },
  { id: 3, type: '运营风险', content: '库存周转率低于行业均值', level: 'medium', source: '库存管理系统', createTime: '2024-01-15 09:12:33', status: 'processing' },
  { id: 4, type: '财务风险', content: '应收账款逾期30天', level: 'medium', source: '应收账款模块', createTime: '2024-01-15 08:56:21', status: 'pending' },
  { id: 5, type: '合规风险', content: '合同即将到期预警', level: 'low', source: '合同管理系统', createTime: '2024-01-15 08:30:00', status: 'pending' },
  { id: 6, type: '运营风险', content: '客户投诉处理超时', level: 'low', source: '客服系统', createTime: '2024-01-15 08:15:45', status: 'resolved' },
  { id: 7, type: '财务风险', content: '银行对账差异', level: 'low', source: '财务系统', createTime: '2024-01-15 07:52:18', status: 'resolved' },
  { id: 8, type: '市场风险', content: '原材料价格波动超限', level: 'medium', source: '采购系统', createTime: '2024-01-14 16:30:00', status: 'processing' },
  { id: 9, type: '运营风险', content: '物流时效不达标', level: 'low', source: '物流跟踪系统', createTime: '2024-01-14 15:20:33', status: 'resolved' },
  { id: 10, type: '信用风险', content: '客户逾期还款预警', level: 'medium', source: '客户信用管理', createTime: '2024-01-14 14:10:55', status: 'pending' }
])

const filteredAlertList = computed(() => {
  return alertList.value.filter(item => {
    if (filterLevel.value && item.level !== filterLevel.value) return false
    if (filterStatus.value && item.status !== filterStatus.value) return false
    return true
  })
})

// Type Distribution Pie Chart
const typeChartOptions = computed(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c}个 ({d}%)'
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
      { value: 15, name: '财务风险', itemStyle: { color: '#f56c6c' } },
      { value: 10, name: '信用风险', itemStyle: { color: '#e6a23c' } },
      { value: 8, name: '运营风险', itemStyle: { color: '#409eff' } },
      { value: 5, name: '合规风险', itemStyle: { color: '#909399' } },
      { value: 2, name: '市场风险', itemStyle: { color: '#67c23a' } }
    ]
  }]
}))

// Level Distribution Bar Chart
const levelChartOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    formatter: (params) => params[0].name + '<br/>数量: ' + params[0].value + '个'
  },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '10%', containLabel: true },
  xAxis: {
    type: 'category',
    data: ['高危', '中危', '低危'],
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' }
  },
  yAxis: {
    type: 'value',
    axisLine: { lineStyle: { color: '#dcdfe6' } },
    axisLabel: { color: '#606266' },
    splitLine: { lineStyle: { color: '#ebeef5' } }
  },
  series: [{
    type: 'bar',
    data: [
      { value: 3, itemStyle: { color: '#f56c6c' } },
      { value: 12, itemStyle: { color: '#e6a23c' } },
      { value: 25, itemStyle: { color: '#67c23a' } }
    ],
    barWidth: '50%',
    itemStyle: { borderRadius: [4, 4, 0, 0] }
  }]
}))

// Helper functions
const getLevelTagType = (level) => {
  const types = { high: 'danger', medium: 'warning', low: 'success' }
  return types[level] || 'info'
}

const getLevelText = (level) => {
  const texts = { high: '高危', medium: '中危', low: '低危' }
  return texts[level] || level
}

const getStatusTagType = (status) => {
  const types = { pending: 'info', processing: 'warning', resolved: 'success' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { pending: '待处理', processing: '处理中', resolved: '已处理' }
  return texts[status] || status
}

const handleView = (row) => {
  console.log('View alert:', row)
}

const handleProcess = (row) => {
  row.status = 'processing'
}

const handleSizeChange = (val) => {
  pageSize.value = val
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

const refreshData = () => {
  console.log('Refreshing data...')
}
</script>

<style scoped>
.risk-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.risk-layout {
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

.risk-menu {
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

.kpi-icon.total { background: rgba(64, 158, 255, 0.1); color: #409eff; }
.kpi-icon.high { background: rgba(245, 108, 108, 0.1); color: #f56c6c; }
.kpi-icon.medium { background: rgba(230, 162, 60, 0.1); color: #e6a23c; }
.kpi-icon.low { background: rgba(103, 194, 58, 0.1); color: #67c23a; }

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

.kpi-value.danger { color: #f56c6c; }
.kpi-value.warning { color: #e6a23c; }
.kpi-value.success { color: #67c23a; }

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
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.table-card {
  background: #fff;
  border-radius: 8px;
}

.table-filters {
  display: flex;
  align-items: center;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
