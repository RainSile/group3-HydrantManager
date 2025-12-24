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
              <el-dropdown-item command="profile">更改密码</el-dropdown-item>
              <el-dropdown-item command="settings">用户管理</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 修改密码模态框 -->
    <el-dialog
        v-model="passwordDialogVisible"
        title="修改密码"
        width="400px"
        :before-close="handlePasswordDialogClose"
    >
      <el-form
          :model="passwordForm"
          :rules="passwordRules"
          ref="passwordFormRef"
          label-width="80px"
      >
        <el-form-item label="新密码" prop="pwd">
          <el-input
              v-model="passwordForm.pwd"
              type="password"
              placeholder="请输入新密码"
              show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPwd">
          <el-input
              v-model="passwordForm.confirmPwd"
              type="password"
              placeholder="请再次输入新密码"
              show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="passwordDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handlePasswordSubmit" :loading="passwordLoading">
            确认修改
          </el-button>
        </span>
      </template>
    </el-dialog>

    

    <!-- 用户管理模态框 -->
    <el-dialog
        v-model="userManagementDialogVisible"
        title="用户管理"
        width="80%"
        style="max-width: 1000px;"
        :before-close="handleUserManagementDialogClose"
        align-center
    >
      <div class="user-management">
        <div class="user-toolbar">
          <el-button type="primary" @click="handleAddUser" :icon="Plus">
            新增用户
          </el-button>
          <div class="search-area">
            <el-input
                v-model="userQuery.username"
                placeholder="搜索用户名"
                style="width: 180px;"
                clearable
            />
            <el-select
                v-model="userQuery.priv"
                placeholder="权限筛选"
                style="width: 120px;"
                clearable
            >
              <el-option label="普通用户" :value="0" />
              <el-option label="管理员" :value="1" />
            </el-select>
            <el-select
                v-model="userQuery.status"
                placeholder="状态筛选"
                style="width: 120px;"
                clearable
            >
              <el-option label="禁用" :value="0" />
              <el-option label="启用" :value="1" />
            </el-select>
            <el-button type="primary" @click="loadUserList" :icon="Search">
              搜索
            </el-button>
          </div>
        </div>

        <div class="table-container">
          <el-table
              :data="userList"
              v-loading="userLoading"
              style="width: 100%;"
          >
            <el-table-column prop="id" label="用户ID" width="220" align="center" />
            <el-table-column prop="username" label="用户名" width="120" align="center" />
            <el-table-column prop="phone" label="联系电话" width="150" align="center" />
            <el-table-column prop="priv" label="权限" width="120" align="center">
              <template #default="{ row }">
                <el-tag :type="row.priv === 1 ? 'danger' : 'primary'">
                  {{ row.priv === 1 ? '管理员' : '普通用户' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                  {{ row.status === 1 ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" align="center">
              <template #default="{ row }">
                <el-button
                    size="small"
                    :type="row.status === 1 ? 'danger' : 'success'"
                    @click="handleToggleUserStatus(row)"
                    :icon="Switch"
                >
                  {{ row.status === 1 ? '禁用' : '启用' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-container">
          <el-pagination
              v-model:current-page="userQuery.pageIndex"
              v-model:page-size="userQuery.pageSize"
              :page-sizes="[10, 20, 50, 100]"
              :total="userTotal"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </el-dialog>

    <!-- 新增用户模态框 -->
    <el-dialog
        v-model="userFormDialogVisible"
        title="新增用户"
        width="500px"
        align-center
    >
      <el-form
          :model="userForm"
          :rules="userFormRules"
          ref="userFormRef"
          label-width="100px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
              v-model="userForm.username"
              placeholder="请输入用户名"
          />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
              v-model="userForm.password"
              type="password"
              placeholder="请输入密码"
              show-password
          />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input
              v-model="userForm.phone"
              placeholder="请输入联系电话"
          />
        </el-form-item>
        <el-form-item label="权限" prop="priv">
          <el-radio-group v-model="userForm.priv">
            <el-radio :label="0">普通用户</el-radio>
            <el-radio :label="1">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="userFormDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUserFormSubmit" :loading="userFormLoading">
            创建
          </el-button>
        </span>
      </template>
    </el-dialog>
  </header>
</template>

<script setup>
import { defineProps, defineEmits, ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Switch } from '@element-plus/icons-vue'
import { userApi } from '../api/hydrantApi.js'

const props = defineProps({
  username: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['logout'])

// 修改密码相关
const passwordDialogVisible = ref(false)
const passwordLoading = ref(false)
const passwordFormRef = ref()
const passwordForm = reactive({
  pwd: '',
  confirmPwd: ''
})

const passwordRules = {
  pwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPwd: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.pwd) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 用户管理相关
const userManagementDialogVisible = ref(false)
const userFormDialogVisible = ref(false)
const userLoading = ref(false)
const userFormLoading = ref(false)
const userFormRef = ref()

const userQuery = reactive({
  pageIndex: 1,
  pageSize: 10,
  username: '',
  phone: '',
  priv: null,
  status: null
})

const userList = ref([])
const userTotal = ref(0)

const userForm = reactive({
  username: '',
  password: '',
  phone: '',
  priv: 0,
  status: 1
})

const userFormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' }
  ]
}

const handleCommand = (command) => {
  if (command === 'logout') {
    emit('logout')
  } else if (command === 'profile') {
    passwordDialogVisible.value = true
  } else if (command === 'settings') {
    userManagementDialogVisible.value = true
    loadUserList()
  }
}

const handlePasswordDialogClose = () => {
  passwordDialogVisible.value = false
  passwordFormRef.value?.resetFields()
}

const handlePasswordSubmit = async () => {
  if (!passwordFormRef.value) return
  try {
    await passwordFormRef.value.validate()

    if (!passwordForm.pwd || passwordForm.pwd.trim() === '') {
      ElMessage.error('密码不能为空')
      return
    }

    passwordLoading.value = true

    const response = await userApi.updatePassword({
      pwd: String(passwordForm.pwd).trim()
    })

    if (response.code === 10000) {
      ElMessage.success('密码修改成功')
      passwordDialogVisible.value = false
      passwordFormRef.value.resetFields()
    } else {
      ElMessage.error(response.message || '密码修改失败')
    }
  } catch (error) {
    console.error('修改密码失败:', error)
    ElMessage.error('密码修改失败')
  } finally {
    passwordLoading.value = false
  }
}

const loadUserList = async () => {
  try {
    userLoading.value = true
    const response = await userApi.listUsers(userQuery)

    if (response.code === 10000) {
      userList.value = response.data?.records || []
      userTotal.value = response.data?.total || 0
    } else {
      ElMessage.error(response.message || '获取用户列表失败')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  } finally {
    userLoading.value = false
  }
}

const handleUserManagementDialogClose = () => {
  userManagementDialogVisible.value = false
  resetUserQuery()
}

const resetUserQuery = () => {
  userQuery.pageIndex = 1
  userQuery.pageSize = 10
  userQuery.username = ''
  userQuery.phone = ''
  userQuery.priv = null
  userQuery.status = null
}

const handleAddUser = () => {
  Object.assign(userForm, {
    username: '',
    password: '',
    phone: '',
    priv: 0,
    status: 1
  })
  userFormDialogVisible.value = true
}

const handleToggleUserStatus = async (user) => {
  try {
    await ElMessageBox.confirm(
        `确定要${user.status === 1 ? '禁用' : '启用'}用户 ${user.username} 吗？`,
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    const response = await userApi.updateStatus(user.status === 1 ? 0 : 1, user.id)

    if (response.code === 10000) {
      ElMessage.success('操作成功')
      loadUserList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const handleUserFormSubmit = async () => {
  if (!userFormRef.value) return

  try {
    await userFormRef.value.validate()
    userFormLoading.value = true

    const response = await userApi.addUser(userForm)

    if (response.code === 10000) {
      ElMessage.success('用户创建成功')
      userFormDialogVisible.value = false
      loadUserList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    userFormLoading.value = false
  }
}

const handleSizeChange = (size) => {
  userQuery.pageSize = size
  loadUserList()
}

const handleCurrentChange = (page) => {
  userQuery.pageIndex = page
  loadUserList()
}
</script>

<style scoped>
.app-header {
  background: linear-gradient(135deg, #3498db 0%, #1a5276 100%);
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

.user-management {
  padding: 10px 0;
}

.user-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 20px;
}

.search-area {
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: center;
  flex: 1;
}

.table-container {
  width: 100%;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 10px 0;
  width: 100%;
}

:deep(.el-table th > .cell),
:deep(.el-table .cell) {
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
}

:deep(.el-table th) {
  text-align: center;
}
</style>