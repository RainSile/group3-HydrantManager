<template>
  <div class="search-card">
    <h3>消防栓搜索</h3>

    <!-- 搜索消防栓 -->
    <el-input
        v-model="searchText"
        placeholder="搜索消防栓ID或位置..."
        clearable
        size="small"
    >
      <template #prefix>
        <el-icon><Search /></el-icon>
      </template>
    </el-input>

    <!-- 地理搜索区域 -->
    <div class="geo-search-section">
      <div class="geo-header">
        <el-tag v-if="currentPosition" type="success" size="small">
          已定位
        </el-tag>
        <el-tag v-else type="info" size="small">
          未定位
        </el-tag>
      </div>

      <!-- 三个功能按钮 -->
      <div class="geo-buttons">
        <el-button
            type="primary"
            :loading="locationLoading"
            @click="getCurrentLocation"
            class="location-btn"
            size="small"
        >
          <el-icon><LocationFilled /></el-icon>
          获取位置
        </el-button>

        <el-button
            :disabled="!currentPosition"
            :loading="geoLoading"
            @click="findNearestHydrant"
            class="nearest-btn"
            size="small"
        >
          <el-icon><Location /></el-icon>
          最近
        </el-button>

        <el-button
            :disabled="!currentPosition"
            :loading="radiusLoading"
            @click="showRadiusDialog = true"
            class="radius-btn"
            size="small"
        >
          <el-icon><Aim /></el-icon>
          半径
        </el-button>
      </div>

      <!-- 位置信息显示 -->
      <div v-if="currentPosition" class="position-info">
        <div class="position-item">
          <span class="label">经度：</span>
          <span class="value">{{ currentPosition.longitude.toFixed(6) }}</span>
        </div>
        <div class="position-item">
          <span class="label">纬度：</span>
          <span class="value">{{ currentPosition.latitude.toFixed(6) }}</span>
        </div>
        <el-button
            type="text"
            size="small"
            @click="clearPosition"
            class="clear-position-btn"
        >
          清除
        </el-button>
      </div>

      <!-- 最近消防栓结果 -->
      <div v-if="nearestHydrant" class="geo-result">
        <div class="result-header">
          <span>最近消防栓</span>
          <el-icon @click="clearNearestResult" class="clear-icon"><Close /></el-icon>
        </div>
        <div class="result-info">
          <div class="result-item">
            <span class="label">位置：</span>
            <span class="value">{{ nearestHydrant.location || '未知位置' }}</span>
          </div>
          <div class="result-item">
            <span class="label">消防栓ID：</span>
            <span class="value">{{ nearestHydrant.id }}</span>
          </div>
          <div class="result-item">
            <span class="label">直线距离：</span>
            <span class="value distance">{{ nearestHydrant.distance }} 千米</span>
          </div>
          <div class="result-item">
            <span class="label">状态：</span>
            <el-tag :type="getStatusTagType(nearestHydrant.status)" size="small">
              {{ getStatusText(nearestHydrant.status) }}
            </el-tag>
          </div>
          <el-button
              type="text"
              size="small"
              @click="selectNearestHydrant"
              class="select-btn"
          >
            查看详情
          </el-button>
        </div>
      </div>

      <!-- 半径搜索结果 -->
      <div v-if="radiusHydrants.length > 0" class="geo-result">
        <div class="result-header">
          <span>半径内消防栓 ({{ radiusHydrants.length }}个)</span>
          <el-icon @click="clearRadiusResult" class="clear-icon"><Close /></el-icon>
        </div>
        <div class="radius-results">
          <div
              v-for="hydrant in radiusHydrants"
              :key="hydrant.id"
              class="radius-item"
              @click="selectRadiusHydrant(hydrant.id)"
          >
            <div class="radius-location">{{ hydrant.location }}</div>
            <div class="radius-distance">{{ hydrant.distance }} 千米</div>
            <el-tag :type="getStatusTagType(hydrant.status)" size="small">
              {{ getStatusText(hydrant.status) }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>

    <!-- 搜索结果显示 -->
    <div v-if="filteredHydrants.length > 0" class="search-results">
      <div class="results-header">搜索结果 ({{ filteredHydrants.length }})</div>
      <div
          v-for="hydrant in filteredHydrants"
          :key="hydrant.id"
          class="search-result-item"
          @click="handleHydrantSelect(hydrant.id)"
      >
        <div class="hydrant-id">#{{ hydrant.id }}</div>
        <div class="hydrant-location">{{ hydrant.location }}</div>
        <el-tag :type="getStatusTagType(hydrant.status)" size="small">
          {{ getStatusText(hydrant.status) }}
        </el-tag>
      </div>
    </div>

    <!-- 半径搜索对话框 -->
    <el-dialog
        v-model="showRadiusDialog"
        title="半径搜索"
        width="360px"
        :close-on-click-modal="false"
    >
      <div class="radius-dialog">
        <div class="dialog-item">
          <span class="label">搜索半径：</span>
          <el-input-number
              v-model="searchRadius"
              :min="0.1"
              :max="50"
              :step="0.1"
              :precision="1"
              controls-position="right"
              size="small"
          >
            <template #append>千米</template>
          </el-input-number>
        </div>
        <div class="current-position" v-if="currentPosition">
          <div class="position-coord">
            当前位置：{{ currentPosition.longitude.toFixed(6) }}, {{ currentPosition.latitude.toFixed(6) }}
          </div>
        </div>
        <div class="dialog-tip">
          将在您当前位置 {{ searchRadius }} 千米范围内搜索消防栓
        </div>
      </div>

      <template #footer>
        <el-button @click="showRadiusDialog = false" size="small">取消</el-button>
        <el-button
            type="primary"
            :loading="radiusLoading"
            @click="findHydrantsInRadius"
            :disabled="!currentPosition"
            size="small"
        >
          开始搜索
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, inject } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Location, Aim, Close, LocationFilled } from '@element-plus/icons-vue'
import { geoApi } from '@/api/hydrantApi.js'

