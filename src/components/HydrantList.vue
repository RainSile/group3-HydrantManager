<template>
  <div class="hydrant-list">
    <div
        v-for="hydrant in hydrants"
        :key="hydrant.id"
        class="hydrant-item"
        :class="{ active: selectedId === hydrant.id }"
        @click="$emit('select', hydrant.id)"
    >
      <div class="hydrant-location">{{ hydrant.location }}</div>
      <div class="hydrant-id">#{{ hydrant.id }}</div>
      <div class="hydrant-status" :class="`status-${hydrant.status}`">
        {{ getStatusText(hydrant.status) }}
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  hydrants: Array,
  selectedId: String
})

defineEmits(['select'])

const getStatusText = (status) => {
  const statusMap = { 0: '正常', 1: '维护中', 2: '故障' }
  return statusMap[status] || '未知'
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

.hydrant-location {
  font-weight: bold;
  color: #ff4d4f;
}

.hydrant-id {
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

.status-0 {
  background-color: #f6ffed;
  color: #52c41a;
  border: 1px solid #b7eb8f;
}

.status-1 {
  background-color: #fffbe6;
  color: #faad14;
  border: 1px solid #ffe58f;
}

.status-2 {
  background-color: #fff2f0;
  color: #ff4d4f;
  border: 1px solid #ffccc7;
}
</style>