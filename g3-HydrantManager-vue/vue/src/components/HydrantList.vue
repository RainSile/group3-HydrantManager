<template>
  <div class="hydrant-list">
    <div
        v-for="hydrant in hydrants"
        :key="hydrant.id"
        class="hydrant-item"
        :class="{ active: $parent.selectedHydrant === hydrant.id }"
        @click="$emit('hydrant-select', hydrant.id)"
    >
      <div class="hydrant-id">消防栓 #{{ hydrant.id }}</div>
      <div class="hydrant-address">{{ hydrant.address }}</div>
      <div class="hydrant-status" :class="`status-${hydrant.status}`">
        {{ getStatusText(hydrant.status) }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HydrantList',
  props: {
    hydrants: Array
  },
  emits: ['hydrant-select'],
  methods: {
    getStatusText(status) {
      const statusMap = {
        normal: '正常',
        warning: '维护中',
        error: '故障'
      }
      return statusMap[status] || '未知'
    }
  }
}
</script>

<style scoped>
.hydrant-list {
  margin-top: 20px;
  max-height: 400px;
  overflow-y: auto;
}

.hydrant-item {
  padding: 12px;
  border-bottom: 1px solid #e8e8e8;
  cursor: pointer;
  transition: background-color 0.3s;
}

.hydrant-item:hover {
  background-color: #f5f5f5;
}

.hydrant-item.active {
  background-color: #fff2f0;
  border-left: 3px solid #ff4d4f;
}

.hydrant-id {
  font-weight: bold;
  color: #ff4d4f;
}

.hydrant-address {
  color: #666;
  font-size: 14px;
  margin-top: 5px;
}

.hydrant-status {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  margin-top: 5px;
}

.status-normal {
  background-color: #f6ffed;
  color: #52c41a;
  border: 1px solid #b7eb8f;
}

.status-warning {
  background-color: #fffbe6;
  color: #faad14;
  border: 1px solid #ffe58f;
}

.status-error {
  background-color: #fff2f0;
  color: #ff4d4f;
  border: 1px solid #ffccc7;
}
</style>