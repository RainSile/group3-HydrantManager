<template>
  <aside class="sidebar">
    <SearchCard
        :hydrants="hydrants"
        @hydrant-select="emit('hydrant-select', $event)"
    />

    <HydrantList
        :hydrants="hydrants"
        :selected-id="selectedHydrant"
        @select="emit('hydrant-select', $event)"
    />

    <AddHydrant
        v-model="showAddDialogVisible"
        @success="handleAddSuccess"
    />

    <StatsCards :stats="stats" />

    <div class="add-section">
      <el-button
          type="primary"
          class="add-button"
          @click="showAddDialog"
      >
        <el-icon><Plus /></el-icon>
        添加消防栓
      </el-button>
    </div>
  </aside>
</template>

<script setup>
import { defineProps, defineEmits, computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import SearchCard from './SearchCard.vue'
import StatsCards from './StatsCards.vue'
import HydrantList from './HydrantList.vue'
import AddHydrant from './AddHydrant.vue'

const props = defineProps({
  hydrants: Array,
  selectedHydrant: String
})

const emit = defineEmits(['hydrant-select', 'refresh'])

const showAddDialogVisible = ref(false)

const stats = computed(() => {
  const normal = props.hydrants.filter(h => h.status === '0').length
  const warning = props.hydrants.filter(h => h.status === '1').length
  const error = props.hydrants.filter(h => h.status === '2').length

  return {
    normal,
    warning,
    error,
    total: props.hydrants.length
  }
})

const showAddDialog = () => {
  showAddDialogVisible.value = true
}

const handleAddSuccess = () => {
  ElMessage.success('消防栓添加成功')
  emit('refresh')
  showAddDialogVisible.value = false
}
</script>

<style scoped>
.sidebar {
  width: 350px;
  display: flex;
  flex-direction: column;
  gap: 16px;
  background: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.add-section {
  display: flex;
  justify-content: center;
}

.add-button {
  width: 100%;
  height: 40px;
  font-size: 14px;
}

:deep(.add-button .el-icon) {
  margin-right: 8px;
}

@media (max-width: 768px) {
  .sidebar {
    width: 100%;
  }
}
</style>