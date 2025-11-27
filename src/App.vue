<template>
  <div id="app">
    <Login v-if="!isLoggedIn" @login-success="handleLoginSuccess" />
    <div v-else class="main-app">
      <AppHeader :username="userInfo?.username" @logout="handleLogout" />
      <HydrantManagement />
      <AppFooter />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { logout } from '@/api/hydrantApi.js'
import AppHeader from './components/Header.vue'
import AppFooter from './components/Footer.vue'
import Login from './components/Login.vue'
import HydrantManagement from './components/HydrantManagement.vue'

const isLoggedIn = ref(false)
const userInfo = ref(null)

const handleLoginSuccess = (userData) => {
  isLoggedIn.value = true
  userInfo.value = userData
}

const handleLogout = async () => {
  try {
    await logout()
    ElMessage.success('已退出登录')
  } catch (error) {
    ElMessage.success('已退出登录')
  } finally {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    isLoggedIn.value = false
    userInfo.value = null
  }
}

const checkLoginStatus = () => {
  const token = localStorage.getItem('token')
  const storedUserInfo = localStorage.getItem('userInfo')

  if (token && storedUserInfo) {
    isLoggedIn.value = true
    userInfo.value = JSON.parse(storedUserInfo)
  }
}

onMounted(() => {
  checkLoginStatus()
})
</script>

<style scoped>
.main-app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>