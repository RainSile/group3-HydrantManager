<template>
  <header class="app-header">
    <div class="header-content">
      <div class="logo">
        <span class="logo-icon">🔥</span>
        <span>消防栓查询系统</span>
      </div>
      <div class="user-info" v-if="username">
        <span class="user-name">👤 {{ username }}</span>
        <el-dropdown @command="handleCommand">
          <span class="user-dropdown">
            ⚙️
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人资料</el-dropdown-item>
              <el-dropdown-item command="settings">系统设置</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </header>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  username: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['logout'])

const handleCommand = (command) => {
  if (command === 'logout') {
    emit('logout')
  } else if (command === 'profile') {
    ElMessage.info('个人资料功能开发中...')
  } else if (command === 'settings') {
    ElMessage.info('系统设置功能开发中...')
  }
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

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-name {
  font-size: 14px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 16px;
}

.user-dropdown:hover {
  background: rgba(255, 255, 255, 0.3);
}
</style>