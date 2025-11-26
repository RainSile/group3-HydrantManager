<template>
  <div class="hydrant-management">
    <div class="main-content">
      <Sidebar
          :hydrants="hydrants"
          :selected-hydrant="selectedHydrant"
          @hydrant-select="handleHydrantSelect"
          @refresh="loadHydrants"
      />

      <div class="content-area">
        <MapContainer
            :selected-hydrant="selectedHydrant"
            :hydrants="hydrants"
            @hydrant-select="handleHydrantSelect"
            ref="mapContainerRef"
        >
          <template #hydrant-detail>
            <HydrantDetail
                v-if="selectedHydrantDetail"
                :hydrant="selectedHydrantDetail"
                class="map-detail-card"
                @close="handleCloseDetail"
                @refresh="loadHydrants"
                @delete-success="handleDeleteSuccess"
                @update-success="loadHydrants"
            />
          </template>
        </MapContainer>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, nextTick, provide } from 'vue'
import { ElMessage } from 'element-plus'
import { hydrantApi } from '@/api/hydrantApi.js'
import Sidebar from './Sidebar.vue'
import MapContainer from './MapContainer.vue'
import HydrantDetail from './HydrantDetail.vue'

const selectedHydrant = ref('')
const hydrants = ref([])
const mapContainerRef = ref()

// 提供地图容器引用给子组件
provide('mapContainerRef', mapContainerRef)

// 计算选中的消防栓详情
const selectedHydrantDetail = computed(() => {
  return hydrants.value.find(h => h.id === selectedHydrant.value) || null
})

// 处理消防栓选择
const handleHydrantSelect = (hydrantId) => {
  selectedHydrant.value = hydrantId
}

// 关闭详情卡片
const handleCloseDetail = () => {
  selectedHydrant.value = ''
}

// 处理删除成功
const handleDeleteSuccess = (deletedHydrantId) => {
  if (selectedHydrant.value === deletedHydrantId) {
    selectedHydrant.value = ''
  }
  loadHydrants()
  ElMessage.success('消防栓已删除')
}

// 加载消防栓数据
const loadHydrants = async () => {
  try {
    const response = await hydrantApi.listHydrants({
      pageIndex: 1,
      pageSize: 50
    })

    if (response.code === 10000 && response.data) {
      hydrants.value = response.data.records.map(item => ({
        id: item.id,
        location: item.location,
        status: item.status?.toString(),
        pressure: item.pressure,
        flowRate: item.flowRate,
        updateTime: item.updateTime,
        longitude: item.longitude,
        latitude: item.latitude,
        data: item.data
      }))

      // 刷新地图标记
      nextTick(() => {
        if (mapContainerRef.value && mapContainerRef.value.refreshMarkers) {
          mapContainerRef.value.refreshMarkers()
        }
      })
    } else {
      ElMessage.warning('获取数据失败')
    }
  } catch (error) {
    console.error('加载消防栓数据失败:', error)
    ElMessage.error('加载数据失败')
  }
}

// 组件挂载时加载数据
onMounted(() => {
  loadHydrants()
})

// 暴露方法给父组件
defineExpose({
  loadHydrants
})
</script>

<style scoped>
.hydrant-management {
  flex: 1;
}

.main-content {
  display: flex;
  gap: 20px;
  padding: 20px;
  background: #f5f5f5;
  height: calc(100vh - 120px);
}

.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.map-detail-card {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 1000;
  width: 320px;
  max-height: 500px;
  overflow-y: auto;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
    padding: 10px;
    height: auto;
  }

  .content-area {
    gap: 10px;
  }

  .map-detail-card {
    width: calc(100% - 20px);
    max-height: 400px;
    top: 10px;
    right: 10px;
  }
}
</style>