<template>
  <div id="app">
    <!-- 如果未登录，显示登录界面 -->
    <Login v-if="!isLoggedIn" @login-success="handleLoginSuccess" />

    <!-- 如果已登录，显示主界面 -->
    <div v-else class="app-container">
      <AppHeader :username="username" @logout="handleLogout" />
      <div class="main-container">
        <Sidebar
            :search-query="searchQuery"
            :filter-status="filterStatus"
            :filter-area="filterArea"
            :filter-date="filterDate"
            :selected-hydrant="selectedHydrant"
            :hydrants="filteredHydrants"
            :stats="stats"
            @update:search-query="searchQuery = $event"
            @update:filter-status="filterStatus = $event"
            @update:filter-area="filterArea = $event"
            @update:filter-date="filterDate = $event"
            @hydrant-select="selectHydrant"
            @search="handleSearch"
        />
        <MapContainer
            :selected-hydrant="selectedHydrant"
            :current-hydrant="currentHydrant"
            @hydrant-select="selectHydrant"
        />
      </div>
      <AppFooter />
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { hydrantsData } from './data/hydrants.js'
import AppHeader from './components/Header.vue'
import Sidebar from './components/Sidebar.vue'
import MapContainer from './components/MapContainer.vue'
import AppFooter from './components/Footer.vue'
import Login from './components/Login.vue'

export default {
  name: 'App',
  components: {
    Login,
    AppHeader,
    Sidebar,
    MapContainer,
    AppFooter
  },
  setup() {
    // 登录状态管理
    const isLoggedIn = ref(false)
    const username = ref('')

    // 检查登录状态
    const checkLoginStatus = () => {
      const loggedIn = localStorage.getItem('isLoggedIn')
      const savedUsername = localStorage.getItem('username')

      if (loggedIn === 'true' && savedUsername) {
        isLoggedIn.value = true
        username.value = savedUsername
      }
    }

    // 登录成功处理
    const handleLoginSuccess = (userData) => {
      isLoggedIn.value = true
      username.value = userData.username
      ElMessage.success(`欢迎回来，${userData.username}！`)
    }

    // 退出登录
    const handleLogout = () => {
      localStorage.removeItem('isLoggedIn')
      localStorage.removeItem('username')
      isLoggedIn.value = false
      username.value = ''
      ElMessage.success('已退出登录')
    }

    // 原有的业务逻辑数据
    const searchQuery = ref('')
    const filterStatus = ref('')
    const filterArea = ref('')
    const filterDate = ref('')
    const selectedHydrant = ref(null)
    const hydrants = ref(hydrantsData)

    // 计算属性
    const stats = computed(() => {
      return {
        normal: hydrants.value.filter(h => h.status === 'normal').length,
        warning: hydrants.value.filter(h => h.status === 'warning').length,
        error: hydrants.value.filter(h => h.status === 'error').length
      }
    })

    const filteredHydrants = computed(() => {
      let result = hydrants.value

      // 搜索过滤
      if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase()
        result = result.filter(h =>
            h.id.toLowerCase().includes(query) ||
            h.address.toLowerCase().includes(query)
        )
      }

      // 状态过滤
      if (filterStatus.value) {
        result = result.filter(h => h.status === filterStatus.value)
      }

      // 区域过滤
      if (filterArea.value) {
        result = result.filter(h => h.area === filterArea.value)
      }

      return result
    })

    const currentHydrant = computed(() => {
      return hydrants.value.find(h => h.id === selectedHydrant.value)
    })

    // 方法
    const handleSearch = () => {
      if (searchQuery.value) {
        ElMessage.success(`搜索: ${searchQuery.value}`)
      }
    }

    const selectHydrant = (id) => {
      selectedHydrant.value = id
    }

    // 生命周期
    onMounted(() => {
      // 检查登录状态
      checkLoginStatus()

      // 只有登录后才加载数据提示
      if (isLoggedIn.value) {
        setTimeout(() => {
          ElMessage.success('消防栓数据加载完成')
        }, 500)
      }
    })

    return {
      // 登录相关
      isLoggedIn,
      username,
      handleLoginSuccess,
      handleLogout,

      // 业务逻辑
      searchQuery,
      filterStatus,
      filterArea,
      filterDate,
      selectedHydrant,
      hydrants,
      stats,
      filteredHydrants,
      currentHydrant,
      handleSearch,
      selectHydrant
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", Arial, sans-serif;
}

body {
  background-color: #f5f7fa;
  color: #333;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-container {
  display: flex;
  flex: 1;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  padding: 20px;
  gap: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-container {
    flex-direction: column;
  }
}
</style>