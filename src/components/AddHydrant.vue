<template>
  <div class="add-hydrant">
    <!-- 添加消防栓弹窗 -->
    <el-dialog
        v-model="dialogVisible"
        title="添加消防栓"
        width="600px"
        :before-close="handleClose"
    >
      <!-- 表单区域 -->
      <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-width="100px"
          class="hydrant-form"
      >
        <!-- 位置描述 - 必填 -->
        <el-form-item label="位置描述" prop="location">
          <el-input
              v-model="formData.location"
              placeholder="请输入消防栓详细位置描述"
              maxlength="100"
              show-word-limit
          />
        </el-form-item>

        <!-- 经纬度坐标区域 -->
        <div class="coordinate-section">
          <!-- 经度 - 必填，不能为0 -->
          <el-form-item label="经度" prop="longitude">
            <el-input-number
                v-model="formData.longitude"
                :precision="6"
                :step="0.000001"
                :min="-180"
                :max="180"
                placeholder="请输入经度"
            />
            <div class="coordinate-tip">经度不能为0</div>
          </el-form-item>

          <!-- 纬度 - 必填，不能为0 -->
          <el-form-item label="纬度" prop="latitude">
            <el-input-number
                v-model="formData.latitude"
                :precision="6"
                :step="0.000001"
                :min="-90"
                :max="90"
                placeholder="请输入纬度"
            />
            <div class="coordinate-tip">纬度不能为0</div>
          </el-form-item>
        </div>

        <!-- 状态选择 - 必填 -->
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio-button label="0">正常</el-radio-button>
            <el-radio-button label="1">维护中</el-radio-button>
            <el-radio-button label="2">故障</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <!-- 监测数据区域 -->
        <div class="monitoring-section">
          <!-- 当前水压 -->
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

          <!-- 当前流速 -->
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

        <!-- 备注信息 - 可选 -->
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

      <!-- 弹窗底部操作按钮 -->
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
import { ref, reactive, nextTick, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { hydrantApi } from '@/api/hydrantApi.js'

// 组件属性定义 - 控制弹窗显示
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

// 组件事件定义
const emit = defineEmits(['update:modelValue', 'success'])

// 响应式数据
const dialogVisible = ref(false)  // 弹窗显示状态
const loading = ref(false)        // 提交加载状态
const formRef = ref()             // 表单引用

// 表单数据
const formData = reactive({
  location: '',     // 位置描述
  longitude: null,  // 经度
  latitude: null,   // 纬度
  status: '0',      // 状态：0-正常, 1-维护中, 2-故障
  pressure: null,   // 水压
  flowRate: null,   // 流速
  data: ''          // 备注信息
})

/**
 * 经纬度验证规则 - 不能为0
 */
const validateLongitude = (rule, value, callback) => {
  if (value === null || value === undefined || value === '') {
    callback(new Error('请输入经度'))
  } else if (value === 0) {
    callback(new Error('经度不能为0'))
  } else {
    callback()
  }
}

const validateLatitude = (rule, value, callback) => {
  if (value === null || value === undefined || value === '') {
    callback(new Error('请输入纬度'))
  } else if (value === 0) {
    callback(new Error('纬度不能为0'))
  } else {
    callback()
  }
}

// 表单验证规则
const formRules = {
  location: [
    { required: true, message: '请输入位置描述', trigger: 'blur' },
    { min: 5, message: '位置描述至少5个字符', trigger: 'blur' }
  ],
  longitude: [
    { required: true, validator: validateLongitude, trigger: 'blur' }
  ],
  latitude: [
    { required: true, validator: validateLatitude, trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 监听父组件传递的modelValue变化
watch(() => props.modelValue, (newVal) => {
  dialogVisible.value = newVal
  if (newVal) {
    resetForm()  // 打开弹窗时重置表单
  }
})

// 监听dialogVisible变化，同步到父组件
watch(dialogVisible, (newVal) => {
  emit('update:modelValue', newVal)
})

/**
 * 关闭弹窗处理
 */
const handleClose = () => {
  dialogVisible.value = false
}

/**
 * 重置表单数据
 */
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
    formRef.value?.clearValidate()  // 清除验证状态
  })
}

/**
 * 提交表单处理
 */
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    // 表单验证
    const valid = await formRef.value.validate()
    if (!valid) return

    // 额外检查经纬度不能为0
    if (formData.longitude === 0) {
      ElMessage.error('经度不能为0')
      return
    }
    if (formData.latitude === 0) {
      ElMessage.error('纬度不能为0')
      return
    }

    loading.value = true

    // 构造提交数据
    const submitData = {
      location: formData.location,
      longitude: formData.longitude,
      latitude: formData.latitude,
      status: parseInt(formData.status),
      pressure: formData.pressure,
      flowRate: formData.flowRate,
      data: formData.data || null
    }

    // 调用API添加消防栓
    const response = await hydrantApi.addHydrant(submitData)

    if (response.code === 10000) {
      ElMessage.success('添加成功')
      emit('success')    // 触发成功事件
      handleClose()      // 关闭弹窗
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

/* 坐标和监测数据区域网格布局 */
.coordinate-section,
.monitoring-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 20px;
}

/* 经纬度提示文字 */
.coordinate-tip {
  font-size: 12px;
  color: #ff4d4f;
  margin-top: 4px;
}

/* 输入框宽度设置 */
:deep(.el-input-number) {
  width: 100%;
}

:deep(.el-textarea) {
  width: 100%;
}

/* 底部按钮布局 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 移动端响应式布局 */
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