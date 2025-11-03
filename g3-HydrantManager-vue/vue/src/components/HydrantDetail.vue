<template>
  <el-card class="hydrant-detail-card">
    <template #header>
      <div class="card-header">
        <span>消防栓详情</span>
        <el-button
            type="danger"
            :icon="Close"
            circle
            size="small"
            @click="handleClose"
            class="close-btn"
        />
      </div>
    </template>

    <div v-if="hydrant">
      <p><strong>编号：</strong>#{{ hydrant.id }}</p>
      <p><strong>地址：</strong>{{ hydrant.address }}</p>
      <p><strong>状态：</strong>
        <el-tag :type="hydrant.status === 'normal' ? 'success' :
                      hydrant.status === 'warning' ? 'warning' : 'danger'">
          {{ getStatusText(hydrant.status) }}
        </el-tag>
      </p>
      <p><strong>类型：</strong>{{ hydrant.type }}</p>
      <p><strong>水压：</strong>{{ hydrant.pressure }} MPa</p>
      <p><strong>最后检查：</strong>{{ hydrant.lastCheck }}</p>
      <p><strong>负责人：</strong>{{ hydrant.manager }}</p>

      <div style="margin-top: 15px;">
        <el-button type="primary" size="small">报修</el-button>
        <el-button type="warning" size="small">检查记录</el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import { Close } from '@element-plus/icons-vue'

const props = defineProps({
  hydrant: Object
})

const emit = defineEmits(['close', 'hydrant-select'])

const handleClose = () => {
  emit('close')
}

const getStatusText = (status) => {
  const statusMap = {
    normal: '正常',
    warning: '维护中',
    error: '故障'
  }
  return statusMap[status] || '未知'
}
</script>

<style scoped>
.hydrant-detail-card {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 300px;
  z-index: 1000;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.close-btn {
  padding: 4px;
  min-width: 24px;
  height: 24px;
}

.close-btn:hover {
  transform: scale(1.1);
  transition: transform 0.2s;
}
</style>