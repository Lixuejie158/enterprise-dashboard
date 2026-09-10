<template>
  <div class="scroll-list" :style="{ maxHeight: maxHeight }">
    <div class="scroll-content" ref="contentRef">
      <div v-for="(item, index) in list" :key="index" class="scroll-item">
        <slot :item="item" :index="index"></slot>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  list: Array,
  maxHeight: { type: String, default: '300px' },
  speed: { type: Number, default: 50 }
})

const contentRef = ref(null)
let scrollTimer = null
let isPaused = false

const startScroll = () => {
  if (!contentRef.value || props.list.length <= 5) return

  scrollTimer = setInterval(() => {
    if (!isPaused && contentRef.value) {
      const scrollTop = contentRef.value.scrollTop
      const itemHeight = contentRef.value.firstChild?.offsetHeight || 50
      const visibleItems = Math.floor(parseInt(props.maxHeight) / itemHeight)

      if (scrollTop >= (props.list.length - visibleItems) * itemHeight) {
        contentRef.value.scrollTop = 0
      } else {
        contentRef.value.scrollTop = scrollTop + 1
      }
    }
  }, props.speed)
}

onMounted(() => {
  startScroll()
})

onUnmounted(() => {
  if (scrollTimer) clearInterval(scrollTimer)
})
</script>

<style scoped>
.scroll-list {
  overflow-y: auto;
  scrollbar-width: none;
}

.scroll-list::-webkit-scrollbar {
  display: none;
}

.scroll-content {
  scroll-behavior: auto;
}

.scroll-item {
  padding: 12px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.scroll-item:last-child {
  border-bottom: none;
}
</style>
