<template>
  <div class="search-card">
    <div class="card-title">
      <el-icon><Search /></el-icon>
      <span>消防栓查询</span>
    </div>
    <el-input
        :model-value="searchQuery"
        @update:model-value="$emit('update:search-query', $event)"
        placeholder="请输入地址或消防栓编号"
        clearable
    >
      <template #append>
        <el-button :icon="Search" @click="$emit('search')" />
      </template>
    </el-input>

    <HydrantList
        :hydrants="hydrants"
        @hydrant-select="$emit('hydrant-select', $event)"
    />
  </div>
</template>

<script>
import HydrantList from './HydrantList.vue'

export default {
  name: 'SearchCard',
  components: {
    HydrantList
  },
  props: {
    searchQuery: String,
    hydrants: Array
  },
  emits: ['update:search-query', 'hydrant-select', 'search']
}
</script>

<style scoped>
.search-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #ff4d4f;
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>