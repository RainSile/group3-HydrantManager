<template>
  <div id="app">
    <AppHeader :username="username" @logout="handleLogout" />
    <div class="main-container">
      <Sidebar
          :search-query="searchQuery"
          :filter-status="filterStatus"
          :filter-area="filterArea"
          :filter-date="filterDate"
          :selected-hydrant="selectedHydrant"
          :hydrants="filteredHydrants"
          :stats="stats"
          @update:search-query="(value) => searchQuery = value"
          @update:filter-status="(value) => filterStatus = value"
          @update:filter-area="(value) => filterArea = value"
          @update:filter-date="(value) => filterDate = value"
          @hydrant-select="selectHydrant"
          @search="handleSearch"
      />
      <MapContainer
          :selected-hydrant="selectedHydrant"
          :current-hydrant="currentHydrant"
          @hydrant-select="selectHydrant"
      />
    </div>
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { hydrantsData } from './data/hydrants.js'
import AppHeader from './components/Header.vue'
import Sidebar from './components/Sidebar.vue'
import MapContainer from './components/MapContainer.vue'
import AppFooter from './components/Footer.vue'

// 响应式数据
const searchQuery = ref('')
const filterStatus = ref('')
const filterArea = ref('')
const filterDate = ref('')
const selectedHydrant = ref(null)
const hydrants = ref(hydrantsData)
const username = ref('管理员') // 简化登录状态

// 计算属性
const stats = computed(() => {
  return {
    normal: hydrants.value.filter(h => h.status === 'normal').length,
    warning: hydrants.value.filter(h => h.status === 'warning').length,
    error: hydrants.value.filter(h => h.status === 'error').length
  }
})

const filteredHydrants = computed(() => {
  let result = hydrants.value

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(h =>
        h.id.toLowerCase().includes(query) ||
        h.address.toLowerCase().includes(query)
    )
  }

  if (filterStatus.value) {
    result = result.filter(h => h.status === filterStatus.value)
  }

  if (filterArea.value) {
    result = result.filter(h => h.area === filterArea.value)
  }

  return result
})

const currentHydrant = computed(() => {
  return hydrants.value.find(h => h.id === selectedHydrant.value)
})

// 方法
const handleSearch = () => {
  if (searchQuery.value) {
    ElMessage.success(`搜索: ${searchQuery.value}`)
  }
}

const selectHydrant = (id) => {
  selectedHydrant.value = id
}

const handleLogout = () => {
  ElMessage.success('已退出登录')
}

// 生命周期
onMounted(() => {
  setTimeout(() => {
    ElMessage.success('消防栓数据加载完成')
  }, 500)
})
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", Arial, sans-serif;
}

body {
  background-color: #f5f7fa;
  color: #333;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-container {
  display: flex;
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  padding: 20px;
  gap: 20px;
}

@media (max-width: 768px) {
  .main-container {
    flex-direction: column;
  }
}
</style>