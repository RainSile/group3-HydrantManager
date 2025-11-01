<template>
  <main class="map-container">
    <div class="map-placeholder">
      <el-icon class="map-icon"><MapLocation /></el-icon>
      <h3>消防栓分布地图</h3>
      <p>点击左侧列表中的消防栓查看详细信息</p>
      <el-button type="primary" style="margin-top: 20px;">
        <el-icon><View /></el-icon>
        切换到地图视图
      </el-button>
    </div>

    <HydrantDetail
        v-if="selectedHydrant"
        :hydrant="currentHydrant"
        @hydrant-select="$emit('hydrant-select', $event)"
    />
  </main>
</template>

<script>
import HydrantDetail from './HydrantDetail.vue'

export default {
  name: 'MapContainer',
  components: {
    HydrantDetail
  },
  props: {
    selectedHydrant: String,
    currentHydrant: Object
  },
  emits: ['hydrant-select']
}
</script>

<style scoped>
.map-container {
  flex: 1;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  position: relative;
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
}

.map-icon {
  font-size: 64px;
  margin-bottom: 20px;
}
</style>