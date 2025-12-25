<template>
    <div class="analytics-view">
      <div class="page-header">
        <h2>消防栓数据分析</h2>
        <p>全面的消防栓状态监控与统计分析</p>
      </div>
      
      <div class="content-container">
        <!-- 统计卡片 -->
        <el-row :gutter="20" class="stats-row">
          <el-col :span="6">
            <div class="statistic-card total">
              <div class="statistic-icon">
                <el-icon><Location /></el-icon>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ summaryStats.total }}</div>
                <div class="statistic-label">消防栓总数</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="statistic-card normal">
              <div class="statistic-icon">
                <el-icon><Check /></el-icon>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ summaryStats.normal }}</div>
                <div class="statistic-label">正常运行</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="statistic-card warning">
              <div class="statistic-icon">
                <el-icon><Tools /></el-icon>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ summaryStats.warning }}</div>
                <div class="statistic-label">维护中</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="statistic-card error">
              <div class="statistic-icon">
                <el-icon><Warning /></el-icon>
              </div>
              <div class="statistic-content">
                <div class="statistic-value">{{ summaryStats.error }}</div>
                <div class="statistic-label">故障</div>
              </div>
            </div>
          </el-col>
        </el-row>
        
        <!-- 主要图表区域 -->
        <div class="main-charts">
          <el-row :gutter="20">
            <el-col :span="16">
              <div class="chart-section">
                <HydrantChart :hydrants="hydrants" />
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-section">
                <div class="info-card">
                  <h4>状态分布概览</h4>
                  <div class="progress-stats">
                    <div class="progress-item">
                      <div class="progress-label">
                        <span class="status-dot normal"></span>
                        <span>正常</span>
                        <span class="progress-value">{{ summaryStats.normal }} ({{ normalPercent }}%)</span>
                      </div>
                      <el-progress :percentage="normalPercent" :color="'#52c41a'" :show-text="false" />
                    </div>
                    <div class="progress-item">
                      <div class="progress-label">
                        <span class="status-dot warning"></span>
                        <span>维护中</span>
                        <span class="progress-value">{{ summaryStats.warning }} ({{ warningPercent }}%)</span>
                      </div>
                      <el-progress :percentage="warningPercent" :color="'#faad14'" :show-text="false" />
                    </div>
                    <div class="progress-item">
                      <div class="progress-label">
                        <span class="status-dot error"></span>
                        <span>故障</span>
                        <span class="progress-value">{{ summaryStats.error }} ({{ errorPercent }}%)</span>
                      </div>
                      <el-progress :percentage="errorPercent" :color="'#ff4d4f'" :show-text="false" />
                    </div>
                  </div>
                </div>
                
                <div class="info-card">
                  <h4>最近更新</h4>
                  <div class="recent-updates">
                    <div v-for="hydrant in recentHydrants" :key="hydrant.id" class="update-item">
                      <div class="update-content">
                        <div class="update-location">{{ hydrant.location }}</div>
                        <div class="update-info">
                          <el-tag :type="getStatusTagType(hydrant.status)" size="small">
                            {{ getStatusText(hydrant.status) }}
                          </el-tag>
                          <span class="update-time">{{ formatTime(hydrant.updateTime) }}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
        
        <!-- 数据表格 -->
        <div class="data-table-section">
          <div class="section-header">
            <h4>消防栓详细列表</h4>
            <el-button type="primary" size="small" @click="exportData">
              <el-icon><Download /></el-icon>
              导出数据
            </el-button>
          </div>
          <el-table
            :data="filteredHydrants"
            v-loading="loading"
            style="width: 100%"
            height="400"
          >
            <el-table-column prop="id" label="ID" width="100" />
            <el-table-column prop="location" label="位置" min-width="200" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusTagType(row.status)" size="small">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="pressure" label="水压(MPa)" width="120">
              <template #default="{ row }">
                {{ row.pressure || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="flowRate" label="流速(L/S)" width="120">
              <template #default="{ row }">
                {{ row.flowRate || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="updateTime" label="更新时间" width="180">
              <template #default="{ row }">
                {{ formatTime(row.updateTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template #default="{ row }">
                <el-button type="text" size="small" @click="viewDetail(row.id)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import {
    Location,
    Check,
    Tools,
    Warning,
    Download
  } from '@element-plus/icons-vue'
  import { hydrantApi } from '@/api/hydrantApi.js'
  import HydrantChart from './HydrantChart.vue'
  
  const router = useRouter()
  const loading = ref(false)
  const hydrants = ref([])
  
  // 计算统计信息
  const summaryStats = computed(() => {
    const normal = hydrants.value.filter(h => h.status === '0').length
    const warning = hydrants.value.filter(h => h.status === '1').length
    const error = hydrants.value.filter(h => h.status === '2').length
    
    return {
      total: hydrants.value.length,
      normal,
      warning,
      error
    }
  })
  
  // 计算百分比
  const normalPercent = computed(() => {
    if (summaryStats.value.total === 0) return 0
    return Math.round((summaryStats.value.normal / summaryStats.value.total) * 100)
  })
  
  const warningPercent = computed(() => {
    if (summaryStats.value.total === 0) return 0
    return Math.round((summaryStats.value.warning / summaryStats.value.total) * 100)
  })
  
  const errorPercent = computed(() => {
    if (summaryStats.value.total === 0) return 0
    return Math.round((summaryStats.value.error / summaryStats.value.total) * 100)
  })
  
  // 获取最近更新的消防栓
  const recentHydrants = computed(() => {
    return [...hydrants.value]
      .sort((a, b) => new Date(b.updateTime) - new Date(a.updateTime))
      .slice(0, 5)
  })
  
  // 过滤消防栓列表
  const filteredHydrants = computed(() => {
    return hydrants.value
  })
  
  // 获取状态文本
  const getStatusText = (status) => {
    const statusMap = { '0': '正常', '1': '维护中', '2': '故障' }
    return statusMap[status] || '未知'
  }
  
  // 获取状态标签类型
  const getStatusTagType = (status) => {
    const typeMap = { '0': 'success', '1': 'warning', '2': 'danger' }
    return typeMap[status] || 'info'
  }
  
  // 格式化时间
  const formatTime = (time) => {
    if (!time) return '-'
    try {
      return new Date(time).toLocaleString('zh-CN')
    } catch {
      return time
    }
  }
  
  // 加载数据
  const loadHydrants = async () => {
    try {
      loading.value = true
      const response = await hydrantApi.listHydrants({
        pageIndex: 1,
        pageSize: 100
      })
  
      if (response.code === 10000 && response.data) {
        hydrants.value = response.data.records.map(item => ({
          id: item.id,
          location: item.location,
          status: item.status?.toString(),
          pressure: item.pressure,
          flowRate: item.flowRate,
          updateTime: item.updateTime,
          longitude: item.longitude,
          latitude: item.latitude,
          data: item.data
        }))
      } else {
        ElMessage.warning('获取数据失败')
      }
    } catch (error) {
      console.error('加载消防栓数据失败:', error)
      ElMessage.error('加载数据失败')
    } finally {
      loading.value = false
    }
  }
  
  // 查看详情
  const viewDetail = (id) => {
    // 这里可以跳转到详情页或者打开详情弹窗
    ElMessage.info(`查看消防栓 ${id} 的详情`)
  }
  
  // 导出数据
  const exportData = () => {
    // 简单的数据导出功能
    const dataStr = JSON.stringify(hydrants.value, null, 2)
    const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr)
    
    const exportFileDefaultName = `hydrants_${new Date().getTime()}.json`
    
    const linkElement = document.createElement('a')
    linkElement.setAttribute('href', dataUri)
    linkElement.setAttribute('download', exportFileDefaultName)
    linkElement.click()
    
    ElMessage.success('数据导出成功')
  }
  
  // 组件挂载时加载数据
  onMounted(() => {
    loadHydrants()
  })
  </script>
  
  <style scoped>
  .analytics-view {
    padding: 20px;
    background: #f5f5f5;
    min-height: calc(100vh - 120px);
  }
  
  .page-header {
    margin-bottom: 30px;
  }
  
  .page-header h2 {
    margin: 0;
    color: #333;
    font-size: 24px;
    font-weight: 600;
  }
  
  .page-header p {
    margin: 8px 0 0;
    color: #666;
    font-size: 14px;
  }
  
  .content-container {
    background: white;
    border-radius: 8px;
    padding: 24px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }
  
  .stats-row {
    margin-bottom: 30px;
  }
  
  .statistic-card {
    display: flex;
    align-items: center;
    padding: 20px;
    border-radius: 8px;
    background: #f8f9fa;
    transition: all 0.3s ease;
    border-left: 4px solid #1890ff;
  }
  
  .statistic-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
  
  .statistic-card.total {
    border-left-color: #1890ff;
  }
  
  .statistic-card.normal {
    border-left-color: #52c41a;
  }
  
  .statistic-card.warning {
    border-left-color: #faad14;
  }
  
  .statistic-card.error {
    border-left-color: #ff4d4f;
  }
  
  .statistic-icon {
    margin-right: 15px;
    font-size: 32px;
    color: #1890ff;
  }
  
  .statistic-card.normal .statistic-icon {
    color: #52c41a;
  }
  
  .statistic-card.warning .statistic-icon {
    color: #faad14;
  }
  
  .statistic-card.error .statistic-icon {
    color: #ff4d4f;
  }
  
  .statistic-value {
    font-size: 24px;
    font-weight: bold;
    color: #333;
    margin-bottom: 4px;
  }
  
  .statistic-label {
    color: #666;
    font-size: 14px;
  }
  
  .main-charts {
    margin-bottom: 30px;
  }
  
  .chart-section {
    height: 100%;
  }
  
  .info-section {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }
  
  .info-card {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 20px;
    border-left: 4px solid #1890ff;
  }
  
  .info-card h4 {
    margin: 0 0 20px 0;
    color: #333;
    font-size: 16px;
    font-weight: 600;
  }
  
  .progress-stats {
    display: flex;
    flex-direction: column;
    gap: 15px;
  }
  
  .progress-item {
    margin-bottom: 10px;
  }
  
  .progress-label {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
    font-size: 14px;
    color: #333;
  }
  
  .status-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    margin-right: 8px;
  }
  
  .status-dot.normal {
    background: #52c41a;
  }
  
  .status-dot.warning {
    background: #faad14;
  }
  
  .status-dot.error {
    background: #ff4d4f;
  }
  
  .progress-value {
    margin-left: auto;
    color: #666;
    font-size: 13px;
  }
  
  .recent-updates {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
  
  .update-item {
    padding: 12px;
    background: white;
    border-radius: 6px;
    border: 1px solid #e8e8e8;
    transition: all 0.3s;
  }
  
  .update-item:hover {
    border-color: #1890ff;
    box-shadow: 0 2px 8px rgba(24, 144, 255, 0.1);
  }
  
  .update-content {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
  
  .update-location {
    font-size: 14px;
    color: #333;
    font-weight: 500;
  }
  
  .update-info {
    display: flex;
    align-items: center;
    gap: 10px;
  }
  
  .update-time {
    font-size: 12px;
    color: #999;
  }
  
  .data-table-section {
    margin-top: 30px;
  }
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .section-header h4 {
    margin: 0;
    color: #333;
    font-size: 18px;
    font-weight: 600;
  }
  
  /* 响应式设计 */
  @media (max-width: 1200px) {
    .main-charts .el-row {
      flex-direction: column;
    }
    
    .main-charts .el-col {
      width: 100%;
    }
  }
  
  @media (max-width: 768px) {
    .stats-row .el-col {
      margin-bottom: 15px;
    }
    
    .statistic-card {
      flex-direction: column;
      text-align: center;
    }
    
    .statistic-icon {
      margin-right: 0;
      margin-bottom: 10px;
    }
    
    .analytics-view {
      padding: 10px;
    }
    
    .content-container {
      padding: 15px;
    }
  }
  </style>