<template>
  <div v-if="hydrant" class="hydrant-detail-card">
    <div class="card-header">
      <h3>消防栓详情</h3>
      <div class="header-actions">
        <el-tag :type="getStatusType(hydrant.status)">
          {{ getStatusText(hydrant.status) }}
        </el-tag>
        <div class="action-buttons">
          <el-button
              type="primary"
              size="small"
              @click="handleEdit"
              :loading="loading"
          >
            <el-icon><Edit /></el-icon>
            编辑
          </el-button>
          <el-button
              type="danger"
              size="small"
              @click="handleDelete"
              :loading="deleteLoading"
          >
            <el-icon><Delete /></el-icon>
            删除
          </el-button>
          <el-button
              text
              size="small"
              class="close-button"
              @click="handleClose"
          >
            <el-icon><Close /></el-icon>
          </el-button>
        </div>
      </div>
    </div>

    <div class="section">
      <h4>基本信息</h4>
      <div class="info-grid">
        <div class="info-item">
          <label>消防栓ID：</label>
          <span>{{ hydrant.id }}</span>
        </div>
        <div class="info-item">
          <label>位置：</label>
          <span>{{ hydrant.location || '暂无位置信息' }}</span>
        </div>
        <div class="info-item">
          <label>坐标：</label>
          <span v-if="hydrant.longitude && hydrant.latitude">
            {{ hydrant.longitude.toFixed(6) }}, {{ hydrant.latitude.toFixed(6) }}
          </span>
          <span v-else>暂无坐标信息</span>
        </div>
      </div>
    </div>

    <div v-if="hasMonitoringData" class="section">
      <h4>监测数据</h4>
      <div class="info-grid">
        <div v-if="hydrant.pressure" class="info-item">
          <label>当前水压：</label>
          <div class="data-value">
            {{ hydrant.pressure }} MPa
            <el-tag
                :type="getPressureTagType(hydrant.pressure)"
                size="small"
            >
              {{ getPressureLevel(hydrant.pressure) }}
            </el-tag>
          </div>
        </div>
        <div v-if="hydrant.flowRate" class="info-item">
          <label>当前流速：</label>
          <div class="data-value">
            {{ hydrant.flowRate }} L/S
            <el-tag
                :type="getFlowRateTagType(hydrant.flowRate)"
                size="small"
            >
              {{ getFlowRateLevel(hydrant.flowRate) }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>

    <div v-if="hasTimeInfo" class="section">
      <h4>时间信息</h4>
      <div class="info-grid">
        <div v-if="hydrant.createTime" class="info-item">
          <label>创建时间：</label>
          <span>{{ formatTime(hydrant.createTime) }}</span>
        </div>
        <div v-if="hydrant.updateTime" class="info-item">
          <label>更新时间：</label>
          <span>{{ formatTime(hydrant.updateTime) }}</span>
        </div>
      </div>
    </div>

    <div v-if="hydrant.data" class="section">
      <h4>备注信息</h4>
      <div class="remark-content">
        {{ hydrant.data }}
      </div>
    </div>

    <el-dialog
        v-model="editDialogVisible"
        title="编辑消防栓信息"
        width="500px"
        :before-close="handleEditClose"
    >
      <el-form
          ref="editFormRef"
          :model="editFormData"
          :rules="editFormRules"
          label-width="100px"
      >
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="editFormData.status">
            <el-radio-button label="0">正常</el-radio-button>
            <el-radio-button label="1">维护中</el-radio-button>
            <el-radio-button label="2">故障</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="当前水压">
          <el-input-number
              v-model="editFormData.pressure"
              :precision="2"
              :step="0.01"
              :min="0"
              :max="10"
              placeholder="MPa"
          >
            <template #append>MPa</template>
          </el-input-number>
        </el-form-item>

        <el-form-item label="当前流速">
          <el-input-number
              v-model="editFormData.flowRate"
              :precision="2"
              :step="0.01"
              :min="0"
              :max="50"
              placeholder="L/S"
          >
            <template #append>L/S</template>
          </el-input-number>
        </el-form-item>

        <el-form-item label="备注信息">
          <el-input
              v-model="editFormData.data"
              type="textarea"
              :rows="3"
              placeholder="请输入消防栓的备注信息"
              maxlength="200"
              show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleEditClose">取消</el-button>
          <el-button
              type="primary"
              :loading="editLoading"
              @click="handleEditSubmit"
          >
            确认修改
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>

  <div v-else class="no-selection">
    <el-empty description="请选择消防栓查看详情" />
  </div>
</template>

<script setup>
import { computed, ref, reactive, nextTick } from 'vue'
import { Close, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { hydrantApi } from '@/api/hydrantApi.js'

const props = defineProps({
  hydrant: Object
})

const emit = defineEmits(['close', 'refresh', 'delete-success'])

const loading = ref(false)
const deleteLoading = ref(false)
const editLoading = ref(false)
const editDialogVisible = ref(false)
const editFormRef = ref()

const editFormData = reactive({
  status: '0',
  pressure: null,
  flowRate: null,
  data: ''
})

const editFormRules = {
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

const hasMonitoringData = computed(() => {
  return props.hydrant && (props.hydrant.pressure || props.hydrant.flowRate)
})

const hasTimeInfo = computed(() => {
  return props.hydrant && (props.hydrant.createTime || props.hydrant.updateTime)
})

const getStatusText = (status) => {
  const statusMap = {
    '0': '正常',
    '1': '维护中',
    '2': '故障'
  }
  return statusMap[status] || '未知'
}

const getStatusType = (status) => {
  const typeMap = {
    '0': 'success',
    '1': 'warning',
    '2': 'danger'
  }
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

const handleClose = () => {
  emit('close')
}

const handleEdit = () => {
  if (!props.hydrant) {
    ElMessage.error('消防栓数据不存在')
    return
  }

  if (!props.hydrant.id) {
    ElMessage.error('消防栓ID不存在，无法编辑')
    return
  }

  Object.assign(editFormData, {
    status: props.hydrant.status?.toString() || '0',
    pressure: props.hydrant.pressure || null,
    flowRate: props.hydrant.flowRate || null,
    data: props.hydrant.data || ''
  })

  editDialogVisible.value = true

  nextTick(() => {
    editFormRef.value?.clearValidate()
  })
}

const handleEditClose = () => {
  editDialogVisible.value = false
}

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

    console.log('提交的更新数据:', filteredData)

    const response = await hydrantApi.changeHydrant(filteredData)

    if (response.code === 10000) {
      ElMessage.success('消防栓信息更新成功')
      editDialogVisible.value = false
      emit('refresh')
      emit('update-success')
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
        {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning',
          confirmButtonClass: 'el-button--danger'
        }
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
    if (error === 'cancel') {
      return
    }
    console.error('删除消防栓失败:', error)
    ElMessage.error('删除失败，请重试')
  } finally {
    deleteLoading.value = false
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
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e8e8e8;
}

.card-header h3 {
  margin: 0;
  color: #333;
  font-size: 16px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.action-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
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

.section {
  margin-bottom: 20px;
}

.section h4 {
  margin: 0 0 12px 0;
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
}

.data-value {
  display: flex;
  align-items: center;
  gap: 8px;
}

.remark-content {
  padding: 12px;
  background: #f9f9f9;
  border-radius: 4px;
  color: #666;
  line-height: 1.6;
  font-size: 14px;
}

.no-selection {
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  text-align: center;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
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
}
</style>