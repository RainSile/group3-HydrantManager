<template>
  <main class="map-container">
    <div id="bmap-container" class="bmap" v-show="mapLoaded"></div>

    <div v-if="!mapLoaded" class="map-placeholder">
      <el-icon class="map-icon"><MapLocation /></el-icon>
      <h3>消防栓分布地图</h3>
      <el-button type="primary" style="margin-top: 20px;" @click="openMap">
        <el-icon><View /></el-icon>
        加载地图
      </el-button>
    </div>

    <div v-if="loading" class="map-loading">
      <div class="loading-spinner"></div>
      <span>地图加载中...</span>
    </div>

    <slot name="hydrant-detail"></slot>
  </main>
</template>

<script setup>
import { ref, defineProps, defineEmits, onMounted, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
const BAIDU_MAP_AK = import.meta.env.VITE_BAIDU_MAP_AK
import { MapLocation, View } from '@element-plus/icons-vue'

const props = defineProps({
  selectedHydrant: String,
  hydrants: Array
})

const emit = defineEmits(['hydrant-select'])

const mapLoaded = ref(false)
const loading = ref(false)
const map = ref(null)
const markers = ref([])

const MAP_CENTER = { lng: 113.396845, lat: 22.533457 }

const openMap = async () => {
  if (!BAIDU_MAP_AK) {
    ElMessage.error('请配置百度地图 API Key')
    return
  }

  loading.value = true

  try {
    await loadBMapScript()
    createMap()
  } catch (error) {
    console.error('地图加载失败:', error)
    ElMessage.error('地图加载失败')
  } finally {
    loading.value = false
  }
}

const loadBMapScript = () => {
  return new Promise((resolve, reject) => {
    if (window.BMap) {
      resolve()
      return
    }

    const script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = `https://api.map.baidu.com/api?v=3.0&ak=${BAIDU_MAP_AK}&callback=initBMapCallback`

    const timeoutId = setTimeout(() => {
      reject(new Error('百度地图API加载超时'))
    }, 10000)

    window.initBMapCallback = () => {
      clearTimeout(timeoutId)
      resolve()
    }

    script.onerror = () => {
      clearTimeout(timeoutId)
      reject(new Error('百度地图API加载失败'))
    }

    document.head.appendChild(script)
  })
}

const createMap = () => {
  map.value = new BMap.Map('bmap-container')
  const point = new BMap.Point(MAP_CENTER.lng, MAP_CENTER.lat)

  map.value.centerAndZoom(point, 15)
  map.value.enableScrollWheelZoom(true)

  addHydrantMarkers()
  mapLoaded.value = true
}

const addHydrantMarkers = () => {
  clearMarkers()

  props.hydrants?.forEach(hydrant => {
    if (hydrant.longitude && hydrant.latitude) {
      const point = new BMap.Point(hydrant.longitude, hydrant.latitude)
      const icon = createHydrantIcon(hydrant.status)
      const marker = new BMap.Marker(point, { icon: icon })

      marker.addEventListener('click', () => {
        emit('hydrant-select', hydrant.id)
      })

      map.value.addOverlay(marker)
      markers.value.push(marker)
    }
  })
}

const createHydrantIcon = (status) => {
  const colors = {
    '0': '#52c41a',
    '1': '#faad14',
    '2': '#ff4d4f'
  }

  const color = colors[status] || colors['0']

  const icon = new BMap.Icon(
      createSvgIcon(color),
      new BMap.Size(30, 40),
      {
        anchor: new BMap.Size(15, 40)
      }
  )

  return icon
}

const createSvgIcon = (color) => {
  const svg = `
    <svg width="30" height="40" viewBox="0 0 30 40" xmlns="http://www.w3.org/2000/svg">
      <path d="M15 2C8.1 2 2.5 7.6 2.5 14.5c0 6.9 12.5 23.5 12.5 23.5s12.5-16.6 12.5-23.5C27.5 7.6 21.9 2 15 2z"
            fill="${color}" stroke="#fff" stroke-width="2"/>
      <circle cx="15" cy="14" r="5" fill="#fff"/>
    </svg>
  `
  return 'data:image/svg+xml;base64,' + btoa(unescape(encodeURIComponent(svg)))
}

const clearMarkers = () => {
  markers.value.forEach(marker => {
    map.value?.removeOverlay(marker)
  })
  markers.value = []
}

watch(() => props.hydrants, (newHydrants) => {
  if (mapLoaded.value && newHydrants) {
    nextTick(() => {
      addHydrantMarkers()
    })
  }
}, { deep: true, immediate: true })

const refreshMarkers = () => {
  if (map.value && mapLoaded.value) {
    addHydrantMarkers()
  }
}

defineExpose({
  refreshMarkers
})

watch(() => props.selectedHydrant, (newVal) => {
  if (newVal && map.value) {
    const hydrant = props.hydrants.find(h => h.id === newVal)
    if (hydrant?.longitude && hydrant?.latitude) {
      const point = new BMap.Point(hydrant.longitude, hydrant.latitude)
      map.value.panTo(point)
    }
  }
})
</script>

<style scoped>
.map-container {
  flex: 1;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  position: relative;
  min-height: 600px;
}

.bmap {
  width: 100%;
  height: 100%;
  min-height: 600px;
}

.map-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #e6f7ff 0%, #bae7ff 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #1890ff;
  min-height: 600px;
}

.map-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.map-loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  background: rgba(255, 255, 255, 0.9);
  padding: 20px;
  border-radius: 8px;
  z-index: 1001;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #1890ff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 10px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>