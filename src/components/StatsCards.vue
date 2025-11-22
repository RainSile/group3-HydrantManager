<template>
  <div class="stats-cards">
    <div class="stat-card">
      <div class="stat-value">{{ stats.total }}</div>
      <div class="stat-label">总数</div>
    </div>
    <div class="stat-card normal">
      <div class="stat-value">{{ stats.normal }}</div>
      <div class="stat-label">正常</div>
    </div>
    <div class="stat-card warning">
      <div class="stat-value">{{ stats.warning }}</div>
      <div class="stat-label">维护中</div>
    </div>
    <div class="stat-card error">
      <div class="stat-value">{{ stats.error }}</div>
      <div class="stat-label">故障</div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  stats: Object
})
</script>

<style scoped>
.stats-cards {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.stat-card {
  background: white;
  padding: 16px;
  border-radius: 8px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  border-left: 4px solid #1890ff;
}

.stat-card.normal {
  border-left-color: #52c41a;
}

.stat-card.warning {
  border-left-color: #faad14;
}

.stat-card.error {
  border-left-color: #ff4d4f;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}
</style>