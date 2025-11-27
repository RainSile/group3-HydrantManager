<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-card">
        <div class="login-header">
          <div class="logo">
            <div class="logo-icon">🔥</div>
            <h1>消防栓查询系统</h1>
          </div>
          <p class="system-desc">Fire Hydrant Management System</p>
        </div>

        <form class="login-form" @submit.prevent="handleLogin">
          <div class="form-group">
            <div class="input-wrapper">
              <span class="input-icon">👤</span>
              <input
                  v-model="loginForm.username"
                  type="text"
                  placeholder="请输入用户名"
                  class="form-input"
                  :class="{ error: errors.username }"
                  @focus="clearError('username')"
              >
            </div>
            <div v-if="errors.username" class="error-message">{{ errors.username }}</div>
          </div>

          <div class="form-group">
            <div class="input-wrapper">
              <span class="input-icon">🔒</span>
              <input
                  v-model="loginForm.password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="请输入密码"
                  class="form-input"
                  :class="{ error: errors.password }"
                  @focus="clearError('password')"
              >
              <button
                  type="button"
                  class="password-toggle"
                  @click="showPassword = !showPassword"
              >
                {{ showPassword ? '🙈' : '👁️' }}
              </button>
            </div>
            <div v-if="errors.password" class="error-message">{{ errors.password }}</div>
          </div>

          <button
              type="submit"
              class="login-button"
              :class="{ loading: loading }"
              :disabled="loading"
          >
            <span v-if="!loading">登录</span>
            <span v-else class="loading-text">
              <span class="spinner"></span>
              登录中...
            </span>
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { login } from '@/api/hydrantApi.js'

const loading = ref(false)
const showPassword = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const errors = reactive({
  username: '',
  password: ''
})

const validateForm = () => {
  let isValid = true
  errors.username = ''
  errors.password = ''

  if (!loginForm.username.trim()) {
    errors.username = '请输入用户名'
    isValid = false
  }

  if (!loginForm.password) {
    errors.password = '请输入密码'
    isValid = false
  }

  return isValid
}

const clearError = (field) => {
  errors[field] = ''
}

const handleLogin = async () => {
  if (!validateForm()) return

  loading.value = true

  try {
    const response = await login({
      username: loginForm.username,
      password: loginForm.password
    })

    if (response.code === 10000) {
      ElMessage.success('登录成功！')

      if (response.data?.token) {
        localStorage.setItem('token', response.data.token)
      }

      const userInfo = {
        username: loginForm.username,
        ...response.data
      }
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
      emit('login-success', userInfo)

    } else {
      ElMessage.error('登录失败,请检查用户名与密码')
    }
  } catch (error) {
    console.error('Login error:', error)
    ElMessage.error('登录失败，请检查用户名和密码')
  } finally {
    loading.value = false
  }
}

const emit = defineEmits(['login-success'])
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #3498db 0%, #1a5276 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background:
      radial-gradient(circle at 20% 80%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
      radial-gradient(circle at 80% 20%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  animation: float 6s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(1deg); }
}

.login-wrapper {
  width: 100%;
  max-width: 400px;
  position: relative;
  z-index: 1;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 40px 30px;
  box-shadow:
      0 20px 40px rgba(0, 0, 0, 0.1),
      0 0 0 1px rgba(255, 255, 255, 0.2);
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 8px;
}

.logo-icon {
  font-size: 32px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

.logo h1 {
  color: #2c3e50;
  font-size: 24px;
  font-weight: 700;
  margin: 0;
}

.system-desc {
  color: #7f8c8d;
  font-size: 14px;
  margin: 0;
  font-weight: 400;
}

.login-form {
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  background: #f8f9fa;
  border: 2px solid #e9ecef;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.input-wrapper:focus-within {
  border-color: #3498db;
  background: white;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

.input-icon {
  padding: 0 15px;
  font-size: 18px;
  color: #6c757d;
}

.form-input {
  flex: 1;
  border: none;
  background: none;
  padding: 15px 0;
  font-size: 16px;
  outline: none;
  color: #2c3e50;
}

.form-input::placeholder {
  color: #adb5bd;
}

.form-input.error {
  color: #e74c3c;
}

.password-toggle {
  background: none;
  border: none;
  padding: 0 15px;
  font-size: 18px;
  cursor: pointer;
  color: #6c757d;
  transition: color 0.3s ease;
}

.password-toggle:hover {
  color: #3498db;
}

.error-message {
  color: #e74c3c;
  font-size: 12px;
  margin-top: 5px;
  padding-left: 15px;
}

.login-button {
  width: 100%;
  background: linear-gradient(135deg, #3498db 0%, #1a5276 100%);
  color: white;
  border: none;
  padding: 15px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(52, 152, 219, 0.4);
}

.login-button:active:not(:disabled) {
  transform: translateY(0);
}

.login-button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.login-button.loading {
  pointer-events: none;
}

.loading-text {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid transparent;
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media (max-width: 480px) {
  .login-container {
    padding: 15px;
  }

  .login-card {
    padding: 30px 20px;
  }

  .logo h1 {
    font-size: 20px;
  }

  .system-desc {
    font-size: 13px;
  }
}

@media (max-height: 600px) {
  .login-container {
    align-items: flex-start;
    padding-top: 40px;
    padding-bottom: 40px;
  }
}
</style>