const props = defineProps({
  hydrants: Array
})

const emit = defineEmits(['hydrant-select'])

// 注入地图容器引用
const mapContainerRef = inject('mapContainerRef')

const searchText = ref('')
const locationLoading = ref(false)
const geoLoading = ref(false)
const radiusLoading = ref(false)
const currentPosition = ref(null)
const nearestHydrant = ref(null)
const radiusHydrants = ref([])
const showRadiusDialog = ref(false)
const searchRadius = ref(1) // 默认1公里，与API单位保持一致

// 过滤消防栓数据
const filteredHydrants = computed(() => {
  if (!searchText.value) return []

  return props.hydrants.filter(hydrant =>
      hydrant.id.includes(searchText.value) ||
      hydrant.location.includes(searchText.value)
  )
})

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = { 0: '正常', 1: '维护中', 2: '故障' }
  return statusMap[status] || '未知'
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  const typeMap = { 0: 'success', 1: 'warning', 2: 'danger' }
  return typeMap[status] || 'info'
}

// 获取当前位置
const getCurrentLocation = () => {
  locationLoading.value = true
  return new Promise((resolve, reject) => {
    if (!navigator.geolocation) {
      ElMessage.error('浏览器不支持地理位置服务')
      locationLoading.value = false
      reject(new Error('浏览器不支持地理位置服务'))
      return
    }

    navigator.geolocation.getCurrentPosition(
        (position) => {
          const coords = {
            longitude: position.coords.longitude,
            latitude: position.coords.latitude
          }
          currentPosition.value = coords
          ElMessage.success('位置获取成功')
          locationLoading.value = false

          // 跳转到用户位置并添加标记
          if (mapContainerRef && mapContainerRef.value) {
            mapContainerRef.value.gotoLocation(
                coords.longitude,
                coords.latitude,
                true // 添加标记
            )
          }

          resolve(coords)
        },
        (error) => {
          locationLoading.value = false
          let errorMessage = '获取位置失败'
          switch (error.code) {
            case error.PERMISSION_DENIED:
              errorMessage = '用户拒绝提供位置权限，请在浏览器设置中允许位置访问'
              break
            case error.POSITION_UNAVAILABLE:
              errorMessage = '位置信息不可用，请检查网络连接'
              break
            case error.TIMEOUT:
              errorMessage = '获取位置超时，请重试'
              break
          }
          ElMessage.error(errorMessage)
          reject(new Error(errorMessage))
        },
        {
          enableHighAccuracy: true,
          timeout: 15000,
          maximumAge: 300000
        }
    )
  })
}

