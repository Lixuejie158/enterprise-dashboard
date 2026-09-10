<template>
  <div class="chart-wrapper">
    <div v-if="title" class="chart-title">{{ title }}</div>
    <div ref="chartRef" class="chart-content"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  title: String,
  options: Object,
  height: { type: String, default: '300px' }
})

const chartRef = ref(null)
let chart = null

const initChart = () => {
  if (!chartRef.value) return
  chart = echarts.init(chartRef.value)
  if (props.options) {
    chart.setOption(props.options)
  }
}

const updateChart = () => {
  if (chart && props.options) {
    chart.setOption(props.options)
  }
}

const resizeChart = () => {
  chart?.resize()
}

onMounted(() => {
  initChart()
  window.addEventListener('resize', resizeChart)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeChart)
  chart?.dispose()
})

watch(() => props.options, updateChart, { deep: true })
</script>

<style scoped>
.chart-wrapper {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px;
  height: 100%;
}

.chart-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.chart-content {
  width: 100%;
  height: v-bind(height);
}
</style>
