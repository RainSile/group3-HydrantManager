<template>
  <div class="add-hydrant">
    <el-dialog
        v-model="dialogVisible"
        title="添加消防栓"
        width="600px"
        :before-close="handleClose"
    >
      <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-width="100px"
          class="hydrant-form"
      >
        <el-form-item label="位置描述" prop="location">
          <el-input
              v-model="formData.location"
              placeholder="请输入消防栓详细位置描述"
              maxlength="100"
              show-word-limit
          />
        </el-form-item>

        <div class="coordinate-section">
          <el-form-item label="经度" prop="longitude">
            <el-input-number
                v-model="formData.longitude"
                :precision="6"
                :step="0.000001"
                :min="-180"
                :max="180"
                placeholder="请输入经度"
            />
          </el-form-item>

          <el-form-item label="纬度" prop="latitude">
            <el-input-number
                v-model="formData.latitude"
                :precision="6"
                :step="0.000001"
                :min="-90"
                :max="90"
                placeholder="请输入纬度"
            />
          </el-form-item>
        </div>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio-button label="0">正常</el-radio-button>
            <el-radio-button label="1">维护中</el-radio-button>
            <el-radio-button label="2">故障</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <div class="monitoring-section">
          <el-form-item label="当前水压" prop="pressure">
            <el-input-number
                v-model="formData.pressure"
                :precision="2"
                :step="0.01"
                :min="0"
                :max="10"
                placeholder="MPa"
            >
              <template #append>MPa</template>
            </el-input-number>
          </el-form-item>

          <el-form-item label="当前流速" prop="flowRate">
            <el-input-number
                v-model="formData.flowRate"
                :precision="2"
                :step="0.01"
                :min="0"
                :max="50"
                placeholder="L/S"
            >
              <template #append>L/S</template>
            </el-input-number>
          </el-form-item>
        </div>

        <el-form-item label="备注信息">
          <el-input
              v-model="formData.data"
              type="textarea"
              :rows="3"
              placeholder="请输入消防栓的备注信息（可选）"
              maxlength="200"
              show-word-limit
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleClose">取消</el-button>
          <el-button type="primary" :loading="loading" @click="handleSubmit">
            确认添加
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, nextTick, watch} from 'vue'
import { ElMessage } from 'element-plus'
import { hydrantApi } from '@/api/hydrantApi.js'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

const dialogVisible = ref(false)
const loading = ref(false)
const formRef = ref()

const formData = reactive({
  location: '',
  longitude: null,
  latitude: null,
  status: '0',
  pressure: null,
  flowRate: null,
  data: ''
})

const formRules = {
  location: [
    { required: true, message: '请输入位置描述', trigger: 'blur' },
    { min: 5, message: '位置描述至少5个字符', trigger: 'blur' }
  ],
  longitude: [
    { required: true, message: '请输入经度', trigger: 'blur' }
  ],
  latitude: [
    { required: true, message: '请输入纬度', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

watch(() => props.modelValue, (newVal) => {
  dialogVisible.value = newVal
  if (newVal) {
    resetForm()
  }
})

watch(dialogVisible, (newVal) => {
  emit('update:modelValue', newVal)
})

const handleClose = () => {
  dialogVisible.value = false
}

const resetForm = () => {
  nextTick(() => {
    Object.assign(formData, {
      location: '',
      longitude: null,
      latitude: null,
      status: '0',
      pressure: null,
      flowRate: null,
      data: ''
    })
    formRef.value?.clearValidate()
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    loading.value = true

    const submitData = {
      location: formData.location,
      longitude: formData.longitude,
      latitude: formData.latitude,
      status: parseInt(formData.status),
      pressure: formData.pressure,
      flowRate: formData.flowRate,
      data: formData.data || null
    }

    const response = await hydrantApi.addHydrant(submitData)

    if (response.code === 10000) {
      emit('success')
      handleClose()
    } else {
      ElMessage.error(response.message || '添加失败')
    }
  } catch (error) {
    console.error('添加消防栓失败:', error)
    ElMessage.error('添加失败，请重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.hydrant-form {
  padding: 0 20px;
}

.coordinate-section,
.monitoring-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 20px;
}

:deep(.el-input-number) {
  width: 100%;
}

:deep(.el-textarea) {
  width: 100%;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

@media (max-width: 768px) {
  .coordinate-section,
  .monitoring-section {
    grid-template-columns: 1fr;
    gap: 0;
  }

  :deep(.el-dialog) {
    width: 90% !important;
    margin: 5% auto !important;
  }
}
</style>