// 查找最近消防栓
const findNearestHydrant = async () => {
  if (!currentPosition.value) {
    ElMessage.warning('请先获取当前位置')
    return
  }

  geoLoading.value = true
  try {
    const response = await geoApi.getNearestHydrant({
      longitude: currentPosition.value.longitude,
      latitude: currentPosition.value.latitude
    })

    if (response.code === 10000 && response.data) {
      // 通过 hid 在 hydrants 数组中查找完整的消防栓信息
      const hydrantId = response.data.hid
      console.log(response.data.hid)
      const fullHydrantInfo = props.hydrants.find(h => h.id === hydrantId)

      if (fullHydrantInfo) {
        nearestHydrant.value = {
          ...response.data,
          id: hydrantId,
          location: fullHydrantInfo.location,
          status: fullHydrantInfo.status?.toString() || '未知',
          distance: (response.data.distance || 0).toFixed(2) // 格式化距离显示
        }
        ElMessage.success(`找到最近消防栓：${fullHydrantInfo.location}`)
      } else {
        // 如果没有找到完整信息，使用基础信息
        nearestHydrant.value = {
          ...response.data,
          id: hydrantId,
          location: '未知位置',
          status: '0',
          distance: (response.data.distance || 0).toFixed(2)
        }
        ElMessage.success('找到最近消防栓')
      }
    } else {
      ElMessage.warning('未找到附近消防栓')
    }
  } catch (error) {
    console.error('查找最近消防栓失败:', error)
    ElMessage.error('查找失败：' + (error.message || '网络错误'))
  } finally {
    geoLoading.value = false
  }
}

// 查找半径内消防栓
const findHydrantsInRadius = async () => {
  if (!currentPosition.value) {
    ElMessage.warning('请先获取当前位置')
    return
  }

  radiusLoading.value = true
  try {
    const response = await geoApi.getHydrantsInRadius({
      longitude: currentPosition.value.longitude,
      latitude: currentPosition.value.latitude,
      distance: searchRadius.value  // 使用正确的参数名称
    })

    if (response.code === 10000 && response.data) {
      // 为每个半径内的消防栓查找完整信息
      radiusHydrants.value = response.data.map(item => {
        const fullHydrantInfo = props.hydrants.find(h => h.id === item.hid)
        return {
          ...item,
          id: item.hid,
          location: fullHydrantInfo?.location || '未知位置',
          status: fullHydrantInfo?.status?.toString() || '0',
          distance: (item.distance || 0).toFixed(2)  // 格式化距离显示，保留2位小数
        }
      })
      ElMessage.success(`在 ${searchRadius.value} 千米范围内找到 ${radiusHydrants.value.length} 个消防栓`)
      showRadiusDialog.value = false
    } else {
      ElMessage.warning('该范围内未找到消防栓')
    }
  } catch (error) {
    console.error('半径搜索失败:', error)
    ElMessage.error('搜索失败：' + (error.message || '网络错误'))
  } finally {
    radiusLoading.value = false
  }
}

// 清除位置信息
const clearPosition = () => {
  currentPosition.value = null
  nearestHydrant.value = null
  radiusHydrants.value = []

  // 清除地图上的用户位置标记
  if (mapContainerRef && mapContainerRef.value) {
    mapContainerRef.value.clearUserLocationMarker()
  }

  ElMessage.info('已清除位置信息')
}

// 选择最近消防栓
const selectNearestHydrant = () => {
  if (nearestHydrant.value) {
    emit('hydrant-select', nearestHydrant.value.id)
  }
}

// 选择半径内消防栓
const selectRadiusHydrant = (hydrantId) => {
  emit('hydrant-select', hydrantId)
}

// 普通搜索选择
const handleHydrantSelect = (hydrantId) => {
  emit('hydrant-select', hydrantId)
  searchText.value = '' // 清空搜索框
}

// 清除最近结果
const clearNearestResult = () => {
  nearestHydrant.value = null
}

// 清除半径结果
const clearRadiusResult = () => {
  radiusHydrants.value = []
}
</script>

<style scoped>
.search-card {
  background: white;
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  height: 560px;
  display: flex;
  flex-direction: column;
}

