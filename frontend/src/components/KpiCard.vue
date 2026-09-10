<template>
  <div class="kpi-card">
    <div class="kpi-label">{{ label }}</div>
    <div class="kpi-value" :style="{ color: valueColor }">{{ formattedValue }}</div>
    <div v-if="trend !== undefined" class="kpi-trend" :class="trendClass">
      {{ trend >= 0 ? '↑' : '↓' }} {{ Math.abs(trend).toFixed(1) }}%
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  label: String,
  value: [Number, String],
  format: { type: String, default: 'number' },
  trend: Number,
  color: { type: String, default: '#00d4ff' }
})

const formattedValue = computed(() => {
  if (props.value === null || props.value === undefined) return '-'
  if (props.format === 'currency') {
    return (props.value / 10000).toFixed(2) + '万'
  }
  if (props.format === 'percent') {
    return props.value.toFixed(1) + '%'
  }
  return props.value.toLocaleString()
})

const valueColor = computed(() => props.color)

const trendClass = computed(() => {
  if (props.trend > 0) return 'up'
  if (props.trend < 0) return 'down'
  return ''
})
</script>

<style scoped>
.kpi-card {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 20px;
  text-align: center;
}

.kpi-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
  margin-bottom: 8px;
}

.kpi-value {
  font-size: 28px;
  font-weight: 600;
}

.kpi-trend {
  font-size: 12px;
  margin-top: 8px;
}

.kpi-trend.up { color: #ff4444; }
.kpi-trend.down { color: #00c853; }
</style>
