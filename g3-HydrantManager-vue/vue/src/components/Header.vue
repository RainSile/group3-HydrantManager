<template>
  <header class="app-header">
    <div class="header-content">
      <div class="logo">
        <el-icon class="logo-icon"><FireExtinguisher /></el-icon>
        <span>消防栓查询系统</span>
      </div>
      <div>
        <el-button type="primary" link style="color: white;">管理员登录</el-button>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: 'AppHeader'
}
</script>

<style scoped>
.app-header {
  background: linear-gradient(135deg, #ff4d4f 0%, #cf1322 100%);
  color: white;
  padding: 0 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  max-width: 1200px;
  margin: 0 auto;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 20px;
  font-weight: bold;
}

.logo-icon {
  font-size: 24px;
}
</style>