.search-card h3 {
  margin: 0 0 16px 0;
  color: #333;
  font-size: 16px;
  font-weight: 600;
  flex-shrink: 0;
}

.geo-search-section {
  margin: 12px 0;
  border-top: 1px solid #f0f0f0;
  padding-top: 12px;
  flex-shrink: 0;
}

.geo-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.geo-buttons {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.location-btn, .nearest-btn, .radius-btn {
  flex: 1;
  font-size: 12px;
  padding: 8px 12px;
  height: 24px;
}

.position-info {
  background: #f0f8ff;
  border-radius: 6px;
  padding: 12px;
  border: 1px solid #d1e9ff;
  font-size: 12px;
}

.position-item {
  display: flex;
  align-items: center;
  margin-bottom: 6px;
}

.position-item .label {
  color: #666;
  min-width: 35px;
  font-size: 12px;
}

.position-item .value {
  color: #1890ff;
  font-family: monospace;
  font-weight: bold;
  font-size: 12px;
}

.clear-position-btn {
  margin-top: 8px;
  color: #999;
  font-size: 12px;
  padding: 4px 8px;
  height: auto;
}

.clear-position-btn:hover {
  color: #ff4d4f;
}

.geo-result {
  background: #f8f9fa;
  border-radius: 6px;
  padding: 12px;
  margin-bottom: 12px;
  border: 1px solid #e9ecef;
  font-size: 12px;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  font-weight: 600;
  color: #333;
  font-size: 13px;
}

.clear-icon {
  cursor: pointer;
  color: #999;
  font-size: 14px;
}

.clear-icon:hover {
  color: #ff4d4f;
}

.result-info {
  font-size: 12px;
}

.result-item {
  display: flex;
  align-items: center;
  margin-bottom: 6px;
}

.result-item .label {
  color: #666;
  min-width: 60px;
  font-size: 12px;
}

.result-item .value {
  color: #333;
  font-size: 12px;
}

.result-item .distance {
  color: #1890ff;
  font-weight: bold;
}

.select-btn {
  margin-top: 8px;
  font-size: 12px;
  padding: 4px 8px;
  height: auto;
}

.radius-results {
  max-height: 150px;
  overflow-y: auto;
}

.radius-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  border-bottom: 1px solid #e8e8e8;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 12px;
}

.radius-item:hover {
  background-color: #f5f5f5;
}

.radius-item:last-child {
  border-bottom: none;
}

.radius-location {
  flex: 1;
  color: #333;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-right: 8px;
}

.radius-distance {
  color: #1890ff;
  font-weight: bold;
  margin: 0 8px;
  font-size: 11px;
  white-space: nowrap;
}

.search-results {
  margin-top: 16px;
  border-top: 1px solid #f0f0f0;
  padding-top: 16px;
  flex: 1;
  overflow-y: auto;
}

.results-header {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.search-result-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 12px;
}

.search-result-item:hover {
  background-color: #f5f5f5;
}

.hydrant-id {
  font-weight: bold;
  color: #ff4d4f;
  font-size: 12px;
  min-width: 70px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.hydrant-location {
  color: #666;
  font-size: 12px;
  flex: 1;
  margin: 0 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.radius-dialog {
  padding: 8px 0;
}

.dialog-item {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.dialog-item .label {
  min-width: 70px;
  color: #333;
  font-size: 13px;
}

.current-position {
  background: #f0f8ff;
  padding: 8px;
  border-radius: 4px;
  margin-bottom: 12px;
  border-left: 3px solid #1890ff;
}

.position-coord {
  font-size: 12px;
  color: #1890ff;
  font-family: monospace;
}

.dialog-tip {
  color: #666;
  font-size: 12px;
  background: #fff7e6;
  padding: 8px;
  border-radius: 4px;
  border-left: 3px solid #faad14;
}

/* 滚动条样式 */
.search-results::-webkit-scrollbar,
.radius-results::-webkit-scrollbar {
  width: 4px;
}

.search-results::-webkit-scrollbar-track,
.radius-results::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 2px;
}

.search-results::-webkit-scrollbar-thumb,
.radius-results::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 2px;
}

.search-results::-webkit-scrollbar-thumb:hover,
.radius-results::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>