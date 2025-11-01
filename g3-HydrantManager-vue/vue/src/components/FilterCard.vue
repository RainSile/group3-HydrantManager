<template>
  <div class="filter-card">
    <div class="card-title">
      <el-icon><Filter /></el-icon>
      <span>筛选条件</span>
    </div>
    <el-form label-width="80px">
      <el-form-item label="状态">
        <el-select
            :model-value="filterStatus"
            @update:model-value="$emit('update:filter-status', $event)"
            placeholder="全部状态"
            clearable
        >
          <el-option label="正常" value="normal" />
          <el-option label="维护中" value="warning" />
          <el-option label="故障" value="error" />
        </el-select>
      </el-form-item>
      <el-form-item label="区域">
        <el-select
            :model-value="filterArea"
            @update:model-value="$emit('update:filter-area', $event)"
            placeholder="全部区域"
            clearable
        >
          <el-option
              v-for="area in areaOptions"
              :key="area.value"
              :label="area.label"
              :value="area.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="最近检查">
        <el-date-picker
            :model-value="filterDate"
            @update:model-value="$emit('update:filter-date', $event)"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { areaOptions } from '../data/hydrants.js'

export default {
  name: 'FilterCard',
  props: {
    filterStatus: String,
    filterArea: String,
    filterDate: [String, Array]
  },
  emits: ['update:filter-status', 'update:filter-area', 'update:filter-date'],
  data() {
    return {
      areaOptions
    }
  }
}
</script>

<style scoped>
.filter-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #ff4d4f;
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>