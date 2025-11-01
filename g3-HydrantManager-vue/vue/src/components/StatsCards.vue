<template>
  <div class="stats-cards">
    <div class="stat-card">
      <div>正常</div>
      <div class="stat-value stat-normal">{{ stats.normal }}</div>
      <div>消防栓</div>
    </div>
    <div class="stat-card">
      <div>维护中</div>
      <div class="stat-value stat-warning">{{ stats.warning }}</div>
      <div>消防栓</div>
    </div>
    <div class="stat-card">
      <div>故障</div>
      <div class="stat-value stat-error">{{ stats.error }}</div>
      <div>消防栓</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'StatsCards',
  props: {
    stats: Object
  }
}
</script>

<style scoped>
.stats-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  margin-top: 20px;
}

.stat-card {
  background: white;
  border-radius: 8px;
  padding: 15px;
  text-align: center;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin: 10px 0;
}

.stat-normal { color: #52c41a; }
.stat-warning { color: #faad14; }
.stat-error { color: #ff4d4f; }

@media (max-width: 768px) {
  .stats-cards {
    grid-template-columns: 1fr;
  }
}
</style>