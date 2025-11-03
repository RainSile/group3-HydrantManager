<template>
  <main class="map-container">
    <!-- 百度地图容器 -->
    <div id="bmap-container" class="bmap" v-show="mapLoaded"></div>

    <!-- 地图未加载时的占位符 -->
    <div v-if="!mapLoaded" class="map-placeholder">
      <el-icon class="map-icon"><MapLocation /></el-icon>
      <h3>消防栓分布地图</h3>
      <p>点击左侧列表中的消防栓查看详细信息</p>
      <el-button type="primary" style="margin-top: 20px;" @click="openMap">
        <el-icon><View /></el-icon>
        切换到地图视图
      </el-button>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="map-loading">
      <div class="loading-spinner"></div>
      <span>地图加载中...</span>
    </div>

    <HydrantDetail
        v-if="selectedHydrant"
        :hydrant="currentHydrant"
        @hydrant-select="onHydrantSelect"
        @close="handleCloseDetail"
    />
  </main>
</template>

<script setup>
import { ref, defineProps, defineEmits, onMounted, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
// ✅ 推荐做法 - 直接从环境变量获取
const BAIDU_MAP_AK = import.meta.env.VITE_BAIDU_MAP_AK
import { MapLocation, View } from '@element-plus/icons-vue'
import HydrantDetail from './HydrantDetail.vue'

// 定义 props
const props = defineProps({
  selectedHydrant: String,
  currentHydrant: Object
})

// 定义 emits
const emit = defineEmits(['hydrant-select'])

// 响应式数据
const mapLoaded = ref(false)
const loading = ref(false)
const map = ref(null)
const markers = ref([])

// 检查环境变量配置
const checkEnvConfig = () => {
  if (!BAIDU_MAP_AK) {
    ElMessage.error('请先在 .env 文件中配置百度地图 API Key')
    return false
  }
  return true
}

// 默认中心点（可以根据需要修改）
const MAP_CENTER = { lng: 113.396845, lat: 22.533457 }


// 消防栓坐标数据
const hydrantCoordinates = {
  '001': { lng: 116.404, lat: 39.915, address: '人民路与中山路交叉口东南角' },
  '002': { lng: 116.414, lat: 39.925, address: '解放大道128号门前' },
  '003': { lng: 116.394, lat: 39.905, address: '新华西路与建设路交叉口' },
  '004': { lng: 116.424, lat: 39.935, address: '环城北路56号对面' },
  '005': { lng: 116.434, lat: 39.945, address: '城南新区幸福广场' },
  '006': { lng: 116.444, lat: 39.955, address: '工业区创业大道88号' }
}

// 打开地图
const openMap = async () => {
  if (!checkEnvConfig()) return

  loading.value = true

  try {
    // 动态加载百度地图API
    await loadBMapScript()
    // 创建地图实例
    createMap()
    ElMessage.success('地图加载成功')
  } catch (error) {
    console.error('地图加载失败:', error)
    ElMessage.error('地图加载失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 加载百度地图脚本
const loadBMapScript = () => {
  return new Promise((resolve, reject) => {
    // 如果已经加载，直接返回
    if (window.BMap) {
      resolve()
      return
    }

    const script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = `https://api.map.baidu.com/api?v=3.0&ak=${BAIDU_MAP_AK}&callback=initBMapCallback`

    // 设置超时
    const timeoutId = setTimeout(() => {
      reject(new Error('百度地图API加载超时'))
    }, 10000)

    // 全局回调函数
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

// 创建地图实例
const createMap = () => {
  // 创建地图实例
  map.value = new BMap.Map('bmap-container')
  const point = new BMap.Point(MAP_CENTER.lng, MAP_CENTER.lat)

  // 初始化地图
  map.value.centerAndZoom(point, 15)

  // 启用功能
  map.value.enableScrollWheelZoom(true) // 启用滚轮缩放
  map.value.enableContinuousZoom(true)   // 启用连续缩放

  // 添加地图控件
  map.value.addControl(new BMap.NavigationControl())  // 缩放平移控件
  map.value.addControl(new BMap.ScaleControl())       // 比例尺控件
  map.value.addControl(new BMap.OverviewMapControl()) // 缩略地图控件

  // 添加消防栓标记
  addHydrantMarkers()

  mapLoaded.value = true
}

// 添加消防栓标记
const addHydrantMarkers = () => {
  // 清除现有标记
  clearMarkers()

  Object.entries(hydrantCoordinates).forEach(([id, coord]) => {
    const point = new BMap.Point(coord.lng, coord.lat)

    // 创建自定义图标
    const icon = createHydrantIcon(id)

    // 创建标记
    const marker = new BMap.Marker(point, { icon: icon })

    // 创建信息窗口
    const infoWindow = createInfoWindow(id, coord.address)

    // 添加点击事件
    marker.addEventListener('click', () => {
      emit('hydrant-select', id)
      map.value.openInfoWindow(infoWindow, point)
    })

    // 添加到地图
    map.value.addOverlay(marker)
    markers.value.push(marker)
  })
}

// 创建消防栓图标
const createHydrantIcon = (hydrantId) => {
  // 根据消防栓状态设置不同颜色
  const hydrantData = props.currentHydrant
  const status = hydrantData?.status || 'normal'

  const colors = {
    normal: '#52c41a', // 绿色-正常
    warning: '#faad14', // 黄色-维护中
    error: '#ff4d4f'    // 红色-故障
  }

  const color = colors[status] || colors.normal

  // 创建自定义图标
  const icon = new BMap.Icon(
      createSvgIcon(color),
      new BMap.Size(30, 40),
      {
        anchor: new BMap.Size(15, 40), // 锚点位置
        imageSize: new BMap.Size(30, 40)
      }
  )

  return icon
}

// 创建SVG图标
const createSvgIcon = (color) => {
  const svg = `
    <svg width="30" height="40" viewBox="0 0 30 40" xmlns="http://www.w3.org/2000/svg">
      <path d="M15 2C8.1 2 2.5 7.6 2.5 14.5c0 6.9 12.5 23.5 12.5 23.5s12.5-16.6 12.5-23.5C27.5 7.6 21.9 2 15 2z"
            fill="${color}" stroke="#fff" stroke-width="2"/>
      <circle cx="15" cy="14" r="5" fill="#fff"/>
      <rect x="13" y="19" width="4" height="8" fill="#fff"/>
    </svg>
  `
  return 'data:image/svg+xml;base64,' + btoa(unescape(encodeURIComponent(svg)))
}

// 创建信息窗口
const createInfoWindow = (id, address) => {
  const content = `
    <div style="padding: 10px; max-width: 200px;">
      <h4 style="margin: 0 0 8px 0; color: #1890ff;">消防栓 #${id}</h4>
      <p style="margin: 0; color: #666; font-size: 12px;">${address}</p>
      <button onclick="window.selectHydrant('${id}')"
              style="margin-top: 8px; padding: 4px 8px; background: #1890ff; color: white; border: none; border-radius: 4px; cursor: pointer;">
        查看详情
      </button>
    </div>
  `

  // 全局函数供信息窗口调用
  window.selectHydrant = (id) => {
    emit('hydrant-select', id)
  }

  return new BMap.InfoWindow(content)
}

// 清除所有标记
const clearMarkers = () => {
  markers.value.forEach(marker => {
    map.value?.removeOverlay(marker)
  })
  markers.value = []
}

// 监听选中的消防栓
watch(() => props.selectedHydrant, (newVal) => {
  if (newVal && hydrantCoordinates[newVal] && map.value) {
    const coord = hydrantCoordinates[newVal]
    const point = new BMap.Point(coord.lng, coord.lat)

    // 平移到选中的消防栓位置
    map.value.panTo(point)

    // 打开信息窗口
    const infoWindow = createInfoWindow(newVal, coord.address)
    map.value.openInfoWindow(infoWindow, point)
  }
})

// 方法
const handleCloseDetail = () => {
  emit('hydrant-select', null)
}

const onHydrantSelect = (id) => {
  emit('hydrant-select', id)
}

// 清理
onMounted(() => {
  // 检查环境变量
  checkEnvConfig()
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

/* 加载状态 */
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

/* 响应式设计 */
@media (max-width: 768px) {
  .map-container,
  .bmap,
  .map-placeholder {
    min-height: 400px;
  }
}
</style>