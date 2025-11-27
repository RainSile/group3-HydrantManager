<template>
  <div v-if="hydrant" class="hydrant-detail-card">
    <div class="card-header">
      <h3>消防栓详情</h3>
      <div class="header-actions">
        <el-tag :type="getStatusType(hydrant.status)">
          {{ getStatusText(hydrant.status) }}
        </el-tag>
        <div class="action-buttons">
          <el-button type="primary" size="small" @click="handleEdit" :loading="loading">
            <el-icon><Edit /></el-icon>编辑
          </el-button>
          <el-button type="danger" size="small" @click="handleDelete" :loading="deleteLoading">
            <el-icon><Delete /></el-icon>删除
          </el-button>
          <el-button text size="small" class="close-button" @click="handleClose">
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
      </div>
    </div>

    <!-- 标签页 -->
    <el-tabs v-model="activeTab" class="detail-tabs">
      <!-- 基本信息 -->
      <el-tab-pane label="基本信息" name="basic">
        <div class="section">
          <h4>基本信息</h4>
          <div class="info-grid">
            <div class="info-item"><label>消防栓ID：</label><span>{{ hydrant.id }}</span></div>
            <div class="info-item"><label>位置：</label><span>{{ hydrant.location || '暂无位置信息' }}</span></div>
            <div class="info-item">
              <label>坐标：</label>
              <span v-if="hydrant.longitude && hydrant.latitude">
                {{ hydrant.longitude.toFixed(6) }}, {{ hydrant.latitude.toFixed(6) }}
              </span>
              <span v-else>暂无坐标信息</span>
            </div>
          </div>
        </div>

        <!-- 监测数据 -->
        <div v-if="hasMonitoringData" class="section">
          <h4>监测数据</h4>
          <div class="info-grid">
            <div v-if="hydrant.pressure" class="info-item">
              <label>当前水压：</label>
              <div class="data-value">
                {{ hydrant.pressure }} MPa
                <el-tag :type="getPressureTagType(hydrant.pressure)" size="small">
                  {{ getPressureLevel(hydrant.pressure) }}
                </el-tag>
              </div>
            </div>
            <div v-if="hydrant.flowRate" class="info-item">
              <label>当前流速：</label>
              <div class="data-value">
                {{ hydrant.flowRate }} L/S
                <el-tag :type="getFlowRateTagType(hydrant.flowRate)" size="small">
                  {{ getFlowRateLevel(hydrant.flowRate) }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>

        <!-- 时间信息 -->
        <div v-if="hasTimeInfo" class="section">
          <h4>时间信息</h4>
          <div class="info-grid">
            <div v-if="hydrant.createTime" class="info-item">
              <label>创建时间：</label><span>{{ formatTime(hydrant.createTime) }}</span>
            </div>
            <div v-if="hydrant.updateTime" class="info-item">
              <label>更新时间：</label><span>{{ formatTime(hydrant.updateTime) }}</span>
            </div>
          </div>
        </div>

        <!-- 备注信息 -->
        <div v-if="hydrant.data" class="section">
          <h4>备注信息</h4>
          <div class="remark-content">{{ hydrant.data }}</div>
        </div>
      </el-tab-pane>

      <!-- 维修管理 -->
      <el-tab-pane label="维修管理" name="maintenance">
        <div class="section">
          <div class="section-header">
            <h4>维修任务管理</h4>
            <el-button type="primary" size="small" @click="handleAddMaintenance" :loading="maintenanceLoading">
              <el-icon><Plus /></el-icon>新建维修任务
            </el-button>
          </div>

          <div class="maintenance-list">
            <!-- 维修任务列表 -->
            <div v-for="maintenance in maintenanceList" :key="maintenance.id" class="maintenance-item">
              <div class="maintenance-header" @click="toggleMaintenance(maintenance.id)">
                <div class="maintenance-info">
                  <span class="maintenance-id">任务ID: {{ maintenance.id }}</span>
                  <span class="repair-person">维修人员: {{ maintenance.uid }}</span>
                  <el-tag type="warning" size="small">进行中</el-tag>
                </div>
                <div class="maintenance-actions">
                  <el-button type="danger" size="small" @click.stop="handleDeleteMaintenance(maintenance)"
                             :loading="deleteMaintenanceLoading === maintenance.id" class="delete-task-btn">
                    删除任务
                  </el-button>
                  <el-icon class="expand-icon" :class="{ 'expanded': expandedMaintenance === maintenance.id }">
                    <ArrowDown />
                  </el-icon>
                </div>
              </div>

              <!-- 日志区域 -->
              <div v-if="expandedMaintenance === maintenance.id" class="log-section">
                <div class="log-header">
                  <h5>维修日志</h5>
                  <el-button type="primary" size="small" @click="handleAddLog(maintenance.id)">
                    <el-icon><Plus /></el-icon>添加日志
                  </el-button>
                </div>

                <div class="log-list">
                  <div v-for="log in getLogsByMaintenance(maintenance.id)" :key="log.id" class="log-item">
                    <div class="log-content">
                      <div class="log-text">{{ log.record }}</div>
                      <div class="log-time">{{ formatTime(log.updateTime) }}</div>
                    </div>
                    <el-button type="danger" size="small" @click="handleDeleteLog(log.id)"
                               :loading="deleteLogLoading === log.id">删除</el-button>
                  </div>

                  <div v-if="getLogsByMaintenance(maintenance.id).length === 0" class="no-logs">
                    暂无维修日志
                  </div>
                </div>
              </div>
            </div>

            <div v-if="maintenanceList.length === 0 && !maintenanceLoading" class="no-data">
              <el-empty description="暂无维修任务" />
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 编辑对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑消防栓信息" width="500px" :before-close="handleEditClose">
      <el-form ref="editFormRef" :model="editFormData" :rules="editFormRules" label-width="100px">
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="editFormData.status">
            <el-radio-button label="0">正常</el-radio-button>
            <el-radio-button label="1">维护中</el-radio-button>
            <el-radio-button label="2">故障</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="当前水压">
          <el-input-number v-model="editFormData.pressure" :precision="2" :step="0.01" :min="0" :max="10" placeholder="MPa">
            <template #append>MPa</template>
          </el-input-number>
        </el-form-item>

        <el-form-item label="当前流速">
          <el-input-number v-model="editFormData.flowRate" :precision="2" :step="0.01" :min="0" :max="50" placeholder="L/S">
            <template #append>L/S</template>
          </el-input-number>
        </el-form-item>

        <el-form-item label="备注信息">
          <el-input v-model="editFormData.data" type="textarea" :rows="3" placeholder="请输入消防栓的备注信息" maxlength="200"
                    show-word-limit />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleEditClose">取消</el-button>
          <el-button type="primary" :loading="editLoading" @click="handleEditSubmit">确认修改</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 新建维修任务对话框 -->
    <el-dialog v-model="maintenanceDialogVisible" title="新建维修任务" width="400px">
      <el-form :model="maintenanceForm" label-width="100px">
        <el-form-item label="维修人员ID" required>
          <el-input v-model="maintenanceForm.uid" placeholder="请输入维修人员ID" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="maintenanceDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="addMaintenanceLoading" @click="handleMaintenanceSubmit">确认创建</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 删除维修任务确认对话框 -->
    <el-dialog v-model="deleteMaintenanceDialogVisible" title="删除维修任务" width="400px" :before-close="handleDeleteMaintenanceClose">
      <div class="delete-confirm-content">
        <el-alert type="warning" title="警告" :closable="false" show-icon>
          确定要删除这个维修任务吗？此操作将永久删除该任务及其所有相关日志，且无法恢复。
        </el-alert>
        <div class="task-info">
          <p><strong>任务ID:</strong> {{ currentMaintenance?.id }}</p>
          <p><strong>维修人员:</strong> {{ currentMaintenance?.uid }}</p>
          <p><strong>相关日志:</strong> {{ getLogsByMaintenance(currentMaintenance?.id).length }} 条</p>
        </div>
        <div class="delete-log-notice">
          <el-alert type="info" :closable="false" show-icon>
            删除维修任务将同时删除所有相关的维修日志记录。
          </el-alert>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDeleteMaintenanceClose" :disabled="deleteMaintenanceLoading">取消</el-button>
          <el-button type="danger" :loading="deleteMaintenanceLoading" @click="handleDeleteMaintenanceConfirm">
            {{ deleteMaintenanceLoading ? '删除中...' : '确认删除' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 添加日志对话框 -->
    <el-dialog v-model="logDialogVisible" title="添加维修日志" width="500px">
      <el-form :model="logForm" label-width="100px">
        <el-form-item label="维修任务ID">
          <el-input v-model="logForm.mid" disabled />
        </el-form-item>
        <el-form-item label="日志内容" required>
          <el-input v-model="logForm.record" type="textarea" :rows="4" placeholder="请输入维修日志内容..." maxlength="500"
                    show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="logDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="addLogLoading" @click="handleLogSubmit">确认添加</el-button>
        </div>
      </template>
    </el-dialog>
  </div>

  <div v-else class="no-selection">
    <el-empty description="请选择消防栓查看详情" />
  </div>
</template>

<script setup>
import { computed, ref, reactive, nextTick, watch } from 'vue'
import { Close, Edit, Delete, Plus, ArrowDown } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { hydrantApi, maintenanceApi, logApi } from '@/api/hydrantApi.js'

const props = defineProps({
  hydrant: Object
})

const emit = defineEmits(['close', 'refresh', 'delete-success'])

// 状态管理
const loading = ref(false)
const deleteLoading = ref(false)
const editLoading = ref(false)
const editDialogVisible = ref(false)
const editFormRef = ref()
const activeTab = ref('basic')

// 维修管理状态
const maintenanceLoading = ref(false)
const maintenanceDialogVisible = ref(false)
const addMaintenanceLoading = ref(false)
const deleteMaintenanceLoading = ref(false)
const deleteMaintenanceDialogVisible = ref(false)
const maintenanceList = ref([])
const expandedMaintenance = ref(null)
const currentMaintenance = ref(null)

// 日志管理状态
const logDialogVisible = ref(false)
const addLogLoading = ref(false)
const deleteLogLoading = ref('')
const logList = ref([])

// 表单数据
const editFormData = reactive({
  status: '0',
  pressure: null,
  flowRate: null,
  data: ''
})

const maintenanceForm = reactive({
  hid: '',
  uid: ''
})

const logForm = reactive({
  mid: '',
  record: ''
})

const editFormRules = {
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 计算属性
const hasMonitoringData = computed(() => props.hydrant && (props.hydrant.pressure || props.hydrant.flowRate))
const hasTimeInfo = computed(() => props.hydrant && (props.hydrant.createTime || props.hydrant.updateTime))

// 方法
const getLogsByMaintenance = (maintenanceId) => logList.value.filter(log => log.mid === maintenanceId)

// 监听消防栓变化
watch(() => props.hydrant, (newHydrant) => {
  if (newHydrant) {
    loadMaintenanceData()
    loadLogData()
  }
})

// 状态相关函数
const getStatusText = (status) => {
  const statusMap = { '0': '正常', '1': '维护中', '2': '故障' }
  return statusMap[status] || '未知'
}

const getStatusType = (status) => {
  const typeMap = { '0': 'success', '1': 'warning', '2': 'danger' }
  return typeMap[status] || 'info'
}

const getPressureTagType = (pressure) => {
  if (pressure < 0.3) return 'danger'
  if (pressure < 0.5) return 'warning'
  return 'success'
}

const getPressureLevel = (pressure) => {
  if (pressure < 0.3) return '过低'
  if (pressure < 0.5) return '偏低'
  return '正常'
}

const getFlowRateTagType = (flowRate) => {
  if (flowRate < 0.5) return 'danger'
  if (flowRate < 1.0) return 'warning'
  return 'success'
}

const getFlowRateLevel = (flowRate) => {
  if (flowRate < 0.5) return '过低'
  if (flowRate < 1.0) return '偏低'
  return '正常'
}

const formatTime = (time) => {
  if (!time) return '-'
  try {
    return new Date(time).toLocaleString('zh-CN')
  } catch {
    return time
  }
}

// 基础操作
const handleClose = () => emit('close')

const handleEdit = () => {
  if (!props.hydrant?.id) {
    ElMessage.error('消防栓数据不存在')
    return
  }

  Object.assign(editFormData, {
    status: props.hydrant.status?.toString() || '0',
    pressure: props.hydrant.pressure || null,
    flowRate: props.hydrant.flowRate || null,
    data: props.hydrant.data || ''
  })

  editDialogVisible.value = true
  nextTick(() => editFormRef.value?.clearValidate())
}

const handleEditClose = () => editDialogVisible.value = false

const handleEditSubmit = async () => {
  if (!editFormRef.value) return

  try {
    const valid = await editFormRef.value.validate()
    if (!valid) return

    editLoading.value = true

    const submitData = {
      id: props.hydrant.id,
      status: parseInt(editFormData.status),
      pressure: editFormData.pressure || 0,
      flowRate: editFormData.flowRate || 0,
      data: editFormData.data || '',
      location: props.hydrant.location || '',
      longitude: props.hydrant.longitude || 0,
      latitude: props.hydrant.latitude || 0
    }

    const filteredData = Object.fromEntries(
        Object.entries(submitData).filter(([_, value]) => value !== null && value !== undefined)
    )

    const response = await hydrantApi.changeHydrant(filteredData)

    if (response.code === 10000) {
      ElMessage.success('消防栓信息更新成功')
      editDialogVisible.value = false
      emit('refresh')
    } else {
      ElMessage.error(response.data)
    }
  } catch (error) {
    console.error('更新消防栓信息失败:', error)
    ElMessage.error('更新失败，请重试')
  } finally {
    editLoading.value = false
  }
}

const handleDelete = async () => {
  if (!props.hydrant) return

  try {
    await ElMessageBox.confirm(
        `确定要删除消防栓 "${props.hydrant.location}" 吗？此操作不可恢复。`,
        '删除确认',
        { confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning' }
    )

    deleteLoading.value = true
    const response = await hydrantApi.removeHydrant(props.hydrant.id)

    if (response.code === 10000) {
      emit('delete-success', props.hydrant.id)
      emit('refresh')
      emit('close')
    } else {
      ElMessage.error(response.data)
    }
  } catch (error) {
    if (error === 'cancel') return
    console.error('删除消防栓失败:', error)
    ElMessage.error('删除失败，请重试')
  } finally {
    deleteLoading.value = false
  }
}

// 维修管理函数
const loadMaintenanceData = async () => {
  if (!props.hydrant?.id) return

  try {
    maintenanceLoading.value = true
    const response = await maintenanceApi.listMaintenance({
      hid: props.hydrant.id,
      pageIndex: 1,
      pageSize: 50
    })

    if (response.code === 10000 && response.data) {
      maintenanceList.value = response.data.records || []
    } else {
      maintenanceList.value = []
    }
  } catch (error) {
    console.error('加载维修数据失败:', error)
    ElMessage.error('加载维修数据失败')
    maintenanceList.value = []
  } finally {
    maintenanceLoading.value = false
  }
}

const loadLogData = async () => {
  if (!props.hydrant?.id) return

  try {
    const response = await logApi.listLog({ pageIndex: 1, pageSize: 100 })
    if (response.code === 10000 && response.data) {
      logList.value = response.data.records || []
    } else {
      logList.value = []
    }
  } catch (error) {
    console.error('加载日志数据失败:', error)
    logList.value = []
  }
}

const toggleMaintenance = (maintenanceId) => {
  expandedMaintenance.value = expandedMaintenance.value === maintenanceId ? null : maintenanceId
}

const handleAddMaintenance = () => {
  if (!props.hydrant?.id) {
    ElMessage.error('请先选择消防栓')
    return
  }

  maintenanceForm.hid = props.hydrant.id
  maintenanceForm.uid = ''
  maintenanceDialogVisible.value = true
}

const handleMaintenanceSubmit = async () => {
  if (!maintenanceForm.uid) {
    ElMessage.error('请输入维修人员ID')
    return
  }

  try {
    addMaintenanceLoading.value = true
    const response = await maintenanceApi.addMaintenance(maintenanceForm)

    if (response.code === 10000) {
      ElMessage.success('维修任务创建成功')
      maintenanceDialogVisible.value = false
      loadMaintenanceData()
    } else {
      ElMessage.error(response.data)
    }
  } catch (error) {
    console.error('创建维修任务失败:', error)
    ElMessage.error('创建维修任务失败')
  } finally {
    addMaintenanceLoading.value = false
  }
}

// 删除维修任务
const handleDeleteMaintenance = (maintenance) => {
  currentMaintenance.value = maintenance
  deleteMaintenanceDialogVisible.value = true
}

const handleDeleteMaintenanceClose = () => {
  if (!deleteMaintenanceLoading.value) {
    deleteMaintenanceDialogVisible.value = false
    currentMaintenance.value = null
  }
}

const handleDeleteMaintenanceConfirm = async () => {
  if (!currentMaintenance.value) return

  try {
    deleteMaintenanceLoading.value = true

    // 强制删除所有相关日志
    const relatedLogs = getLogsByMaintenance(currentMaintenance.value.id)
    for (const log of relatedLogs) {
      try {
        await logApi.removeLog(log.id)
      } catch (error) {
        console.error('删除日志失败:', error)
      }
    }

    // 删除维修任务
    const response = await maintenanceApi.removeMaintenance(currentMaintenance.value.id)

    if (response.code === 10000) {
      ElMessage.success('维修任务及相关日志删除成功')
      deleteMaintenanceDialogVisible.value = false
      currentMaintenance.value = null
      loadMaintenanceData()
      loadLogData()
    } else {
      ElMessage.error('删除维修任务失败：' + response.data)
    }
  } catch (error) {
    console.error('删除维修任务失败:', error)
    ElMessage.error('删除维修任务失败：' + (error.message || '未知错误'))
  } finally {
    deleteMaintenanceLoading.value = false
  }
}

const handleAddLog = (maintenanceId) => {
  logForm.mid = maintenanceId
  logForm.record = ''
  logDialogVisible.value = true
}

const handleLogSubmit = async () => {
  if (!logForm.record) {
    ElMessage.error('请填写日志内容')
    return
  }

  try {
    addLogLoading.value = true
    const response = await logApi.addLog(logForm)

    if (response.code === 10000) {
      ElMessage.success('日志添加成功')
      logDialogVisible.value = false
      loadLogData()
    } else {
      ElMessage.error(response.data)
    }
  } catch (error) {
    console.error('添加日志失败:', error)
    ElMessage.error('添加日志失败')
  } finally {
    addLogLoading.value = false
  }
}

const handleDeleteLog = async (logId) => {
  try {
    await ElMessageBox.confirm('确定要删除这条日志吗？', '删除确认', {
      confirmButtonText: '确定删除', cancelButtonText: '取消', type: 'warning'
    })

    deleteLogLoading.value = logId
    const response = await logApi.removeLog(logId)

    if (response.code === 10000) {
      ElMessage.success('日志删除成功')
      loadLogData()
    } else {
      ElMessage.error(response.data)
    }
  } catch (error) {
    if (error === 'cancel') return
    console.error('删除日志失败:', error)
    ElMessage.error('删除日志失败')
  } finally {
    deleteLogLoading.value = ''
  }
}
</script>

<style scoped>
.hydrant-detail-card {
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  border: 1px solid #e8e8e8;
  overflow: hidden;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e8e8e8;
  flex-wrap: nowrap;
  gap: 12px;
}

.card-header h3 {
  margin: 0;
  color: #333;
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
  flex-shrink: 0;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.action-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: nowrap;
}

.close-button {
  padding: 4px;
  border: none;
  background: none;
  cursor: pointer;
  color: #999;
  border-radius: 4px;
  transition: all 0.3s;
}

.close-button:hover {
  background-color: #f5f5f5;
  color: #666;
}

.detail-tabs {
  margin-top: 10px;
  overflow: hidden;
}

.section {
  margin-bottom: 20px;
  overflow: hidden;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  flex-wrap: wrap;
  gap: 12px;
}

.section h4 {
  margin: 0;
  color: #666;
  font-size: 14px;
  font-weight: 600;
}

.info-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 12px;
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-item label {
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.info-item span {
  color: #333;
  font-size: 14px;
  word-break: break-all;
}

.data-value {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.remark-content {
  padding: 12px;
  background: #f9f9f9;
  border-radius: 4px;
  color: #666;
  line-height: 1.6;
  font-size: 14px;
  word-break: break-word;
}

/* 维修任务列表样式 */
.maintenance-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.maintenance-item {
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  overflow: hidden;
  transition: all 0.3s;
}

.maintenance-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.maintenance-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #fafafa;
  cursor: pointer;
  transition: background-color 0.3s;
}

.maintenance-header:hover {
  background: #f0f0f0;
}

.maintenance-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.maintenance-id {
  font-weight: 600;
  color: #333;
}

.repair-person {
  color: #666;
  font-size: 14px;
}

.maintenance-actions {
  display: flex;
  align-items: center;
  gap: 6px;
}

.delete-task-btn {
  padding: 5px 8px;
  height: 24px;
  font-size: 12px;
}

.maintenance-actions .el-button {
  padding: 5px 8px;
  height: 24px;
  font-size: 12px;
}

.expand-icon {
  transition: transform 0.3s;
  color: #999;
  font-size: 14px;
}

.expand-icon.expanded {
  transform: rotate(180deg);
}

/* 日志区域样式 */
.log-section {
  border-top: 1px solid #e8e8e8;
  background: white;
}

.log-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #f9f9f9;
  border-bottom: 1px solid #e8e8e8;
}

.log-header h5 {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.log-list {
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.log-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 12px;
  background: #fafafa;
  border-radius: 4px;
  border-left: 3px solid #409eff;
}

.log-content {
  flex: 1;
  margin-right: 12px;
}

.log-text {
  color: #333;
  line-height: 1.5;
  margin-bottom: 4px;
}

.log-time {
  color: #999;
  font-size: 12px;
}

.no-logs {
  text-align: center;
  color: #999;
  padding: 20px;
  font-size: 14px;
}

.no-data {
  padding: 40px 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.dialog-footer .el-button {
  min-width: 80px;
}

.no-selection {
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  text-align: center;
}

.delete-confirm-content {
  padding: 10px 0;
}

.task-info {
  margin: 16px 0;
  padding: 12px;
  background: #f5f5f5;
  border-radius: 4px;
}

.task-info p {
  margin: 8px 0;
  color: #666;
}

.delete-log-notice {
  margin-top: 16px;
}

@media (max-width: 768px) {
  .hydrant-detail-card {
    padding: 16px;
  }

  .card-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .header-actions {
    width: 100%;
    justify-content: space-between;
  }

  .action-buttons {
    width: 100%;
    justify-content: flex-end;
  }

  .data-value {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .maintenance-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .maintenance-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .maintenance-actions {
    width: 100%;
    justify-content: space-between;
    gap: 4px;
  }

  .delete-task-btn {
    padding: 4px 6px;
    height: 22px;
    font-size: 11px;
  }

  .maintenance-actions .el-button {
    padding: 4px 6px;
    height: 22px;
    font-size: 11px;
  }

  .log-item {
    flex-direction: column;
    gap: 8px;
  }

  .log-content {
    margin-right: 0;
  }
}
</style>