<template>
  <div class="search-card">
    <h3>消防栓搜索</h3>
    <el-input
        v-model="searchText"
        placeholder="搜索消防栓ID或位置..."
        clearable
    >
      <template #prefix>
        <el-icon><Search /></el-icon>
      </template>
    </el-input>

    <div v-if="filteredHydrants.length > 0" class="search-results">
      <div
          v-for="hydrant in filteredHydrants"
          :key="hydrant.id"
          class="search-result-item"
          @click="handleHydrantSelect(hydrant.id)"
      >
        <div class="hydrant-id">#{{ hydrant.id }}</div>
        <div class="hydrant-location">{{ hydrant.location }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Search } from '@element-plus/icons-vue'

const props = defineProps({
  hydrants: Array
})

const emit = defineEmits(['hydrant-select'])

const searchText = ref('')

const filteredHydrants = computed(() => {
  if (!searchText.value) return []

  return props.hydrants.filter(hydrant =>
      hydrant.id.includes(searchText.value) ||
      hydrant.location.includes(searchText.value)
  )
})

const handleHydrantSelect = (hydrantId) => {
  emit('hydrant-select', hydrantId)
  searchText.value = '' // 清空搜索框
}
</script>

<style scoped>
.search-card {
  background: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.search-card h3 {
  margin: 0 0 12px 0;
  color: #333;
}

.search-results {
  margin-top: 12px;
  max-height: 200px;
  overflow-y: auto;
}

.search-result-item {
  padding: 8px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.search-result-item:hover {
  background-color: #f5f5f5;
}

.hydrant-id {
  font-weight: bold;
  color: #ff4d4f;
  font-size: 14px;
}

.hydrant-location {
  color: #666;
  font-size: 12px;
  margin-top: 4px;
}
</style>