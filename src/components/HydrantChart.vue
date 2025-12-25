<template>
    <div class="hydrant-chart">
      <div class="chart-header">
        <h3>消防栓数据统计</h3>
        <div class="chart-controls">
          <el-select v-model="chartType" placeholder="选择图表类型" size="small">
            <el-option label="柱状图" value="bar" />
            <el-option label="饼图" value="pie" />
            <el-option label="折线图" value="line" />
          </el-select>
          <el-select v-model="timeRange" placeholder="时间范围" size="small">
            <el-option label="今天" value="today" />
            <el-option label="本周" value="week" />
            <el-option label="本月" value="month" />
            <el-option label="全年" value="year" />
          </el-select>
        </div>
      </div>
      
      <div class="chart-container">
        <div ref="chartRef" class="chart"></div>
      </div>
      
      <div class="chart-stats">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="stat-card">
              <div class="stat-value">{{ statusStats.total }}</div>
              <div class="stat-label">消防栓总数</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card normal">
              <div class="stat-value">{{ statusStats.normal }}</div>
              <div class="stat-label">正常运行</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card warning">
              <div class="stat-value">{{ statusStats.warning + statusStats.error }}</div>
              <div class="stat-label">需要关注</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted, onUnmounted, watch, computed } from 'vue'
  import * as echarts from 'echarts'
  import { ElMessage } from 'element-plus'
  
  const props = defineProps({
    hydrants: {
      type: Array,
      default: () => []
    }
  })
  
  const chartRef = ref(null)
  let chartInstance = null
  const chartType = ref('bar')
  const timeRange = ref('month')
  
  // 计算统计信息
  const statusStats = computed(() => {
    const normal = props.hydrants.filter(h => h.status === '0').length
    const warning = props.hydrants.filter(h => h.status === '1').length
    const error = props.hydrants.filter(h => h.status === '2').length
    
    return {
      total: props.hydrants.length,
      normal,
      warning,
      error
    }
  })
  
  // 准备图表数据
  const prepareChartData = () => {
    const hydrants = props.hydrants
    
    // 按区域分组统计
    const areaStats = {}
    const statusCount = {
      '正常': 0,
      '维护中': 0,
      '故障': 0
    }
    
    // 模拟区域数据（实际中hydrant应该有area字段）
    const areas = ['东城区', '西城区', '南城区', '北城区', '中心区']
    
    // 初始化区域统计
    areas.forEach(area => {
      areaStats[area] = {
        normal: 0,
        warning: 0,
        error: 0,
        total: 0
      }
    })
    
    // 统计状态
    hydrants.forEach(hydrant => {
      const statusText = getStatusText(hydrant.status)
      statusCount[statusText]++
      
      // 随机分配区域（实际应从数据中获取）
      const randomArea = areas[Math.floor(Math.random() * areas.length)]
      if (hydrant.status === '0') {
        areaStats[randomArea].normal++
      } else if (hydrant.status === '1') {
        areaStats[randomArea].warning++
      } else if (hydrant.status === '2') {
        areaStats[randomArea].error++
      }
      areaStats[randomArea].total++
    })
    
    return {
      areaStats,
      statusCount,
      areas
    }
  }
  
  // 获取状态文本
  const getStatusText = (status) => {
    const statusMap = { '0': '正常', '1': '维护中', '2': '故障' }
    return statusMap[status] || '未知'
  }
  
  // 初始化图表
  const initChart = () => {
    if (!chartRef.value) return
    
    // 销毁现有实例
    if (chartInstance) {
      chartInstance.dispose()
    }
    
    // 初始化新实例
    chartInstance = echarts.init(chartRef.value)
    
    // 准备数据
    const chartData = prepareChartData()
    
    // 根据图表类型选择不同的配置
    let option
    if (chartType.value === 'bar') {
      option = getBarOption(chartData)
    } else if (chartType.value === 'pie') {
      option = getPieOption(chartData)
    } else if (chartType.value === 'line') {
      option = getLineOption(chartData)
    }
    
    // 设置图表配置
    chartInstance.setOption(option)
    
    // 监听窗口大小变化
    window.addEventListener('resize', handleResize)
  }
  
  // 柱状图配置
  const getBarOption = (chartData) => {
    const { areaStats, areas } = chartData
    
    return {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        },
        formatter: function(params) {
          let result = `${params[0].name}<br/>`
          params.forEach(param => {
            const color = param.color
            const marker = `<span style="display:inline-block;margin-right:5px;border-radius:50%;width:10px;height:10px;background-color:${color};"></span>`
            result += `${marker} ${param.seriesName}: ${param.value}<br/>`
          })
          return result
        }
      },
      legend: {
        data: ['正常', '维护中', '故障'],
        top: 10
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: '20%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        data: areas,
        axisLabel: {
          rotate: 45
        }
      },
      yAxis: {
        type: 'value',
        name: '数量'
      },
      series: [
        {
          name: '正常',
          type: 'bar',
          stack: 'total',
          emphasis: {
            focus: 'series'
          },
          itemStyle: {
            color: '#52c41a'
          },
          data: areas.map(area => areaStats[area]?.normal || 0)
        },
        {
          name: '维护中',
          type: 'bar',
          stack: 'total',
          emphasis: {
            focus: 'series'
          },
          itemStyle: {
            color: '#faad14'
          },
          data: areas.map(area => areaStats[area]?.warning || 0)
        },
        {
          name: '故障',
          type: 'bar',
          stack: 'total',
          emphasis: {
            focus: 'series'
          },
          itemStyle: {
            color: '#ff4d4f'
          },
          data: areas.map(area => areaStats[area]?.error || 0)
        }
      ]
    }
  }
  
  // 饼图配置
  const getPieOption = (chartData) => {
    const { statusCount } = chartData
    
    const pieData = [
      { value: statusCount['正常'], name: '正常', itemStyle: { color: '#52c41a' } },
      { value: statusCount['维护中'], name: '维护中', itemStyle: { color: '#faad14' } },
      { value: statusCount['故障'], name: '故障', itemStyle: { color: '#ff4d4f' } }
    ].filter(item => item.value > 0)
    
    
    return {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        top: 'middle'
      },
      series: [
        {
          name: '状态分布',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{b}: {c} ({d}%)'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '16',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: true
          },
          data: pieData
        }
      ]
    }
  }
  
  // 折线图配置
  const getLineOption = (chartData) => {
    // 模拟时间序列数据
    const months = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    
    // 模拟各月数据
    const monthlyData = {
      normal: months.map(() => Math.floor(Math.random() * 20) + 30),
      warning: months.map(() => Math.floor(Math.random() * 10) + 5),
      error: months.map(() => Math.floor(Math.random() * 5) + 1)
    }
    
    return {
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
          label: {
            backgroundColor: '#6a7985'
          }
        }
      },
      legend: {
        data: ['正常', '维护中', '故障'],
        top: 10
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        top: '20%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: months
      },
      yAxis: {
        type: 'value',
        name: '数量'
      },
      series: [
        {
          name: '正常',
          type: 'line',
          smooth: true,
          lineStyle: {
            color: '#52c41a',
            width: 3
          },
          symbol: 'circle',
          symbolSize: 8,
          itemStyle: {
            color: '#52c41a'
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(82, 196, 26, 0.3)' },
              { offset: 1, color: 'rgba(82, 196, 26, 0.1)' }
            ])
          },
          data: monthlyData.normal
        },
        {
          name: '维护中',
          type: 'line',
          smooth: true,
          lineStyle: {
            color: '#faad14',
            width: 3
          },
          symbol: 'circle',
          symbolSize: 8,
          itemStyle: {
            color: '#faad14'
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(250, 173, 20, 0.3)' },
              { offset: 1, color: 'rgba(250, 173, 20, 0.1)' }
            ])
          },
          data: monthlyData.warning
        },
        {
          name: '故障',
          type: 'line',
          smooth: true,
          lineStyle: {
            color: '#ff4d4f',
            width: 3
          },
          symbol: 'circle',
          symbolSize: 8,
          itemStyle: {
            color: '#ff4d4f'
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(255, 77, 79, 0.3)' },
              { offset: 1, color: 'rgba(255, 77, 79, 0.1)' }
            ])
          },
          data: monthlyData.error
        }
      ]
    }
  }
  
  // 处理窗口大小变化
  const handleResize = () => {
    if (chartInstance) {
      chartInstance.resize()
    }
  }
  
  // 监听图表类型变化
  watch(chartType, () => {
    initChart()
  })
  
  // 监听数据变化
  watch(() => props.hydrants, () => {
    if (chartInstance) {
      initChart()
    }
  }, { deep: true })
  
  // 组件挂载和卸载
  onMounted(() => {
    nextTick(() => {
      initChart()
    })
  })
  
  onUnmounted(() => {
    if (chartInstance) {
      chartInstance.dispose()
      chartInstance = null
    }
    window.removeEventListener('resize', handleResize)
  })
  
  // 暴露刷新方法
  const refreshChart = () => {
    initChart()
  }
  
  defineExpose({ refreshChart })
  </script>
  
  <style scoped>
  .hydrant-chart {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  }
  
  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  .chart-header h3 {
    margin: 0;
    color: #333;
    font-size: 18px;
    font-weight: 600;
  }
  
  .chart-controls {
    display: flex;
    gap: 10px;
  }
  
  .chart-container {
    height: 400px;
    margin-bottom: 20px;
  }
  
  .chart {
    width: 100%;
    height: 100%;
  }
  
  .chart-stats {
    margin-top: 20px;
    border-top: 1px solid #e8e8e8;
    padding-top: 20px;
  }
  
  .stat-card {
    background: #f8f9fa;
    padding: 20px;
    border-radius: 8px;
    text-align: center;
    border-left: 4px solid #1890ff;
  }
  
  .stat-card.normal {
    border-left-color: #52c41a;
  }
  
  .stat-card.warning {
    border-left-color: #faad14;
  }
  
  .stat-value {
    font-size: 24px;
    font-weight: bold;
    color: #333;
    margin-bottom: 8px;
  }
  
  .stat-label {
    color: #666;
    font-size: 14px;
  }
  
  /* 响应式设计 */
  @media (max-width: 768px) {
    .chart-header {
      flex-direction: column;
      gap: 10px;
      align-items: flex-start;
    }
    
    .chart-controls {
      width: 100%;
    }
    
    .chart-container {
      height: 300px;
    }
    
    .stat-card {
      padding: 15px;
    }
    
    .stat-value {
      font-size: 20px;
    }
  }
  </style>