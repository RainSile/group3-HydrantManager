<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-card">
        <!-- 头部 -->
        <div class="login-header">
          <div class="logo">
            <div class="logo-icon">🔥</div>
            <h1>消防栓查询系统</h1>
          </div>
          <p class="system-desc">Fire Hydrant Management System</p>
        </div>

        <!-- 登录表单 -->
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

          <div class="form-options">
            <label class="remember-me">
              <input
                  v-model="loginForm.remember"
                  type="checkbox"
                  class="checkbox"
              >
              <span class="checkmark"></span>
              记住密码
            </label>
            <a href="#" class="forgot-password" @click.prevent="handleForgotPassword">
              忘记密码？
            </a>
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

        <!-- 底部信息 -->
        <div class="login-footer">
          <p class="footer-text">
            首次使用？
            <a href="#" class="register-link" @click.prevent="handleRegister">
              联系管理员注册账号
            </a>
          </p>
        </div>

        <!-- 演示账号提示 -->
        <div class="demo-account">
          <p>演示账号：admin / 123456</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

// 响应式数据
const loading = ref(false)
const showPassword = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

const errors = reactive({
  username: '',
  password: ''
})

// 表单验证
const validateForm = () => {
  let isValid = true

  // 清空错误信息
  errors.username = ''
  errors.password = ''

  // 用户名验证
  if (!loginForm.username.trim()) {
    errors.username = '请输入用户名'
    isValid = false
  } else if (loginForm.username.trim().length < 3) {
    errors.username = '用户名至少3个字符'
    isValid = false
  }

  // 密码验证
  if (!loginForm.password) {
    errors.password = '请输入密码'
    isValid = false
  } else if (loginForm.password.length < 6) {
    errors.password = '密码至少6个字符'
    isValid = false
  }

  return isValid
}

// 清除错误信息
const clearError = (field) => {
  errors[field] = ''
}

// 登录处理
const handleLogin = async () => {
  if (!validateForm()) {
    return
  }

  loading.value = true

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1500))

    // 模拟登录验证
    if (loginForm.username === 'admin' && loginForm.password === '123456') {
      ElMessage.success('登录成功！')

      // 保存登录状态
      localStorage.setItem('isLoggedIn', 'true')
      localStorage.setItem('username', loginForm.username)

      // 如果选择记住密码，保存到本地存储
      if (loginForm.remember) {
        localStorage.setItem('rememberedUsername', loginForm.username)
      } else {
        localStorage.removeItem('rememberedUsername')
      }

      // 触发登录成功事件
      emit('login-success', {
        username: loginForm.username,
        timestamp: new Date().toISOString()
      })
    } else {
      ElMessage.error('用户名或密码错误！')
    }
  } catch (error) {
    ElMessage.error('登录失败，请检查网络连接')
    console.error('Login error:', error)
  } finally {
    loading.value = false
  }
}

// 忘记密码处理
const handleForgotPassword = () => {
  ElMessage.info('请联系系统管理员重置密码')
}

// 注册处理
const handleRegister = () => {
  ElMessage.info('请联系系统管理员创建账号')
}

// 定义emit
const emit = defineEmits(['login-success'])

// 初始化记住的用户名
const initRememberedUsername = () => {
  const rememberedUsername = localStorage.getItem('rememberedUsername')
  if (rememberedUsername) {
    loginForm.username = rememberedUsername
    loginForm.remember = true
  }
}

// 组件挂载时初始化
initRememberedUsername()
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  border-color: #667eea;
  background: white;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
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
  color: #667eea;
}

.error-message {
  color: #e74c3c;
  font-size: 12px;
  margin-top: 5px;
  padding-left: 15px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #6c757d;
  font-size: 14px;
  position: relative;
}

.checkbox {
  position: absolute;
  opacity: 0;
  cursor: pointer;
}

.checkmark {
  width: 18px;
  height: 18px;
  border: 2px solid #ddd;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.checkbox:checked + .checkmark {
  background: #667eea;
  border-color: #667eea;
}

.checkbox:checked + .checkmark::after {
  content: '✓';
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.forgot-password {
  color: #667eea;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
}

.forgot-password:hover {
  color: #5a6fd8;
  text-decoration: underline;
}

.login-button {
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
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

.login-footer {
  text-align: center;
  margin-top: 25px;
  padding-top: 20px;
  border-top: 1px solid #e9ecef;
}

.footer-text {
  color: #6c757d;
  font-size: 14px;
  margin: 0;
}

.register-link {
  color: #667eea;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.register-link:hover {
  color: #5a6fd8;
  text-decoration: underline;
}

.demo-account {
  margin-top: 20px;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 10px;
  text-align: center;
}

.demo-account p {
  margin: 0;
  color: #6c757d;
  font-size: 14px;
  font-weight: 500;
}

/* 响应式设计 */
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